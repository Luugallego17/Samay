package com.samay.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.samay.app.data.prefs.OnboardingPrefs
import com.samay.app.navigation.SamayNavHost
import com.samay.app.ui.theme.SamayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SamayTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val prefs = remember { OnboardingPrefs(applicationContext) }
                    // null = todavía cargando el flag; cuando llega, decidimos Welcome vs Home.
                    val done by produceState<Boolean?>(initialValue = null, prefs) {
                        prefs.onboardingDone.collect { value = it }
                    }
                    done?.let { SamayNavHost(onboardingDone = it) }
                }
            }
        }
    }
}
