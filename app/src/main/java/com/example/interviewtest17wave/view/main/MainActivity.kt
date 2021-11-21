package com.example.interviewtest17wave.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.interviewtest17wave.databinding.ActivityMainBinding
import com.example.mvvmcodebase.view.base.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelFactory() }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        setContentView(binding.root)
        Toast.makeText(this, "${binding.mainHello.text}", Toast.LENGTH_SHORT).show();
    }
}