package com.test.findvenues.presentation.venuelist

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.findvenues.R
import com.test.findvenues.databinding.ActivityVenueListBinding
import com.test.findvenues.di.Components
import com.test.findvenues.presentation.venuedetail.VenueDetailActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable

class VenueListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVenueListBinding
    private lateinit var venueListAdapter: VenueListAdapter
    private lateinit var viewModel: VenueListViewModel
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_venue_list)
        setSupportActionBar(binding.toolbarContainer.toolbar)
        setUpUiComponents()

        val factory = DaggerVenueListComponent.builder()
                .appComponent(Components.appComponent())
                .venueListModule(VenueListModule())
                .build()
                .venueListViewModelFactory()

        viewModel = ViewModelProvider(this, factory).get(VenueListViewModel::class.java)
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

    private fun setUpUiComponents() {
        venueListAdapter = VenueListAdapter(object : VenueListAdapter.ClickListener {
            override fun onVenueItemClicked(venueId: String) {
                VenueDetailActivity.start(this@VenueListActivity, venueId)
            }
        })

        binding.venues.apply {
            adapter = venueListAdapter
            layoutManager = LinearLayoutManager(this@VenueListActivity)

            val divider = DividerItemDecoration(this@VenueListActivity, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(this@VenueListActivity, R.drawable.drawable_divider_transparent)?.apply {
                divider.setDrawable(this)
            }
            addItemDecoration(divider)
        }

        binding.search.setOnEditorActionListener { view, keyCode, _ ->
            if (keyCode == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchVenues(view.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.swipeLayout.setOnRefreshListener { viewModel.searchVenues(binding.search.text.toString()) }
    }

    private fun onStateChanges(state: ViewState) {
        if (state != ViewState.Loading) enableRefreshing(false)

        when (state) {
            is ViewState.Loading -> enableRefreshing(true)
            is ViewState.VenuesLoaded -> displayVenueList(state.venues)
            is ViewState.VenuesNotFound -> displayEmptyList()
            is ViewState.Error -> showDefaultError(state.errorMessage)
        }
    }

    private fun enableRefreshing(enable: Boolean) {
        binding.swipeLayout.isRefreshing = enable
    }

    private fun displayVenueList(venueList: List<VenueListView>) {
        venueListAdapter.venues = venueList

        with(binding) {
            emptyList.visibility = View.GONE
            venues.visibility = View.VISIBLE
        }
    }

    private fun displayEmptyList() {
        with(binding) {
            venues.visibility = View.GONE
            emptyList.visibility = View.VISIBLE
        }
    }

    private fun showDefaultError(error: String?) {
        Toast.makeText(this, getString(R.string.default_error_message, error), Toast.LENGTH_LONG).show()
    }
}