package com.pomelo.lucky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by huzan on 2016/12/26 21:24.
 */

public class LuckySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder holder;
    private Canvas canvas;
    private Thread t;
    private boolean isRunning;

    private String[] strs = new String[]{"单反相机", "IPAD", "恭喜发财", "IPHONE", "服装一套", "恭喜发财"};
    private int[] pics = new int[]{R.drawable.danfan, R.drawable.ipad, R.drawable.f040, R.drawable.iphone, R.drawable.meizi, R.drawable.f015};
    private int[] colors = new int[]{0xFFFFC300, 0xFFF17E01, 0xFFFFC300, 0xFFF17E01, 0xFFFFC300, 0xFFF17E01};
    private Bitmap[] bitmaps;
    private int mItemCount = 6;


    private RectF rectF;
    private int radius;
    private Bitmap bitmapBg = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
    private Paint arcPaint;
    private int padding;
    private int center;


    private Paint textPaint;
    private float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());


    private float speed ;
    private volatile float startAngle;
    private boolean isEnd;

    private int index;
    private Context context;


    public LuckySurfaceView(Context context) {
        this(context, null);
    }


    public LuckySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);

        setFocusable(true);
        setFocusableInTouchMode(true);
        //常量
        setKeepScreenOn(true);



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(getMeasuredHeight(), getMeasuredWidth());
        padding = getPaddingLeft();
        radius = width - padding * 2;
        rectF = new RectF(padding, padding, padding + radius, padding + radius);
        center = width / 2;
        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setDither(true);

        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(0XFFFFFFFF);


        bitmaps = new Bitmap[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(getResources(), pics[i]);
        }


        isRunning = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {

            draw();
        }
    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            if (canvas != null) {

                drawBg();

                float tempAngle = startAngle;
                float sweepAngle = 360 / mItemCount;

                for (int i = 0; i < mItemCount; i++) {
                    arcPaint.setColor(colors[i]);
                    canvas.drawArc(rectF, tempAngle, sweepAngle, true, arcPaint);
                    drawText(tempAngle, sweepAngle, strs[i]);
                    drawPic(tempAngle, bitmaps[i]);
                    tempAngle += sweepAngle;
                }
                startAngle += speed;
                if (isEnd) {
                    speed -= 1;
                }

                if (speed <= 0) {
                    speed = 0;
                    isEnd = false;
                }

            }
        } catch (Exception e) {

        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void start(int index,Context context) {

        this.context = context;
        this.index = index;
        float angle = 360/mItemCount;

        float from = 270-(index+1)*angle;
        float end = from+angle;

        float targetFrom = 4*360 + from;
        float targetEnd = 4*360+end;

        float v1 = (float) ((-1+Math.sqrt(1+8*targetFrom))/2);
        float v2 = (float) ((-1+Math.sqrt(1+8*targetEnd))/2);
        speed = (float) (v1+Math.random()*(v2-v1));
//        speed = v2;
        isEnd = false;


    }



    public void end(){
        startAngle = 0;
        isEnd = true;

    }

    public boolean isStart(){
        return speed!=0;
    }

    public boolean isEnd(){
        return isEnd;
    }

    private void drawPic(float tempAngle, Bitmap bitmap) {
        int picWidth = radius / 8;
        float angle = (float) ((tempAngle + 360 / mItemCount / 2) * Math.PI / 180);
        int x = (int) (center + radius / 2 / 2 * Math.cos(angle));
        int y = (int) (center + radius / 2 / 2 * Math.sin(angle));
        Rect r = new Rect(x - picWidth / 2, y - picWidth / 2, x + picWidth / 2, y + picWidth / 2);
        canvas.drawBitmap(bitmap, null, r, null);
    }

    private void drawText(float tempAngle, float sweepAngle, String str) {
        Path path = new Path();
        path.addArc(rectF, tempAngle, sweepAngle);
        float textwidth = textPaint.measureText(str);
        int hOffset = (int) (radius * Math.PI / mItemCount / 2 - textwidth / 2);
        canvas.drawTextOnPath(str, path, hOffset, radius / 2 / 6, textPaint);

    }

    /**
     * 绘制背景
     */
    private void drawBg() {
        canvas.drawColor(0xffffffff);
        canvas.drawBitmap(bitmapBg, null, new Rect(padding / 2, padding / 2, getMeasuredWidth() - padding / 2, getMeasuredHeight() - padding / 2), null);
    }
}
