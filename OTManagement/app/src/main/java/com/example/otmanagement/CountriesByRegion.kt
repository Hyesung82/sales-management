package com.example.otmanagement

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.otmanagement.databinding.ActivityCountriesByRegionBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CountriesByRegion : AppCompatActivity(), AdapterView.OnItemSelectedListener, OnMapReadyCallback {
    val TAG = "CountriesByRegion"
    private lateinit var binding: ActivityCountriesByRegionBinding
    private lateinit var mMap: GoogleMap
<<<<<<< HEAD
=======

    val europe = LatLng(47.04, 15.74)
    val americas = LatLng(5.13, -80.91)
    val asia = LatLng(22.25, 76.84)
    val africa = LatLng(9.88, 20.41)
    val regions = arrayOf(europe, americas, asia, africa)
>>>>>>> 06421bcefab15efc9b163d38be10ef6068f1ce68

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_countries_by_region)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.regions_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.sRegion.adapter = adapter
        }

        binding.sRegion.onItemSelectedListener = this


        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.fMap) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding.sRegion.setSelection(intent.getIntExtra(EXTRA_REGION, 0))
        Log.d(TAG, "extra: ${intent.getIntExtra(EXTRA_REGION, 0)}")
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
<<<<<<< HEAD
=======

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(regions[pos], -10F))
>>>>>>> 06421bcefab15efc9b163d38be10ef6068f1ce68
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
<<<<<<< HEAD
=======

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(asia, -10F))
>>>>>>> 06421bcefab15efc9b163d38be10ef6068f1ce68
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val seoul = LatLng(37.56, 126.97)

        val markerOptions = MarkerOptions()
        markerOptions.position(seoul)
        markerOptions.title("서울")
        markerOptions.snippet("한국의 수도")
        mMap.addMarker(markerOptions)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 10F))
    }
}