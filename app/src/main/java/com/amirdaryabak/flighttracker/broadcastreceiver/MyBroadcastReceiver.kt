package com.amirdaryabak.flighttracker.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val test = intent?.getBooleanExtra("state", false) ?: return
        if (test) {
            Toast.makeText(context, "True", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "False", Toast.LENGTH_LONG).show()
        }
    }

}