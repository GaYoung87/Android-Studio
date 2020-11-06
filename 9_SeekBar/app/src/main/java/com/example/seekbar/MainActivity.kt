package com.example.seekbar

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { view ->
            textView.text = "seek1 : " + seekBar.progress
            textView2.text = "seek2 : " + seekBar2.progress
        }
        button2.setOnClickListener { view ->
            seekBar.incrementProgressBy(1)
            seekBar2.incrementProgressBy(1)
        }
        button3.setOnClickListener { view ->
            seekBar.incrementProgressBy(-1)
            seekBar2.incrementProgressBy(-1)
        }

        var listener = SeekListener()
        seekBar.setOnSeekBarChangeListener(listener)

        // override하는 것이 두개 이상이면 람다식으로 구현할 수 없음 -> 익명중첩클래스 사용
        // OnSeekBarChangeListener를 구현한 클래스만든다음에 그것의 객체 생성 후 매개변수에 넣음
        // 이것은 객체만들고 var해서 넣어준 것(위에 두줄)을 한번에 한 것
        seekBar2.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textView2.text = "seek2 : " + progress
            }

            // 값을 변경하기 위해 사용자가 터치했을 때
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            // 값을 변경한 후 터치를 때었을 때
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    inner class SeekListener:SeekBar.OnSeekBarChangeListener{
        // seekbar의 값이 변경되었을 때 seekbar, 현재값, 터치해서 값이바뀌면true/코드로 값이 바뀌면 false
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            textView.text = "seek1 : " + progress
        }

        // 값을 변경하기 위해 사용자가 터치했을 때
        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        // 값을 변경한 후 터치를 때었을 때
        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }

    }
}