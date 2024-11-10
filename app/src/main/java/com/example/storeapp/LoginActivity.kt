package com.example.storeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Configuración de Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Inicializa el ActivityResultLauncher
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }

        // Referencias a las vistas
        val emailEditText = findViewById<EditText>(R.id.etEmail)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val loginGoogleButton = findViewById<Button>(R.id.btnLoginGoogle)

        // Lista de usuarios predefinidos
        val usuarios = listOf(
            Usuario("admin@example.com", "admin123", "admin"),
            Usuario("user@example.com", "user123", "user"),
            Usuario("guest@example.com", "guest123", "user")
        )

        // Lógica para inicio de sesión normal
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Email y contraseña no pueden estar vacíos")
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Por favor, ingresa un correo electrónico válido")
                return@setOnClickListener
            }

            val usuario = usuarios.find { it.correo == email && it.contrasena == password }
            if (usuario != null) {
                showToast("Inicio de sesión exitoso. Rol: ${usuario.rol}")
                when (usuario.rol) {
                    "admin" -> startActivity(Intent(this, AddProductActivity::class.java))
                    "user" -> startActivity(Intent(this, HomeActivity::class.java))
                    else -> showToast("Rol no reconocido")
                }
            } else {
                showToast("Correo o contraseña incorrectos")
            }
        }

        // Lógica para inicio de sesión con Google
        loginGoogleButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    // Método para manejar el inicio de sesión con Google
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    // Procesa el resultado del inicio de sesión de Google
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                showToast("Inicio de sesión con Google exitoso. Email: ${account.email}")
                startActivity(Intent(this, HomeActivity::class.java)) // Redirige al usuario
            }
        } catch (e: ApiException) {
            Log.w("LoginActivity", "signInResult:failed code=" + e.statusCode)
            showToast("Falló el inicio de sesión con Google")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
