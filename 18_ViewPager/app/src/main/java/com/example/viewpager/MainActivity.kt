package com.example.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var view_list = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_list.add(layoutInflater.inflate(R.layout.view1, null))
        view_list.add(layoutInflater.inflate(R.layout.view2, null))
        view_list.add(layoutInflater.inflate(R.layout.view3, null))

        pager.adapter = CustomAdapter()

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                textView.text = "${position} 번재 뷰가 나타났습니다"
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageSelected(position: Int) {

            }
        })
    }

    inner class CustomAdapter : PagerAdapter(){
        override fun getCount(): Int {
            return view_list.size
        }

        // 현재 객체가 보여줄 객체와 일치하는지
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`  // view와 `object`가 일치할 때만 화면상에 보여주겠다.
        }

        // 항목구성을 위해 호출하는 것
        // 여기 안에서 보여주고자하는 View를 pager에 집어넣고 반환
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            pager.addView(view_list[position])  // view_list[position]은 isViewFromObject의 view로 들어가고
            return view_list[position]  // view_list[position]은 isViewFromObject의 `object`로 들어간다
        }

        // 이것이 없으면 정상작동 못함
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            pager.removeView(`object` as View)  // `object`를 View로 형변환 한 후 제거해라
        }
    }
}