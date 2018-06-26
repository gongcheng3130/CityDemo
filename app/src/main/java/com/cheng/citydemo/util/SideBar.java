package com.cheng.citydemo.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class SideBar extends View {

    private OnLetterChangedListener onLetterChangedListener;// 触摸事件
    public List<String> letter = new ArrayList<>();
//    public static String[] letter = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
//            "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#" };
    private int choose = -1;// 选中
    private Paint paint = new Paint();
    private float singleHeight;

    public void setLetter(List<String> letter) {
        this.letter = letter;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        // 获取每一个字母的高度
        singleHeight = (height * 1f) / letter.size();
        singleHeight = (height * 1f - singleHeight / 2) / letter.size();
        for (int i = 0; i < letter.size(); i++) {
            paint.setColor(Color.rgb(23, 122, 216));
            // paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(singleHeight*2/3);
            // 选中的状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#c60000"));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(letter.get(i)) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(letter.get(i), xPos, yPos, paint);
            paint.reset();// 重置画笔
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnLetterChangedListener listener = onLetterChangedListener;
        final int c = (int) (y / getHeight() * letter.size());// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                choose = -1;//
                invalidate();
                if (listener != null) listener.onLetterReleaseListener();
                break;
            // 除开松开事件的任何触摸事件
            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < letter.size()) {
                        if (listener != null) listener.onLetterChangedListener(c, letter.get(c));
                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setOnLetterChangedListener(OnLetterChangedListener onLetterChangedListener) {
        this.onLetterChangedListener = onLetterChangedListener;
    }

    public interface OnLetterChangedListener {
        void onLetterChangedListener(int index, String s);
        void onLetterReleaseListener();
    }

}
