package com.example.application

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
         auth = FirebaseAuth.getInstance()

       // isNetworkAvailable()
        Handler(Looper.getMainLooper()).postDelayed({
            val user = auth.currentUser
            if (user!= null) {

           val intent = Intent(this,HomeActivity::class.java)
             startActivity(intent)
              finish()

            }else{
                val mainActivity= Intent(this, SignUpActivity::class.java)
                startActivity(mainActivity)
                finish()
            }
        }, 3000)
    }

    private fun isNetworkAvailable():Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           // val intent = Intent(this,HomeActivity::class.java)
          //  startActivity(intent)
            //finish()
            val network = cm.activeNetwork

            network ?: return false
            val actNetwork = cm.getNetworkCapabilities(network)
            return when {
                actNetwork!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    val homeActivity = Intent(this,HomeActivity::class.java)
                    startActivity(homeActivity )
                    finish()
                    Log.d("Network", "wifi connected")
                    true
                }
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    val homeActivity = Intent(this,HomeActivity::class.java)
                    startActivity(homeActivity)
                    finish()
                    Log.d("Network", "cellular network connected")
                    true
                }
                else -> {
                   // val networkActivity = Intent(this, NetworkActivity::class.java)
                    //startActivity(networkActivity)
                   // finish()
                    Toast.makeText(this,"No internet connection", Toast.LENGTH_SHORT).show()
                    Log.d("Network", "internet not connected ")
                    false
                }
            }
        }
        return false
    }



}