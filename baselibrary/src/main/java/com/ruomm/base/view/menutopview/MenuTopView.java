package com.ruomm.base.view.menutopview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruomm.R;
import com.ruomm.base.tools.DisplayUtil;

public class MenuTopView extends RelativeLayout {
	private static final int[] ATTRS = new int[] { android.R.attr.layout_height };
	private final static int Local_Left = 0;
	private final static int Local_Right = 1;
	private final static int Local_Center = 2;
	// 监听
	private MenuTopListener listener;
	private final Context mContext;
	private final int height;
	private float menuImageSizePercent = 0.55f;
	private float menuPaddinghorizontalPercent = 0.25f;
	private int menuPaddinghorizontal;
	private final int menuPaddingvertical;
	private final int image_height;
	private ImageView ivleft;
	private int imageLeft_width;
	private ImageView ivright;
	private int imageRight_width;
	private View viewLeft;
	private View viewRight;
	private TextView tvleft;
	private int textLeft_size;
	private final int textLeft_color;
	private TextView tvright;
	private int textRight_size;
	private final int textRight_color;
	private TextView tvcenter;
	private int textCenter_size = 18;
	private int textCenter_color = 0xffffff;
	private boolean textCenter_isBlod = false;
	private boolean isCenterClick = true;
	private View view_buttom;
	private int menuButtomLineColor;
	private int menuButtomLineSize;

	// private final MyOnClickListener myOnClickListener;

	public MenuTopView(Context context) {
		this(context, null);
	}

	public MenuTopView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint({ "Recycle", "NewApi" })
	public MenuTopView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		try {
			listener = (MenuTopListener) mContext;
		}
		catch (Exception e) {
			listener = null;
		}
		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
		height = a.getDimensionPixelSize(0, 96);
		a.recycle();
		a = context.obtainStyledAttributes(attrs, R.styleable.MenuTopView);
		int imageLeft = a.getResourceId(R.styleable.MenuTopView_imageLeft, -1);
		int imageRight = a.getResourceId(R.styleable.MenuTopView_imageRight, -1);
		String textLeft = a.getString(R.styleable.MenuTopView_textLeft);
		String textCenter = a.getString(R.styleable.MenuTopView_textCenter);
		String textRight = a.getString(R.styleable.MenuTopView_textRight);
		textCenter_color = a.getColor(R.styleable.MenuTopView_menuTextColor, textCenter_color);
		textLeft_color = a.getColor(R.styleable.MenuTopView_textLeft_Color, textCenter_color);
		textRight_color = a.getColor(R.styleable.MenuTopView_textRight_Color, textCenter_color);
		textCenter_size = a.getDimensionPixelSize(R.styleable.MenuTopView_menuTextSize, textCenter_size);
		textLeft_size = a.getDimensionPixelSize(R.styleable.MenuTopView_textLeft_Size, textCenter_size);
		textRight_size = a.getDimensionPixelSize(R.styleable.MenuTopView_textRight_Size, textCenter_size);
		textCenter_size = DisplayUtil.pxTodip(mContext, textCenter_size);
		textCenter_isBlod = a.getBoolean(R.styleable.MenuTopView_menuText_isBlod, false);
		textLeft_size = DisplayUtil.pxTodip(mContext, textLeft_size);
		textRight_size = DisplayUtil.pxTodip(mContext, textRight_size);
		menuImageSizePercent = a.getFloat(R.styleable.MenuTopView_menuImageSizePercent, menuImageSizePercent);
		isCenterClick = a.getBoolean(R.styleable.MenuTopView_centerView_Clickable, isCenterClick);
		float imageLeft_widthPercent = a.getFloat(R.styleable.MenuTopView_imageLeft_percent, 1.0f);
		float imageRight_widthPercent = a.getFloat(R.styleable.MenuTopView_imageLeft_percent, 1.0f);
		menuPaddingvertical = (int) (height * (1 - menuImageSizePercent) / 2);
		image_height = height - 2 * menuPaddingvertical;
		imageLeft_width = (int) (image_height * imageLeft_widthPercent);
		imageRight_width = (int) (image_height * imageRight_widthPercent);
		menuPaddinghorizontal = a.getDimensionPixelSize(R.styleable.MenuTopView_menuPaddinghorizontal, -1);

		if (menuPaddinghorizontal < 0) {
			menuPaddinghorizontalPercent = a.getFloat(R.styleable.MenuTopView_menuPaddingPercent,
					menuPaddinghorizontalPercent);
			menuPaddinghorizontal = (int) (menuPaddinghorizontalPercent * height);
		}
		menuButtomLineSize = a.getDimensionPixelSize(R.styleable.MenuTopView_menuButtomLineSize, 0);
		menuButtomLineColor = a.getColor(R.styleable.MenuTopView_menuButtomLineColor, 0);
		a.recycle();
		setLeftView(textLeft, imageLeft, true);
		setRightView(textRight, imageRight, true);
		setCenterView(textCenter, true);
		setBottomView();

	}

	private LayoutParams getLayoutText(int positon) {
		//
		LayoutParams layoutParams = null;
		if (positon == Local_Left) {
			layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, height);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		}
		else if (positon == Local_Right) {
			layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, height);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		}
		else {
			int width = (int) (DisplayUtil.getDispalyAbsWidth(mContext) - 2.5 * height);
			layoutParams = new LayoutParams(width, height);
			// layoutParams.setMargins((int) (1.2 * height), 0, (int) (1.2 * height), 0);
			layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			// layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			// layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			// layoutParams.setMargins(menuPaddinghorizontal * 2 + height, 0, menuPaddinghorizontal
			// * 2 + height, 0);
		}
		return layoutParams;
	}

	private LayoutParams getLayoutImage(int positon) {
		//
		LayoutParams layoutParams;
		if (positon == Local_Left) {
			layoutParams = new LayoutParams(menuPaddinghorizontal * 2 + imageLeft_width, height);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		}
		else {
			layoutParams = new LayoutParams(menuPaddinghorizontal * 2 + imageRight_width, height);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		}
		return layoutParams;
	}

	private LayoutParams getLayoutView(int positon) {
		//
		LayoutParams layoutParams;
		if (positon == Local_Left) {
			layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, height);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		}
		else {
			layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, height);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		}
		return layoutParams;
	}

	private LayoutParams getLayoutButtom() {
		LayoutParams layoutParams;
		layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, menuButtomLineSize);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		return layoutParams;
	}

	private void setLeftView(String textres, int imageres, boolean isAutoAdd) {
		if (null != tvleft) {
			tvleft.setOnClickListener(null);
			tvleft.setClickable(false);
			removeView(tvleft);
			tvleft = null;
		}
		if (null != ivleft) {
			ivleft.setOnClickListener(null);
			ivleft.setClickable(false);
			removeView(ivleft);
			ivleft = null;
		}
		if (null != viewLeft) {
			viewLeft.setOnClickListener(null);
			viewLeft.setClickable(false);
			removeView(viewLeft);
			viewLeft = null;
		}
		if (!TextUtils.isEmpty(textres)) {
			tvleft = new TextView(mContext);
			tvleft.setLayoutParams(getLayoutText(Local_Left));
			tvleft.setGravity(Gravity.CENTER);
			tvleft.setPadding(menuPaddinghorizontal, 0, menuPaddinghorizontal, 0);
			tvleft.setTextColor(textLeft_color);
			tvleft.setTextSize(textLeft_size);
			addView(tvleft);
			if (isAutoAdd) {
				tvleft.setText(textres);
			}
		}
		else if (imageres > 0) {
			ivleft = new ImageView(mContext);
			ivleft.setLayoutParams(getLayoutImage(Local_Left));
			ivleft.setPadding(menuPaddinghorizontal, menuPaddingvertical, menuPaddinghorizontal, menuPaddingvertical);
			ivleft.setScaleType(ScaleType.FIT_XY);
			addView(ivleft);
			if (isAutoAdd) {
				ivleft.setImageResource(imageres);
			}

		}
		if (null != getViewLeft()) {
			getViewLeft().setId(R.id.menutop_left);
			getViewLeft().setOnClickListener(myOnClickListener);
		}
	}

	private void setRightView(String textres, int imageres, boolean isAutoAdd) {
		if (null != tvright) {
			tvright.setOnClickListener(null);
			tvright.setClickable(false);
			removeView(tvright);
			tvright = null;
		}
		if (null != ivright) {
			ivright.setOnClickListener(null);
			ivright.setClickable(false);
			removeView(ivright);
			ivright = null;
		}
		if (null != viewRight) {
			viewRight.setOnClickListener(null);
			viewRight.setClickable(false);
			removeView(viewRight);
			viewRight = null;
		}
		if (!TextUtils.isEmpty(textres)) {
			tvright = new TextView(mContext);
			tvright.setLayoutParams(getLayoutText(Local_Right));
			tvright.setGravity(Gravity.CENTER);
			tvright.setPadding(menuPaddinghorizontal, 0, menuPaddinghorizontal, 0);
			tvright.setTextColor(textRight_color);
			tvright.setTextSize(textRight_size);
			tvright.setText(textres);
			addView(tvright);
			if (isAutoAdd) {
				tvright.setText(textres);
			}
		}
		else if (imageres > 0) {
			ivright = new ImageView(mContext);
			ivright.setLayoutParams(getLayoutImage(Local_Right));
			ivright.setPadding(menuPaddinghorizontal, menuPaddingvertical, menuPaddinghorizontal, menuPaddingvertical);
			ivright.setScaleType(ScaleType.FIT_XY);
			addView(ivright);
			if (isAutoAdd) {
				ivright.setImageResource(imageres);
			}
		}
		if (null != getViewRight()) {
			getViewRight().setId(R.id.menutop_right);
			getViewRight().setOnClickListener(myOnClickListener);
		}

	}

	private void setCenterView(String textres, boolean isAutoAdd) {
		if (null == tvcenter) {
			tvcenter = new TextView(mContext);
			tvcenter.setLayoutParams(getLayoutText(Local_Center));
			// tvcenter.setGravity(Gravity.CENTER);
			// tvcenter.setPadding(menuPaddinghorizontal, 0, menuPaddinghorizontal, 0);
			tvcenter.setTextColor(textCenter_color);
			tvcenter.setTextSize(textCenter_size);
			tvcenter.setGravity(Gravity.CENTER);
			tvcenter.setSingleLine(true);
			tvcenter.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
			if (textCenter_isBlod) {
				tvcenter.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			}
			else {
				tvcenter.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
			}
			addView(tvcenter);
			if (null != getViewCenter()) {
				getViewCenter().setId(R.id.menutop_center);
				if (isCenterClick) {
					getViewCenter().setOnClickListener(myOnClickListener);
				}
				else {
					getViewCenter().setOnClickListener(null);
					getViewCenter().setClickable(false);
				}
			}
		}
		if (isAutoAdd) {
			tvcenter.setText(textres);
		}
	}

	private void setBottomView() {
		if (null != view_buttom) {
			removeView(view_buttom);
			view_buttom = null;
		}
		if (menuButtomLineSize > 0) {
			view_buttom = new View(mContext);
			view_buttom.setLayoutParams(getLayoutButtom());
			view_buttom.setBackgroundColor(menuButtomLineColor);
			addView(view_buttom);
		}
	}

	private final OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (null != listener) {
				listener.onMenuTopClick(v, v.getId());
			}

		}
	};

	public void setMenuClickListener(MenuTopListener menuTopListener) {
		this.listener = menuTopListener;
	}

	public View getViewLeft() {
		if (null != tvleft) {
			return tvleft;
		}
		else if (null != ivleft) {
			return ivleft;
		}
		else if (null != viewLeft) {
			return viewLeft;
		}
		else {
			return null;
		}
	}

	public View getViewRight() {
		if (null != tvright) {
			return tvright;
		}
		else if (null != ivright) {
			return ivright;
		}
		else if (null != viewRight) {
			return viewRight;
		}
		else {
			return null;
		}
	}

	public View getViewCenter() {
		return tvcenter;
	}

	public void setLeftImage(int imageres) {
		this.imageLeft_width = image_height;
		setLeftView(null, imageres, true);
	}

	public void setLeftImage(int imageres, float percentVale) {
		this.imageLeft_width = (int) (percentVale * image_height);
		setLeftView(null, imageres, true);
	}

	public void setLeftText(CharSequence charSequence) {
		if (TextUtils.isEmpty(charSequence)) {
			setLeftView(null, -1, true);
		}
		else {
			setLeftView(charSequence.toString(), -1, false);
			if (null != tvleft) {
				tvleft.setText(charSequence);
			}

		}
	}

	public void setLeftTextRes(int textResID) {
		try {
			String value = mContext.getResources().getString(textResID);
			setLeftText(value);

		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void setLeftView(int leftResId) {
		setLeftView(null, -1, false);
		if (null != viewLeft) {
			removeView(viewLeft);
			viewLeft = null;
		}
		if (leftResId > 0) {
			LayoutParams layoutParams = getLayoutView(Local_Left);
			viewLeft = LayoutInflater.from(mContext).inflate(leftResId, null);
			viewLeft.setLayoutParams(layoutParams);
			viewLeft.setPadding(menuPaddinghorizontal, 0, menuPaddinghorizontal, 0);
			addView(viewLeft);
		}
		if (null != getViewLeft()) {
			getViewLeft().setId(R.id.menutop_right);
			getViewLeft().setOnClickListener(myOnClickListener);
		}
	}

	public void setRightImage(int imageres) {
		this.imageRight_width = image_height;
		setRightView(null, imageres, true);
	}

	public void setRightImage(int imageres, float percentVale) {
		this.imageRight_width = (int) (percentVale * image_height);
		setLeftView(null, imageres, true);
	}

	public void setRightText(CharSequence charSequence) {
		if (TextUtils.isEmpty(charSequence)) {
			setRightView(null, -1, true);
		}
		else {
			setRightView(charSequence.toString(), -1, false);
			if (null != tvright) {
				tvright.setText(charSequence);
			}

		}
	}

	public void setRightTextRes(int textResID) {
		try {
			String value = mContext.getResources().getString(textResID);
			setRightText(value);

		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void setRightView(int rightResId) {
		setRightView(null, -1, false);
		if (null != viewRight) {
			removeView(viewRight);
			viewRight = null;
		}
		if (rightResId > 0) {
			LayoutParams layoutParams = getLayoutView(Local_Right);
			viewRight = LayoutInflater.from(mContext).inflate(rightResId, null);
			viewRight.setLayoutParams(layoutParams);
			viewRight.setPadding(menuPaddinghorizontal, 0, menuPaddinghorizontal, 0);
			addView(viewRight);
		}
		if (null != getViewRight()) {
			getViewRight().setId(R.id.menutop_right);
			getViewRight().setOnClickListener(myOnClickListener);
		}
	}

	// 设置中间的文本
	public void setCenterText(CharSequence charSequence) {
		setCenterView(null, false);
		tvcenter.setText(charSequence);
	}

	public void setCenterTextRes(int textResID) {
		try {
			String value = mContext.getResources().getString(textResID);
			setCenterText(value);

		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void setCenterViewCanClick(boolean isCenterClick) {
		this.isCenterClick = isCenterClick;
		if (null != getViewCenter()) {
			getViewCenter().setId(R.id.menutop_center);
			if (this.isCenterClick) {
				getViewCenter().setOnClickListener(myOnClickListener);
			}
			else {
				getViewCenter().setOnClickListener(null);
				getViewCenter().setClickable(false);
			}
		}
	}

	public void setBottomLineShowEnable(boolean isShowEnable) {
		if (isShowEnable) {
			if (null != view_buttom) {
				view_buttom.setVisibility(View.VISIBLE);
			}
		}
		else {
			if (null != view_buttom) {
				view_buttom.setVisibility(View.INVISIBLE);
			}
		}
	}

	public void setBottomLine(int lineSize, int lineColor) {
		this.menuButtomLineSize = lineSize;
		this.menuButtomLineColor = lineColor;
		setBottomView();
	}

	public void setMenuVisible(int visible_status) {
		if (visible_status == View.GONE || visible_status == View.INVISIBLE || visible_status == View.VISIBLE) {
			setMenuLeftVisible(visible_status);
			setMenuRightVisible(visible_status);
		}

	}

	public void setMenuLeftVisible(int visible_status) {
		if (visible_status == View.GONE || visible_status == View.INVISIBLE || visible_status == View.VISIBLE) {
			View viewtemp = getViewLeft();
			if (null != viewtemp) {
				viewtemp.setVisibility(visible_status);
				viewtemp = null;
			}
		}
	}

	public void setMenuRightVisible(int visible_status) {
		if (visible_status == View.GONE || visible_status == View.INVISIBLE || visible_status == View.VISIBLE) {
			View viewtemp = getViewRight();
			if (null != viewtemp) {
				viewtemp.setVisibility(visible_status);
				viewtemp = null;
			}
		}
	}

}
