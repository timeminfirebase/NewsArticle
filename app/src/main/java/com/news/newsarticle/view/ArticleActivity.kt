package com.news.newsarticle.view

import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.news.newsarticle.R
import com.news.newsarticle.entity.Story

class ArticleActivity : AppCompatActivity() {

    private lateinit var story: Story


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)


        story = intent.getSerializableExtra("story") as Story

        val bundle = Bundle()
        bundle.putSerializable("story", story)

        val progressBar: ProgressBar =findViewById(R.id.progress_bar_fragment_article)
        val webView: WebView = findViewById(R.id.webview_fragment_article)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object: WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                progressBar.visibility = View.GONE
                webView.visibility = View.VISIBLE
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
                handler?.proceed()
            }
        }
        Log.d("url",story.url)
        webView.loadUrl(story.url)


    }

}