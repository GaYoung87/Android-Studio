package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

// class는 상속
// extends는 부모의 메소드를 그대로 사용할 수 있고, 오버라이딩 필요없이 부모에 구현되어있는 것을 직접 사용 가능
public class MainActivity extends AppCompatActivity {
    // public: 모두 접근 허용: 같은 패키지 + 같은 클래스 -> 허용
    //                        같은 패키지 + 다른 클래스 -> 허용
    //                        다른 패키지              -> 허용
    // default: 같은 패키지에서만 접근 허용: 같은 패키지 + 같은 클래스 -> 허용
    //                                    같은 패키지 + 다른 클래스 -> 허용
    //                                    다른 패키지              -> 허용 X
    // private: 같은 클래스에서만 접근 허용 : 같은 패키지 + 같은 클래스 -> 허용
    //                                     같은 패키지 + 다른 클래스 -> 허용 X
    //                                     다른 패키지              -> 허용 X
    // protected: 같은 클래스에서만 접근 허용 : 같은 패키지 + 같은 클래스 -> 허용
    //   (상속시 다른패키지에서 접근허용)        같은 패키지 + 다른 클래스 -> 허용
    //                                        다른 패키지 + 상속       -> 허용
    //                                        다른 패키지              -> 허용 X

    private SnakeView mSnakeView;
    private TextView mTime;
    private TextView mScore;
    private TextView mSpeed;

    // 만약 private  static final String 이라면 값 선언 후 수정할 수 없음
    // static: 이를 사용하는데 필요한 인스턴스 없음
    private static String ICICLE_KEY = "snake-view";

    @Override
    // Activity는 수명 주기 존재

    // onCreate 메서드는 Activity가 액티비티가 최초 생성될 때 호출됨
    // onCreate: 기기 화면에 표시되는 것을 정의하는 메소드
    public void onCreate(Bundle savedInstanceState) {
        // super = 상위 클래스 -> 상위 클래스의 onCreate 메소드를 먼저 호출하여 먼저 실행하고, 오버라이드된 메소드를 처리 한다는 의미!
        super.onCreate(savedInstanceState);

        // No Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);  // R파일, layout에 있는 파일, activity_main파일을 참조해 화면에 표시한다.
        mTime = findViewById(R.id.time);  // 선택기로부터 시간 조사
        mScore = findViewById(R.id.score);
        mSpeed = findViewById(R.id.speed);

        // Snake게임에서
        mSnakeView = (SnakeView) findViewById(R.id.snake);
        mSnakeView.setTextView((TextView) findViewById(R.id.text));
        // 기본값 설정
        mTime.setText("00 : 00 : 00");
        mScore.setText("Score : " + 0);
        mSpeed.setText("Delay : " + 0);
        mSnakeView.setTextView(mTime, mScore, mSpeed);

        if (savedInstanceState == null) {
            // 이제 시작했다면, 새로운 게임을 준비
            mSnakeView.setMode(SnakeView.READY);
        } else {
            // 안드로이드 UI 가 아니기 때문에 복원하는 코드가 들어간다. 안드로이드 UI는 자동 복원됨.
            // savedInstanceState: 예전으로 돌아갈 수 있도록하는 것
            //                     현재 탐색 또는 선택 정보를 원하면, 액티비티의 현재 인스턴스와 연결된 상태를 저장하기 위한 것
            Bundle map = savedInstanceState.getBundle(ICICLE_KEY);

            // map이 존재하면 상태 복원
            // 존재하지 않는다면 SnakeView를 멈추겠다??????????
            if (map != null) {
                mSnakeView.restoreState(map);
            } else {
                mSnakeView.setMode(SnakeView.PAUSE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 게임을 activity와 함께 멈추겠다
        mSnakeView.setMode(SnakeView.PAUSE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // 게임상태를 저장하겠다.
        super.onSaveInstanceState(outState);
        outState.putBundle(ICICLE_KEY, mSnakeView.saveState());
    }

    /*
     * 1 => NORTH
     * 2 => SOURTH
     * 3 => EAST
     * 4 => WEST
     */

    public void onUpClicked(View v) {
        mSnakeView.processKey(1);
    }
    public void onDownClicked(View v) {
        mSnakeView.processKey(2);
    }
    public void onRightClicked(View v) {
        mSnakeView.processKey(3);
    }
    public void onLeftClicked(View v) {
        mSnakeView.processKey(4);

    }
}
