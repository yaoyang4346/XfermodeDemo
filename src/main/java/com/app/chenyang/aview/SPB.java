package com.app.chenyang.aview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenyang on 2018/5/11.
 */

public class SPB extends View{
    private RectF bgRect;
    private int side, radius, w, h;
    private float progress,max;
    private Context context;
    private Paint sidePaint, progressPaint, textPaint;
    private PorterDuffXfermode mode;
    private PorterDuffXfermode progressMode;
    private float textY;

    public SPB(Context context) {
        this(context,null);
    }

    public SPB(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        side = dp2px(2);
        sidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sidePaint.setStyle(Paint.Style.STROKE);
        sidePaint.setStrokeWidth(side);
        sidePaint.setColor(Color.parseColor("#FE4A29"));

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setStrokeWidth(side);
        progressPaint.setColor(Color.parseColor("#FE557D"));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.parseColor("#FE557D"));
        mode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        radius = h/2;
        textPaint.setTextSize(h/2);;
        bgRect = new RectF(side,side,w-side,h-side);
        Paint.FontMetrics fmi = textPaint.getFontMetrics();
        textY = h/2 - (fmi.descent+fmi.ascent)/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSide(canvas);
        drawProgress(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        String str = "ABCDEFGH";
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(bitmap);
        textCanvas.drawText(str,w/2-textPaint.measureText(str)/2,textY,textPaint);
        textPaint.setXfermode(mode);
        textPaint.setColor(Color.WHITE);
        textCanvas.drawRoundRect(new RectF(bgRect.left,bgRect.top,bgRect.right*progress,bgRect.bottom), radius, radius, textPaint);
        canvas.drawBitmap(bitmap, 0, 0, null);
        textPaint.setXfermode(null);
        textPaint.setColor(Color.parseColor("#FE557D"));
    }

    private void drawProgress(Canvas canvas) {
        if (progress == 0)
            return;
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas progressCanvas = new Canvas(bitmap);
        progressPaint.setColor(Color.WHITE);
        progressCanvas.drawRoundRect(bgRect, radius, radius, progressPaint);
        progressPaint.setColor(Color.parseColor("#FE557D"));
        progressPaint.setXfermode(mode);
        progressCanvas.drawRoundRect(new RectF(bgRect.left,bgRect.top,bgRect.right*progress,bgRect.bottom), radius, radius, progressPaint);
        progressPaint.setXfermode(null);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private void drawSide(Canvas canvas) {
        canvas.drawRoundRect(bgRect, radius, radius, sidePaint);
    }

    private int dp2px(float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    public void setMax(float max){
        this.max = max;
    }

    public void setProgress(float progress){
        if(progress > max)
            this.progress = 1.0f;
        else
            this.progress = progress/max;
        invalidate();
    }
}
