package com.example.otmanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.otmanagement.data.Warehouse
import com.example.otmanagement.databinding.ActivityCountriesByRegionBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

const val EXTRA_WAREHOUSE = "warehouse"

class CountriesByRegion : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    val TAG = "CountriesByRegion"
    private lateinit var binding: ActivityCountriesByRegionBinding
    private lateinit var mMap: GoogleMap
    private var regionNum: Int? = null

    val serverAddr = ""
    val europe = LatLng(47.04, 15.74)
    val americas = LatLng(5.13, -80.91)
    val asia = LatLng(22.25, 76.84)
    val africa = LatLng(9.88, 20.41)
    val regions = arrayOf(europe, americas, asia, africa)

    var warehouse = arrayListOf<Warehouse>(
        Warehouse(1,"Southlake, Texas",LatLng(32.9545, -97.1488)),
        Warehouse(2,"San Francisco",LatLng(37.6563, -122.377)),
        Warehouse(3,"New Jersey",LatLng(40.3917, -74.5458)),
        Warehouse(4,"Seattle, Washington",LatLng(47.6205, -122.351)),
        Warehouse(5,"Toronto",LatLng(43.7166, -79.3407)),
        Warehouse(6,"Sydney",LatLng(-33.8667, 151.2)),
        Warehouse(7,"Mexico City",LatLng(19.4978, -99.1269)),
        Warehouse(8,"Beijing", LatLng(39.9035, 116.388)),
        Warehouse(9,"Bombay", LatLng(19.0712, 72.8762)))

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
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(regions[pos], -10F))
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(asia, -10F))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        val seoul = LatLng(37.56, 126.97)
//
//        val markerOptions = MarkerOptions()
//        markerOptions.position(seoul)
//        markerOptions.title("서울")
//        markerOptions.snippet("한국의 수도")
//        mMap.addMarker(markerOptions)

        regionNum = intent.getIntExtra(EXTRA_REGION, 0)
        binding.sRegion.setSelection(regionNum!!)
        Log.d(TAG, "extra: $regionNum")

        for (w: Warehouse in warehouse) {
            val marker = mMap.addMarker(MarkerOptions()
                .position(w.location)
                .title(w.id.toString())
                .snippet(w.name))
            marker.showInfoWindow()
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(regions[regionNum!!], -10F))
        mMap.setOnInfoWindowClickListener {
            val intent = Intent(applicationContext, ItemByCategory::class.java).apply {
                Log.d(TAG, "extra_warehouse: ${it.snippet}")
                putExtra(EXTRA_WAREHOUSE, it.snippet)
            }
            startActivity(Intent(intent))
        }
    }

    override fun onInfoWindowClick(marker: Marker?) {
        if (marker != null) {
            val intent = Intent(applicationContext, ItemByCategory::class.java).apply {
                Log.d(TAG, "extra_warehouse: ${marker.snippet}")
                putExtra(EXTRA_WAREHOUSE, marker.snippet)
            }
            startActivity(Intent(intent))
        }
    }

    fun getData() {
        val data = JSONObject()
        val url = URL("$serverAddr/cur_user")
        data.accumulate("region", regions[regionNum!!])

        lateinit var allText: String

        GlobalScope.launch(Dispatchers.Main) {

            val con: HttpURLConnection? = url.openConnection() as HttpURLConnection?
            con!!.requestMethod = "POST"
            con.setRequestProperty("Cache-Control", "no-cache")
            con.setRequestProperty("content-Type", "application/json")
            con.setRequestProperty("Accept", "text/html")
            con.doOutput = true
            con.doInput = true

            suspend fun coroutineRequest(url: URL) =
                withContext(IO) { // runs at I/O level and frees the Main thread
                    with(con) {
                        val outStream: OutputStream = con.outputStream
                        val writer: BufferedWriter = BufferedWriter(OutputStreamWriter(outStream))
                        writer.write(data.toString())
                        writer.flush()
                        writer.close()

                        allText = inputStream.bufferedReader().use(BufferedReader::readText)
                        Log.d("coroutineRequest", allText)
                    }
                }

            coroutineRequest(url)

//            binding.userName.text = allText
//            binding.userEmail.text = GlobalApplication.prefs.userId
//
//            val markerOptions = MarkerOptions()
//            markerOptions.position(seoul)
//            markerOptions.title("서울")
//            markerOptions.snippet("한국의 수도")
//            mMap.addMarker(markerOptions)
        }
    }
}
