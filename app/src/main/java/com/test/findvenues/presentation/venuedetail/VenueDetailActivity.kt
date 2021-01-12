package com.test.findvenues.presentation.venuedetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.test.findvenues.R
import com.test.findvenues.databinding.ActivityVenueDetailBinding
import com.test.findvenues.di.Components
import com.test.findvenues.presentation.VenueModule
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable

class VenueDetailActivity : AppCompatActivity() {

    companion object {

        private const val VENUE_ID = "VENUE_ID"

        fun start(context: Context, venueId: String) {
           Intent(context, VenueDetailActivity::class.java).apply {
               putExtra(VENUE_ID, venueId)
               context.startActivity(this)
           }
        }
    }

    private lateinit var binding: ActivityVenueDetailBinding
    private lateinit var viewModel: VenueDetailViewModel
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_venue_detail)
        setSupportActionBar(binding.toolbarContainer.toolbar)

        val venueId = requireNotNull(intent.extras?.getString(VENUE_ID))
        val factory = DaggerVenueDetailComponent.builder()
                .appComponent(Components.appComponent())
                .venueDetailModule(VenueDetailModule(venueId))
                .venueModule(VenueModule(this))
                .build()
                .venueDetailViewModelFactory()

        viewModel = ViewModelProvider(this, factory).get(VenueDetailViewModel::class.java)
        lifecycle.addObserver(viewModel)
    }

    override fun onStart() {
        super.onStart()
        disposable = viewModel.stateObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onStateChanges) { showDefaultError(it.message) }
    }

    override fun onStop() {
        disposable.dispose()
        super.onStop()
    }

    private fun onStateChanges(state: ViewState) {
        if (state != ViewState.Loading) enableRefreshing(false)

        when (state) {
            is ViewState.Loading -> enableRefreshing(true)
            is ViewState.Loaded -> displayVenueDetail(state.venue)
            is ViewState.Error -> showDefaultError(state.errorMessage)
        }
    }

    private fun enableRefreshing(enable: Boolean) {
        with(binding) {
            if (enable) {
                content.visibility = View.GONE
                progress.visibility = View.VISIBLE
            } else {
                progress.visibility = View.GONE
                content.visibility = View.VISIBLE
            }
        }
    }

    private fun displayVenueDetail(venue: VenueView) {
        with(binding) {
            venue.photos.also {
                if (it.isEmpty()) {
                    photos.visibility = View.GONE
                } else {
                    photos.setImageListener { position, imageView -> Picasso.get().load(it[position]).into(imageView) }
                    photos.pageCount = it.size
                    photos.visibility = View.VISIBLE
                }
            }

            title.text = venue.name
            rating.setTextOrGone(venue.rating?.let { getString(it.stringRes, it.value) })
            description.setTextOrGone(venue.description)
            address.setTextOrGone(venue.location)

            val contacts = StringBuilder().apply {
                venue.contacts.forEach { appendLine(getString(it.stringRes, it.value)) }
            }.removeSuffix("\n").toString()

            contact.setTextOrGone(contacts)
        }
    }

    private fun showDefaultError(error: String?) {
        Toast.makeText(this, getString(R.string.default_error_message, error), Toast.LENGTH_LONG).show()
    }

    private fun TextView.setTextOrGone(text: CharSequence?) {
        when {
            text.isNullOrEmpty() -> this.visibility = View.GONE
            else -> {
                this.visibility = View.VISIBLE
                this.text = text
            }
        }
    }
}