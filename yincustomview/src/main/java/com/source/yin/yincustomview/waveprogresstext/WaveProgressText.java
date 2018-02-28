package com.source.yin.yincustomview.waveprogresstext;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.source.yin.yincustomview.R;

/**
 * 海浪型填满文字的展示进度的 TextView
 * Created by yin on 2018/2/28.
 */

public class WaveProgressText extends android.support.v7.widget.AppCompatTextView {

    private int progress;
    private int maxProgress = 100;
    private int width;
    private int height;
    private int xOffset;
    private Canvas shaderCanvas;
    private Paint paint;
    //振幅原点的 y 坐标
    private int baseLineY;
    private int waveLength;
    //波长控制，view 的宽度除以此值即为波长，默认为1，即 view 的宽度刚好为一个完整波长
    private int waveLengthTag = 1;
    //振幅控制，view 的高度除以此值即为贝塞尔曲线的控制点距离振幅原点的高度值（用来决定振幅），默认为8，即大约 view 的高度为8个振幅
    private double swingTag = 8;
    private int duration = 2000;
    private int waveColor = Color.WHITE;


    public WaveProgressText(Context context) {
        this(context, null);
    }

    public WaveProgressText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveProgressText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaveProgressText,
                defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.WaveProgressText_wave_color) {
                this.waveColor = a.getColor(attr, Color.WHITE);
            }
        }
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        shaderCanvas.drawColor(getCurrentTextColor());
        shaderCanvas.drawPath(getPath(), paint);
        super.onDraw(canvas);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        Log.d("yzh", "onSizeChanged");
        init();
    }

    private void init() {
        waveLength = width / waveLengthTag;
        ValueAnimator xMoveAnimator = ValueAnimator.ofInt(0, waveLength);
        xMoveAnimator.setInterpolator(new LinearInterpolator());
        xMoveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                xOffset = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        xMoveAnimator.setDuration(duration);
        xMoveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        xMoveAnimator.start();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        paint = new Paint();
        paint.setColor(waveColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        shaderCanvas = new Canvas(bitmap);
        Shader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        getPaint().setShader(shader);
    }


    //获取波浪的 path
    private Path getPath() {
        int swing = (int) (height / swingTag);
        int dy = (int) (1.0 * height * progress / maxProgress);
        baseLineY = height - dy;
//        Log.d("yzh", "progress = " + progress + "\theight * progress = " + (height * progress));
        int halfWaveLength = waveLength / 2;//半个波长
        Path path = new Path();
        path.moveTo(-waveLength + xOffset, baseLineY);
        for (int i = 0; i < waveLengthTag * 2 * 2; i++) {
            int startX = -waveLength + i * halfWaveLength + xOffset;
            //控制点的X（半波长波峰/波谷的 x)
            int controlX = startX + halfWaveLength / 2;
            //控制点的Y （波峰/波谷）
            int controlY = i % 2 == 0 ? (baseLineY - swing) : (baseLineY + swing);
            //结束点 X
            int endX = startX + halfWaveLength;
            //结束点 Y
            int endY = baseLineY;

            path.quadTo(controlX, controlY, endX, endY);
        }

        //形成了一封闭区间，让曲线以下的面积填充一种颜色
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
        return path;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (progress > maxProgress) {
            this.progress = maxProgress;
        }
        this.progress = progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        if (maxProgress <= 0) {
            throw new RuntimeException("maxProgress can not <= 0");
        }
        this.maxProgress = maxProgress;
    }

    public int getWaveLengthTag() {
        return waveLengthTag;
    }

    public void setWaveLengthTag(int waveLengthTag) {
        this.waveLengthTag = waveLengthTag;
    }

    public double getSwingTag() {
        return swingTag;
    }

    public void setSwingTag(double swingTag) {
        this.swingTag = swingTag;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWaveColor() {
        return waveColor;
    }

    public void setWaveColor(int waveColor) {
        this.waveColor = waveColor;
        if (paint != null) {
            paint.setColor(waveColor);
        }
    }
}
