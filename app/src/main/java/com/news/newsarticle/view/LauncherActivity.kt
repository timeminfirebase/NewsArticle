package com.news.newsarticle.view

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.news.newsarticle.R

class LauncherActivity : AppCompatActivity() {

    private lateinit var mIVLogo: ImageView
    private lateinit var mProgressBar: ProgressBar

    private var heightOfView = 0
    private var ivLocationCoordinated = IntArray(2)
    private var screenSize = Array(2) { 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        mIVLogo = findViewById(R.id.iv_logo_hn_small_512X512_with_alpha_activity_launcher)
        mProgressBar = findViewById(R.id.progress_bar_activity_launcher)

        mIVLogo.viewTreeObserver.addOnGlobalLayoutListener {
            heightOfView = mIVLogo.height
            screenSize = getScreenSize()
            animateUI()
        }

        val splashTimeOut = 2000L

        Handler().postDelayed({
            lateinit var intent: Intent

            intent = Intent(this@LauncherActivity, MainActivity::class.java)
            intent.putExtra("isUserSignedIn", true)

            startActivity(intent)
            finish()
        }, splashTimeOut)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        mIVLogo.getLocationOnScreen(ivLocationCoordinated)
        animateUI()
    }

    // Get the SharedPreferences that stored true if the user was signed in last time or false if he signed out.


    private fun animateUI() {
        val finalLocation: Float = (heightOfView / 2) - (screenSize[1] / 4).toFloat()
        ObjectAnimator.ofFloat(mIVLogo, "translationY", finalLocation).apply {
            duration = 1000
            start()
        }
        mProgressBar.visibility = View.VISIBLE
    }

    private fun getScreenSize(): Array<Int> {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        return arrayOf(size.x, size.y)
    }

}
