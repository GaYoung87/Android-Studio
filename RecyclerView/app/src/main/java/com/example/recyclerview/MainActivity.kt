package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private val myfragment by lazy { MainFragment() }  // 아까 만든것에 대한 instance생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.activity_main, myfragment).commit()  // commit을 붙여야 가능
    }
}

