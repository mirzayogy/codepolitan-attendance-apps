package com.mirzayogy.codepolitanattendanceapps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.toRadians
import java.util.*
import kotlin.math.*


class MainActivity : AppCompatActivity() {

    companion object{
        const val ID_LOCATION_PERMISSION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissionLocation()
        onClick()
    }

    private fun checkPermissionLocation() {
        if(checkPermission()){
            if(!isLocationEnabled()){
                Toast.makeText(this,getString(R.string.please_activate_location_permission),Toast.LENGTH_SHORT).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }else{
            requestPermission()
        }
    }

    private fun checkPermission():Boolean{

        if(ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) ==  PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==  PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false
    }

    private fun isLocationEnabled():Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            return true
        }

        return false
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this,
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ), ID_LOCATION_PERMISSION
            )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == ID_LOCATION_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this,getString(R.string.permission_granted),Toast.LENGTH_SHORT).show()

                if(!isLocationEnabled()){
                    Toast.makeText(this,getString(R.string.please_activate_location_permission),Toast.LENGTH_SHORT).show()
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            }
        }
    }

    private fun onClick(){
        fabCheckIn.setOnClickListener{
            loadScanLocation()
            Handler(Looper.getMainLooper()).postDelayed({
                getLastLocation()
            },4000)
        }
    }

    private fun loadScanLocation(){
        rippleBackground.startRippleAnimation()
        textViewScanning.visibility = View.VISIBLE
        textViewCheckInSuccess.visibility = View.GONE
    }

    private fun stopScanLocation(){
        rippleBackground.stopRippleAnimation()
        textViewScanning.visibility = View.GONE
    }

    private fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener { location ->
                    if(location!=null){
                        val currentLat = location.latitude
                        val currentLong = location.longitude
                        val distance = calculateDistance(
                                currentLat,
                                currentLong,
                                getAddresses()[0].latitude,
                                getAddresses()[0].longitude
                        ) * 1000

                        textViewCheckInSuccess.visibility = View.VISIBLE
                        textViewCheckInSuccess.text = getString(R.string.lat_long,currentLat,currentLong)

                        Log.d("coba","size: ${getAddresses().size}")
                        Log.d("coba","distance: $distance")

                        for(address: Address in getAddresses()){
                            Log.d("coba","lat: ${address.latitude}")
                        }

                    }else{
                        Toast.makeText(this,getString(R.string.get_location_failed),Toast.LENGTH_SHORT).show()
                    }
                    stopScanLocation()
                }
            }else{
                Toast.makeText(this,getString(R.string.please_activate_location_permission),Toast.LENGTH_SHORT).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }else{
            requestPermission()
        }
    }
    private fun getAddresses() : List<Address>{
        val destinationPlace = "Lembaga Layanan Pendidikan Tinggi (LLDIKTI) Wilayah XI Kalimantan"
        val geocode = Geocoder(this, Locale.getDefault())
        return geocode.getFromLocationName(destinationPlace, 100)
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6372.8 // in kilometers
        val radiansLat1 = toRadians(lat1)
        val radiansLat2 = toRadians(lat2)
        val dLat = toRadians(lat2 - lat1)
        val dLon = toRadians(lon2 - lon1)
        return 2 * r * asin(sqrt(sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(radiansLat1) * cos(radiansLat2)))
    }
}