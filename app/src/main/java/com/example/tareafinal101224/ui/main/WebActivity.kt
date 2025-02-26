package com.example.tareafinal101224.ui.main

import android.content.Intent
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
import com.example.tareafinal101224.R
import com.example.tareafinal101224.databinding.ActivityWebBinding
import com.example.tareafinal101224.utils.Preferences
import com.google.android.gms.common.api.Api
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Locale

class WebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding

    private lateinit var preferences: Preferences
    private lateinit var auth: FirebaseAuth
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
        auth = Firebase.auth
        preferences = Preferences(this)
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

        if(!preferences.isAdmin()){
            binding.menuWeb.menu.removeItem(R.id.item_portal_usuarios)
        }

        binding.menuWeb.setCheckedItem(R.id.item_portal_web)

        binding.menuWeb.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item_portal_inicio -> {
                    startActivity(Intent(this, PortalActivity::class.java))
                    true
                }

                R.id.item_portal_productos -> {
                    startActivity(Intent(this, ApiActivity::class.java))
                    true
                }

                R.id.item_portal_proveedores -> {
                    startActivity(Intent(this, SQLiteActivity::class.java))
                    true
                }

                R.id.item_portal_web -> {
                    startActivity(Intent(this, WebActivity::class.java))
                    true
                }

                R.id.item_portal_cerrar_sesion -> {
                    auth.signOut()
                    preferences.limpiarPreferencias()
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                R.id.item_portal_salir -> {
                    finishAffinity()
                    true
                }

                R.id.item_portal_usuarios -> {
                    startActivity(Intent(this, ViewModelActivity::class.java))
                    true
                }

                else -> false
            }
        }
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