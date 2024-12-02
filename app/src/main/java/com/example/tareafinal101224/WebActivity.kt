package com.example.tareafinal101224

import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tareafinal101224.databinding.ActivityWebBinding
import java.util.Locale

class WebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        iniciarWebView()
        setListeners()
    }

    private fun setListeners() {
        binding.swipe.setOnRefreshListener {
            binding.web.reload()
        }

        binding.search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val busqueda = p0.toString().trim().lowercase(Locale.ROOT)
                if(android.util.Patterns.WEB_URL.matcher(busqueda).matches()){
                    binding.web.loadUrl(busqueda)
                    return true
                }
                val url="https://www.google.es/search?q=${busqueda}"

                binding.web.loadUrl(url)

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
    }

    private fun iniciarWebView() {
        binding.web.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.swipe.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.swipe.isRefreshing = false
            }
        }
        binding.web.webChromeClient = object : WebChromeClient(){

        }

        binding.web.settings.javaScriptEnabled = true

        binding.web.loadUrl("https://www.google.es")
    }

    override fun onBackPressed() {
        if(binding.web.canGoBack()){
            binding.web.goBack()
        }else {
            super.onBackPressed()
        }
    }
}