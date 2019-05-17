package com.renbo.newsproject.splash;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.renbo.newsproject.R;

/* 自定义控件（画自定义控件） */
public class TimeView extends View {
    // 文字画笔（画文字）
    TextPaint mTextPaint;
    // 外圆画笔
    Paint excirclePaint;
    // 内圆画笔
    Paint innerPaint;

    String content = "跳过";
    // 内边距
    int padding = 5;
    // 内圆圈直径
    int inner_d;
    // 外圆圈直径
    int excircle_d;

    int angle;

    OnTimeClickListener mListener;

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 接收XML传过来的自定义属性
        TypedArray typedArr = context.obtainStyledAttributes(attrs, R.styleable.TimeView);
        int outerColor = typedArr.getColor(R.styleable.TimeView_outer_color, Color.GRAY);
        int innerColor = typedArr.getColor(R.styleable.TimeView_inner_color, Color.BLUE);

        // 1.设置文字画笔
        mTextPaint = new TextPaint();
        // 抗锯齿
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(24);
        // 画笔颜色
        mTextPaint.setColor(Color.WHITE);
        // 计算画的文字宽度
        float text_width = mTextPaint.measureText(content);
        inner_d = (int)text_width + 2 * padding;
        excircle_d = inner_d + 2 * padding;

        // 2.设置外圆画笔
        excirclePaint = new Paint();
        excirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        excirclePaint.setColor(outerColor);
        // 画笔风格空心
        excirclePaint.setStyle(Paint.Style.STROKE);
        // 画笔宽度
        excirclePaint.setStrokeWidth(padding);

        // 3.设置内圆画笔
        innerPaint = new Paint();
        innerPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        innerPaint.setColor(innerColor);


        // 使用过后得回收
        typedArr.recycle();
    }

    // 返回控件的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(excircle_d, excircle_d);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画背景填充色
        //canvas.drawColor(Color.RED);

        // 画外圆
        canvas.drawCircle(excircle_d / 2, excircle_d / 2, inner_d / 2, innerPaint);
        RectF rectF = new RectF(padding / 2, padding / 2, excircle_d - padding / 2, excircle_d - padding / 2);

        canvas.save();
        // 旋转画布
        canvas.rotate(-90, excircle_d / 2, excircle_d / 2);
        canvas.drawArc(rectF, 0, angle, false, excirclePaint);
        canvas.restore();

        float yPos = canvas.getHeight() / 2 - (mTextPaint.descent() + mTextPaint.ascent()) / 2;
        // 画中间的文字（x：左边距；y：是到baseLine的距离，而不是到顶部的距离）
        canvas.drawText(content, 2 * padding, yPos, mTextPaint);
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    // 刷新控件
    public void setProgress(int total, int now) {
        int space = 360 / total;
        angle = space * now;
        // 刷新控件(在UI线程中刷新)
        invalidate();
        // 刷新控件(在子线程中刷新)
        //postInvalidate();
    }

    // 设置按钮点击时高亮效果
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 手指按下去时
            setAlpha(0.3f);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // 手指抬起来时
            setAlpha(1.0f);
            // 执行点击事件的内容
            if (null != mListener) {
                mListener.onClickSkip(this);
            }
        }

        return true;
    }

    public void setListener(OnTimeClickListener listener) {
        this.mListener = listener;
    }
}
