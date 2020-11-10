package com.example.checkbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { view ->
            textView.text = ""

            if (checkBox.isChecked == true) {  // check되어있으면 true
                textView.append("체크박스1이 체크되었습니다\n")  // append하면 문자열 뒤에 계속 붙어나옴
            }
            if (checkBox2.isChecked == true) {  // check되어있으면 true
                textView.append("체크박스2가 체크되었습니다\n")  // append하면 문자열 뒤에 계속 붙어나옴
            }
            if (checkBox3.isChecked == true) {  // check되어있으면 true
                textView.append("체크박스3이 체크되었습니다\n")  // append하면 문자열 뒤에 계속 붙어나옴
            }
        }

        button2.setOnClickListener { view ->
            checkBox.isChecked = true;
            checkBox2.isChecked = true;
            checkBox3.isChecked = true;
        }

        button3.setOnClickListener { view ->
            checkBox.isChecked = false;
            checkBox2.isChecked = false;
            checkBox3.isChecked = false;
        }

        button4.setOnClickListener { view ->
            checkBox.toggle()
            checkBox2.toggle()
            checkBox3.toggle()
        }

        // class 이용
        var listener1 = CheckBoxListener()
        checkBox.setOnCheckedChangeListener(listener1)

        // lambda 이용
        checkBox2.setOnCheckedChangeListener { CompoundButton, b ->
            if (b == true) {
                textView.text = "이벤트 : 체크박스2가 체크되었습니다"
            } else {
                textView.text = "이벤트 : 체크박스2가 체크 해제되었습니다"
            }
        }

        checkBox3.setOnCheckedChangeListener { CompoundButton, b ->
            if (b == true) {
                textView.text = "이벤트 : 체크박스3이 체크되었습니다"
            } else {
                textView.text = "이벤트 : 체크박스3이 체크 해제되었습니다"
            }
        }
    }

    inner class CheckBoxListener : CompoundButton.OnCheckedChangeListener{
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
            if (p1 == true) {
                textView.text = "이벤트 : 체크박스1이 체크되었습니다"
            } else {
                textView.text = "이벤트 : 체크박스1이 체크 해제되었습니다"
            }
        }
    }
}