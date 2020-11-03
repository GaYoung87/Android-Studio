package com.example.textview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // kotlin은 자료형에 관계없이 var를 이용
    var text2:TextView? = null;  // textView 클래스로부터 만든 객체의 주소값을 담을 참조변수 선언
    // 모든 view는 객체로 만들어서 메모리에 올라간다.
    // view의 주소값(ID속성)을 얻어와서 필요 작업 진행하면 된다.
    // kotlin은 변수선언하면 null을 담을 수 없음 -> 뒤에 ? 넣어라

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 주소값 받아오기
        text2 = findViewById<TextView>(R.id.textView2)  // ID가 textView2인 애를 가지고와서 TextView 클래스타입으로 변환시켜서 넘겨준다.
        text2?.text = "문자열2"  // kotlin에서는 .text하면 .setText호출한다. -> text2?.setText("문자열10")과 동일

        // kotlin에서는 ID = textView3이 그대로 변수가 자동으로 선언된다 -> 객체 주소값이 자동으로 저장됨
        // 주소값 받는 작업 필요없이 바로 진행
        // build.gradle에 id 'kotlin-android-extensions' 추가해야 변수가 객체 주소값으로 자동으로 저장
        textView3.text = "문자열3"
    }
}