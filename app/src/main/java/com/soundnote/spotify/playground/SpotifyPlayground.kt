package com.soundnote.spotify.playground

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.soundnote.R
import com.soundnote.databinding.ActivitySpotifyPlaygroundBinding
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class SpotifyPlayground : AppCompatActivity() {


//    private val clientId = "f00e7e19bec74c3bbf53e7b9c921fca1"
    private val redirectUri = "yourcustomprotocol://callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private var token: String? = null

    private val REQUEST_CODE: Int = 1337
    private val REDIRECT_URI: String = "yourcustomprotocol://callback"

    lateinit var binding: ActivitySpotifyPlaygroundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySpotifyPlaygroundBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val extras = intent.data
        getToken(intent)

//        spotifyAppRemote = SpotifyAppRemote()

        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams, object: Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("SpotifyPlayground", "Connectes!! Yay")
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("SpotifyPlayground", throwable.message, throwable)
            }
        })

        val builder = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri)

        builder.setScopes(arrayOf("streaming", " app-remote-control"))

        val authRequest = builder.build()



        binding.spotifyAuth.setOnClickListener {
//            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, authRequest)
//        AuthorizationClient.openLoginInBrowser(this, authRequest);

//            spotifyAppRemote?.playerApi?.play("spotify:playlist:37i9dQZF1DX7K31D69s4M1")

            spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
                val track = it.track
                Log.d("Track Uri", track.uri)
                Log.d("Track", track.name)
                val time = it.playbackPosition
                Log.d("time", time.toString())
            }

        }
    }

    fun getToken(intent: Intent){
        val uri = intent.data

        uri?.let {
            val response = AuthorizationResponse.fromUri(uri)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Log.d("Token", response.accessToken)
                    token = response.accessToken
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.d("Token error", response.error)
                }
                else -> {
                    Log.d("Token something", response.type.toString())
                }
            }
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        getToken(intent)
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)

        if(requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Log.d("Token", response.accessToken)
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.d("Token error", response.error)
                }
                else -> {
                    Log.d("Token something", response.type.toString())
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }
}