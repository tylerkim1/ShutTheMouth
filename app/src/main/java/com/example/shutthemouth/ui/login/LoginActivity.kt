package com.example.shutthemouth.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shutthemouth.ApiObject
import com.example.shutthemouth.MainActivity
import com.example.shutthemouth.R
import com.example.shutthemouth.User
import com.example.shutthemouth.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val RC_SIGN_IN = 123
const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var imageView: ImageView

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignInResult(task)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding.loginGif1
        Glide.with(this).load(R.raw.bug).into(imageView)
        imageView = binding.loginGif2
        Glide.with(this).load(R.raw.dancingman).into(imageView)
        imageView = binding.loginGif3
        Glide.with(this).load(R.raw.duck).into(imageView)
        imageView = binding.loginGif4
        Glide.with(this).load(R.raw.groot).into(imageView)
        imageView = binding.loginGif5
        Glide.with(this).load(R.raw.jola).into(imageView)
        imageView = binding.loginGif6
        Glide.with(this).load(R.raw.squid).into(imageView)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("550310973720-eisrmf6ve2n44pjvvqn7ofg4ip0uu2o2.apps.googleusercontent.com")  // Replace "YOUR_SERVER_CLIENT_ID" with your actual server client ID
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if (account != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // 아직 로그인하지 않은 사용자
            binding.loginSignInButton.visibility = View.VISIBLE
            binding.loginText.visibility = View.GONE
            binding.loginSignInButton.setSize(SignInButton.SIZE_STANDARD)
            binding.loginSignInButton.setOnClickListener {
                loginWithGoogle()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    fun loginWithGoogle() {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("550310973720-ttnclqac9drqkkth45unts22bckanjpj.apps.googleusercontent.com")
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent: Intent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            if (completedTask.isSuccessful) {
                val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
                val token: String = account?.idToken!!

                // Use your token
                val user = User().apply {
                    this.key = token
                }
                Log.d(TAG, user.key.toString())

                val call = ApiObject.getRetrofitService.isUserExist(user)
                call.enqueue(object: Callback<Boolean> {
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if (response.isSuccessful && response.body() == true) {
                            // User exists,
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // User does not exist,
                            val intent = Intent(this@LoginActivity, SetnameActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        // Handle network failure
                        Log.e(TAG, "Network request failed", t)
                    }
                })

            } else {
                Log.e("AUTH", "Google Auth failed: ${completedTask.exception}")
            }
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            binding.loginSignInButton.visibility = View.VISIBLE
            binding.loginText.visibility = View.GONE
        }
    }
}