package com.example.khuta.battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//A special view class to display a battleship board as a 2D grid.

public class BoardView extends View implements Serializable {

    //Listeners to be notified upon board touches.
    private final List<BoardTouchListener> listeners = new ArrayList<>();

    // Board background color.
    private final int boardColor = Color.argb(0, 255, 255, 255); // Transparent




    //Red color circle
    private final int redColor = Color.rgb(255, 69, 0);

    //Blue color circle
    private final int blueColor = Color.rgb(0, 0, 255);

    //Black color circle
    private final int blackColor = Color.rgb(0, 0, 0);

    //White color circle
    private final int whiteColor = Color.rgb(255, 255, 255);



    //Board background paint.
    private final Paint boardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //Board grid line paint.
    private final Paint boardLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);




    //Red background paint
    private final Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //Blue background paint
    private final Paint bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //Black background paint
    private final Paint blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //White background paint
    private final Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);



    //---------------------------------------------------------------

//    int[][] coordinatesOfPlayer1Ships = new int[10][10];
//    int[][] coordinatesOfPlayer2Ships = new int[10][10];
//    int[][] gameCoordinates = new int[10][10];

    int[][] coordinatesOfPlayer1Ships;
    int[][] coordinatesOfPlayer2Ships;
    int[][] gameCoordinates;


    //Size of the board.
    private int boardSize;
    private int size;

    //---------------------------------------------------------------


    {
        boardPaint.setColor(boardColor);
    }

    {
        redPaint.setColor(redColor);
    }

    {
        bluePaint.setColor(blueColor);
    }

    {
        blackPaint.setColor(blackColor);
    }

    {
        whitePaint.setColor(whiteColor);
    }

    {
        /* Board grid line color. */
        int boardLineColor = Color.GREEN;
        boardLinePaint.setColor(boardLineColor);
        boardLinePaint.setStrokeWidth(3);
    }

    /**
     * Create a new board view to be run in the given context.
     */

    public BoardView(Context context) {
        super(context);
    }

    /**
     * Create a new board view with the given attribute set.
     */
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Create a new board view with the given attribute set and style.
     */
    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Set the board to to be displayed by this view.
     */
//    void setBoard(Board board) {
//        /* Board to be displayed by this view. */
//        this.boardSize = board.getGridSize();
//    }

    void setBoard(int size) {
        /* Board to be displayed by this view. */
        this.boardSize = size;
    }

    public void defineCoordinatesSize() {
        /* Board to be displayed by this view. */
        coordinatesOfPlayer1Ships = new int[boardSize][boardSize];
        coordinatesOfPlayer2Ships = new int[boardSize][boardSize];
        gameCoordinates = new int[boardSize][boardSize];
    }


    /**
     * Overridden here to detect a board touch. When the board is
     * touched, the corresponding place is identified,
     * and registered listeners are notified.
     *
     * @see BoardTouchListener
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int xy = locatePlace(event.getX(), event.getY());
                invalidate();
                if (xy >= 0) {
                    notifyBoardTouch(xy / 100, xy % 100);
                }
                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    /**
     * Overridden here to draw a 2-D representation of the board.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPlayer1Ships(canvas);
        drawBoard(canvas);
        drawGrid(canvas);
    }

    private void drawPlayer1Ships(Canvas canvas) {
        // Iterate and look for "1" which indicate position of boats, hence do some coloring.
        if (coordinatesOfPlayer1Ships != null) {
            for (int i = 0; i < coordinatesOfPlayer1Ships.length; i++) {
                for (int j = 0; j < coordinatesOfPlayer1Ships.length; j++) {
                    if (coordinatesOfPlayer1Ships[i][j] >= 1) {       // DRAW ALL BOATS HERE
                        float drawX = (i * lineGap()) + (lineGap() / 2);
                        float drawY = (j * lineGap()) + (lineGap() / 2);
                        int left = (int) (drawX - (lineGap() / 2));
                        int top = (int) (drawY - (lineGap() / 2));
                        int right = (int) (drawX + (lineGap() / 2));
                        int bottom = (int) (drawY + (lineGap() / 2));
                        canvas.drawRect(left, top, right, bottom, bluePaint);
                    }
                }
            }
        }
    }

    /**
     * Keeps track of all the places either the Computer or Human has shot at the boardView.
     */
    private void drawBoard(Canvas canvas) {
        if (gameCoordinates != null) {
            for (int i = 0; i < gameCoordinates.length; i++) {
                for (int j = 0; j < gameCoordinates.length; j++) {
                    if (gameCoordinates[i][j] == 8) { // HIT
                        float xMiss = (i * lineGap()) + (lineGap() / 2);
                        float yMiss = (j * lineGap()) + (lineGap() / 2);
                        canvas.drawCircle(xMiss, yMiss, (lineGap() / 2), redPaint); // HIT
                    }
                    if (gameCoordinates[i][j] == -9) { // MISS
                        float xMiss = (i * lineGap()) + (lineGap() / 2);
                        float yMiss = (j * lineGap()) + (lineGap() / 2);
                        canvas.drawCircle(xMiss, yMiss, (lineGap() / 2), blackPaint);  // MISS
                    }
                }
            }
        }
    }

    /**
     * Draw horizontal and vertical lines.
     */
    private void drawGrid(Canvas canvas) {
        final float maxCoord = maxCoord();
        final float placeSize = lineGap();
        canvas.drawRect(0, 0, maxCoord, maxCoord, boardPaint);
        for (int i = 0; i < numOfLines(); i++) {
            float xy = i * placeSize;
            canvas.drawLine(0, xy, maxCoord, xy, boardLinePaint); // horizontal line
            canvas.drawLine(xy, 0, xy, maxCoord, boardLinePaint); // vertical line
        }
    }

    /**
     * Calculate the gap between two horizontal/vertical lines.
     */
    private float lineGap() {
        return Math.min(getMeasuredWidth(), getMeasuredHeight()) / (float) boardSize;
    }

    /**
     * Calculate the number of horizontal/vertical lines.
     */
    private int numOfLines() {
        return boardSize + 1;
    }

    /**
     * Calculate the maximum screen coordinate.
     */
    private float maxCoord() {
        return lineGap() * (numOfLines() - 1);
    }

    /**
     * Given screen coordinates, locate the corresponding place in the board
     * and return its coordinates; return -1 if the screen coordinates
     * don't correspond to any place in the board.
     * The returned coordinates are encoded as <code>x*100 + y</code>.
     */
    private int locatePlace(float x, float y) {
        if (x <= maxCoord() && y <= maxCoord()) {
            final float placeSize = lineGap();
            int ix = (int) (x / placeSize);
            int iy = (int) (y / placeSize);
            Log.w("(ix, iy)", String.valueOf(ix) + "," + String.valueOf(iy));
            return ix * 100 + iy;
        }
        return -1;
    }

    int locateX(float x) {
        if (x <= maxCoord()) {
            final float placeSize = lineGap();
            return (int) (x / placeSize);
        }
        return -1;
    }

    int locateY(float y) {
        return locateX(y);
    }

    /**
     * Register the given listener.
     */
    void addBoardTouchListener(BoardTouchListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Unregister the given listener.
     */
    public void removeBoardTouchListener(BoardTouchListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify all registered listeners.
     */
    private void notifyBoardTouch(int x, int y) {
        for (BoardTouchListener listener : listeners) {
            listener.onTouch(x, y);
        }
    }

    /**
     * Callback interface to listen for board touches.
     */
    public interface BoardTouchListener {

        /**
         * Called when a place of the board is touched.
         * The coordinate of the touched place is provided.
         *
         * @param x 0-based column index of the touched place
         * @param y 0-based row index of the touched place
         */
        void onTouch(int x, int y);

    }
}
