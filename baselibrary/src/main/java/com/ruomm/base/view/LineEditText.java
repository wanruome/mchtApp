/**
 *	@copyright 亿康通 -2015 
 * 	@author liufangcai  
 * 	@create 2015年12月25日 下午5:21:21 
 */
package com.ruomm.base.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.ruomm.R;

public class LineEditText extends EditText {
	private final Paint mPaint;
	private int mPaitnColor;

	public LineEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mPaint = new Paint();

		mPaint.setStyle(Paint.Style.STROKE);
		mPaitnColor = getResources().getColor(R.color.lineedittext_textcolor);
		mPaint.setColor(mPaitnColor);
	}

	public void setLineEditTextPaintColor(int mPaitnColor) {
		this.mPaitnColor = mPaitnColor;
		mPaint.setColor(this.mPaitnColor);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawLine(0, this.getHeight() - 2, this.getWidth() - 1, this.getHeight() - 2, mPaint);
	}

}
