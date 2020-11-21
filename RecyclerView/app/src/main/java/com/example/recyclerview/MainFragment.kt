package com.example.recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(){

    val testList = listOf(
        MyItem(
            R.drawable.ic_launcher_foreground,
            "TITLE1111111",
            "TEXT1111111"
        ),
        MyItem(
            R.drawable.ic_launcher_foreground,
            "TITLE2222222",
            "TEXT222222"
        ),
        MyItem(
            R.drawable.ic_launcher_foreground,
            "TITLE333333",
            "TEXT3333333"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // val : 불변 값, var : 초기화 후 변경 가능
        val view = inflater.inflate(R.layout.fragment_main, container,false)  // 변수 선언
//        return super.onCreateView(R.layout.fragment_main, container, savedInstanceState)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_test.adapter = MyAdapter(testList)
    }
}
