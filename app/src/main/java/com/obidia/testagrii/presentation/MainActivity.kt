package com.obidia.testagrii.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.obidia.testagrii.R.layout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)
  }
}
