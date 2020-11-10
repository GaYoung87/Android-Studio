package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // R.layout = res.layout

        Log.d("abcd", "on create");  // 제대로 나왔는지 확인하려면 tag인 abcd를 검색하면 된다.
        println("on create")
    }

    override fun onStart() {
        super.onStart()
        Log.d("abcd", "on start")  // kotlin은 뒤에 ;안붙여도 괜찮다.

    }

    override fun onResume() {
        super.onResume()
        Log.d("abcd", "on resume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("abcd", "on restart")
    }

    override fun onPause() {
        super.onPause()
        Log.d("abcd", "on pause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("abcd", "on stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("abcd", "on destroy")
    }
    // 시작 후 back버튼 : start -> resume -> pause -> stop -> destroy
    // 시작 후 home버튼 : start -> resume -> pause -> stop -> restart -> start -> resume
}