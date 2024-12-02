package com.example.tareafinal101224

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tareafinal101224.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var email = ""
    private var contraseña = ""

    private lateinit var preferences: Preferences
    private var isAdmin = false

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val datos = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val cuenta = datos.getResult(ApiException::class.java)
                if(cuenta != null){
                    val credenciales = GoogleAuthProvider.getCredential(cuenta.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credenciales)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                if(cuenta.email.equals("admin@admin.es")){
                                    preferences.setAdmin(true)
                                }

                                iniciarActivityPortal()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                }
            }catch (e: ApiException){
                Log.d("ERROR de API:>>>>", e.message.toString())
            }
        }
        if(it.resultCode == RESULT_CANCELED){
            Toast.makeText(this, "El usuario cancelo", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        preferences = Preferences(this)
        setListeners()
    }

    private fun setListeners() {
        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.tvRegistrar.setOnClickListener {
            registrar()
        }

        binding.tvLocalizar.setOnClickListener {
            iniciarActivityMapa()
        }

        binding.btnGoogle.setOnClickListener {
            loginGoogle()
        }
    }

    private fun loginGoogle() {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_client_id))
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, googleConf)

        googleClient.signOut()

        responseLauncher.launch(googleClient.signInIntent)
    }

    private fun iniciarActivityMapa() {
        startActivity(Intent(this, MapaActivity::class.java))
    }

    private fun comprobarCampos(): Boolean {
        email = binding.etEmail.text.toString().trim()
        contraseña = binding.etContrasena.toString().trim()

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.error="ERROR. Debe poner un email válido."
            return false
        }

        if(contraseña.length < 8){
            binding.etContrasena.error="ERROR. La contraseña debe tener al menos ocho caracteres"
            return false
        }
        return true
    }

    private fun registrar() {
        if(!comprobarCampos()) return

        auth.createUserWithEmailAndPassword(email, contraseña)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    login()
                }
            }

            .addOnFailureListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun login() {
        if(!comprobarCampos()) return

        auth.signInWithEmailAndPassword(email, contraseña)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    if(email.equals("admin@admin.es")){
                        preferences.setAdmin(true)
                    }

                    iniciarActivityPortal()
                }
            }

            .addOnFailureListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun iniciarActivityPortal() {
        startActivity(Intent(this, PortalActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
        val usuario = auth.currentUser
        if(usuario!=null){
            isAdmin = preferences.isAdmin()

            iniciarActivityPortal()
        }
    }
}