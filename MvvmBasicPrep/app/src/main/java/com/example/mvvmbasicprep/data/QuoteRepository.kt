package com.example.mvvmbasicprep.data

class QuoteRepository private constructor(private val quoteDao: FakeQuoteDao){   // 의존성주입을 위해 quoteDao를 삽입해준 것. 넘겨받는 instance를 여기서 만드는게 좋음

    fun addQuote(quote: Quote) { //Dao에 연결된 애를 가지고옴
        quoteDao.addQuotes(quote)
    }

    fun getQuotes() = quoteDao.getQuotes()

    // FakeDatabase랑 FakeQuoteDao랑 연결하는 것이 QuoteRepository
    companion object {  // java에서 final과 동일
        @Volatile
        private var instance: QuoteRepository? = null

        //repository는 viewmodel과 model 사이에 있음
        fun getInstance(quoteDao: FakeQuoteDao) =
            instance ?: synchronized(this) {
                instance ?: QuoteRepository(quoteDao).also { instance = it } // it은 .앞에있는것까지를 칭한다 -> FakeDatabase
            }
    }

}