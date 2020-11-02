**Snake 질문**

[ 전체 ]

1. 작성 순서 어떻게 되는거쥬?
TileView -> SnakeView -> Mainactivity
xml은 base.html처럼 미리 기본적인걸 -> activity main에서 사용하기 힘든 것들을 customview로 만든다. 내가 뭔가를 만들면 선언해야함

[ attrs ]
1. 왜 Mainactivity.java에서 R.id.time/score/speed 빨간줄이 없어지는거지?
2. 왜 존재하는것?
3. declare-styleable -> 선언한 후 사용하겠다?

[ string ]
1. name을 어디서 사용?

[ TileView ]
1. private final Paint mPaint = new Paint(); 여기서 첫번째 Paint는 왜씀?
2. Custom View는 무엇? 그리고 왜 public TileView여러개 만듦?
     --> Custom View 2개만드는게 공식!
3. 56번째 줄
4. mXOffset은 뭐죠? -> 여백이요!

[ SnakeView ]
1. @param은 주석처리로 써야하는지 -> 함수에서 이 변수가 사용된다는 것을 말함
2. 184번째 줄 -> mNextDirection = NORTH;
3. Handler 찾기

Snake = setTile, clearTiles, getContext, onKeyDown, @Override, resetTiles,
loadTile, 

커스텀 뷰가 attrs에 들어감 -> snakeview가 

우리가 attrs에서한건 tileview -> 레이아웃에서는 snakeview를 사용
tileview내에 들어가면 snakeview에도 똑같은 부분 있음 -> 

[ 실습 ]
1. activity간 구조 어떻게 흘러가나
2. style은 계산기에서 실행해보기가능