package com.example.screenman

import android.content.*
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import android.widget.Toast
import java.util.*


class MainActivity : AppCompatActivity() {

    var mEdit: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mEdit = findViewById(R.id.editTextNumberSigned);
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(mPlugInReceiver, getIntentFilter())
    }

    private fun getIntentFilter(): IntentFilter {
        val iFilter = IntentFilter()
        iFilter.addAction(Intent.ACTION_USER_PRESENT)
        return iFilter
    }

    private val mPlugInReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_USER_PRESENT -> {
                    fixOScan()
                }
            }
        }
    }

    public fun mrClick(view: View) {
        fixOScan()
    }

    public fun mrClickRest(view: View) {
        restOScan()
    }

    public fun mrScreenClick(view: View) {
        screenShrink()
    }

    fun fixOScan(){
        val resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android")
        var navigationBarHeight = 0
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId)
        }

        try {
            val process = Runtime.getRuntime().exec("su -c wm overscan 0,0,0,-"+navigationBarHeight.toString())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun restOScan(){
        try {
            val process = Runtime.getRuntime().exec("su -c wm overscan 0,0,0,0")
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun screenShrink(){

        /*
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(getString(R.string.scrnHeight), Integer.parseInt(mEdit?.getText().toString()))
            commit()
        }*/

        try {
            val process = Runtime.getRuntime().exec("su -c wm size 1440x"+mEdit?.getText().toString())
            val processtwo = Runtime.getRuntime().exec("su -c wm density 450")
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        basicAlert()
    }


    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            "All Good", Toast.LENGTH_SHORT).show()
    }
    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            "Cancel", Toast.LENGTH_SHORT).show()
    }

    fun basicAlert(){

        Timer("SettingUp", false).schedule(500) {
            doSomething()
        }


        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Androidly Alert")
            setMessage("We have a message")
            setPositiveButton("OK",positiveButtonClick)
            setNegativeButton("Cancel",negativeButtonClick)
            show()
        }


    }

}