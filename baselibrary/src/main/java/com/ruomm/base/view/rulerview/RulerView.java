/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年10月10日 上午9:24:40 
 */
package com.ruomm.base.view.rulerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.view.rulerview.tools.RulerChangerListener;
import com.ruomm.base.view.rulerview.tools.RulerValueFormat;
import com.ruomm.R;

public class RulerView extends View {
	private static final int[] ATTRS = new int[] { android.R.attr.padding, android.R.attr.paddingLeft,
			android.R.attr.paddingTop, android.R.attr.paddingRight, android.R.attr.paddingBottom };
	// 最小开始滚动的速度
	private static final int Value_BaseFlingVelocity = 600;
	// 滚动距离的速度参考
	private static final int Value_BaseFlingSetp = 600;
	// 滚动速度，速度越大手势Fling时候越快
	private static final float Default_flingspeed = 1.0f;
	// 滚动的速度参考，此值越大滚动起始和结束速度差越小
	private static final float Value_Scroll_MinSpeend = 10;
	// 滚动的步长百分比
	private static final float Value_Scroll_PerStep = 0.25f;
	// 默认标尺开始数值
	private static final int Default_valueStart = 0;
	// 默认标尺结束数值
	private static final int Default_valueEnd = 100;
	// 默认单位数值
	private static final int Default_valueUnit = 10;
	// 默认每个单位格子数
	private static final int Default_valueUnitMarkNums = 10;
	// 默认标记文字大小
	private static final int Default_markTextSize = 14;
	// 默认默认单位宽度
	private static final int Default_unitWidth = 50;
	// 默认当期刻度标记高度
	private static final int Default_currentMarkHeight = 32;
	// 默认当期刻度标记宽度
	private static final float Default_currentMarkWidth = 2.5f;
	// 默认当期刻度标记颜色
	private static final int Default_currentMarkColor = 0xFFEB3036;
	// 默认大刻度标记高度
	private static final int Default_markHeight = 28;
	// 默认大刻度标记宽度
	private static final float Default_markWidth = 1.5f;
	// 默认小刻度标记高度
	private static final int Default_smallMarkHeight = 20;
	// 默认小刻度标记宽度
	private static final float Default_smallMarkWidth = 1.5f;
	// 默认渐变颜色中间色
	private static final int Default_shaderColor_dark = 0xFF333333;
	// 默认渐变颜色两侧颜色
	private static final int Default_shaderColor_little = 0xFFC8C8C8;
	private int paddingLeft;
	private int paddingRight;
	private int paddingTop;
	private int paddingButtom;
	protected float valueStart = Default_valueStart;
	protected float valueEnd = Default_valueEnd;
	protected float valueUnit = Default_valueUnit;
	protected float absValueUnit = Default_valueUnit;
	protected int valueUnitMarkNums = Default_valueUnitMarkNums;
	protected int markTextSize = 0;
	protected int unitWidth = 0;
	protected int currentMarkHeight = 0;
	protected int currentMarkWidth = 0;
	protected int currentMarkColor = Default_currentMarkColor;
	protected int markHeight = 0;
	protected int markWidth = 0;
	protected int smallMarkHeight = 0;
	protected int smallMarkWidth = 0;
	protected int shaderColor_dark = Default_shaderColor_dark;
	protected int shaderColor_little = Default_shaderColor_little;

	protected Context mContext;
	private float currentValue = valueStart;

	private Paint paint_ruler;
	private Paint paint_current;
	private Shader shader_ruler;
	private String valueFormat;
	private String valueCurrentFormat;
	// 滚动事件
	private GestureDetector mGestureDetector;
	private float flingSpeedVaule = Default_flingspeed;
	private float flingStepValue = 32;
	private FlingThread mFlingThread = null;
	// 监听和适配器
	private Handler UIHandler;
	private RulerChangerListener mRulerChangerListener;
	private RulerValueFormat rulerValueFormat;

	/**
	 * 公共调用函数
	 */
	public RulerChangerListener getRulerChangerListener() {
		return mRulerChangerListener;
	}

	// 设置监听器
	public void setRulerChangerListener(RulerChangerListener mRulerChangerListener) {
		this.mRulerChangerListener = mRulerChangerListener;
	}

	// 设置输出格式(适配器方式)
	public void setValueFormat(RulerValueFormat rulerValueFormat) {
		this.rulerValueFormat = rulerValueFormat;
		this.valueFormat = null;
	}

	// 设置输出格式(StringFormat方式)
	public void setValueFormat(String valueFormat, String valueCurrentFormat) {
		this.valueFormat = valueFormat;
		this.valueCurrentFormat = valueCurrentFormat;
		this.rulerValueFormat = null;
	}

	// 设置Ruler的数值范围、单元刻度、单元刻度中小刻度数目
	public void setValueConfig(float valueStart, float valueEnd) {
		setValueConfig(valueStart, valueEnd, 0, 0);
	}

	public void setValueConfig(float valueStart, float valueEnd, float valueUnit) {
		setValueConfig(valueStart, valueEnd, valueUnit, 0);
	}

	public void setValueConfig(float valueStart, float valueEnd, float valueUnit, int unitMarkerNumbers) {
		this.valueStart = valueStart;
		this.valueEnd = valueEnd;
		if (valueUnit != 0) {
			this.valueUnit = valueUnit;
		}
		setRealUintValue();
		if (unitMarkerNumbers > 0) {
			this.valueUnitMarkNums = unitMarkerNumbers;
		}
		float oldValue = this.currentValue;
		this.currentValue = getRealCurrentValue(this.currentValue);
		if (null != mRulerChangerListener) {
			mRulerChangerListener.onRulerValueChanger(RulerView.this, this.currentValue, oldValue);
		}
		invalidate();
	}

	// 设置当期游标位置
	public void setCurrentValue(float currentValue) {
		float oldValue = this.currentValue;
		this.currentValue = getRealCurrentValue(currentValue);
		publishValueChange(this.currentValue, oldValue, false);
		invalidate();
	}

	// 获取当期游标位置的值
	public float getCurrentValue() {
		return this.currentValue;
	}

	// 获取当期游标位置的值的字符串
	public String getCurrentValueToString() {
		if (null != rulerValueFormat) {
			return rulerValueFormat.getCurrentStringByValue(this.currentValue);
		}
		else if (!TextUtils.isEmpty(valueCurrentFormat)) {
			return String.format(valueCurrentFormat, this.currentValue);
		}
		else {

			if (absValueUnit >= 100) {
				return String.format("%.0f", this.currentValue);
			}
			else if (absValueUnit >= 10 && absValueUnit < 100) {
				return String.format("%.1f", this.currentValue);
			}
			else if (absValueUnit >= 1 && absValueUnit < 10) {
				return String.format("%.2f", this.currentValue);
			}
			else {
				return String.format("%.3f", this.currentValue);
			}
		}
	}

	/**
	 * 构造函数
	 */
	public RulerView(Context context) {
		super(context);
		this.mContext = context;
		initPublic(context);

	}

	public RulerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttr(context, attrs, 0);
		initPublic(context);
	}

	public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttr(context, attrs, defStyleAttr);
		initPublic(context);
	}

	private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
		TypedArray aPadding = context.obtainStyledAttributes(attrs, ATTRS);
		int padding = aPadding.getDimensionPixelSize(0, 0);
		paddingLeft = aPadding.getDimensionPixelSize(1, padding);
		paddingTop = aPadding.getDimensionPixelSize(2, padding);
		paddingRight = aPadding.getDimensionPixelSize(3, padding);
		paddingButtom = aPadding.getDimensionPixelSize(4, padding);
		aPadding.recycle();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RulerView);
		valueStart = a.getFloat(R.styleable.RulerView_ruler_valueStart, valueStart);
		valueEnd = a.getFloat(R.styleable.RulerView_ruler_valueEnd, valueEnd);
		valueUnit = a.getFloat(R.styleable.RulerView_ruler_valueUnit, valueUnit);
		valueUnitMarkNums = a.getInteger(R.styleable.RulerView_ruler_valueUnitMarkNums, valueUnitMarkNums);
		currentValue = a.getFloat(R.styleable.RulerView_ruler_valueCurrent, currentValue);
		markTextSize = a.getDimensionPixelSize(R.styleable.RulerView_ruler_markTextSize, markTextSize);
		unitWidth = a.getDimensionPixelSize(R.styleable.RulerView_ruler_unitWidth, unitWidth);
		currentMarkHeight = a.getDimensionPixelSize(R.styleable.RulerView_ruler_currentMarkHeight, currentMarkHeight);
		currentMarkWidth = a.getDimensionPixelSize(R.styleable.RulerView_ruler_currentMarkWidth, currentMarkWidth);
		currentMarkColor = a.getColor(R.styleable.RulerView_ruler_currentMarkColor, currentMarkColor);
		markHeight = a.getDimensionPixelSize(R.styleable.RulerView_ruler_markHeight, markHeight);
		markWidth = a.getDimensionPixelSize(R.styleable.RulerView_ruler_markWidth, markWidth);
		smallMarkHeight = a.getDimensionPixelSize(R.styleable.RulerView_ruler_smallMarkHeight, smallMarkHeight);
		smallMarkWidth = a.getDimensionPixelSize(R.styleable.RulerView_ruler_smallMarkWidth, smallMarkWidth);
		shaderColor_dark = a.getColor(R.styleable.RulerView_ruler_shaderColor_drak, shaderColor_dark);
		shaderColor_little = a.getColor(R.styleable.RulerView_ruler_shaderColor_little, shaderColor_little);
		flingSpeedVaule = a.getFloat(R.styleable.RulerView_ruler_flingSpeed, flingSpeedVaule);
		a.recycle();
	}

	private void initPublic(Context context) {
		this.mContext = context;
		this.UIHandler = new Handler(Looper.getMainLooper());
		// 初始化参数设置
		if (markTextSize <= 0) {
			markTextSize = DisplayUtil.dipTopx(context, Default_markTextSize);
		}
		if (unitWidth <= 0) {
			unitWidth = DisplayUtil.dipTopx(context, Default_unitWidth);
		}
		if (currentMarkHeight <= 0) {
			currentMarkHeight = DisplayUtil.dipTopx(context, Default_currentMarkHeight);
		}
		if (currentMarkWidth <= 0) {
			currentMarkWidth = DisplayUtil.dipTopx(context, Default_currentMarkWidth);
		}
		if (markWidth <= 0) {
			markWidth = DisplayUtil.dipTopx(context, Default_markWidth);
		}
		if (markHeight <= 0) {
			markHeight = DisplayUtil.dipTopx(context, Default_markHeight);
		}
		if (smallMarkWidth <= 0) {
			smallMarkWidth = DisplayUtil.dipTopx(context, Default_smallMarkWidth);
		}
		if (smallMarkHeight <= 0) {
			smallMarkHeight = DisplayUtil.dipTopx(context, Default_smallMarkHeight);
		}
		if (valueUnitMarkNums < 1) {
			valueUnitMarkNums = Default_valueUnitMarkNums;
		}
		currentValue = getRealCurrentValue(currentValue);
		setRealUintValue();
		absValueUnit = Math.abs(valueUnit);
		// 初始化参数设置结束
		// paint赋值开始
		paint_ruler = new Paint();
		paint_ruler.setAntiAlias(true);
		paint_ruler.setColor(shaderColor_dark);

		paint_ruler.setTextSize(markTextSize);
		paint_ruler.setStyle(Paint.Style.FILL);
		paint_current = new Paint();
		paint_current.setAntiAlias(true);
		paint_current.setTextSize(markTextSize);
		paint_current.setColor(currentMarkColor);
		paint_current.setStyle(Paint.Style.FILL);
		// 滚动事件
		mGestureDetector = new GestureDetector(context, mGestureListener);
		mGestureDetector.setIsLongpressEnabled(false);
		flingStepValue = DisplayUtil.dipTopx(context, 32);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		drawRulerByValue(currentValue, canvas);

	}

	private void drawRulerByValue(float value, Canvas canvas) {
		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();
		if (canvasWidth <= 0 || canvasHeight <= 0) {
			return;
		}
		if (valueUnit == 0 || valueStart == valueEnd) {
			return;
		}
		canvas.save();
		canvas.clipRect(paddingLeft, paddingTop, canvasWidth - paddingRight, canvasHeight - paddingButtom);
		int cavansCenterX = (canvasWidth + paddingLeft - paddingRight) / 2;
		int currentTop = paddingTop;
		int currentLeft = cavansCenterX - currentMarkWidth / 2;
		int currentBottom = currentTop + currentMarkHeight;
		int currentRight = currentLeft + currentMarkWidth;
		canvas.drawRect(currentLeft, currentTop, currentRight, currentBottom, paint_current);
		if (null == shader_ruler) {
			float shaderLeft = paddingLeft * 1.0f / canvasWidth;
			float shaderRight = 1 - paddingRight * 1.0f / canvasWidth;
			float shaderCenter = (shaderLeft + shaderRight) / 2;
			shader_ruler = new LinearGradient(0, 0, canvasWidth, 0, new int[] { shaderColor_little, shaderColor_little,
					shaderColor_dark, shaderColor_little, shaderColor_little }, new float[] { 0.0f, shaderLeft,
					shaderCenter, shaderRight, 1.0f }, TileMode.REPEAT);

			paint_ruler.setShader(shader_ruler);
		}
		float offsetOfValue = value - valueStart;
		float unitsOfValue = offsetOfValue / valueUnit;
		int originUnits = (int) unitsOfValue;
		float originValue = valueStart + originUnits * valueUnit;
		int originDrawX = (int) (cavansCenterX - (unitsOfValue - originUnits) * unitWidth);
		float drawSideIndex = 0;
		for (int i = 0;; i++) {
			int drawX = i * unitWidth / valueUnitMarkNums + originDrawX;
			float valueDraw = originValue + valueUnit * i / valueUnitMarkNums;
			if (drawX > canvasWidth - paddingRight || drawX < paddingLeft) {
				drawSideIndex = drawSideIndex + valueUnitMarkNums;
				float drawSideX = drawSideIndex * unitWidth / valueUnitMarkNums + originDrawX;
				float drawSideValue = originValue + valueUnit * drawSideIndex / valueUnitMarkNums;
				if (!isTrueDrawValue(drawSideValue)) {
					break;
				}
				String valueDrawString = getStringByValue(drawSideValue);
				if (!TextUtils.isEmpty(valueDrawString)) {
					float drawTextWidth = paint_ruler.measureText(valueDrawString);
					int drawTextX = (int) (drawSideX - drawTextWidth / 2);
					int drawTextY = canvasHeight - paddingButtom - (int) (markTextSize * 0.15f);
					canvas.drawText(valueDrawString, drawTextX, drawTextY, paint_ruler);
				}
				break;
			}
			if (!isTrueDrawValue(valueDraw)) {
				break;
			}

			Rect rect = null;
			if (i % valueUnitMarkNums == 0) {
				drawSideIndex = i;
				int drawLeft = drawX - markWidth / 2;
				int drawRight = drawX - markWidth / 2 + markWidth;
				int drawTop = paddingTop;
				int drawBottm = drawTop + markHeight;
				rect = new Rect(drawLeft, drawTop, drawRight, drawBottm);
				// 画刻度文字

				String valueDrawString = getStringByValue(valueDraw);
				if (!TextUtils.isEmpty(valueDrawString)) {

					float drawTextWidth = paint_ruler.measureText(valueDrawString);
					int drawTextX = (int) (drawX - drawTextWidth / 2);
					int drawTextY = canvasHeight - paddingButtom - (int) (markTextSize * 0.15f);
					canvas.drawText(valueDrawString, drawTextX, drawTextY, paint_ruler);
				}
			}
			else if (valueUnitMarkNums == 10 && i % 5 == 0) {
				int drawLeft = drawX - smallMarkWidth / 2;
				int drawRight = drawX - smallMarkWidth / 2 + smallMarkWidth;
				int drawTop = paddingTop;
				int drawBottm = drawTop + smallMarkHeight + (markHeight - smallMarkHeight) * 2 / 5;
				rect = new Rect(drawLeft, drawTop, drawRight, drawBottm);
			}
			else {
				int drawLeft = drawX - smallMarkWidth / 2;
				int drawRight = drawX - smallMarkWidth / 2 + smallMarkWidth;
				int drawTop = paddingTop;
				int drawBottm = drawTop + smallMarkHeight;
				rect = new Rect(drawLeft, drawTop, drawRight, drawBottm);
			}
			canvas.drawRect(rect, paint_ruler);
		}

		for (int i = -1;; i--) {
			int drawX = i * unitWidth / valueUnitMarkNums + originDrawX;
			float valueDraw = originValue + valueUnit * i / valueUnitMarkNums;
			if (drawX > canvasWidth - paddingRight || drawX < paddingLeft) {
				drawSideIndex = drawSideIndex - valueUnitMarkNums;
				float drawSideX = drawSideIndex * unitWidth / valueUnitMarkNums + originDrawX;
				float drawSideValue = originValue + valueUnit * drawSideIndex / valueUnitMarkNums;
				if (!isTrueDrawValue(drawSideValue)) {
					break;
				}
				String valueDrawString = getStringByValue(drawSideValue);
				if (!TextUtils.isEmpty(valueDrawString)) {
					float drawTextWidth = paint_ruler.measureText(valueDrawString);
					int drawTextX = (int) (drawSideX - drawTextWidth / 2);
					int drawTextY = canvasHeight - paddingButtom - (int) (markTextSize * 0.15f);
					canvas.drawText(valueDrawString, drawTextX, drawTextY, paint_ruler);
				}
				break;
			}
			if (!isTrueDrawValue(valueDraw)) {
				break;
			}

			Rect rect = null;
			if (i % valueUnitMarkNums == 0) {
				drawSideIndex = i;
				int drawLeft = drawX - markWidth / 2;
				int drawRight = drawX - markWidth / 2 + markWidth;
				int drawTop = paddingTop;
				int drawBottm = drawTop + markHeight;
				rect = new Rect(drawLeft, drawTop, drawRight, drawBottm);
				// 画刻度文字

				String valueDrawString = getStringByValue(valueDraw);
				if (!TextUtils.isEmpty(valueDrawString)) {
					float drawTextWidth = paint_ruler.measureText(valueDrawString);
					int drawTextX = (int) (drawX - drawTextWidth / 2);
					int drawTextY = canvasHeight - paddingButtom - (int) (markTextSize * 0.15f);
					canvas.drawText(valueDrawString, drawTextX, drawTextY, paint_ruler);
				}
			}
			else if (valueUnitMarkNums == 10 && i % 5 == 0) {
				int drawLeft = drawX - smallMarkWidth / 2;
				int drawRight = drawX - smallMarkWidth / 2 + smallMarkWidth;
				int drawTop = paddingTop;
				int drawBottm = drawTop + smallMarkHeight + (markHeight - smallMarkHeight) * 2 / 5;
				rect = new Rect(drawLeft, drawTop, drawRight, drawBottm);
			}
			else {
				int drawLeft = drawX - smallMarkWidth / 2;
				int drawRight = drawX - smallMarkWidth / 2 + smallMarkWidth;
				int drawTop = paddingTop;
				int drawBottm = drawTop + smallMarkHeight;
				rect = new Rect(drawLeft, drawTop, drawRight, drawBottm);
			}
			canvas.drawRect(rect, paint_ruler);
		}
		canvas.restore();

	}

	public String getStringByValue(float valueDraw) {
		if (null != rulerValueFormat) {
			return rulerValueFormat.getStringByValue(valueDraw);
		}
		else if (!TextUtils.isEmpty(valueFormat)) {
			return String.format(valueFormat, valueDraw);
		}
		else {

			if (absValueUnit >= 10) {
				return String.format("%.0f", valueDraw);
			}
			else if (absValueUnit >= 1 && absValueUnit < 10) {
				return String.format("%.1f", valueDraw);
			}
			else {
				return String.format("%.2f", valueDraw);
			}
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return true;
	}

	private final SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {

		@Override
		public boolean onDown(MotionEvent e) {
			stopFlingThread();
			return super.onDown(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			if (null != mFlingThread) {
				mFlingThread.setStop();
				mFlingThread = null;
			}
			else {
				float oldValue = currentValue;
				float value = currentValue + distanceX * valueUnit / unitWidth;
				currentValue = getRealCurrentValue(value);
				publishValueChange(currentValue, oldValue, false);
				invalidate();
			}
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			float total_distanceX = 0;
			if (velocityX > Value_BaseFlingVelocity) {
				float temp_offset = velocityX - Value_BaseFlingVelocity;

				total_distanceX = -(1 + temp_offset * flingSpeedVaule / Value_BaseFlingSetp) * flingStepValue;
			}
			else if (velocityX < -Value_BaseFlingVelocity) {
				float temp_offset = -velocityX - Value_BaseFlingVelocity;
				total_distanceX = (1 + temp_offset * flingSpeedVaule / Value_BaseFlingSetp) * flingStepValue;
			}
			if (total_distanceX > 10 || total_distanceX < -10) {
				stopFlingThread();
				startFlingThread(total_distanceX);

			}
			else {
				return false;
			}
			return true;
		}

	};

	private void setRealUintValue() {
		absValueUnit = Math.abs(valueUnit);
		if (valueStart < valueEnd) {
			valueUnit = absValueUnit;
		}
		else if (valueStart > valueEnd) {
			valueUnit = -absValueUnit;
		}
		else {
			valueUnit = absValueUnit;
		}
	}

	// private float getRealUnitValue(float value) {
	// if (valueStart < valueEnd) {
	// return Math.abs(value);
	// }
	// else if (valueStart > valueEnd) {
	// return -Math.abs(value);
	// }
	// else {
	// return Math.abs(value);
	// }
	// }

	private float getRealCurrentValue(float value) {
		if (valueStart < valueEnd) {
			if (value < valueStart) {
				return valueStart;
			}
			else if (value > valueEnd) {
				return valueEnd;
			}
		}
		else {
			if (value > valueStart) {
				return valueStart;
			}
			else if (value < valueEnd) {
				return valueEnd;
			}
		}
		return value;
	}

	// 开始滑动
	private void startFlingThread(float total_distanceX) {
		mFlingThread = new FlingThread(total_distanceX);
		mFlingThread.start();
	}

	// 停止滑动
	private void stopFlingThread() {
		if (null != mFlingThread) {
			mFlingThread.setStop();
			mFlingThread = null;
		}
	}

	class FlingThread extends Thread {
		private float total_distanceX = 0;
		private int target = 1;
		private final boolean flag = true;
		private boolean isStop = false;

		public FlingThread(float total_distanceX) {
			super();
			this.total_distanceX = total_distanceX;
			if (this.total_distanceX < 0) {
				target = -1;
			}
			else {
				target = 1;
			}
		}

		public boolean isStop() {
			return isStop;
		}

		public void setStop() {
			this.isStop = true;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (flag) {

				float current_speed = (float) Math.sqrt(Math.abs(total_distanceX) + Value_Scroll_MinSpeend
						* Value_Scroll_MinSpeend);
				float distanceX = current_speed / Value_Scroll_MinSpeend * flingStepValue * Value_Scroll_PerStep;
				if (target == -1) {
					distanceX = 0 - distanceX;
				}

				total_distanceX = total_distanceX - distanceX;
				if (target == 1 && total_distanceX < 0) {
					break;
				}
				else if (target == -1 && total_distanceX > 0) {
					break;
				}
				else if (isStop) {
					break;
				}
				float oldValue = currentValue;
				float value = currentValue + distanceX * valueUnit / unitWidth;
				currentValue = getRealCurrentValue(value);
				publishValueChange(currentValue, oldValue, true);
				postInvalidate();
				if (currentValue == valueStart || currentValue == valueEnd) {
					break;
				}
				SystemClock.sleep(20);

			}

		}
	}

	private void publishValueChange(final float newValue, final float oldValue, boolean isOnThread) {
		if (null == mRulerChangerListener) {
			return;
		}
		if (newValue == oldValue) {
			return;
		}
		if (isOnThread) {
			UIHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (null != mRulerChangerListener) {

						mRulerChangerListener.onRulerValueChanger(RulerView.this, newValue, oldValue);
					}
				}
			});
		}
		else {
			if (null != mRulerChangerListener) {

				mRulerChangerListener.onRulerValueChanger(RulerView.this, newValue, oldValue);
			}
		}
	}

	private boolean isTrueDrawValue(float valueDraw) {
		if (valueStart < valueEnd) {
			if (valueDraw < valueStart || valueDraw > valueEnd) {
				return false;
			}
		}
		else {
			if (valueDraw > valueStart || valueDraw < valueEnd) {
				return false;
			}
		}
		return true;
	}

}
