package com.example.customadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row.*

class MainActivity : AppCompatActivity() {

    var data = arrayOf("데이터1","데이터2","데이터3","데이터4","데이터5","데이터6")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // listView구성할 때, 항목 하나에 셋팅할 데이터가 하나면 arrayAdapter, 2개 이상이면 simpleAdapter
//        var adapter = ArrayAdapter<String>(this, R.layout.row, R.id.textView2, data)
//        listView.adapter = adapter

        // 버튼마다 listener 설정
        var adapter = ListAdapter()
        listView.adapter = adapter

    }

    inner class ListAdapter : BaseAdapter() {

        var listener = BtnListener()

        override fun getCount(): Int {
            return data.size
        }

        // 항목을 구성하기위해 만든 view객체 반환
        override fun getItem(position: Int): Any? {
            return null
        }

        // 항목을 대표하는 id값 반환
        override fun getItemId(position: Int): Long {
            return 0
        }

        // 항목 하나를 호출하기 위해 사용
        /*
        data가 길면 스크롤이 내려감 -> 보였던 것이 안보이고, 안보였던 것이 보이게 됨
        이것을 버리지않고 convertView: View?로 가지고온다(재사용) -> View를 이용해 무제한으로 사용
        */
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var convertView:View? = convertView // 재사용가능한 View가 없으면 convertView에 null값이 들어감

            if (convertView == null) {  // 재사용가능한 View가 없으면 새로 만들어준다.
                convertView = layoutInflater.inflate(R.layout.row, null)
            }

            // convertView객체로부터 id가 textView2인 값을 추출한다.
            var text:TextView? = convertView?.findViewById<TextView>(R.id.textView2)
            // 여기서의 버튼은 항목마다 있는 버튼을 의미
            var button1:Button? = convertView?.findViewById<Button>(R.id.button1)
            var button2:Button? = convertView?.findViewById<Button>(R.id.button2)

            button1?.setOnClickListener(listener)
            button2?.setOnClickListener(listener)

            // 몇번째 list인지 지정
           button1?.tag = position
            button2?.tag = position

            text?.text = data[position]

            return convertView
        }
    }

    inner class BtnListener : View.OnClickListener{
        // 사용자가 누를 버튼이 어떤버튼인지 알아야 처리가능 -> 버튼 값으로 분개
        override fun onClick(v: View?) { // v = 버튼의 주소값

            var idx = v?.tag as Int
            when(v?.id) {
                R.id.button1 ->
                    textView.text = "${idx} : 첫 번째 버튼을 눌렀습니다.\n"

                R.id.button2 ->
                    textView.text = "${idx} : 두 번째 버튼을 눌렀습니다.\n"
            }
        }
    }
}