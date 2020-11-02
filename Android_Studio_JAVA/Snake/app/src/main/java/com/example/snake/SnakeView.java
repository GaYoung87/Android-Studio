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

import static androidx.core.content.res.TypedArrayUtils.getDrawable;

public class SnakeView extends TileView{

    private static final String TAG = "SnakeView";

    /**
     * Current mode of application(현재 모드): READY to run, RUNNING, or you have already lost
     * reason why static final ints are used instead of an enum for performance
     * 성능을 위해 enum대신 int로 최종입력이 됨
     */
    // 현재 상태
    // 여기서 READY, PAUSE를 설정함에 따라 MainActivity에서 작동 가능
    private boolean isStart = false;
    private int mode = READY;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;

    /**
     * Current direction the snake is headed.
     */
    // 방향
    private int gameTime = 0;
    private int direction = NORTH;
    private int nextDirection = NORTH;
    private static final int NORTH = 1;
    private static final int SOUTH = 2;
    private static final int EAST = 3;
    private static final int WEST = 4;

    /**
     * Labels for the drawables that will be loaded into the TileView class
     */
    // TileView 로 전해질 변수
    private static final int RED_STAR = 1;
    private static final int YELLOW_STAR = 2;
    private static final int GREEN_STAR = 3;

    // 사과를 얼마나 먹었는지
    private long score = 0;
    // moveDelay : milliseconds 단위, 사과를 먹을수록 줄어들도록 함
    private long moveDelay = 200;
    // 마지막으로 움직인 절대시간 / moveDelay 에 기초했는지 판단
    private long lastMove;

    /**
     * mStatusText: text shows to the user in some run states
     */
    // user상황 보여줌
    private TextView statusText;
    private TextView textTime;
    private TextView textScore;
    private TextView textSpeed;

    /**
     * mSnakeTrail: a list of Coordinates that make up the snake's body
     * mAppleList: the secret location of the juicy apples the snake craves
     */
    // 뱀과 사과의 위치
    private ArrayList<Coordinate> snakeTrail = new ArrayList<Coordinate>();
    private ArrayList<Coordinate> appleList = new ArrayList<Coordinate>();

    /**
     * Everyone needs a little randomness in their life -> 랜덤(사과 위치는 랜덤)
     */
    private static final Random RNG = new Random();

    /**
     * Create a simple handler that we can use to cause animation to happen.  We
     * set ourselves as a target and we can use the sleep()
     * function to cause an update/invalidate to occur at a later date.
     */
    // Thread : 프로그램 안에서 실행을 담당하는 하나의 흐름
    // MessageQueue : Message 를 담는 자료구조
    // Looper : MessageQueue -> Handler
    // 일어날 사건을 만들수 있는 handler(Thread 간의 통신 장치)
    // 애니메이션을 발생시키는데 사용할 수 있는 간단한 handler 작성
    // 목표설정후 sleep()기능을 사용해 업데이트, 유효성 검사를 나중에 할 수 있음
    private RefreshHandler redrawHandler = new RefreshHandler();

    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // 업데이트, 유효성검사할 때 문제메세지 제공
            SnakeView.this.update();
            SnakeView.this.invalidate();
        }

        // sleep 기능
        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };

    // message보내려고 handler만들었다
    // Thread간의 통신 -> Thread는 프로그램안에서 실행되는게 몇기의 흐름으로 나뉨. 보통 프로그램을 Thread라고 하고
    // Tread안에 message큐가 있음 그 메세지를 서로 보내는 방법이 handler
    Handler snakeHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int hh = gameTime / 3600;
            int mm = (gameTime % 3600) / 60;
            int sec = gameTime % 60;

            String strTime = String.format("%02d : %02d : %02d", hh, mm, sec);
            sendEmptyMessageDelayed(0, 1000);
            textTime.setText(strTime);
            gameTime++;
        }
    };

    /**
     * Constructs a SnakeView based on inflation from XML
     * XML의 인플레이션을 기준으로 SnakeView압축
     *
     * @param context
     * @param attrs
     */
    // xml 파일에 기초해 Snake View 구성
    // Custom View
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
        snakeTrail.clear();
        appleList.clear();

        // 뱀 기본 위치
        snakeTrail.add(new Coordinate(7, 7));
        snakeTrail.add(new Coordinate(6, 7));
        snakeTrail.add(new Coordinate(5, 7));
        snakeTrail.add(new Coordinate(4, 7));
        snakeTrail.add(new Coordinate(3, 7));
        snakeTrail.add(new Coordinate(2, 7));
        nextDirection = NORTH;

        // 사과 기본 위치
        addRandomApple();
        addRandomApple();

        moveDelay = 200;
        score = 0;
    }

    // time, score, speed 보여주기
    public void setTextView(TextView time, TextView score, TextView speed) {
        textTime = time;
        textScore = score;
        textSpeed = speed;
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

        map.putIntArray("appleList", coordArrayListToArray(appleList));
        map.putInt("direction", Integer.valueOf(direction));
        map.putInt("nextDirection", Integer.valueOf(nextDirection));
        map.putLong("moveDelay", Long.valueOf(moveDelay));
        map.putLong("score", Long.valueOf(score));
        map.putInt("gameTime", Integer.valueOf(gameTime));
        map.putIntArray("snakeTrail", coordArrayListToArray(snakeTrail));

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

        appleList = coordArrayToArrayList(icicle.getIntArray("appleList"));
        direction = icicle.getInt("direction");
        nextDirection = icicle.getInt("nextDirection");
        moveDelay = icicle.getLong("moveDelay");
        score = icicle.getLong("score");
        gameTime = icicle.getInt("gameTime");
        snakeTrail = coordArrayToArrayList(icicle.getIntArray("snakeTrail"));
    }

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
            if (mode == READY | mode == LOSE) {
                // 게임이 READY상태이거나 지면 이전 게임은 끝나고 새로운 게임이 만들어져야함
                initNewGame();
                setMode(RUNNING);
                update();
                if (isStart == false) {
                    isStart = true;
                    snakeHandler.sendEmptyMessage(0);
                    textScore.setText("Score : " + score);
                    textSpeed.setText("Delay : " + moveDelay);
                }
                return;
            }

            // 일시정지
            if (mode == PAUSE) {
                setMode(RUNNING);
                update();
                if (isStart == false) {
                    isStart = true;
                    snakeHandler.sendEmptyMessage(0);
                    textScore.setText("Score : " + score);
                    textSpeed.setText("Delay : " + moveDelay);
                }
                return;
            }

            if (direction != SOUTH) {
                nextDirection = NORTH;
                return;
            }
        }

        if (dir == SOUTH) {
            if (direction != NORTH) {
                nextDirection = SOUTH;
            }
        }

        if (dir == WEST) {
            if (direction != EAST) {
                nextDirection = WEST;
            }
        }

        if (dir == EAST) {
            if (direction != WEST) {
                nextDirection = EAST;
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
        statusText = newView;
    }


    // 게임 모드에 따른 설정
    public void setMode(int newMode) {
        int oldMode = mode;
        mode = newMode;
        if (newMode == RUNNING & oldMode != RUNNING) {
            statusText.setVisibility(View.INVISIBLE);
            update();
            return;
        }

        Resources res = getContext().getResources();
        CharSequence str = "";
        if (newMode == PAUSE) {
            str = res.getText(R.string.mode_pause);
            if(isStart) {
                snakeHandler.removeMessages(0);
                isStart = false;
            }
        }
        if (newMode == READY) {
            str = res.getText(R.string.mode_ready);
        }
        if (newMode == LOSE) {
            str = res.getString(R.string.mode_lose_prefix) + score
                    + res.getString(R.string.mode_lose_suffix);
            if(isStart) {
                snakeHandler.removeMessages(0);
                isStart = false;
                gameTime = 0;
            }
        }

        statusText.setText(str);
        statusText.setVisibility(View.VISIBLE);
    }

    /**
     * RUNNING / PAUSED / textview보여줘야하는 상태로 업데이트하기
     *
     * @param newMode
     */
    // 게임 모드에 따른 설정
    private void addRandomApple() {
        Coordinate newCoord = null;
        boolean found = false;
        while (!found) {
            // 새로운 위치
            int newX = 1 + RNG.nextInt(xTileCount - 2);
            int newY = 1 + RNG.nextInt(yTileCount - 2);
            newCoord = new Coordinate(newX, newY);

            // 뱀과 겹치지 x
            boolean collision = false;
            int snakelength = snakeTrail.size();
            for (int index = 0; index < snakelength; index++) {
                if (snakeTrail.get(index).equals(newCoord)) {
                    collision = true;
                }
            }
            // 충돌이 나지 않을 경우 탈출
            found = !collision;
        }
        if (newCoord == null) {
            Log.e(TAG, "Somehow ended up with a null newCoord!");
        }
        appleList.add(newCoord);
    }

    /**
     * Handles the basic update loop, checking to see if we are in the running
     * state, determining if a move should be made, updating the snake's location.
     */
    // game mode 와, 움직임이 실제로 일어났는지를 확인해 뱀의 위치를 새로 지정
    public void update() {
        if (mode == RUNNING) {
            long now = System.currentTimeMillis();

            if (now - lastMove > moveDelay) {
                clearTiles();
                updateWalls();
                updateSnake();
                updateApples();
                lastMove = now;
            }
            redrawHandler.sleep(moveDelay);
        }

    }

    // 벽 업데이트
    private void updateWalls() {
        for (int x = 0; x < xTileCount; x++) {
            setTile(GREEN_STAR, x, 0);
            setTile(GREEN_STAR, x, yTileCount- 1);
        }
        for (int y = 1; y < xTileCount - 1; y++) {
            setTile(GREEN_STAR, 0, y);
            setTile(GREEN_STAR, yTileCount - 1, y);
        }
    }

    /**
     * Draws some apples.
     *
     */
    // 사과 업데이트
    private void updateApples() {
        for (Coordinate c : appleList) {
            setTile(YELLOW_STAR, c.x, c.y);
        }
    }


    /**
     * Figure out which way the snake is going, see if he's run into anything (the
     * walls, himself, or an apple). If he's not going to die, we then add to the
     * front and subtract from the rear in order to simulate motion. If we want to
     * grow him, we don't subtract from the rear.
     */
    // 뱀 업데이트
    // 뱀 위치 변경: 만약 죽지 않았다면, 뒤 하나의 위치를 앞으로 옮김, 만약 사과를 먹었다면 옮기지 x
    private void updateSnake() {
        boolean growSnake = false;

        // head, newHead 지정
        Coordinate head = snakeTrail.get(0);
        Coordinate newHead = new Coordinate(1, 1);

        direction = nextDirection;

        switch (direction) {
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
        if ((newHead.x < 1) || (newHead.y < 1) || (newHead.x > xTileCount - 2)
                || (newHead.y > yTileCount - 2)) {
            setMode(LOSE);
            return;

        }

        // 뱀이 자신의 몸에 닿을 경우
        int snakelength = snakeTrail.size();
        for (int snakeindex = 0; snakeindex < snakelength; snakeindex++) {
            Coordinate c = snakeTrail.get(snakeindex);
            if (c.equals(newHead)) {
                setMode(LOSE);
                return;
            }
        }

        // eat apple
        int applecount = appleList.size();
        for (int appleindex = 0; appleindex < applecount; appleindex++) {
            Coordinate c = appleList.get(appleindex);
            if (c.equals(newHead)) {
                appleList.remove(c);
                addRandomApple();

                score++;
                if(moveDelay > 50) {
                    moveDelay -= 1;
                }
                textScore.setText("Score : " + score);
                textSpeed.setText("Delay : " + moveDelay);

                growSnake = true;
            }
        }

        // 뱀 위치 조정
        snakeTrail.add(0, newHead);
        // 뱀이 자라나길 원하면
        if (!growSnake) {
            snakeTrail.remove(snakeTrail.size() - 1);
        }

        int index = 0;
        for (Coordinate c : snakeTrail) {
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
