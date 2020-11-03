package com.example.button

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 방법1. class 만들기 -> var로 객체 생성 -> 실행시키기
       var listener1 = BtnListener();
        button.setOnClickListener(listener1)

        var listener2 = BtnListener2();
        button2.setOnClickListener(listener2)

        var listener3 = BtnListener3();
        button3.setOnClickListener(listener3)
        button5.setOnClickListener(listener3)

        // 방법2. lambda 이용
        button6.setOnClickListener { view ->  // 여기서 view는 class에서 (v: View?)의미
            textView.text = "다섯 번째 버튼을 눌렀습니다"
        }
        button7.setOnClickListener { view ->  // 여기서 view는 class에서 (v: View?)의미
            textView.text = "여섯 번째 버튼을 눌렀습니다"
        }

        var listener4 = View.OnClickListener { view ->
            when(view.id) {
                R.id.button8 ->
                    textView.text = "일곱 번째 버튼을 눌렀습니다"
                R.id.button9 ->
                    textView.text = "여덟 번째 버튼을 눌렀습니다"
            }
        }
        button8.setOnClickListener(listener4)
        button9.setOnClickListener(listener4)
    }
    // 중첩클래스를 만들기 위해서는 앞에 inner를 붙인다.
    inner class BtnListener:View.OnClickListener{

        override fun onClick(v: View?) {
            // 버튼을 누르면, textView가 "첫 번째 버튼을 눌렀습니다"로 바뀐다.
            textView.text = "첫 번째 버튼을 눌렀습니다"
        }
    }

    inner class BtnListener2:View.OnClickListener{
        override fun onClick(v: View?) {
            textView.text = "두 번째 버튼을 눌렀습니다"
        }
    }

    // 하나의 클래스에서 모든 것을 다 처리하겠다
    inner class BtnListener3:View.OnClickListener{
        override fun onClick(v: View?) {
            when(v?.id){ // java의 switch함수
                R.id.button3 ->
                    textView.text = "세 번째 버튼을 눌렀습니다"
                R.id.button5 ->
                    textView.text = "네 번째 버튼을 눌렀습니다"
            }
        }
    }
}