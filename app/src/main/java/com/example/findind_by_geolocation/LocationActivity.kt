package com.example.findind_by_geolocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.CaseMap.Title
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.geo_location.btnAccept
import kotlinx.android.synthetic.main.geo_location.btnStreetView
import kotlinx.android.synthetic.main.geo_location.search_edt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationActivity : FragmentActivity() , OnMapReadyCallback , LocationListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener{
    
    private var mMap: GoogleMap? = null
    private lateinit var mLastLocation: Location
    private var mCurrLocationMarker: Marker? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest
    internal lateinit var MarkerPoints: ArrayList<LatLng>
    private  var desAddr  : LatLng? = null
    private  var strAddrDes  : String ? = ""
    lateinit var btnTxtAdrs  : TextView
    lateinit var txtLat : TextView
    lateinit var txtLng: TextView
    private val defaultLatLng = LatLng(37.421998, -122.084000)
    private var dtPostcode : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.geo_location)

        initInstances()
    }

    fun initInstances() {

        val mapFragment = getSupportFragmentManager().findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
        MarkerPoints = ArrayList<LatLng>()
        btnTxtAdrs = (findViewById(R.id.txtAddressName) as TextView)
        txtLat = findViewById(R.id.txtAddressPositiondetail1) as TextView
        txtLng = findViewById(R.id.txtAddressPositiondetail2) as TextView

        checkLocationIsOn()

        btnAccept.setOnClickListener {
            if(desAddr!!.latitude != 0.0 && desAddr!!.longitude != 0.0 ) {
                showDialog( "Your location is" + strAddrDes , "Geo Location")
            }
        }
    }

    fun showDialog(str : String , title: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(str)
        builder.setPositiveButton("Close") { dialog, which ->
            Toast.makeText(applicationContext, "Close", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    private fun getGeoAddress(latLng: LatLng): String {
        // 1
        val geocoder = Geocoder(this , Locale("en", "EN"))
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""
        try {
            // 2
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                addressText = address.getAddressLine(0)
                val country =  address.countryName
                dtPostcode =  address.postalCode
                val state = address.adminArea
                val city =  address.locality
                val knownName = address.featureName
//                address.subLocality
//                address.thoroughfare
//                address.subThoroughfare
                // addresses[0].featureName?:"" + " " + addresses[0].thoroughfare?:"" + " "
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }
        return addressText
    }

    private fun geGeotLatLng(strAddress : String) : LatLng {
        // 1
        val geocoder = Geocoder(this , Locale("th", "TH"))
        val geocodeMatches: List<Address>?
        var latlng : LatLng? = null

        try {
            geocodeMatches = geocoder.getFromLocationName(strAddress , 1)
            val latitude = geocodeMatches!![0].latitude
            val longitude = geocodeMatches[0].longitude
            latlng = LatLng(latitude,longitude)
        }catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return latlng!!
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.setInterval(1000)
        mLocationRequest.setFastestInterval(1000)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient!!, mLocationRequest, this)
        }

    }

    override fun onConnectionSuspended(i: Int) {
    }

    override fun onLocationChanged(location: Location) {
        try{
            mLastLocation = location
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker!!.remove()
            }
            //Place current location marker
            val latLng = LatLng(location.getLatitude(), location.getLongitude())
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)

            desAddr =  LatLng(location.getLatitude() , location.getLongitude())

            strAddrDes =  getGeoAddress(desAddr!!)
            mMap!!.addMarker(
                MarkerOptions()
                    .position(desAddr!!)
                    .title(strAddrDes))
            //move map camera
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(desAddr!!))
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15F))
            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient!!, this)
            }
            btnTxtAdrs.setText(strAddrDes!!)
            txtLat.setText(String.format("%s°" ,  String.format("%.6f", desAddr!!.latitude )))
            txtLng.setText(String.format("%s°" ,  String.format("%.6f", desAddr!!.longitude)))
        }catch(e : Exception) {
            e.printStackTrace()
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
    }


    @SuppressLint("ServiceCast")
    private fun checkLocationIsOn() {
        val locationManager = this@LocationActivity!!.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        var gps_enabled = false
        var network_enabled = false
        try {
            gps_enabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        try {
            network_enabled = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        if (!gps_enabled && !network_enabled) {
            showDialog( " Please allow the app to access your location for accuracy of search functions", "Geo Location")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val thailand = LatLng(13.7563, 100.5018)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(thailand))
        mMap!!.uiSettings.isMapToolbarEnabled
        mMap!!.uiSettings.isZoomGesturesEnabled
        mMap!!.uiSettings.isZoomControlsEnabled

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap!!.setMyLocationEnabled(true)
            }
        } else {
            buildGoogleApiClient()
            mMap!!.setMyLocationEnabled(true)
        }

        mMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(p0: LatLng) {
                try {
                    mMap!!.clear()
                    desAddr = p0!!
                    strAddrDes = getGeoAddress(desAddr!!)
                    mMap!!.addMarker(
                        MarkerOptions()
                            .position(desAddr!!)
                            .title(strAddrDes)
                    )
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLng(desAddr!!))
                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15F))
                    btnTxtAdrs.setText(strAddrDes)
                    txtLat.setText(String.format("%s°", String.format("%.6f", desAddr!!.latitude)))
                    txtLng.setText(String.format("%s°", String.format("%.6f", desAddr!!.longitude)))
                } catch (e: Exception) {
                    e.message

                }
            }
        })
        search_edt.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                try{
                    mMap!!.clear()
                    val  destination = search_edt.text.toString()
                    desAddr = geGeotLatLng(destination)
                    // Find real location name
                    strAddrDes =  getGeoAddress(desAddr!!)
                    mMap!!.addMarker(MarkerOptions().position(desAddr!!).title(strAddrDes))
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLng(desAddr!!))
                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15F))
                    btnTxtAdrs.setText(strAddrDes)
                    txtLat.setText(String.format("%s°" , String.format("%.6f", desAddr!!.latitude )))
                    txtLng.setText(String.format("%s°" ,  String.format("%.6f", desAddr!!.longitude)))
                    return@OnKeyListener true
                }catch ( e : Exception){
                    e.message
                }
            }
            false
        })

        mMap!!.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View {
                val v = layoutInflater.inflate(R.layout.custom_marker_option, null)
                val tv_title = v.findViewById(R.id.tv_title) as TextView
                val position = v.findViewById(R.id.position) as TextView
                val position2 = v.findViewById(R.id.position2) as TextView
                try{
                    tv_title.setText(marker.getTitle().toString())
                    position.setText(desAddr!!.latitude.toString())
                    position2.setText(desAddr!!.longitude.toString())
                }catch (e : Exception){
                    Log.e("MapInfo : " ,e.printStackTrace().toString() )
                }

                return v
            }

            override fun getInfoContents(marker: Marker): View? {
                return null
            }
        })

        btnStreetView.setOnClickListener{
            try {
                if(desAddr!!.latitude != 0.0 && desAddr!!.longitude != 0.0 ){
                    StreetViewLocationActivity.StartWithType( this@LocationActivity ,
                        strAddrDes!! ,
                        desAddr!!.latitude.toString() ,
                        desAddr!!.longitude.toString()
                    )
                }else{

                }
            }catch (e: Exception){
                Log.e("geo-street" , e.printStackTrace().toString())
            }

        }

    }


    override fun onBackPressed(){
        MainActivity.start(this)
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, LocationActivity::class.java)
            context?.startActivity(intent)
        }
    }
}