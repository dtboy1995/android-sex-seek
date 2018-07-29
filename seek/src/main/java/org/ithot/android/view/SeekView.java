package org.ithot.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.ithot.android.view.listener.SVBaseCallback;

import java.lang.reflect.Method;

public class SeekView extends View {

    private static final Object NULL = null;
    private static final String TAG = "[SeekView]";
    public static final float MAX_PROGRESS = 100f;
    private static final int LONG_PRESS = 500;
    private static final int ZERO = 0;

    private boolean unPerformLongClick;
    private boolean touchEnable;
    private Handler handler = new Handler();
    private Paint.Cap seekCap = Paint.Cap.ROUND;
    private int progress;
    private SVBaseCallback callback;
    private Method methodStep;

    private int seekStartX;
    private int seekStandardY;
    private int seekBackgroundEndX;
    private int seekForegroundOffsetX;
    private int indicatorX;

    private Paint indicatorPaint;
    private boolean indicatorShadowEnable;
    private int indicatorShadowRadius;
    private int indicatorColor;
    private int indicatorRadius;
    private int indicatorShadowColor;

    private Paint seekBackgroundPaint;
    private Paint seekForegroundPaint;
    private boolean seekShadowEnable;
    private int seekForegroundColor;

    private int seekBackgroundColor;
    private int seekShadowRadius;
    private int seekShadowColor;
    private int seekHeight;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            unPerformLongClick = false;
            performLongClick();
        }
    };

    private SeekView(Context context) {
        this(context, (AttributeSet) NULL, ZERO);
    }

    public SeekView(Context context, AttributeSet attrs) {
        this(context, attrs, ZERO);
    }

    public SeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SeekView);

        indicatorRadius = (int) ta.getDimension(R.styleable.SeekView_indicatorRadius, dp2px(10));
        indicatorColor = ta.getColor(R.styleable.SeekView_indicatorColor, Color.WHITE);
        indicatorShadowEnable = ta.getBoolean(R.styleable.SeekView_indicatorShadowEnable, true);
        indicatorShadowColor = ta.getColor(R.styleable.SeekView_indicatorShadowColor, Color.BLACK);
        indicatorShadowRadius = (int) ta.getDimension(R.styleable.SeekView_indicatorShadowRadius, dp2px(2));

        seekHeight = (int) ta.getDimension(R.styleable.SeekView_seekHeight, dp2px(6));
        seekBackgroundColor = ta.getColor(R.styleable.SeekView_seekBackgroundColor, Color.GRAY);
        seekForegroundColor = ta.getColor(R.styleable.SeekView_seekForegroundColor, Color.CYAN);
        seekShadowEnable = ta.getBoolean(R.styleable.SeekView_seekShadowEnable, true);
        seekShadowColor = ta.getColor(R.styleable.SeekView_seekShadowColor, Color.BLACK);
        seekShadowRadius = (int) ta.getDimension(R.styleable.SeekView_seekShadowRadius, dp2px(2));
        touchEnable = ta.getBoolean(R.styleable.SeekView_touchEnable, true);
        String onStep = ta.getString(R.styleable.SeekView_onStep);

        if (onStep != null) {
            try {
                methodStep = getContext().getClass().getDeclaredMethod(onStep, new Class[]{int.class});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        ta.recycle();

        if (indicatorShadowEnable || seekShadowEnable) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, (Paint) NULL);
        }
        // >
        seekBackgroundPaint = new Paint();
        seekBackgroundPaint.setColor(seekBackgroundColor);
        seekBackgroundPaint.setAntiAlias(true);
        seekBackgroundPaint.setStrokeCap(seekCap);
        seekBackgroundPaint.setStrokeWidth(seekHeight);
        if (seekShadowEnable) {
            seekBackgroundPaint.setShadowLayer(seekShadowRadius, ZERO, ZERO, seekShadowColor);
        }
        seekBackgroundPaint.setStyle(Paint.Style.STROKE);
        // >
        seekForegroundPaint = new Paint();
        seekForegroundPaint.setColor(seekForegroundColor);
        seekForegroundPaint.setAntiAlias(true);
        seekForegroundPaint.setStrokeCap(seekCap);
        seekForegroundPaint.setStrokeWidth(seekHeight);
        seekForegroundPaint.setStyle(Paint.Style.STROKE);
        // >
        indicatorPaint = new Paint();
        indicatorPaint.setColor(indicatorColor);
        indicatorPaint.setAntiAlias(true);
        indicatorPaint.setStyle(Paint.Style.FILL);
        if (indicatorShadowEnable) {
            indicatorPaint.setShadowLayer(indicatorShadowRadius, ZERO, ZERO, indicatorShadowColor);
        }
    }

    private void setupCoordinate() {
        seekStandardY = getMeasuredHeight() / 2;
        indicatorX = indicatorRadius + indicatorShadowRadius;
        seekStartX = indicatorX;
        seekBackgroundEndX = getMeasuredWidth() - seekStartX;
        seekForegroundOffsetX = seekStartX;
        float each = (seekBackgroundEndX - seekStartX) / MAX_PROGRESS * this.progress;
        indicatorX += each;
        seekForegroundOffsetX += each;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int indicator = indicatorRadius * 2 + indicatorShadowRadius * 2;
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), Math.max(height, indicator));
        setupCoordinate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // >
        canvas.drawLine(seekStartX, seekStandardY, seekBackgroundEndX, seekStandardY, seekBackgroundPaint);
        // >
        canvas.drawLine(seekStartX, seekStandardY, seekForegroundOffsetX, seekStandardY, seekForegroundPaint);
        // >
        canvas.drawCircle(indicatorX, seekStandardY, indicatorRadius, indicatorPaint);
    }

    private boolean inBoundary(float ex) {
        return ex >= seekStartX && ex <= seekBackgroundEndX;
    }

    public void init(int progress) {
        this.progress = progress;
        setupCoordinate();
        invalidate();
    }

    public void init(int progress, int min, int max) {
        float unit = (max - min) / SeekView.MAX_PROGRESS;
        this.progress = (int) ((progress - min) / unit);
        debug(this.progress);
        setupCoordinate();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ex = event.getX();
        if (!inBoundary(ex)) return true;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // >
                moveIndicatorAndForeground(ex);
                unPerformLongClick = true;
                handler.removeCallbacks(runnable);
                // >
                handler.postDelayed(runnable, LONG_PRESS);
                break;
            case MotionEvent.ACTION_MOVE:
                moveIndicatorAndForeground(ex);
                break;
            case MotionEvent.ACTION_CANCEL:
                moveIndicatorAndForeground(ex);
                handler.removeCallbacks(runnable);
                break;
            case MotionEvent.ACTION_UP:
                moveIndicatorAndForeground(ex);
                if (unPerformLongClick) {
                    performClick();
                }
                break;
        }
        return true;
    }

    private void moveIndicatorAndForeground(float ex) {
        if (!touchEnable) return;
        indicatorX = (int) ex;
        seekForegroundOffsetX = (int) ex;
        this.progress = calculateSeek(ex);
        if (callback != null) {
            callback.call(progress);
        } else {
            if (methodStep != null) {
                try {
                    methodStep.invoke(getContext(), progress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        invalidate();
    }

    private int calculateSeek(float ex) {
        return Math.round((ex - seekStartX) / (seekBackgroundEndX - seekStartX) * MAX_PROGRESS);
    }

    public void setSVCallback(SVBaseCallback callback) {
        this.callback = callback;
    }

    public void setTouchEnable(boolean touchEnable) {
        this.touchEnable = touchEnable;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        indicatorPaint.setColor(this.indicatorColor);
        invalidate();
    }

    public void setSeekForegroundColor(int seekForegroundColor) {
        this.seekForegroundColor = seekForegroundColor;
        seekForegroundPaint.setColor(this.seekForegroundColor);
        invalidate();
    }

    public void setSeekBackgroundColor(int seekBackgroundColor) {
        this.seekBackgroundColor = seekBackgroundColor;
        seekBackgroundPaint.setColor(this.seekBackgroundColor);
        invalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int dp2px(int dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    private void debug(Object o) {
        Log.d(TAG, String.valueOf(o));
    }
}