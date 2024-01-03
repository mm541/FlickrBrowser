package com.example.flickrbrowser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flickrbrowser.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var _binding:ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val toolbarSearch = _binding.toolbarSearch
        setSupportActionBar(toolbarSearch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}