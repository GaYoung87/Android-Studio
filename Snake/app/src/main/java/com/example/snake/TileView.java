package com.example.snake;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.jar.Attributes;

public class TileView extends View {
    /**
     * Parameters controlling the size of the tiles and their range within view.
     * Width/Height are in pixels, and Drawables will be scaled to fit to these
     * dimensions. X/Y Tile Counts are the number of tiles that will be drawn.
     */

    protected  static int mTileSize;

    protected static int mXTileCount;
    protected static int mYTileCount;

    private static int mXOffset;
    private static int mYOffset;

    /**
     * A hash that maps integer handles specified by the subclasser to the
     * drawable that will be used for that reference
     * 하위 클래스에 의해 지정된 정수 핸들을 해당 참조에 사용될 임계값에 매핑 하는 해시
     */
    private Bitmap[] mTileArray;

    /**
     * A two-dimensional array of integers in which the number represents the index of the tile
     * that should be drawn at that locations
     * 숫자가 해당 위치에서 그려야 하는 타일의 색인을 나타내는 2차원 정수 배열
     */
    private int[][] mTileGrid;

    // final: 더이상 값 수정 불가
    private final Paint mPaint = new Paint();

    // CustomView 생성
    public TileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // a 설정 -> 무슨의미?
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TileView);
        // R.styleable.TileView_tileSize : attrs에서 온건가?
        // R.styleable.TileView_tileSize로 검색해서 mTileSize에 불러오는데, 없으면 24로 대체
        mTileSize = a.getInt(R.styleable.TileView_tileSize, 24);
        // recycle: 가비지 컬렉터가 더이상 사용하지 않는 것을 회수해갈 수 있도록 명시적으로 호출
        // 꼭 적어줘야함!
        a.recycle();
    }

    /**
     * Rests the internal array of Bitmaps used for drawing tiles, and
     * sets the maximum index of tiles to be inserted
     * 비트맵의 내부배열을 다시 설정하고, 삽입할 최대 인덱스를 설정
     *
     * @param tilecount
     */
    // 아까 설정 했던 타일 다시 설정!
    public void resetTiles(int tilecount) {
        mTileArray = new Bitmap[tilecount];
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Math.floor: 소수점 이하 버림
        // Math.ceil: 소수점 이하 올림
        // Math.round: 소수점 이하 반올림
        // mX(Y)TileCount: 전체 너비에서 tilesiz 나누기
        mXTileCount = (int) Math.floor(w / mTileSize);
        mYTileCount = (int) Math.floor(h / mTileSize);

        mXOffset = ((w - (mTileSize * mXTileCount)) / 2);
        mYOffset = ((h - (mTileSize * mYTileCount)) / 2);

        mTileGrid = new int[mXTileCount][mYTileCount];
        clearTiles();
    }







}
