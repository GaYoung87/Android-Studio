package com.example.progressbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { view ->
            progressBar4.incrementProgressBy(5)  // 5씩 증가함
        }

        button2.setOnClickListener { view ->
            progressBar4.incrementProgressBy(-5)  // 5씩 감소함
        }

        button3.setOnClickListener { view ->
            progressBar4.progress = 50
        }
    }
}