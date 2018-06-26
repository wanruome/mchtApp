/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年9月9日 上午10:11:45 
 */
package com.ruomm.base.view.tagtextviews;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.view.tagtextviews.util.TagTextValue;
import com.ruomm.base.view.tagtextviews.util.TagTextViewDrawItem;
import com.ruomm.R;

public class TagTextView extends View {
	// ICON位置常量
	private static final int Value_AlginType_Left = 0;
	private static final int Value_AlginType_Right = 1;
	Paint textPaint;
	private static final int[] ATTRS = new int[] { android.R.attr.layout_height };
	private static final float TEXTHEIGHT_TO_ROWHEGHT = 1.3f;
	private List<TagTextValue> listTagTextVaules;

	private final ArrayList<TagTextViewDrawItem> listValues = new ArrayList<TagTextViewDrawItem>();

	private int textColor = 0xFF333333;
	private int textSize = 12;
	private int iconAlginType = Value_AlginType_Left;
	private int iconId = 0;
	private int iconWidth = 0;
	private int iconHeight = 0;
	private int textPadding = 0;
	private int iconPadding = 0;
	private float lineSpacing = 1.0f;
	private int lineHeight = 15;
	private int layoutParamsHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

	public TagTextView(Context context) {
		super(context);
		initPublic(context, null);
	}

	public TagTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPublic(context, attrs);
	}

	public TagTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initPublic(context, attrs);
	}

	private void initPublic(Context mContext, AttributeSet attrs) {
		if (null == attrs) {

			textSize = DisplayUtil.dipTopx(mContext, 12);
			textPadding = DisplayUtil.dipTopx(mContext, 4);
			iconWidth = DisplayUtil.dipTopx(mContext, 10);
			iconHeight = DisplayUtil.dipTopx(mContext, 10);
			iconPadding = DisplayUtil.dipTopx(mContext, 2);
			this.layoutParamsHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
		}
		else {
			textSize = DisplayUtil.dipTopx(mContext, 12);
			TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.TagTextView);
			iconId = a.getResourceId(R.styleable.TagTextView_tagtv_icon, iconId);
			textSize = a.getDimensionPixelSize(R.styleable.TagTextView_tagtv_textSize, textSize);
			textColor = a.getColor(R.styleable.TagTextView_tagtv_textColor, textColor);
			lineSpacing = a.getFloat(R.styleable.TagTextView_tagtv_lineSpacing, lineSpacing);
			iconWidth = a.getDimensionPixelSize(R.styleable.TagTextView_tagtv_iconWidth, textSize);
			iconHeight = a.getDimensionPixelSize(R.styleable.TagTextView_tagtv_iconHeight, textSize);
			textPadding = a.getDimensionPixelSize(R.styleable.TagTextView_tagtv_textPadding, textPadding);
			iconPadding = a.getDimensionPixelSize(R.styleable.TagTextView_tagtv_iconPadding, iconPadding);
			iconAlginType = a.getInteger(R.styleable.TagTextView_tagtv_iconAlginType, iconAlginType);
			a.recycle();
			a = mContext.obtainStyledAttributes(attrs, ATTRS);
			layoutParamsHeight = a.getLayoutDimension(0, layoutParamsHeight);
			a.recycle();

		}

		lineHeight = (int) (textSize * lineSpacing * TEXTHEIGHT_TO_ROWHEGHT);
		textPaint = new Paint();
		textPaint.setTextSize(textSize);
		textPaint.setAntiAlias(true);
		textPaint.setColor(textColor);
	}

	public List<TagTextValue> getTextImageValues() {
		return listTagTextVaules;
	}

	public void setTagTextDatas(List<TagTextValue> listTagTextVaules) {

		this.listTagTextVaules = listTagTextVaules;
		this.listValues.clear();
		if (null != this.listTagTextVaules) {
			for (TagTextValue itemValue : listTagTextVaules) {
				if (itemValue.imageValue <= 0) {
					itemValue.imageValue = iconId;
				}
				if (itemValue.imageValue <= 0) {
					itemValue.imageValue = 0;
					itemValue.imageWidth = 0;
				}
				else {
					if (itemValue.imageWidth <= 0) {
						itemValue.imageWidth = iconWidth;
					}
				}
			}
		}
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (layoutParamsHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
			int height = lineHeight;
			int indexEnd = listValues.size() - 1;
			if (indexEnd >= 0) {
				height = listValues.get(indexEnd).drawY + lineHeight / 2;
			}
			setMeasuredDimension(widthMeasureSpec, height);
		}
		else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (layoutParamsHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
			int width = canvas.getWidth();

			if (width <= 0) {
				super.onDraw(canvas);
				return;
			}
			else {
				if (listValues.size() <= 0) {
					calListValues(canvas.getWidth());
				}
				this.requestLayout();

				super.onDraw(canvas);
				int indexEnd = listValues.size() - 1;
				int height = lineHeight;
				if (indexEnd >= 0) {
					height = listValues.get(indexEnd).drawY + lineHeight / 2;
				}
				if (this.getHeight() == height) {
					for (TagTextViewDrawItem itemValue : listValues) {
						drawItem(canvas, itemValue);
					}
				}
			}
		}
		else {
			super.onDraw(canvas);
			if (listValues.size() <= 0) {
				calListValues(canvas.getWidth());
			}
			for (TagTextViewDrawItem itemValue : listValues) {
				drawItem(canvas, itemValue);
			}
		}

	}

	protected void calListValues(int viewWidth) {

		int drawX = 0;
		int drawY = lineHeight / 2;
		listValues.clear();
		if (null == listTagTextVaules || listTagTextVaules.size() <= 0) {
			return;
		}
		for (TagTextValue itemValue : listTagTextVaules) {
			int widthItemNoPadding = getItemWidthWithOutPadding(itemValue);
			if (drawX > 0) {
				if (drawX + widthItemNoPadding > viewWidth) {
					drawX = 0;
					drawY = drawY + lineHeight;
				}
				else {
					TagTextViewDrawItem valueItem = new TagTextViewDrawItem();
					valueItem.drawX = drawX;
					valueItem.drawY = drawY;
					valueItem.textValue = itemValue.textValue;
					valueItem.imageWidth = itemValue.imageWidth;
					valueItem.imageValue = itemValue.imageValue;
					valueItem.drawWidth = widthItemNoPadding;
					listValues.add(valueItem);
					drawX = drawX + widthItemNoPadding + textPadding;
				}
			}
			if (drawX == 0) {
				TagTextViewDrawItem valueItem = new TagTextViewDrawItem();
				valueItem.drawX = drawX;
				valueItem.drawY = drawY;
				valueItem.textValue = itemValue.textValue;
				valueItem.imageWidth = itemValue.imageWidth;
				valueItem.imageValue = itemValue.imageValue;
				valueItem.drawWidth = widthItemNoPadding;
				listValues.add(valueItem);
				drawX = drawX + widthItemNoPadding + textPadding;
			}
		}
	}

	protected void drawItem(Canvas canvas, TagTextViewDrawItem itemValue) {
		if (null == itemValue) {
			return;
		}
		if (iconAlginType == Value_AlginType_Left) {
			if (itemValue.imageValue > 0) {
				Drawable imageDrawable = getResources().getDrawable(itemValue.imageValue);
				Rect rect = new Rect();
				rect.set(itemValue.drawX, itemValue.drawY - iconHeight / 2, itemValue.drawX + itemValue.imageWidth,
						itemValue.drawY - iconHeight / 2 + iconHeight);
				imageDrawable.setBounds(rect);
				imageDrawable.draw(canvas);
			}
			if (!TextUtils.isEmpty(itemValue.textValue)) {
				int textDrawX = itemValue.drawX;
				int textDrawY = (int) (itemValue.drawY + textSize * 0.4);
				if (itemValue.imageValue <= 0) {
					textDrawX = itemValue.drawX;
				}
				else {
					textDrawX = itemValue.drawX + itemValue.imageWidth + iconPadding;
				}
				canvas.drawText(itemValue.textValue, textDrawX, textDrawY, textPaint);
			}
		}
		else {

			if (!TextUtils.isEmpty(itemValue.textValue)) {
				// iconDrawX=
				int textDrawX = itemValue.drawX;
				int textDrawY = (int) (itemValue.drawY + textSize * 0.4);
				canvas.drawText(itemValue.textValue, textDrawX, textDrawY, textPaint);
			}
			if (itemValue.imageValue > 0) {
				int iconDrawX = itemValue.drawX;
				if (TextUtils.isEmpty(itemValue.textValue)) {
					iconDrawX = itemValue.drawX;
				}
				else {
					iconDrawX = itemValue.drawX + itemValue.drawWidth - itemValue.imageWidth;
				}
				Drawable imageDrawable = getResources().getDrawable(itemValue.imageValue);
				Rect rect = new Rect();
				rect.set(iconDrawX, itemValue.drawY - iconHeight / 2, iconDrawX + itemValue.imageWidth, itemValue.drawY
						- iconHeight / 2 + iconHeight);
				imageDrawable.setBounds(rect);
				imageDrawable.draw(canvas);

			}
		}

	}

	protected int getItemWidthWithOutPadding(TagTextValue value) {

		if (null == value) {
			return 0;
		}

		if (value.imageValue > 0) {
			if (TextUtils.isEmpty(value.textValue)) {
				return value.imageWidth;
			}
			else {
				return value.imageWidth + iconPadding + (int) (textPaint.measureText(value.textValue) + 0.5f);
			}
		}
		else {
			if (TextUtils.isEmpty(value.textValue)) {
				return 0;
			}
			else {
				return (int) (textPaint.measureText(value.textValue) + 0.5f);
			}
		}

	}

	protected int getItemWidthWith(TagTextValue value) {

		return getItemWidthWithOutPadding(value) + textPadding;

	}
}
