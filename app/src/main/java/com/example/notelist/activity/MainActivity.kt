package com.example.notelist.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.core.Constants.APP_ACTIVITY
import com.example.notelist.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY = this
    }
}