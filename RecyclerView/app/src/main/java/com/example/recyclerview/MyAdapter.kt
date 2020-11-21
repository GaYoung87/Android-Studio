package com.example.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.my_container.view.*

// adapter쓸 때 item형식의 데이터들을 리스트형태로 넘긴다.
class MyAdapter(private val items: List<MyItem>) : RecyclerView.Adapter<MyAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.my_container, parent, false)  // R.layout.item은 아이템 하나하나

        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item :MyItem) {
            itemView.myTitle.text = item.title
            itemView.myText.text = item.text
        }
    }
}

