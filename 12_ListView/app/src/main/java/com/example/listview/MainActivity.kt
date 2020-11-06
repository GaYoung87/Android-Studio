package com.example.listview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // 항목 하나에 문자열 하나 보여주는 리스트
    var data = arrayOf("리스트1","리스트2","리스트3","리스트4","리스트5","리스트6",
        "리스트7","리스트8","리스트9","리스트10","리스트11","리스트12")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        listView.adapter = adapter

//        var listener = ListListener()
//        listView.setOnItemClickListener(listener)
        listView.setOnItemClickListener { parent, view, position, id ->
            textView.text = data[position]
        }
    }

    inner class ListListener : AdapterView.OnItemClickListener{
        // position : 사용자가 터치한 항목의 인덱스번호
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            textView.text = data[position]
        }
    }
}