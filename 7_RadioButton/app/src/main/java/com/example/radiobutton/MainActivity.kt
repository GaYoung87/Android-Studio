package com.example.radiobutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { view ->
            when (group1.checkedRadioButtonId) { // 현재 체크되어있는 id값을 가지고올 수 있음
                R.id.radioButton ->
                    textView.text = "라디오 1-1이 선택되었습니다."
                R.id.radioButton2 ->
                    textView.text = "라디오 1-2가 선택되었습니다."
                R.id.radioButton3 ->
                    textView.text = "라디오 1-3이 선택되었습니다."
            }

            /*
            when(group2.checkedRadioButtonId) {
                R.id.radioButton4 ->
                    textView.text = "라디오 2-1이 선택되었습니다."
                R.id.radioButton5 ->
                    textView.text = "라디오 2-2가 선택되었습니다."
                R.id.radioButton6 ->
                    textView.text = "라디오 2-3이 선택되었습니다."
            } */
        }

        // lambda 이용
        group2.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton4 ->
                    textView.text = "라디오 2-1이 선택되었습니다."
                R.id.radioButton5 ->
                    textView.text = "라디오 2-2가 선택되었습니다."
                R.id.radioButton6 ->
                    textView.text = "라디오 2-3이 선택되었습니다."
            }

            button2.setOnClickListener { view ->
                radioButton2.isChecked = true;
                radioButton6.isChecked = true;
            }

        }
    }

    inner class RadioListener:RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {  // Int:라디오 값
            when(group?.id){
                R.id.group1 ->
                    when(checkedId){
                        R.id.radioButton ->
                            textView.text = "체크 이벤트 : 라디오 1-1 체크"
                        R.id.radioButton2 ->
                            textView.text = "체크 이벤트 : 라디오 1-2 체크"
                        R.id.radioButton3 ->
                            textView.text = "체크 이벤트 : 라디오 1-3 체크"
                    }
                R.id.group2 ->
                    when(checkedId){
                        R.id.radioButton4 ->
                            textView.text = "체크 이벤트 : 라디오 2-1 체크"
                        R.id.radioButton5 ->
                            textView.text = "체크 이벤트 : 라디오 2-2 체크"
                        R.id.radioButton6 ->
                            textView.text = "체크 이벤트 : 라디오 2-3 체크"
                    }
            }
        }
    }
}