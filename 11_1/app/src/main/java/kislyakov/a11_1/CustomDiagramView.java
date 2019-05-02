package kislyakov.a11_1;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CustomDiagramView extends View {

    private Paint textPaint;
    private Paint datePaint;

    private List<Integer> columns;
    private List<Date> dates;
    private int columnCount;

    private boolean mAnimateOnDisplay;
    private long mAnimationStartTime;
    private float[] mCurrentValue;
    private float endValue;
    private Interpolator mInterpolator;

    private int animationTime;
    private boolean animationFinished;

    private float density;
    float textHeight;

    float columnTotalWidth;
    float columnTotalHeight;
    float columnWidth;

    Paint.FontMetrics fm;

    private String dateColor;
    private String columnColor;

    public CustomDiagramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomDiagramView,
                0, 0);

        try {
            dateColor = a.getString(R.styleable.CustomDiagramView_dateColor);
            columnColor = a.getString(R.styleable.CustomDiagramView_columnColor);
            if (dateColor == null) {
                dateColor = "#888888";
            }
            if (columnColor == null) {
                columnColor = "#FF0000";
            }
        } finally {
            a.recycle();
        }
        init();
    }

    public void setData(int columnCount){
        this.columnCount = columnCount;
        columns = new ArrayList<>();
        dates = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < columnCount; i++){
            columns.add(rnd.nextInt(100));
            dates.add(new Date(new Date().getTime() - 3600000*24*(columnCount-i)));
            Log.d("myTag", "Added = " + columns.get(i) + "  " + dates.get(i));
        }

    }

    public void drawColumns(Canvas canvas){
        columnTotalWidth = canvas.getWidth()/columnCount;
        columnTotalHeight = canvas.getHeight()-textHeight;
        columnWidth = density*4; //4 dp from zeplin

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor(columnColor));

        int maxColumn = Collections.max(columns);

        for (int i = 0; i < columnCount; i++){
            Rect bounds = new Rect();
            textPaint.getTextBounds(String.valueOf(columns.get(i)), 0,
                    String.valueOf(columns.get(i)).length(), bounds);

            float textWidth = bounds.width();
            float columnCurrentHeight = (columns.get(i)*(columnTotalHeight-textHeight*1.5f)/maxColumn)*mCurrentValue[i];

            canvas.drawRoundRect(new RectF(i*columnTotalWidth + columnTotalWidth/2-columnWidth/2,
                    columnTotalHeight,
                    (i+1)*columnTotalWidth - columnTotalWidth/2+columnWidth/2,
                    columnTotalHeight-columnCurrentHeight-1),
                    10,10,paint);

            canvas.drawText(String.valueOf(columns.get(i)), i*columnTotalWidth + columnTotalWidth/2-textWidth/2,
                    columnTotalHeight - textHeight/2 - columnCurrentHeight, textPaint);

            Paint.FontMetrics dateMetrics = datePaint.getFontMetrics();
            float dateHeight = (dateMetrics.descent-dateMetrics.ascent)*2;
            Rect dateBounds = new Rect();
            datePaint.getTextBounds(convertDates(dates.get(i)), 0,
                    convertDates(dates.get(i)).length(), dateBounds);
            float dateWidth = dateBounds.width();

            canvas.drawText(convertDates(dates.get(i)), i*columnTotalWidth + columnTotalWidth/2-dateWidth/2,
                    columnTotalHeight + dateHeight/2, datePaint);

        }

        if(mAnimateOnDisplay && setCurrentmValue()[setCurrentmValue().length-1] < endValue){
            invalidate();
        }
        if(mAnimateOnDisplay && setCurrentmValue()[setCurrentmValue().length-1] >= endValue){
            invalidate();
            mAnimateOnDisplay = false;
        }
    }

    public String convertDates(Date date){
        String pattern = "MM/dd";

        DateFormat df = new SimpleDateFormat(pattern);

        return df.format(date);
    }

    public void showAnimation() {
        mAnimateOnDisplay = true;
        animationFinished = false;
        //mCurrentValue = 0f;
        mAnimationStartTime = 0;
        invalidate();
    }

    private void init() {
        columnCount = 9;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#dd0000"));

        mCurrentValue = new float[columnCount];
        for (int i = 0; i < columnCount; i++){
            mCurrentValue[i] = 0.0f;
        }

        mInterpolator = new AccelerateDecelerateInterpolator();
        endValue = 1.0f;

        animationTime = 1000;

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor(columnColor));

        fm = textPaint.getFontMetrics();
        textHeight = (fm.descent-fm.ascent)*2;

        int spSize = 16;
        float scaledSizeInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spSize, getResources().getDisplayMetrics());
        textPaint.setTextSize(scaledSizeInPixels);

        datePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        datePaint.setTextSize(scaledSizeInPixels);
        datePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        spSize = 12;
        scaledSizeInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spSize, getResources().getDisplayMetrics());
        datePaint.setTextSize(scaledSizeInPixels);
        datePaint.setColor(Color.parseColor(dateColor));

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = dm.density;
        Log.d("myTag", String.valueOf(density));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("LES", "onSizeChanged w = " + w + ", h = " + h);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float dateWidth = 0;
        float dateHeight = 0;
        if(dates!=null){
        Rect dateBounds = new Rect();
        datePaint.getTextBounds(convertDates(dates.get(0))+"_", 0,   //добавляю символ-пробел между датами
                convertDates(dates.get(0)).length()+1, dateBounds);
        dateWidth = dateBounds.width();
        dateHeight = dateBounds.height(); //высота текста даты

        textPaint.getTextBounds(convertDates(dates.get(0)), 0,
                    convertDates(dates.get(0)).length(), dateBounds);
            dateHeight += dateBounds.height(); // + высота подписи столбца
            dateHeight += 200; // + константа для столбца диаграммы
    }

        int minW = getPaddingLeft() + getPaddingRight() + (int)dateWidth*dates.size(); // ширина даты * число столбцов

        int minH = getPaddingBottom() + getPaddingTop() + (int)dateHeight;

        int w = resolveSizeAndState(minW, widthMeasureSpec, 0);
        int h = resolveSizeAndState(minH, heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }

    private int measureDimension(int desiredSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = desiredSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        if (result < desiredSize){
            Log.e("LES", "The view is too small, the content might get cut");
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("LES", "onDraw");
        super.onDraw(canvas);
        if (mAnimationStartTime == 0) {
            mAnimationStartTime = System.currentTimeMillis();
        }
        drawColumns(canvas);

    }

   /* private float getCurrentmValue() {
        long now = System.currentTimeMillis();
        float pathGone = ((float) (now - mAnimationStartTime) / (animationTime));
        float interpolatedPathGone = mInterpolator.getInterpolation(pathGone);

        if (pathGone < 1.0f) {
            mCurrentValue = interpolatedPathGone;
            //mListener.onCircleAnimation(getCurrentAnimationFrameValue(interpolatedPathGone));
        } else {
            mCurrentValue = endValue;
            //mListener.onCircleAnimation(getCurrentAnimationFrameValue(1.0f));
        }

        return mCurrentValue;
    }*/

    private float[] setCurrentmValue() {

        for(int i = 0; i < mCurrentValue.length; i++){
            long now = System.currentTimeMillis();
            float pathGone = ((float) (now - mAnimationStartTime - i*animationTime/columnCount/2) / (animationTime));
            if(pathGone < 0.0f){
                mCurrentValue[i] = 0;
            }
            else if (pathGone < 1.0f) {
                float interpolatedPathGone = mInterpolator.getInterpolation(pathGone);
                mCurrentValue[i] = interpolatedPathGone;
                //mListener.onCircleAnimation(getCurrentAnimationFrameValue(interpolatedPathGone));
            } else {
                mCurrentValue[i] = endValue;
                //mListener.onCircleAnimation(getCurrentAnimationFrameValue(1.0f));
            }
        }

        return mCurrentValue;
    }


}
