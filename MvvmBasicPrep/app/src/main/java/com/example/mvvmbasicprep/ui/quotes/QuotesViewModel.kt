package com.example.mvvmbasicprep.ui.quotes

import androidx.lifecycle.ViewModel
import com.example.mvvmbasicprep.data.Quote
import com.example.mvvmbasicprep.data.QuoteRepository

// quoteRepository: db를 어디서 가지고올지 판단하는 역할도한다.
class QuotesViewModel(private val quoteRepository: QuoteRepository)
    : ViewModel() { // 기존에 있는 ViewModel을 extend함
    // ViewModel도 하나만 존재해야함 -> ViewModel factory가 있다

    fun getQuotes() = quoteRepository.getQuotes()

    fun addQuotes(quote: Quote) = quoteRepository.addQuote(quote)

}

