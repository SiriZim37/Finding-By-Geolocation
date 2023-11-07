package com.example.findind_by_geolocation

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.geo_streetview.*
import java.text.DecimalFormat
import java.util.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment

class StreetViewLocationActivity : FragmentActivity(), StreetViewPanorama.OnStreetViewPanoramaChangeListener{

    lateinit var mStreetViewPanorama: StreetViewPanorama
    private var dLat: Double? = null
    private var dLng: Double? = null
    private var c_province: String? = null
    private lateinit var desAddr  : LatLng
    private lateinit var strAdrrDes  : String
    private lateinit var strCurrentAddr  : String
    lateinit var btnTxtAdrs  : TextView
    lateinit var btmLat : TextView
    lateinit var btmLng: TextView
    private var dtPostcode : String = ""

    private val s_Location by lazy {
        intent.getStringExtra("locationData") ?: ""
    }

    private val sLat by lazy {
        intent.getStringExtra("latData") ?: ""
    }
    private val sLng by lazy {
        intent.getStringExtra("lngData") ?: ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.geo_streetview)
        initInstances()
        initViewModel()
    }


    fun initInstances() {
        try{
            val streetViewFragment = supportFragmentManager.findFragmentById(R.id.streetviewpanorama) as SupportStreetViewPanoramaFragment

            btnTxtAdrs = (findViewById(R.id.txtAddressName) as TextView)
            btmLat = findViewById(R.id.txtPositiondetail1) as TextView
            btmLng = findViewById(R.id.txtPositiondetail2) as TextView

            streetViewFragment.getStreetViewPanoramaAsync { streetViewPanorama ->
                streetViewPanorama.setPosition(LatLng(sLat.toDouble(), sLng.toDouble()))
                mStreetViewPanorama = streetViewPanorama
                mStreetViewPanorama.getLocation()
                mStreetViewPanorama.setOnStreetViewPanoramaChangeListener(
                    this@StreetViewLocationActivity
                )
            }

            strCurrentAddr =  s_Location

            btnAccept.setOnClickListener {
                showDialog()

            }

        }catch (e : Exception){
            Log.e("geo street instance : " , e.stackTrace.toString())
        }
    }

    fun initViewModel(){
        try{

        }catch (e : Exception){
            Log.e("geo street view : " , e.stackTrace.toString())
        }
    }

    override fun onStreetViewPanoramaChange(location: StreetViewPanoramaLocation) {
        try {
            if (location != null) {
                dLat = location.position.latitude
                dLng = location.position.longitude
                desAddr = LatLng(dLat!!,dLng!!)
                val geoCoder = Geocoder(this ,Locale("en", "EN"))
                val addresses = geoCoder.getFromLocation(dLat!!, dLng!!, 1)
                 strAdrrDes = addresses!![0].getAddressLine(0) //0 to obtain first possible address
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                dtPostcode = addresses[0].postalCode
                //create your custom title
                c_province = country
                val formatter = DecimalFormat("#,###.00")
                val lat = formatter.format(dLat!!)
                val lng = formatter.format(dLng!!)
                val strLatlng = String.format("Lat: %s°, Long: %s°", lat, lng)
                btnTxtAdrs.setText(strAdrrDes)
                btmLat.setText(String.format("%s°" ,  String.format("%.6f", dLat)))
                btmLng.setText(String.format("%s°" ,  String.format("%.6f", dLng)))

                // addresses[0].featureName?:"" + " " + addresses[0].thoroughfare?:"" + " "

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun showDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Geo Streetview")
        builder.setMessage("Your location is" + strAdrrDes)
        builder.setPositiveButton("Close") { dialog, which ->
            Toast.makeText(applicationContext, "Close", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    override fun onBackPressed(){
        OnClickBacktoLastActivity()
    }

    fun OnClickBacktoLastActivity(){
        try {
            LocationActivity.start(this@StreetViewLocationActivity)
        }catch (e : Exception){
            e.message
        }
    }

    companion object {
        fun StartWithType(context: Context?  , curr_location : String  , lat : String ,lng : String ) {
            val intent = Intent(context, StreetViewLocationActivity::class.java)
            intent.putExtra("locationData" , curr_location)
            intent.putExtra("latData" , lat)
            intent.putExtra("lngData" , lng)
            context?.startActivity(intent)
        }
    }
}