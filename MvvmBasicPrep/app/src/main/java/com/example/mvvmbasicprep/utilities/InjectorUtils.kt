package com.example.mvvmbasicprep.utilities

import com.example.mvvmbasicprep.data.FakeDatabase
import com.example.mvvmbasicprep.data.QuoteRepository
import com.example.mvvmbasicprep.ui.quotes.QuotesViewModelFactory

object InjectorUtils {

    fun provideQuotesViewModelFactory(): QuotesViewModelFactory {
        val quoteRepository = QuoteRepository.getInstance(FakeDatabase.getInstance().quoteDao)
        return QuotesViewModelFactory(quoteRepository)
    }
}