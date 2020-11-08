package com.example.mvvmbasicprep.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FakeQuoteDao {
    private val quoteList = mutableListOf<Quote>()
    // mutableListOf
    private val quotes = MutableLiveData<List<Quote>>()  // 바뀌면 알아서 업데이터 되는 데이터

    init {
        quotes.value = quoteList
    }

    fun addQuotes(quote: Quote) {
        quoteList.add(quote)
        quotes.value = quoteList
    }

    fun getQuotes() = quotes as LiveData<List<Quote>> // SingleTon : ......???

}