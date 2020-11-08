package com.example.mvvmbasicprep.data

// 데이터만 넣는 곳
data class Quote(val quoteText: String, val author: String) {
    override fun toString(): String { // override:기본적으로 있는 애들을 이용하겠다..?
        return "$quoteText - $author"  // 화면에서 넣는 값들을 val로 데이터설정은 한 것이고
        // toString은 어떤거로 들어와도 다 String으로 들어오는데 return 모양으로 바꿔서 보여준다.
    }
}