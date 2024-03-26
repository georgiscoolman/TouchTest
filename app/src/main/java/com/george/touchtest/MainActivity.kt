package com.george.touchtest

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val options = ActivityOptions.makeBasic().apply {
            //launchDisplayId = 4
        }
        val intent = Intent(this, TouchTestActivity::class.java)
        startActivity(intent, options.toBundle())
    }
}