package com.example.leakcanarydemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.leakcanarydemo.databinding.ActivityMainBinding

/** First view – only navigates to [SecondActivity]. No leak here. */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.openSecondButton.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.leakedCountText.text = getString(R.string.leaked_count, LeakyRegistry.listenerCount)
    }
}
