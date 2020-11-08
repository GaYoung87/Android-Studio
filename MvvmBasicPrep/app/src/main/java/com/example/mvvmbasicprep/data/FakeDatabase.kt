package com.example.mvvmbasicprep.data

class FakeDatabase private constructor() {

    var quoteDao = FakeQuoteDao()
        private set
    // repository, database -> singletom
    // repository에서는 이미 있는 것은 가지고올필요가없고 없는것은 가지고온다는 것을 판단하기 위함

    // Room을 사용하면 안씀
    // singleton으로 만들기 위해 씀
    companion object {  // java에서 final과 동일
        @Volatile
        private var instance: FakeDatabase? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: FakeDatabase().also { instance = it } // it은 .앞에있는것까지를 칭한다 -> FakeDatabase
            }
    }

}