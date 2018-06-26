package com.ruomm.base.view.siderbar;

/**
 *	@copyright 长本网络-2015
 * 	@author wanruome
 * 	@create 2015年3月13日 下午3:30:31
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ruomm.R;
import com.ruomm.base.tools.DisplayUtil;

public class SideBar extends View {
	private static final float PaintTextOffset = 0.45f;
	private static final int[] ATTRS = new int[] { android.R.attr.padding, android.R.attr.paddingTop,
			android.R.attr.paddingBottom };
	private List<String> mListItems;
	private OnTouchingLetterChangedListener mOnTouchingLetterChangedListener;

	private int textMaxsize = 32;
	private float textpercent = 0.85f;
	private float textpercent_selected = 0.9f;
	private int textcolor = 0xFF336598;
	private int textcolor_selected = 0xFF3399ff;
	private int padding = 0;
	private int paddingtop = 0;
	private int paddingbottom = 0;
	private int mCurrentSectionPosition = -1;
	private Paint mIndexPaint;
	private boolean isBold = true;
	private boolean mIsIndexing;
	private TextView mTextDialog;

	public SideBar(Context context) {
		this(context, null);
	}

	public SideBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
		padding = a.getDimensionPixelSize(0, padding);
		paddingtop = a.getDimensionPixelSize(1, paddingtop);
		paddingbottom = a.getDimensionPixelSize(2, paddingbottom);
		a.recycle();
		a = context.obtainStyledAttributes(attrs, R.styleable.SideBar);
		textcolor = a.getColor(R.styleable.SideBar_sidebar_textColor, textcolor);
		textcolor_selected = a.getColor(R.styleable.SideBar_sidebar_textColor_selected, textcolor_selected);
		textpercent = a.getFloat(R.styleable.SideBar_sidebar_textSizepercent, textpercent);
		textpercent_selected = a.getFloat(R.styleable.SideBar_sidebar_textSizepercent_selected, textpercent_selected);
		isBold = a.getBoolean(R.styleable.SideBar_sidebar_textIsbold, isBold);
		int tempSize = a.getDimensionPixelSize(R.styleable.SideBar_sidebar_textMaxSize, 0);
		if (tempSize > 0) {
			textMaxsize = DisplayUtil.pxTodip(context, tempSize);
		}

		a.recycle();
		paddingtop = paddingtop == 0 ? padding : paddingtop;
		paddingbottom = paddingbottom == 0 ? padding : paddingbottom;
		setData(new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
				"R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#" });

	}

	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.mOnTouchingLetterChangedListener = onTouchingLetterChangedListener;

	}

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
		if (null != this.mTextDialog) {
			mTextDialog.setText(null);
			mTextDialog.setVisibility(View.GONE);
		}
	}

	public void setData(String[] arrayValues) {
		this.mListItems = new ArrayList<String>();

		if (null != arrayValues && arrayValues.length > 0) {
			for (String value : arrayValues) {
				this.mListItems.add(value);
			}
		}
		if (null == mIndexPaint) {
			mIndexPaint = new Paint();
			if (isBold) {
				mIndexPaint.setTypeface(Typeface.DEFAULT_BOLD);
			}
			else {
				mIndexPaint.setTypeface(Typeface.DEFAULT);
			}
		}
		invalidate();
	}

	public void setData(List<String> listValue) {
		this.mListItems = listValue;
		if (null == mIndexPaint) {
			mIndexPaint = new Paint();
			if (isBold) {
				mIndexPaint.setTypeface(Typeface.DEFAULT_BOLD);
			}
			else {
				mIndexPaint.setTypeface(Typeface.DEFAULT);
			}
		}

		invalidate();
	}

	public void setCurrentItem(int index) {
		this.mCurrentSectionPosition = index;
		if (mCurrentSectionPosition < 0) {
			mCurrentSectionPosition = -1;
		}
		if (mCurrentSectionPosition >= mListItems.size()) {
			mCurrentSectionPosition = -1;
		}
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mListItems != null && mListItems.size() > 1) {
			float sectionHeight = (getMeasuredHeight() - paddingbottom - paddingtop) / mListItems.size();
			float textsize = sectionHeight * textpercent;
			float textsize_selected = sectionHeight * textpercent_selected;
			if (textsize_selected > textMaxsize) {
				textsize_selected = textMaxsize;
				textsize = textsize_selected * textpercent / textpercent_selected;
			}
			if (mIsIndexing) {

				for (int i = 0; i < mListItems.size(); i++) {
					if (i != mCurrentSectionPosition) {
						mIndexPaint.setTextSize(textsize);
						mIndexPaint.setColor(textcolor);

						float drawx = (getMeasuredWidth() - mIndexPaint.measureText(getSectionText(i))) / 2;
						float drawy = paddingtop + sectionHeight * i + sectionHeight / 2 + textsize * PaintTextOffset;
						canvas.drawText(getSectionText(i), drawx, drawy, mIndexPaint);
					}
					else {

						mIndexPaint.setTextSize(textsize_selected);
						mIndexPaint.setColor(textcolor_selected);
						float drawx = (getMeasuredWidth() - mIndexPaint.measureText(getSectionText(i))) / 2;
						float drawy = paddingtop + sectionHeight * i + sectionHeight / 2 + textsize_selected
								* PaintTextOffset;
						canvas.drawText(getSectionText(i), drawx, drawy, mIndexPaint);
					}

				}
			}
			else {
				for (int i = 0; i < mListItems.size(); i++) {
					mIndexPaint.setTextSize(textsize);
					mIndexPaint.setColor(textcolor);
					float drawx = (getMeasuredWidth() - mIndexPaint.measureText(getSectionText(i))) / 2;
					float drawy = paddingtop + sectionHeight * i + sectionHeight / 2 + textsize * PaintTextOffset;
					canvas.drawText(getSectionText(i), drawx, drawy, mIndexPaint);

				}
			}
		}

	}

	private String getSectionText(int sectionPosition) {
		if (null == mListItems || mListItems.size() <= 0) {
			return null;
		}

		if (sectionPosition < 0 || sectionPosition >= mListItems.size()) {
			return null;
		}
		return mListItems.get(sectionPosition);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (null == mListItems || mListItems.size() < 2) {
			return true;
		}
		switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
				mIsIndexing = false;
				this.setSelected(false);
				onTouchValueChange(ev.getY());
				break;
			case MotionEvent.ACTION_CANCEL:
				mIsIndexing = false;
				this.setSelected(false);
				onTouchValueChange(ev.getY());
				break;
			default:
				mIsIndexing = true;
				this.setSelected(true);
				onTouchValueChange(ev.getY());
				break;
		}
		return true;

	}

	// @SuppressLint("ClickableViewAccessibility")
	// @Override
	// public boolean onTouchEvent(MotionEvent ev) {
	// if (null == mListItems || mListItems.size() < 2) {
	// return false;
	// }
	// switch (ev.getAction()) {
	//
	// case MotionEvent.ACTION_DOWN:
	// mCurrentSectionPosition = -1;
	// mIsIndexing = true;
	// onTouchValueChange(ev.getY());
	// break;
	//
	// case MotionEvent.ACTION_MOVE:
	// onTouchValueChange(ev.getY());
	// break;
	// case MotionEvent.ACTION_UP:
	// mIsIndexing = false;
	// onTouchValueChange(ev.getY());
	// break;
	// case MotionEvent.ACTION_CANCEL:
	// mIsIndexing = false;
	// onTouchValueChange(ev.getY());
	// break;
	// }
	// return false;
	// }

	private int getIndexByEvent(float y) {
		float ycurrent = y - paddingtop;
		float sectionHeight = (getMeasuredHeight() - paddingbottom - paddingtop) / mListItems.size();
		int index = (int) (ycurrent / sectionHeight);
		if (index < 0) {
			return 0;
		}
		else if (index >= mListItems.size()) {
			return mListItems.size() - 1;
		}
		else {
			return index;
		}
	}

	private void onTouchValueChange(float sideIndexY) {
		int index = getIndexByEvent(sideIndexY);

		if (index != mCurrentSectionPosition) {
			mCurrentSectionPosition = index;
			invalidate();
			String textValue = getSectionText(index);
			if (null != mTextDialog) {
				mTextDialog.setText(textValue);
				mTextDialog.setVisibility(View.VISIBLE);
			}
			if (null != mOnTouchingLetterChangedListener) {
				mOnTouchingLetterChangedListener.onTouchingLetterChanged(mCurrentSectionPosition, textValue);
			}

		}
		if (!mIsIndexing) {
			invalidate();
			if (null != mTextDialog) {
				mTextDialog.setText(null);
				mTextDialog.setVisibility(View.GONE);
			}
			mCurrentSectionPosition = -1;
		}

	}
}
