package com.example.poc_halos_sw_2019

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_circle.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class UpCricle : AppCompatActivity() {
    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        fullscreen_content.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        supportActionBar?.show()
        fullscreen_content_controls.visibility = View.VISIBLE
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }
    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    var brightnessup: Boolean = true
    var brightness = 0
    var saveBrightness = 0

    private val timer = object: CountDownTimer(30000, 100) {

        override fun onTick(millisUntilFinished: Long) {
            var y = findViewById<ImageView>(R.id.halo).height
            var x = findViewById<ImageView>(R.id.halo).width
            if (brightnessup) {
                brightness += 5
                y += 20
                x += 20
            } else {
                brightness -= 5
                y -= 20
                x -= 20
            }
            if (brightness > 100)
                brightnessup = false
            else if (brightness <= 0)
                brightnessup = true
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
            findViewById<ImageView>(R.id.halo).getLayoutParams().height = y
            findViewById<ImageView>(R.id.halo).layoutParams.width = x
            findViewById<ImageView>(R.id.halo).requestLayout()
        }
        override fun onFinish() {}
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mVisible = true
        fullscreen_content.setOnClickListener { toggle() }
        dummy_button.setOnTouchListener(mDelayHideTouchListener)
        if (Settings.System.canWrite(this.baseContext)) {
            saveBrightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
            Settings.System.putInt(this.contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.data = Uri.parse("package:com.example.poc_halos_sw_2019")
            startActivity(intent)
        }
        timer.start()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(100)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        supportActionBar?.hide()
        fullscreen_content_controls.visibility = View.GONE
        mVisible = false

        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        fullscreen_content.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    fun back(v : View) {
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, saveBrightness)
        timer.cancel()
        finish()
    }

    companion object {
        private const val AUTO_HIDE = true
        private const val AUTO_HIDE_DELAY_MILLIS = 3000
        private const val UI_ANIMATION_DELAY = 300
    }
}
