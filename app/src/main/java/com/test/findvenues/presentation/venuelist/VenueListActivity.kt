package com.test.findvenues.presentation.venuelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.test.findvenues.R
import com.test.findvenues.databinding.ActivityVenueListBinding

class VenueListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVenueListBinding
    private lateinit var venueListAdapter: VenueListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_venue_list)
        setSupportActionBar(binding.toolbarContainer.toolbar)
    }
}