package com.example.customlistview2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SimpleAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var imgRes = intArrayOf(R.drawable.imgflag1, R.drawable.imgflag2, R.drawable.imgflag3,R.drawable.imgflag4,
        R.drawable.imgflag5, R.drawable.imgflag6, R.drawable.imgflag7, R.drawable.imgflag8)  // int array인 경우 intArrayOf사용

    var data1 = arrayOf("토고","프랑스","스위스","스페인","일본","독일","브라질","대한민국")
    var data2 = arrayOf("togo","france","swiss","spain","japan","german","brazil","korea") // imgRes와 갯수가 일치해야함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var list = ArrayList<HashMap<String, Any>>()

        var idx = 0
        while(idx < data1.size) {  // 데이터만 집어넣은 상태
            var map = HashMap<String, Any>()

            map.put("flag", imgRes[idx])
            map.put("data1", data1[idx])
            map.put("data2", data2[idx])

            list.add(map)
            idx++
        }

        // 어떤 데이터를 어디에 집어넣겠다고 정하는 것
        var key = arrayOf("flag", "data1", "data2")
        var ids = intArrayOf(R.id.imageView, R.id.textView2, R.id.textView3)

        // layout file을 이용해 항목 하나를 만들고 진행
        var adapter = SimpleAdapter(this, list, R.layout.row, key, ids)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            textView.text = data1[position]
        }
    }
}