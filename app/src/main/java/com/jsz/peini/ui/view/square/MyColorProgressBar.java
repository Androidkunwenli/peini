package com.jsz.peini.ui.view.square;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.jsz.peini.R;


/**
 * Created by th on 2017/1/16.
 */

public class MyColorProgressBar extends View {

    private int mWidth;
    private int mHeight;
    //直径
    private int diameter = 500;

    //圆心
    private float centerX;
    private float centerY;

    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;

    private RectF bgRect;

    private ValueAnimator progressAnimator;

    private float startAngle = 135;
    private float sweepAngle = 270;
    private float currentAngle = 0;
    private float lastAngle;
    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private float maxValues = 100;
    private float curValues = 0;
    private float bgArcWidth = dipToPx(10);
    private float progressWidth = dipToPx(10);
    private float textSize = dipToPx(60);
    private float hintSize = dipToPx(13);
    private float curSpeedSize = dipToPx(16);
    private int aniSpeed = 1000;
    private float longdegree = dipToPx(13);
    private float shortdegree = dipToPx(5);
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(8);
    private String hintColor = "#fb4e30";
    private String longDegreeColor = "#d6d6d6";
    private String shortDegreeColor = "#d6d6d6";
    private String bgArcColor = "#d6d6d6";
    private boolean isShowCurrentSpeed = true;
    private String hintString;
    private boolean isNeedTitle;
    private boolean isNeedUnit;
    private boolean isNeedDial;
    private boolean isNeedContent;
    private String titleString;


    // sweepAngle / maxValues 的值
    private float k;
    private String sanHintString = "#666666";

    public MyColorProgressBar(Context context) {
        super(context, null);
        initView();
    }

    public MyColorProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initCofig(context, attrs);
        initView();
    }

    public MyColorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     * 初始化布局配置
     * 初始布局配置
     *
     * @param context
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        //获得所有的属性值
        //R.styleable.MyColorProgressBar是需要在values下建一个attrs固定目录
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyColorProgressBar_ColorArcProgressBar);
        int color1 = a.getColor(R.styleable.MyColorProgressBar_ColorArcProgressBar_front_color1, Color.MAGENTA);
        int color2 = a.getColor(R.styleable.MyColorProgressBar_ColorArcProgressBar_front_color2, Color.GREEN);
        int color3 = a.getColor(R.styleable.MyColorProgressBar_ColorArcProgressBar_front_color3, Color.BLUE);
        colors = new int[]{color1, color2, color3};

        sweepAngle = a.getInteger(R.styleable.MyColorProgressBar_ColorArcProgressBar_total_engle, 270);
        bgArcWidth = a.getDimension(R.styleable.MyColorProgressBar_ColorArcProgressBar_back_width, dipToPx(10));
        progressWidth = a.getDimension(R.styleable.MyColorProgressBar_ColorArcProgressBar_front_width, dipToPx(10));
        isNeedTitle = a.getBoolean(R.styleable.MyColorProgressBar_ColorArcProgressBar_is_need_title, false);
        isNeedContent = a.getBoolean(R.styleable.MyColorProgressBar_ColorArcProgressBar_is_need_content, false);
        isNeedUnit = a.getBoolean(R.styleable.MyColorProgressBar_ColorArcProgressBar_is_need_unit, false);
        isNeedDial = a.getBoolean(R.styleable.MyColorProgressBar_ColorArcProgressBar_is_need_dial, false);
        hintString = a.getString(R.styleable.MyColorProgressBar_ColorArcProgressBar_string_unit);
        titleString = a.getString(R.styleable.MyColorProgressBar_ColorArcProgressBar_string_title);
        curValues = a.getFloat(R.styleable.MyColorProgressBar_ColorArcProgressBar_current_value, 0);
        maxValues = a.getFloat(R.styleable.MyColorProgressBar_ColorArcProgressBar_max_value, 100);
        setCurrentValues(curValues);
        setMaxValues(maxValues);
        a.recycle();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        int height = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        setMeasuredDimension(width, height);
    }

    private void initView() {
        diameter = 3 * getScreenWidth() / 5;
        //弧形的矩阵区域
        bgRect = new RectF();
        bgRect.top = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);

        //圆心
        centerX = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;
        centerY = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(Color.parseColor(longDegreeColor));

        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(Color.parseColor(bgArcColor));
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(getResources().getColor(R.color.RED_FB4E30));
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //显示单位文字
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(Color.parseColor(sanHintString));
        hintPaint.setTextAlign(Paint.Align.CENTER);

        //显示标题文字
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(Color.parseColor(hintColor));
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        //抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        if (isNeedDial) {
            //画刻度线
            for (int i = 0; i < 40; i++) {
                if (i > 15 && i < 25) {
                    canvas.rotate(9, centerX, centerY);
                    continue;
                }
                if (i % 5 == 0) {
                    degreePaint.setStrokeWidth(dipToPx(2));
                    degreePaint.setColor(Color.parseColor(longDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE, centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - longdegree, degreePaint);
                } else {
                    degreePaint.setStrokeWidth(dipToPx(1.4f));
                    degreePaint.setColor(Color.parseColor(shortDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2, centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2 - shortdegree, degreePaint);
                }

                canvas.rotate(9, centerX, centerY);
            }
        }

        //整个弧
        canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);

        //设置渐变色
        SweepGradient sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(130, centerX, centerY);
        sweepGradient.setLocalMatrix(matrix);
        progressPaint.setShader(sweepGradient);

        //当前进度
        canvas.drawArc(bgRect, startAngle, currentAngle, false, progressPaint);
        if (curValues <= 50) {
            titleString = "信用不够";
        } else if (curValues < 85) {
            titleString = "信用一般";
        } else if (curValues < 95) {
            titleString = "信用很好";
        } else if (curValues <= 100) {
            titleString = "信用极好";
        }
        if (isNeedContent) {
            canvas.drawText(titleString, centerX, centerY + dipToPx(22), curSpeedPaint); //中间文字
        }
        if (isNeedUnit) {
            canvas.drawText(hintString, centerX, centerY + dipToPx(48), hintPaint); //第三行文字
        }
        if (isNeedTitle) {
            canvas.drawText(String.format("%.0f", curValues), centerX, centerY, vTextPaint);  //第一行文字
        }

        invalidate();

    }

    /**
     * 设置最大值
     *
     * @param maxValues
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        k = sweepAngle / maxValues;
    }

    /**
     * 设置当前值
     *
     * @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        this.curValues = currentValues;
        lastAngle = currentAngle;
        setAnimation(lastAngle, currentValues * k, aniSpeed, currentValues);
    }

    /**
     * 设置整个圆弧宽度
     *
     * @param bgArcWidth
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**
     * 设置进度宽度
     *
     * @param progressWidth
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置速度文字大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置单位文字大小
     *
     * @param hintSize
     */
    public void setHintSize(int hintSize) {
        this.hintSize = hintSize;
    }

    /**
     * 设置单位文字
     *
     * @param hintString
     */
    public void setUnit(String hintString) {
        this.hintString = hintString;
        invalidate();
    }

    /**
     * 设置单位文字
     *
     * @param sanHintString
     */
    public void setSanUnit(String sanHintString) {
        this.sanHintString = sanHintString;
        invalidate();
    }

    /**
     * 设置直径大小
     *
     * @param diameter
     */
    public void setDiameter(int diameter) {
        this.diameter = diameter;
        reloadView();
        requestLayout();
        invalidate();
    }

    /**
     * 为进度设置动画
     *
     * @param last
     * @param current
     * @param currentValues curValues = currentAngle / k;
     */
    private void setAnimation(float last, float current, int length, float currentValues) {
        if (currentValues <= 50) {
            current = (currentValues / 50) * 27f;
        } else if (currentValues > 50 && currentValues <= 60) {
            current = 27f + (currentValues - 50f) * 2.7f;
        } else {
            current = ((currentValues - 50) / 50) * sweepAngle;

        }
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                currentAngle = animatedValue;
            }
        });
        progressAnimator.start();
    }


    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setIsShowCurrentSpeed(boolean isShowCurrentSpeed) {
        this.isShowCurrentSpeed = isShowCurrentSpeed;
    }

    private void reloadView() {

        bgRect = new RectF();
        bgRect.top = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);

        //圆心
        centerX = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;
        centerY = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(Color.parseColor(longDegreeColor));

        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(Color.parseColor(bgArcColor));
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(getResources().getColor(R.color.RED_FB4E30));
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //显示单位文字
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(Color.parseColor(sanHintString));
        hintPaint.setTextAlign(Paint.Align.CENTER);

        //显示标题文字
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(Color.parseColor(hintColor));
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);

    }
}