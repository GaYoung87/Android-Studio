package com.example.edittext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { view ->
            textView.text = editTextTextPersonName.text
        }

        button2.setOnClickListener { view ->
            editTextTextPersonName.setText("문자열")  // string이 아니라 editable이라는 객체로 받음
            // editTextTextPersonName.setText("") : 기존입력 내용이 없어지게 됨
        }

//        var listener1 = EnterListener()
//        editTextTextPersonName.setOnEditorActionListener(listener1)

        // lambda로 표현
        editTextTextPersonName.setOnEditorActionListener { v, actionId, event ->
            textView.text = editTextTextPersonName.text
            false
        }

//        var watcher = EditWatcher()
//        editTextTextPersonName.addTextChangedListener(watcher)

        editTextTextPersonName.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textView.text = s
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    inner class EnterListener:TextView.OnEditorActionListener{
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {  // Boolean은 반환값
            textView.text = editTextTextPersonName.text;
            return true; // 모든걸 처리하고 키보드 안내리면 return true, 내리면 return false
        }
    }

    inner class EditWatcher:TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            textView.text = s
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
}