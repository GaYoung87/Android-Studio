package com.example.snake;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class TileView extends View {
    /**
     * Parameters controlling the size of the tiles and their range within view.
     * Width/Height are in pixels, and Drawables will be scaled to fit to these
     * dimensions. X/Y Tile Counts are the number of tiles that will be drawn.
     */

    protected static int tileSize;

    protected static int xTileCount;
    protected static int yTileCount;

    private static int xOffset;
    private static int yOffset;

    /**
     * A hash that maps integer handles specified by the subclasser to the
     * drawable that will be used for that reference
     */
    // tile 을 그릴 array
    private Bitmap[] tileArray;

    /**
     * A two-dimensional array of integers in which the number represents the index of the tile
     * that should be drawn at that locations
     * 숫자가 해당 위치에서 그려야 하는 타일의 색인을 나타내는 2차원 정수 배열
     */
    // 각 위치의 인덱스를 저장할 이차원 배열
    private int[][] tileGrid;
    // final: 더이상 값 수정 불가
    private final Paint tilePaint = new Paint();


    // CustomView 생성 (getAttrs: attrs.xml 에 선언한 attribute 를 View 에 설정)
    // cutomview를 만드는게 attrs에 만들고, view라는 자바클래스를 getatt, init만들어야 custom
    // 내부에 있는 init
    // pygame - class Block 만드는 것과 동일
    // getAttrs : attrs.xml 에 선언한 attribute 를 View 에 설정
    public TileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    // getAttrs
    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    // initView : xml 파일 할당, 각각의 view 설정
    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TileView);
        // R.styleable.TileView_tileSize : attrs에서 온건가?
        // R.styleable.TileView_tileSize로 검색해서 mTileSize에 불러오는데, 없으면 24로 대체
        tileSize = a.getInt(R.styleable.TileView_tileSize, 24);
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
    // Bitmap 으로 초기 리스트 리셋
    public void resetTiles(int tilecount) {
        tileArray = new Bitmap[tilecount];
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        xTileCount = (int) Math.floor(w / tileSize);
        yTileCount = (int) Math.floor(h / tileSize);
        // Math.floor: 소수점 이하 버림
        // Math.ceil: 소수점 이하 올림
        // Math.round: 소수점 이하 반올림
        // mX(Y)TileCount: 전체 너비에서 tilesize 나누기

        // 여백 -> 점수판 쓰려고 만들어진듯
        xOffset = ((w - (tileSize * xTileCount)) / 2);
        yOffset = ((h - (tileSize * yTileCount)) / 2);

        tileGrid = new int[xTileCount][yTileCount];
        clearTiles();
    }
    /**
     * Function to set the specified Drawable as the tile for a particular integer key.
     * 특정한 Drawable을 Bitmap
     *
     * @param key
     * @param tile
     */
    // Tile 그리기
    public void loadTile(int key, Drawable tile) {
        Bitmap bitmap = Bitmap.createBitmap(tileSize, tileSize, Bitmap.Config.ARGB_8888);
        // Drawable을 Bitmap의 canvas에 그린다. 그러면 Drawable이 Bitmap에 그려진다.
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, tileSize, tileSize);
        tile.draw(canvas);

        tileArray[key] = bitmap;
    }

    /**
     * Resets all tiles to 0 (empty)
     */
    // Tile 초기화
    public void clearTiles() {
        for (int x = 0; x < xTileCount; x++) {
            for (int y = 0; y < yTileCount; y++) {
                setTile(0, x, y);
            }
        }
    }

    /**
     * Used to indicate that a particular tile (set with loadTile and referenced
     * by an integer) should be drawn at the given x/y coordinates during the
     * next invalidate/draw cycle.
     * 특정 타일(loadTile로 설정되고 정수로 참조됨)을 다음 유효 기간/날짜/시간 좌표에 그려야 함을 나타내는 데 사용
     *
     * @param tileindex
     * @param x
     * @param y
     */
    // 특정 타일 표시
    public void setTile(int tileindex, int x, int y) {
        tileGrid[x][y] = tileindex;
    }

    // canvas : 도화지, paint : 색연필
    // 위에서 타일을 표시했기 때문에 이를 칠할 수 있음(loadTile)
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int x = 0; x < xTileCount; x += 1) {
            for (int y = 0; y < yTileCount; y += 1) {
                if (tileGrid[x][y] > 0) {
                    canvas.drawBitmap(tileArray[tileGrid[x][y]],
                            xOffset + x * tileSize,
                            yOffset + y * tileSize,
                            tilePaint);
                }
            }
        }

    }

}
