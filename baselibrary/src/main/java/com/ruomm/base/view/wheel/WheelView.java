/**
 *	@copyright 视秀科技-2014
 * 	@author wanruome
 * 	@create 2014-11-22 下午3:58:04
 */
package com.ruomm.base.view.wheel;

import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.ruomm.base.view.wheel.adaper.WheelAdapter;
import com.ruomm.base.view.wheel.listener.OnWheelChangedListener;
import com.ruomm.base.view.wheel.listener.OnWheelScrollListener;
import com.ruomm.base.view.wheel.util.WheelItemValue;
import com.ruomm.R;

public class WheelView extends View {
	/*
	 * 下面注释了的常量定义配合if和else后不需要使用了 private static final int Value_AlginType_Right = 1;
	 */
	/*
	 * wheel 对齐方式
	 */
	private static final int Value_AlginType_Left = 0;
	private static final int Value_AlginType_Center = 2;
	/*
	 * wheel 最小开始滚动的速度
	 */
	private static final int Value_BaseFlingVelocity = 600;
	/*
	 * wheel 滚动距离的速度参考
	 */
	private static final int Value_BaseFlingSetp = 800;
	/*
	 * wheel 滚动的步长百分比
	 */
	private static final float Value_Scroll_PerStep = 0.25f;
	/*
	 * wheel 滚动的速度参考，此值越大滚动起始和结束速度差越小
	 */
	private static final float Value_Scroll_MinSpeend = 15;
	/*
	 * 字体实际高度和字体声明高度百分之比
	 */
	private static final float Value_PaintText_Offset = 0.6f;

	private String wheel_tag_text = null;
	private int wheel_tag_color = 0;
	private float wheel_tag_percent = -1;
	private float wheel_centerX_percent = 0.5f;
	private float wheel_tag_size = 36;

	// 适配器
	private WheelAdapter mWheelAdapter;
	// View的高度和宽度以及中心的位置
	private int size_height;
	private int size_width;
	private float center_y;
	private float center_x;
	// item最大的宽度
	private float max_textwidth;

	// 滚动事件定义
	private final GestureDetector mGestureDetector;
	private final Paint paint_text;
	private final Paint paint_line;
	// 文本着色器
	private Shader shader_bg;
	private Shader shader_current;
	// 定义字体颜色
	// 当期字体颜色
	private int textcolor_current = 0xFFF8413C;
	// 渐变中心颜色
	private int textcolor_dark = 0xA0000000;
	// 渐变边缘颜色
	private int textcolor_little = 0x32000000;
	// 中间分割线颜色
	private int skiplinecolor = 0x00000000;
	// 每个Item行距
	private float text_lineSpacing = 1.7f;
	// 文字的间距，1为实际排版无间距
	private float text_rowSpacing = 1.1f;
	// 每行开始和结束文字倾斜角度
	private float text_Skew_Start = 0;
	private float text_Skew_End = 0;
	// Item的缩放偏移百分比,最顶端的宽度为最大宽度X(1-zoom_width)
	private float zoom_width = 0.0f;
	// 文字大小偏移百分比,最顶端的文字大小最大文字大小X(1-zoom_textsize)
	private float zoom_textsize = 0.0f;
	// 滚轮x方向距离中心偏移系数，
	private float wheel_x_skewing = 0;
	// Wheel滚动速度，速度越大手势Fling时候越快
	private float wheel_flingspeed = 1.0f;
	// 定义字体大小
	private float textsize = 36;
	// 中心Item高度
	private float Max_Offset = 36;
	// 定义对齐方式
	private int algin_type = Value_AlginType_Center;
	// 上下文字倾斜是否自适应，自适应则上下倾斜相反，否则上下倾斜一致
	private boolean isAdjustTextSkew = true;
	// 是否循环滚动
	boolean isCyclic = true;

	// 滑动任务
	private FlingThread myScrollTask;
	// 当前Item
	private int currentItem;
	// 当前Item距离中心的偏移量
	private float scrollingOffset;
	// 绘制Item的参考坐标系
	private float[] base_coords;
	// 需要绘制的Item的集合
	private final LinkedList<WheelItemValue> listItemValues = new LinkedList<WheelItemValue>();
	// 监听事件
	private final LinkedList<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
	private final LinkedList<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();

	public WheelView(Context context) {
		this(context, null);
	}

	public WheelView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelView);
		textcolor_current = a.getColor(R.styleable.WheelView_wheel_textcolor_current, textcolor_current);
		textcolor_dark = a.getColor(R.styleable.WheelView_wheel_textcolor_drak, textcolor_dark);
		textcolor_little = a.getColor(R.styleable.WheelView_wheel_textcolor_little, textcolor_little);
		text_Skew_Start = a.getFloat(R.styleable.WheelView_wheel_text_Skew_Start, text_Skew_Start);
		text_Skew_End = a.getFloat(R.styleable.WheelView_wheel_text_Skew_End, text_Skew_End);
		text_lineSpacing = a.getFloat(R.styleable.WheelView_wheel_text_lineSpacing, text_lineSpacing);
		text_rowSpacing = a.getFloat(R.styleable.WheelView_wheel_text_rowSpacing, text_rowSpacing);
		skiplinecolor = a.getColor(R.styleable.WheelView_wheel_skiplinecolor, skiplinecolor);
		textsize = a.getDimension(R.styleable.WheelView_wheel_textsize, textsize);
		zoom_textsize = a.getFloat(R.styleable.WheelView_wheel_zoom_textsize, zoom_textsize);
		zoom_width = a.getFloat(R.styleable.WheelView_wheel_zoom_width, zoom_width);
		wheel_x_skewing = a.getFloat(R.styleable.WheelView_wheel_x_skewing, wheel_x_skewing) * textsize;
		wheel_flingspeed = a.getFloat(R.styleable.WheelView_wheel_flingspeed, wheel_flingspeed);
		wheel_tag_color = a.getColor(R.styleable.WheelView_wheel_tag_color, wheel_tag_color);
		wheel_tag_text = a.getString(R.styleable.WheelView_wheel_tag_text);
		wheel_tag_size = a.getDimension(R.styleable.WheelView_wheel_tag_size, wheel_tag_size);
		wheel_tag_percent = a.getFloat(R.styleable.WheelView_wheel_tag_percent, wheel_tag_percent);
		wheel_centerX_percent = a.getFloat(R.styleable.WheelView_wheel_centerX_percent, wheel_centerX_percent);
		algin_type = a.getInteger(R.styleable.WheelView_wheel_algin_type, algin_type);
		isAdjustTextSkew = a.getBoolean(R.styleable.WheelView_wheel_isAdjustTextSkew, isAdjustTextSkew);
		isCyclic = a.getBoolean(R.styleable.WheelView_wheel_isCyclic, isCyclic);
		a.recycle();
		if (text_Skew_Start > 0.99 || text_Skew_Start < -0.99) {
			text_Skew_Start = 0;
		}
		if (text_Skew_End > 0.99 || text_Skew_End < -0.99) {
			text_Skew_End = 0;
		}
		if (wheel_flingspeed < 0.1 || wheel_flingspeed > 100) {
			wheel_flingspeed = 1;
		}
		if (wheel_tag_color == 0) {
			wheel_tag_color = textcolor_current;
		}
		Max_Offset = textsize * text_lineSpacing;
		// 声明画笔
		paint_line = new Paint();
		paint_line.setAntiAlias(true);
		paint_line.setColor(skiplinecolor);
		paint_line.setStyle(Paint.Style.STROKE);// 设置非填充
		paint_line.setStrokeWidth(1);
		paint_text = new Paint();
		paint_text.setAntiAlias(true);
		paint_text.setColor(Color.BLACK);
		paint_text.setTextSize(textsize);
		paint_text.setStyle(Paint.Style.STROKE);
		// 滑动变量定义
		mGestureDetector = new GestureDetector(context, mGestureListener);
		mGestureDetector.setIsLongpressEnabled(false);

	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		size_width = MeasureSpec.getSize(widthMeasureSpec);
		size_height = MeasureSpec.getSize(heightMeasureSpec);
		center_y = size_height / 2.0f;
		center_x = size_width * wheel_centerX_percent;
		// 初始化画笔
		shader_bg = new LinearGradient(0, 0, 0, size_height, new int[] { textcolor_little, textcolor_dark,
				textcolor_little }, new float[] { 0.0f, 0.5f, 1.0f }, TileMode.REPEAT);
		shader_current = new LinearGradient(0, 0, 0, size_height, textcolor_current, textcolor_current, TileMode.CLAMP);
	};

	// 设置适配器
	public void setAdapter(WheelAdapter wheelAdapter) {
		this.mWheelAdapter = wheelAdapter;
		calBaseCoord();
		calCurrentWhenSetAdapter();
		invalidate();

	}

	private void calBaseCoord() {
		if (null != mWheelAdapter) {
			String tempstring = mWheelAdapter.getCalWidhtString();
			if (null != tempstring && tempstring.length() > 0) {
				paint_text.setTextScaleX(1.0f);
				paint_text.setTextSkewX(0);
				paint_text.setTextSize(textsize);
				int temp_length = tempstring.length();
				float chars_length[] = new float[temp_length];

				paint_text.getTextWidths(tempstring, chars_length);
				// 计算最大宽度
				max_textwidth = 0;
				for (int temp = 0; temp < temp_length; temp++) {
					if (temp != temp_length - 1) {
						max_textwidth += chars_length[temp] * text_rowSpacing;
					}
					else {
						max_textwidth += chars_length[temp];
					}
				}
				// 计算坐标系
				base_coords = new float[temp_length];
				if (algin_type == Value_AlginType_Left) {
					base_coords[0] = 0;
					float temp_coord = 0;
					for (int temp = 0; temp < chars_length.length; temp++) {
						base_coords[temp] = temp_coord * text_rowSpacing;
						temp_coord += chars_length[temp];
					}
				}
				else if (algin_type == Value_AlginType_Center) {
					float temp_coord = 0;
					for (int temp = 0; temp < chars_length.length; temp++) {
						base_coords[temp] = (temp_coord - max_textwidth / 2) * text_rowSpacing;
						temp_coord += chars_length[temp];
					}
				}
				else {
					float temp_coord = 0;
					base_coords[temp_length - 1] = chars_length[temp_length - 1];
					for (int temp = temp_length - 2; temp >= 0; temp--) {
						temp_coord += chars_length[temp + 1] * text_rowSpacing;
						base_coords[temp] = chars_length[temp] + temp_coord;

					}
				}
			}
			else {
				base_coords = null;
				max_textwidth = 0;
			}
		}
		else {
			base_coords = null;
			max_textwidth = 0;
		}

	}

	public WheelAdapter getAdapter() {
		return mWheelAdapter;
	}

	// 设置监听
	public void addChangingListener(OnWheelChangedListener listener) {
		changingListeners.add(listener);
	}

	public void removeChangingListener(OnWheelChangedListener listener) {
		changingListeners.remove(listener);
	}

	protected void notifyChangingListeners(int oldValue, int newValue) {
		for (OnWheelChangedListener listener : changingListeners) {
			listener.onChanged(this, oldValue, newValue);
		}
	}

	public void addScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.add(listener);
	}

	public void removeScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.remove(listener);
	}

	protected void notifyScrollingListenersAboutStart() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingStarted(this);
		}
	}

	protected void notifyScrollingListenersAboutEnd() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingFinished(this);
		}
	}

	// 设置当前的Item
	public int getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(int index) {
		if (mWheelAdapter == null || mWheelAdapter.getCount() == 0) {
			return;
		}
		if (index < 0 || index >= mWheelAdapter.getCount()) {
			if (isCyclic) {
				while (index < 0) {
					index += mWheelAdapter.getCount();

				}
				index = index % mWheelAdapter.getCount();
			}
			else {
				return;
			}
		}

		// if (null != myScrollTask) {
		// myScrollTask.cancel(true);
		// }
		if (index != currentItem) {
			int old = currentItem;
			currentItem = index;
			notifyChangingListeners(old, currentItem);
		}

		forceStopScrolling();
	}

	private final SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {

		@Override
		public boolean onDown(MotionEvent e) {
			// MLog.i("手势解析", "onDown");
			return super.onDown(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			calCurrentAndOffset(distanceY);
			invalidate();
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			float total_distanceY = 0;
			if (velocityY > Value_BaseFlingVelocity) {
				float temp_offset = velocityY - Value_BaseFlingVelocity;
				total_distanceY = -(1 + temp_offset * wheel_flingspeed / Value_BaseFlingSetp) * Max_Offset;
			}
			else if (velocityY < -Value_BaseFlingVelocity) {
				float temp_offset = -Value_BaseFlingVelocity - velocityY;
				total_distanceY = (1 + temp_offset * wheel_flingspeed / Value_BaseFlingSetp) * Max_Offset;

			}
			if (total_distanceY > 10 || total_distanceY < -10) {
				myScrollTask = new FlingThread(total_distanceY);
				myScrollTask.start();
				// myScrollTask.execute(total_distanceY);
				return true;
			}
			else {
				return false;
			}

		}

	};

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int taction = event.getAction();
		boolean flagEvent = mGestureDetector.onTouchEvent(event);
		if (taction == MotionEvent.ACTION_DOWN) {
			forceStopScrolling();
			notifyScrollingListenersAboutEnd();
		}
		if (taction != MotionEvent.ACTION_DOWN && taction != MotionEvent.ACTION_MOVE) {

			if (flagEvent == false) {
				stopScrolling();
				notifyScrollingListenersAboutEnd();
			}

		}

		return true;
	}

	private void calCurrentWhenSetAdapter() {
		int old = currentItem;
		scrollingOffset = 0;
		if (null == mWheelAdapter || mWheelAdapter.getCount() == 0) {
			currentItem = -1;

		}
		else {
			if (currentItem < 0) {
				currentItem = 0;
			}
			else if (currentItem >= mWheelAdapter.getCount()) {
				currentItem = mWheelAdapter.getCount() - 1;
			}
		}
		notifyChangingListeners(old, currentItem);
	}

	private void calCurrentAndOffset(float distanceY) {
		if (null == mWheelAdapter || mWheelAdapter.getCount() == 0) {
			scrollingOffset = 0;
			currentItem = -1;
			return;
		}
		scrollingOffset = scrollingOffset + distanceY;
		int offset_item = 0;
		if (scrollingOffset > Max_Offset / 2) {
			while (scrollingOffset > Max_Offset / 2) {
				offset_item = offset_item + 1;
				scrollingOffset = scrollingOffset - Max_Offset;

			}
		}
		else if (scrollingOffset < -Max_Offset / 2) {
			while (scrollingOffset < -Max_Offset / 2) {
				offset_item = offset_item - 1;
				scrollingOffset = scrollingOffset + Max_Offset;
			}
		}
		int old = currentItem;
		if (isCyclic) {

			currentItem = currentItem + offset_item;
			if (currentItem < 0) {
				while (currentItem < 0) {
					currentItem = currentItem + mWheelAdapter.getCount();

				}
			}
			else if (currentItem >= mWheelAdapter.getCount()) {
				while (currentItem >= mWheelAdapter.getCount()) {
					currentItem = currentItem - mWheelAdapter.getCount();

				}
			}
		}
		else {
			currentItem = currentItem + offset_item;
			if (currentItem < 0) {
				currentItem = 0;

			}
			else if (currentItem >= mWheelAdapter.getCount()) {
				currentItem = mWheelAdapter.getCount() - 1;

			}
			if (currentItem == 0 && scrollingOffset < 0) {
				scrollingOffset = 0;
			}
			else if (currentItem == mWheelAdapter.getCount() - 1 && scrollingOffset > 0) {
				scrollingOffset = 0;
			}
		}
		if (old != currentItem) {
			notifyChangingListeners(old, currentItem);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		canvas.save();
		calCurrentValue();
		drawItemsValue(canvas);
		canvas.restore();
	}

	private void calCurrentValue() {
		listItemValues.clear();
		// 画笔还原
		paint_text.setTextScaleX(1.0f);
		if (null == mWheelAdapter || mWheelAdapter.getCount() == 0) {
			return;
		}
		float current_y = size_height / 2 - scrollingOffset;
		WheelItemValue value_top;
		WheelItemValue value_buttom;
		if (current_y >= size_height / 2) {
			// 计算底部坐标系
			value_buttom = new WheelItemValue();
			value_buttom.index = currentItem;
			value_buttom.y_postion = current_y;
			// 计算顶部坐标系
			value_top = new WheelItemValue();
			int temp_index = currentItem - 1;
			if (isCyclic) {

				while (temp_index < 0) {
					temp_index = temp_index + mWheelAdapter.getCount();

				}
				value_top.index = temp_index;
			}
			else {

				value_top.index = temp_index;
			}
			value_top.y_postion = value_buttom.y_postion - Max_Offset;

			listItemValues.add(value_top);
			listItemValues.add(value_buttom);
		}
		else {
			// 计算顶部坐标系
			value_top = new WheelItemValue();
			value_top.index = currentItem;
			value_top.y_postion = current_y;
			// 计算底部坐标系
			value_buttom = new WheelItemValue();
			int temp_index = currentItem + 1;
			if (isCyclic) {
				while (temp_index >= mWheelAdapter.getCount()) {
					temp_index = temp_index - mWheelAdapter.getCount();

				}
				value_buttom.index = temp_index;
			}
			else {

				value_buttom.index = temp_index;
			}
			value_buttom.y_postion = value_top.y_postion + Max_Offset;

		}
		// 计算各种属性
		calItemValue(value_buttom);
		calItemValue(value_top);
		// 计算文字水平拉伸

		listItemValues.add(value_top);
		listItemValues.add(value_buttom);
		calValues_Top(value_top);
		calValues_buttom(value_buttom);

	}

	private void calValues_Top(WheelItemValue value_temp) {
		float ytemp = value_temp.y_postion - value_temp.text_size * text_lineSpacing;
		int temp_index = value_temp.index - 1;
		if (isCyclic) {

			while (temp_index < 0) {
				temp_index = temp_index + mWheelAdapter.getCount();

			}
		}
		while (ytemp > 0) {
			WheelItemValue value = new WheelItemValue();
			value.index = temp_index;
			value.y_postion = ytemp;
			// 计算各种属性
			calItemValue(value);
			listItemValues.add(value);
			// 重新计算
			ytemp = value.y_postion - value.text_size * text_lineSpacing;
			temp_index = temp_index - 1;
			if (isCyclic) {

				while (temp_index < 0) {
					temp_index = temp_index + mWheelAdapter.getCount();
				}
			}

		}

	}

	private void calValues_buttom(WheelItemValue value_temp) {
		float ytemp = value_temp.y_postion + value_temp.text_size * text_lineSpacing;
		int temp_index = value_temp.index + 1;
		if (isCyclic) {

			while (temp_index >= mWheelAdapter.getCount()) {
				temp_index = temp_index - mWheelAdapter.getCount();

			}
		}
		while (ytemp < size_height) {
			WheelItemValue value = new WheelItemValue();
			value.index = temp_index;
			value.y_postion = ytemp;
			// 计算各种属性
			calItemValue(value);
			listItemValues.add(value);
			// 重新计算
			ytemp = value.y_postion + value.text_size * text_lineSpacing;
			temp_index = temp_index + 1;
			if (isCyclic) {

				while (temp_index >= mWheelAdapter.getCount()) {
					temp_index = temp_index - mWheelAdapter.getCount();
				}
			}

		}

	}

	// 计算每个Item的参数
	private void calItemValue(WheelItemValue value_cal) {
		float percent_cal = Math.abs(value_cal.y_postion - center_y) / center_y;
		float zoom_temp_width = 1 - percent_cal * zoom_width;
		float zoom_temp_text = 1 - percent_cal * zoom_textsize;
		value_cal.x_skewx = (float) (wheel_x_skewing * percent_cal * Math.sqrt(percent_cal));
		value_cal.text_size = (int) (textsize * zoom_temp_text);
		value_cal.text_ScaleX = zoom_temp_width / zoom_temp_text;
		value_cal.text_width = max_textwidth * zoom_temp_width;
		// 计算坐标系
		if (null != base_coords) {
			int temp_lenght = base_coords.length;
			value_cal.text_coords = new float[temp_lenght];
			for (int temp = 0; temp < temp_lenght; temp++) {
				value_cal.text_coords[temp] = base_coords[temp] * zoom_temp_width;
			}
		}

		// 计算文字斜角
		float skewxStart = 0;
		float skewxOffset = 0;

		if (isAdjustTextSkew) {
			if (value_cal.y_postion < center_y) {
				skewxStart = text_Skew_End * percent_cal;
				skewxOffset = (text_Skew_Start - text_Skew_End) * percent_cal;
			}
			else {
				skewxStart = -text_Skew_End * percent_cal;
				skewxOffset = (text_Skew_End - text_Skew_Start) * percent_cal;
			}
		}
		else {
			skewxStart = text_Skew_End * percent_cal;
			skewxOffset = (text_Skew_Start - text_Skew_End) * percent_cal;
		}

		if (null != base_coords) {
			int tempcounts = base_coords.length;
			value_cal.skewxs = new float[tempcounts];
			for (int temp = 0; temp < tempcounts; temp++) {
				float temppercent = (tempcounts - temp - 1) * 1.0f / (tempcounts - 1);
				value_cal.skewxs[temp] = skewxStart + skewxOffset * temppercent;
			}
		}

		// 获取文本
		if (value_cal.index >= 0 && value_cal.index < mWheelAdapter.getCount()) {
			value_cal.text_value = mWheelAdapter.getItem(value_cal.index);
		}
	}

	private void drawItemsValue(Canvas canvas) {

		for (WheelItemValue value : listItemValues) {
			if (null == value.text_value || value.text_value.length() == 0) {
				continue;
			}
			paint_text.setTextSize(value.text_size);
			paint_text.setTextScaleX(value.text_ScaleX);
			float base_y = value.y_postion + value.text_size * Value_PaintText_Offset / 2;
			float base_x = 0;
			if (algin_type == Value_AlginType_Center) {

				base_x = center_x + value.x_skewx;
			}
			else if (algin_type == Value_AlginType_Left) {
				base_x = center_x - max_textwidth / 2 + value.x_skewx;
			}
			else {
				base_x = center_x + max_textwidth / 2 + value.x_skewx;
			}
			if (value.index == currentItem) {
				paint_text.setShader(shader_current);
			}
			else {
				paint_text.setShader(shader_bg);
			}
			if (algin_type == Value_AlginType_Left || algin_type == Value_AlginType_Center) {
				// canvas.drawText(value.text_value, base_x, base_y, paint_text);
				for (int temp = 0; temp < value.text_value.length(); temp++) {
					paint_text.setTextSkewX(value.skewxs[temp]);
					canvas.drawText(value.text_value.substring(temp, temp + 1), base_x + value.text_coords[temp],
							base_y, paint_text);
				}
			}
			else {
				for (int temp = 0; temp < value.text_value.length(); temp++) {
					paint_text.setTextSkewX(value.skewxs[temp]);
					canvas.drawText(value.text_value.substring(temp, temp + 1), base_x - value.text_coords[temp],
							base_y, paint_text);
				}
			}

		}
		if (null != wheel_tag_text && wheel_tag_text.length() > 0) {
			paint_text.setTextSize(wheel_tag_size);
			paint_text.setColor(wheel_tag_color);
			paint_text.setShader(null);
			paint_text.setTextSkewX(0.0f);
			paint_text.setTextScaleX(1.0f);
			if (wheel_tag_percent > 0 && wheel_tag_percent < 1) {
				float x = size_width * wheel_tag_percent;
				canvas.drawText(" " + wheel_tag_text, x, center_y + textsize * Value_PaintText_Offset / 2, paint_text);
			}
			else {
				canvas.drawText(" " + wheel_tag_text, center_x + max_textwidth / 2, center_y + textsize
						* Value_PaintText_Offset / 2, paint_text);
			}
		}
		if (skiplinecolor != 0x00000000) {
			canvas.drawLine(0, center_y - Max_Offset / 2, size_width, center_y - Max_Offset / 2, paint_line);
			canvas.drawLine(0, center_y + Max_Offset / 2, size_width, center_y + Max_Offset / 2, paint_line);
		}

	}

	class FlingThread extends Thread {
		float total_distance;
		float target = 0;
		boolean isStopTask = false;
		boolean isForceStop = false;
		boolean flag = true;

		public FlingThread(float total_distance) {
			super();
			this.total_distance = total_distance;
			if (this.total_distance < 0) {
				this.target = -1;
			}
			else if (this.total_distance > 0) {
				this.target = 1;
			}
		}

		public void stopTask() {
			this.isStopTask = true;
		}

		public void forceStopTask() {
			this.isForceStop = true;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (flag) {
				float current_speed = (float) Math.sqrt(Math.abs(total_distance) + Value_Scroll_MinSpeend
						* Value_Scroll_MinSpeend);

				final float current_distance;
				if (target == -1) {
					current_distance = -current_speed / Value_Scroll_MinSpeend * Max_Offset * Value_Scroll_PerStep;
				}
				else {
					current_distance = current_speed / Value_Scroll_MinSpeend * Max_Offset * Value_Scroll_PerStep;
				}

				total_distance = total_distance - current_distance;
				// 更新界面
				WheelView.this.post(new Runnable() {

					@Override
					public void run() {
						if (isForceStop) {
							return;
						}
						if (flag) {
							calCurrentAndOffset(current_distance);
							invalidate();
						}

					}
				});

				if (isCyclic) {
					if (target == 1 && total_distance < 0) {
						flag = false;
					}
					else if (target == -1 && total_distance > 0) {
						flag = false;
					}
				}
				else {
					if (target == 1) {
						if (total_distance < 0) {
							flag = false;
						}
						else if (null != mWheelAdapter && currentItem == mWheelAdapter.getCount() - 1) {
							flag = false;
						}

					}
					else if (target == -1) {
						if (total_distance > 0) {
							flag = false;
						}

						else if (currentItem == 0) {
							flag = false;
						}
					}
				}
				if (isStopTask || isForceStop) {
					break;
				}
				SystemClock.sleep(20);

			}
			if (!isForceStop) {
				WheelView.this.post(new Runnable() {

					@Override
					public void run() {
						stopScrolling();
						notifyScrollingListenersAboutEnd();

					}
				});

			}

		}

	}

	private void forceStopScrolling() {
		if (null != myScrollTask) {
			myScrollTask.forceStopTask();
		}

		scrollingOffset = 0;
		calCurrentAndOffset(0);
		invalidate();

	}

	private void stopScrolling() {
		scrollingOffset = 0;
		calCurrentAndOffset(0);
		invalidate();

	}
}
