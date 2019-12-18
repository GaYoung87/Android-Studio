package com.example.snake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static com.example.snake.TileView.mXTileCount;
import static com.example.snake.TileView.mYTileCount;

public class SnakeView {

    private static final String TAG = "SnakeView";

    /**
     * Current mode of application(현재 모드): READY to run, RUNNING, or you have already lost
     * reason why static final ints are used instead of an enum for performance
     * 성능을 위해 enum대신 int로 최종입력이 됨
     */
    // 현재 상태
    // 여기서 READY, PAUSE를 설정함에 따라 MainActivity에서 작동 가능
    private boolean isStart = false;
    private int mMode = READY;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;

    /**
     * Current direction the snake is headed.
     */
    // 방향
    private int mGameTime = 0;
    private int mDirection = NORTH;
    private int mNextDirection = NORTH;
    private static final int NORTH = 1;
    private static final int SOUTH = 2;
    private static final int EAST = 3;
    private static final int WEST = 4;

    /**
     * Labels for the drawables that will be loaded into the TileView class
     * TileView 로 전해질 변수
     */
    private static final int RED_STAR = 1;
    private static final int YELLOW_STAR = 2;
    private static final int GREEN_STAR = 3;

    /**
     * mScore: 잡힌 사과 수 추적하는데 사용됨 -> 사과를 얼마나 먹었는지
     * mMoveDelay: 사과먹으면 delay 감소, 밀리초단위 뱀의 움직임
     */
    private long mScore = 0;  // 잡힌 사과 수 추적하는데 사용됨 -> 사과 먹으면 점수 올림
    private long mMoveDelay = 200;  // 사과먹으면 delay 감소, 밀리초단위 뱀의 움직임

    /**
     * mLastMove: tracks the absolute time when the snake last moved, and is used to determine if a move should be made based on mMoveDelay.
     * 뱀이 마지막으로 움직인 시간 추적, 이동방향 결정하는데 사용됨 / moveDelay에 기초했는지 판단
     */
    private long mLastMove;

    /**
     * mStatusText: text shows to the user in some run states
     * user상황 보여줌
     */
    private TextView mStatusText;
    private TextView mTextTime;
    private TextView mTextScore;
    private TextView mTextSpeed;

    /**
     * mSnakeTrail: a list of Coordinates that make up the snake's body
     * mAppleList: the secret location of the juicy apples the snake craves
     * 뱀과 사과의 위치
     */
    private ArrayList<Coordinate> mSnakeTrail = new ArrayList<Coordinate>();
    private ArrayList<Coordinate> mAppleList = new ArrayList<Coordinate>();

    /**
     * Everyone needs a little randomness in their life -> 랜덤(사과 위치는 랜덤)
     */
    private static final Random RNG = new Random();

    /**
     * Create a simple handler that we can use to cause animation to happen.  We
     * set ourselves as a target and we can use the sleep()
     * function to cause an update/invalidate to occur at a later date.
     * 애니메이션을 발생시키는데 사용할 수 있는 간단한 handler 작성
     * 목표설정후 sleep()기능을 사용해 업데이트, 유효성 검사를 나중에 할 수 있음
     */

    // Thread : 프로그램 안에서 실행을 담당하는 하나의 흐름
    // MessageQueue : Message 를 담는 자료구조
    // Looper : MessageQueue -> Handler
    // 일어날 사건을 만들수 있는 handler(Thread 간의 통신 장치)
    private RefreshHandler mRedrawHandler = new RefreshHandler();

    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // 업데이트, 유효성검사 하게 메세지
            SnakeView.this.update();
            SnakeView.this.invalidate();
        }

        // sleep기능
        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };

    // message보내려고 handler만들었다
    // Thread간의 통신 -> Thread는 프로그램안에서 실행되는게 몇기의 흐름으로 나뉨. 보통 프로그램을 Thread라고 하고
    // Tread안에 message큐가 있음 그 메세지를 서로 보내는 방법이 handler
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int hh = mGameTime / 3600;
            int mm = (mGameTime % 3600) / 60;
            int sec = mGameTime % 60;

            String strTime = String.format("%02d : %02d : %02d", hh, mm, sec);
            sendEmptyMessageDelayed(0, 1000);
            mTextTime.setText(strTime);
            mGameTime++;
        }
    };

    /**
     * Constructs a SnakeView based on inflation from XML
     * XML의 인플레이션을 기준으로 SnakeView압축
     *
     * @param context
     * @param attrs
     */
    // 두개나 왜 쓰는거지?
    // CustomView
    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSnakeView();
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSnakeView();
    }

    // 개발자가 완벽히 알맞은 코드나 충돌 가능성이 있는 코드를 사용할때 @SuppressLint(...)를 사용
    @SuppressLint("ResourceType")
    private void initSnakeView() {
        setFocusable(true);

        Resources r = this.getContext().getResources();

        resetTiles(4);
        loadTile(RED_STAR, r.getDrawable(R.drawable.redstar));
        loadTile(YELLOW_STAR, r.getDrawable(R.drawable.yellowstar));
        loadTile(GREEN_STAR, r.getDrawable(R.drawable.greenstar));

    }

    // 새로운게임 초기화
    private void initNewGame() {
        mSnakeTrail.clear();
        mAppleList.clear();

        // 뱀 기본 위치
        mSnakeTrail.add(new Coordinate(7, 7));
        mSnakeTrail.add(new Coordinate(6, 7));
        mSnakeTrail.add(new Coordinate(5, 7));
        mSnakeTrail.add(new Coordinate(4, 7));
        mSnakeTrail.add(new Coordinate(3, 7));
        mSnakeTrail.add(new Coordinate(2, 7));
        // ??????????????????????????
        mNextDirection = NORTH;

        // Two apples to start with
        addRandomApple();
        addRandomApple();

        mMoveDelay = 200;
        mScore = 0;
    }

    // time, score, speed 보여주기
    public void setTextView(TextView time, TextView score, TextView speed) {
        mTextTime = time;
        mTextScore = score;
        mTextSpeed = speed;
    }

    /**
     * Given a ArrayList of coordinates, we need to flatten them into an array of
     * ints before we can stuff them into a map for flattening and storage.
     *
     * @param cvec : a ArrayList of Coordinate objects
     * @return : a simple array containing the x/y values of the coordinates
     * as [x1,y1,x2,y2,x3,y3...]
     */
    // 위치를 평면화 시키기 위한 함수 -> return : [x, y] 의 리스트
    // cvec : 2차원 좌표
    private int[] coordArrayListToArray(ArrayList<Coordinate> cvec) {
        int count = cvec.size();
        int[] rawArray = new int[count * 2];
        for (int index = 0; index < count; index++) {
            Coordinate c = cvec.get(index);
            rawArray[2 * index] = c.x;
            rawArray[2 * index + 1] = c.y;
        }
        return rawArray;
    }

    /**
     * Save game state so that the user does not lose anything
     * if the game process is killed while we are in the
     * background.
     *
     * @return a Bundle with this view's state
     */
    // 게임 정보 저장
    public Bundle saveState() {
        Bundle map = new Bundle();

        map.putIntArray("mAppleList", coordArrayListToArray(mAppleList));
        map.putInt("mDirection", Integer.valueOf(mDirection));
        map.putInt("mNextDirection", Integer.valueOf(mNextDirection));
        map.putLong("mMoveDelay", Long.valueOf(mMoveDelay));
        map.putLong("mScore", Long.valueOf(mScore));
        map.putInt("mGameTime", Integer.valueOf(mGameTime));
        map.putIntArray("mSnakeTrail", coordArrayListToArray(mSnakeTrail));

        return map;
    }

    /**
     * Given a flattened array of ordinate pairs, we reconstitute them into a
     * ArrayList of Coordinate objects
     *
     * @param rawArray : [x1,y1,x2,y2,...]
     * @return a ArrayList of Coordinates
     */
    // 평면화된 좌표를 다시 이차원상으로 구축
    private ArrayList<Coordinate> coordArrayToArrayList(int[] rawArray) {
        ArrayList<Coordinate> coordArrayList = new ArrayList<Coordinate>();

        int coordCount = rawArray.length;
        for (int index = 0; index < coordCount; index += 2) {
            Coordinate c = new Coordinate(rawArray[index], rawArray[index + 1]);
            coordArrayList.add(c);
        }
        return coordArrayList;
    }

    // 게임 정보 다시 불러오기
    public void restoreState(Bundle icicle) {
        setMode(PAUSE);

        mAppleList = coordArrayToArrayList(icicle.getIntArray("appleList"));
        mDirection = icicle.getInt("direction");
        mNextDirection = icicle.getInt("nextDirection");
        mMoveDelay = icicle.getLong("moveDelay");
        mScore = icicle.getLong("score");
        mGameTime= icicle.getInt("gameTime");
        mSnakeTrail = coordArrayToArrayList(icicle.getIntArray("snakeTrail"));
    }

    /*
     * handles key events in the game. Update the direction our snake is traveling
     * based on the DPAD. Ignore events that would cause the snake to immediately
     * turn back on itself.
     *
     * (non-Javadoc)
     *
     * @see android.view.View#onKeyDown(int, android.os.KeyEvent)
     */
    // Key Down Event
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            processKey(NORTH);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            processKey(SOUTH);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            processKey(WEST);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            processKey(EAST);
            return true;
        }

        return super.onKeyDown(keyCode, msg);
    }

    public void processKey(int dir) {
        if (dir == NORTH) {
            if (mMode == READY | mMode == LOSE) {
                // 게임이 READY상태이거나 지면 이전 게임은 끝나고 새로운 게임이 만들어져야함
                initNewGame();
                setMode(RUNNING);
                update();
                if(isStart == false) {
                    isStart = true;
                    mHandler.sendEmptyMessage(0);
                    mTextScore.setText("Score : " + mScore);
                    mTextSpeed.setText("Delay : " + mMoveDelay);
                }
                return;
            }

            // 일시정지시
            if (mMode == PAUSE) {
                setMode(RUNNING);
                update();
                if(isStart == false) {
                    isStart = true;
                    mHandler.sendEmptyMessage(0);
                    mTextScore.setText("Score : " + mScore);
                    mTextSpeed.setText("Delay : " + mMoveDelay);
                }
                return;
            }

            if (mDirection != SOUTH) {
                mNextDirection = NORTH;
                return;
            }
        }

        if (dir == SOUTH) {
            if (mDirection != NORTH) {
                mNextDirection = SOUTH;
            }
        }

        if (dir == WEST) {
            if (mDirection != EAST) {
                mNextDirection = WEST;
            }
        }

        if (dir == EAST) {
            if (mDirection != WEST) {
                mNextDirection = EAST;
            }
        }
    }

    /**
     * TextView는 사용자에게 GameOver와 같은말을 보여줌
     * res/values/strings.xml에 있다
     *
     * @param newView: 함수에서 이 변수가 사용된다는 것을 말함
     */
    public void setTextView(TextView newView) {
        mStatusText = newView;
    }

    /**
     * RUNNING / PAUSED / textview보여줘야하는 상태로 업데이트하기
     *
     * @param newMode
     */
    // 게임 모드에 따른 설정
    public void setMode(int newMode) {
        int oldMode = mMode;
        mMode = newMode;

        if (newMode == RUNNING & oldMode != RUNNING) {
            mStatusText.setVisibility(View.INVISIBLE);
            update();
            return;
        }

        Resources res = getContext().getResources();
        CharSequence str = "";
        if (newMode == PAUSE) {
            str = res.getText(R.string.mode_pause);
            if(isStart) {
                mHandler.removeMessages(0);
                isStart = false;
            }
        }
        if (newMode == READY) {
            str = res.getText(R.string.mode_ready);
        }
        if (newMode == LOSE) {
            str = res.getString(R.string.mode_lose_prefix) + mScore
                    + res.getString(R.string.mode_lose_suffix);
            if(isStart) {
                mHandler.removeMessages(0);
                isStart = false;
                mGameTime = 0;
            }
        }

        mStatusText.setText(str);
        mStatusText.setVisibility(View.VISIBLE);
    }

    /**
     * Selects a random location within the garden that is not currently covered by the snake.
     * Currently _could_ go into an infinite loop if the snake currently fills the garden, but we'll leave discovery of this prize to a truly excellent snake-player.
     * 뱀이 있지않은 곳 중에서 랜덤으로 위치 선택
     *
     */
    private void addRandomApple() {
        Coordinate newCoord = null;
        boolean found = false;
        while (!found) {

            // 새로운 위치
            int newX = 1 + RNG.nextInt(mXTileCount - 2);
            int newY = 1 + RNG.nextInt(mYTileCount - 2);
            newCoord = new Coordinate(newX, newY);

            // 충돌확인 -> 뱀과 겹치지 않게
            boolean collision = false;
            int snakelength = mSnakeTrail.size();
            for (int index = 0; index < snakelength; index++) {
                if (mSnakeTrail.get(index).equals(newCoord)) {
                    collision = true;
                }
            }

            // 충돌이 나지 않는다면 탈출!.
            found = !collision;
        }
        if (newCoord == null) {
            Log.e(TAG, "Somehow ended up with a null newCoord!");
        }
        mAppleList.add(newCoord);
    }

    /**
     * Handles the basic update loop, checking to see if we are in the running
     * state, determining if a move should be made, updating the snake's location.
     */
    // game mode 와, 움직임이 실제로 일어났는지를 확인해 뱀의 위치를 새로 지정
    public void update() {
        if (mMode == RUNNING) {
            long now = System.currentTimeMillis();

            if (now - mLastMove > mMoveDelay) {
                clearTiles();
                updateWalls();
                updateSnake();
                updateApples();
                mLastMove = now;
            }
            mRedrawHandler.sleep(mMoveDelay);
        }

    }

    // 벽 업데이트
    private void updateWalls() {
        for (int x = 0; x < mXTileCount; x++) {
            setTile(GREEN_STAR, x, 0);
            setTile(GREEN_STAR, x, mYTileCount- 1);
        }
        for (int y = 1; y < mYTileCount - 1; y++) {
            setTile(GREEN_STAR, 0, y);
            setTile(GREEN_STAR, mXTileCount - 1, y);
        }
    }

    /**
     * Draws some apples.
     *
     */
    // 사과 업데이트
    private void updateApples() {
        for (Coordinate c : mAppleList) {
            setTile(YELLOW_STAR, c.x, c.y);
        }
    }

    /**
     * Figure out which way the snake is going, see if he's run into anything (the
     * walls, himself, or an apple). If he's not going to die, we then add to the
     * front and subtract from the rear in order to simulate motion. If we want to
     * grow him, we don't subtract from the rear.
     *
     */
    // 뱀 업데이트
    // 뱀 위치 변경: 만약 죽지 않았다면, 뒤 하나의 위치를 앞으로 옮김, 만약 사과를 먹었다면 옮기지 x
    private void updateSnake() {
        boolean growSnake = false;

        // head, newHead 지정
        Coordinate head = mSnakeTrail.get(0);
        Coordinate newHead = new Coordinate(1, 1);

        mDirection = mNextDirection;

        switch (mDirection) {
            case EAST: {
                newHead = new Coordinate(head.x + 1, head.y);
                break;
            }
            case WEST: {
                newHead = new Coordinate(head.x - 1, head.y);
                break;
            }
            case NORTH: {
                newHead = new Coordinate(head.x, head.y - 1);
                break;
            }
            case SOUTH: {
                newHead = new Coordinate(head.x, head.y + 1);
                break;
            }
        }

        // 충돌
        if ((newHead.x < 1) || (newHead.y < 1) || (newHead.x > mXTileCount - 2)
                || (newHead.y > mYTileCount - 2)) {
            setMode(LOSE);
            return;
        }

        // 뱀이 자신의 몸에 닿을 경우
        int snakelength = mSnakeTrail.size();
        for (int snakeindex = 0; snakeindex < snakelength; snakeindex++) {
            Coordinate c = mSnakeTrail.get(snakeindex);
            if (c.equals(newHead)) {
                setMode(LOSE);
                return;
            }
        }

        // Look for apples
        int applecount = mAppleList.size();
        for (int appleindex = 0; appleindex < applecount; appleindex++) {
            Coordinate c = mAppleList.get(appleindex);
            if (c.equals(newHead)) {
                mAppleList.remove(c);
                addRandomApple();

                mScore++;
                if(mMoveDelay > 50) {
                    mMoveDelay -= 1;
                }
                mTextScore.setText("Score : " + mScore);
                mTextSpeed.setText("Delay : " + mMoveDelay);

                growSnake = true;
            }
        }

        // 뱀 위치 조정
        mSnakeTrail.add(0, newHead);
        // 뱀이 자라길 원하면
        if (!growSnake) {
            mSnakeTrail.remove(mSnakeTrail.size() - 1);
        }

        int index = 0;
        for (Coordinate c : mSnakeTrail) {
            if (index == 0) {
                setTile(YELLOW_STAR, c.x, c.y);
            } else {
                setTile(RED_STAR, c.x, c.y);
            }
            index++;
        }

    }

    // x, y 로 이차원 배열을 만드는 간단한 class
    private class Coordinate {
        public int x;
        public int y;

        public Coordinate(int newX, int newY) {
            x = newX;
            y = newY;
        }

        public boolean equals(Coordinate other) {
            if (x == other.x && y == other.y) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Coordinate: [" + x + "," + y + "]";
        }
    }




}
