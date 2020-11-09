package com.example.customlistview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var data = arrayOf("데이터1", "데이터2", "데이터3", "데이터4", "데이터5", "데이터6")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // android.R.layout.simple_list_item_1 : 항목 하나를 구성하기 위해 사용한 화면의 모양을 만든 layout모양
        // var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)  // 안드로이드os가 알고있는 것 사용
        // 문자열 하나만 셋팅할 경우에는 ArrayAdapter사용
        var adapter = ArrayAdapter(this, R.layout.row1, R.id.textView2, data)  // 안드로이드 os가 알고있는 이름(id)로 두었다고 해도, 파일을 내가 만들면 직접 이름을 고쳐줘야함
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            textView.text = data[position]
        }
    }
}