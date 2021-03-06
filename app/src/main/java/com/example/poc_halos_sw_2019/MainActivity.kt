package com.example.poc_halos_sw_2019

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun start(v :View) {
        val intent = Intent(this, FullscreenActivity::class.java)
        startActivity(intent)
    }
    fun circleact(v: View) {
        val intent = Intent(this, CircleActivity::class.java)
        startActivity(intent)
    }
    fun UpCircle(v: View) {
        val intent = Intent(this, UpCricle::class.java)
        startActivity(intent)
    }
}
