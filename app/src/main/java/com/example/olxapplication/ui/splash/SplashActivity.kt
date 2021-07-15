package com.example.olxapplication.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.olxapplication.BaseActivity
import com.example.olxapplication.MainActivity
import com.example.olxapplication.R
import com.example.olxapplication.ui.login.LoginActivity
import com.example.olxapplication.utilities.Constants
import com.example.olxapplication.utilities.SharedPref
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.*

class SplashActivity: BaseActivity()
{
    private val MY_PERMISSIONS_REQUEST_LOCATION= 100
    private val REQUEST_GPS= 101
    private var locationRequest: LocationRequest? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        askforPermissions()

        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this)

        getlocationCallback()
    }

    override fun onResume() {
        super.onResume()

        askforPermissions()
    }

    private fun askforPermissions() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions,MY_PERMISSIONS_REQUEST_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== MY_PERMISSIONS_REQUEST_LOCATION)
        {
            var granted = false
            for(grantResults in grantResults)
            {
                if(grantResults== PackageManager.PERMISSION_GRANTED)
                {
                    granted= true
                }
            }
            if(granted)
                enableGps()
        }
    }

    private fun enableGps() {
        locationRequest= LocationRequest.create()
        locationRequest?.setInterval(3000)
        locationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)

        val task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        task.addOnCompleteListener(object: OnCompleteListener<LocationSettingsResponse>
        {
            override fun onComplete(p0: Task<LocationSettingsResponse>) {

                try {
                    val response= task.getResult(ApiException::class.java)
                    startLocationUpdates()
                }catch (exception:ApiException)
                {
                    when(exception.statusCode){

                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED->{

                            val resolvable = exception as ResolvableApiException

                            resolvable.startResolutionForResult(this@SplashActivity,REQUEST_GPS)

                        }
                    }

                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_GPS)
        {
            startLocationUpdates()
        }

    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,null)
    }

    private fun getlocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                val location = p0.lastLocation

                SharedPref(this@SplashActivity).setString(Constants.CITY_NAME, getCityName(location))

                if(SharedPref(this@SplashActivity).getString(Constants.USER_ID)?.isEmpty()!!) {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }
                else
                {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun getCityName(location: Location?):String {
        var cityName=""

        val geocoder= Geocoder(this, Locale.getDefault())
        try{
            val address= geocoder.getFromLocation(location!!.latitude,location.longitude, 1 )

            cityName= address[0].locality
        }catch (e:IOException){
            Log.d("LocationException", "failed")
        }
        return cityName
    }
}