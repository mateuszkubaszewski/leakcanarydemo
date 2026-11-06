package com.example.leakcanarydemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.leakcanarydemo.databinding.ActivitySecondBinding

/**
 * Second view – LEAKS ITSELF.
 *
 * The Activity registers itself as a listener in the process-wide [LeakyRegistry]
 * but never unregisters in onDestroy(). After pressing Back the Activity is destroyed,
 * yet it remains reachable: LeakyRegistry.INSTANCE -> listeners (ArrayList) -> SecondActivity
 * (together with its whole view hierarchy and binding).
 *
 * LeakCanary (and the "Find Memory Leaks with LeakCanary" profiler task) reports it as
 * a retained / leaking Activity instance (mDestroyed = true).
 */
class SecondActivity : AppCompatActivity(), LeakyRegistry.Listener {

    private lateinit var binding: ActivitySecondBinding

    // Some extra weight so the leak is clearly visible in the heap dump (~5 MB per instance).
    @Suppress("unused")
    private val bigBuffer = ByteArray(5 * 1024 * 1024)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // BUG: registering `this` (the Activity) in a singleton...
        LeakyRegistry.register(this)

        binding.leakedCountText.text = getString(R.string.leaked_count, LeakyRegistry.listenerCount)
        binding.backButton.setOnClickListener { finish() }
    }

    override fun onEvent(message: String) {
        binding.leakedCountText.text = message
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LeakCanaryDemo", "SecondActivity destroyed – but still referenced by LeakyRegistry!")
        // FIX would be: LeakyRegistry.unregister(this)
    }
}
