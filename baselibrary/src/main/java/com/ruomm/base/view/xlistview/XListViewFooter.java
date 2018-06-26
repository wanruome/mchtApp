/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.ruomm.base.view.xlistview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruomm.R;

public class XListViewFooter extends LinearLayout {
	private final int ROTATE_ANIM_DURATION = 180;
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	private int mState = STATE_NORMAL;

	private Context mContext;

	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;
	private TextView mComplete;
	private ImageView mArrowImageView;
	private TextView mRefreshTime;

	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;
	private String normalTip;

	private String mRefreshLable;

	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);

	}

	public void setViewByState(int state) {
		if (state == STATE_LOADING) { // 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
		}
		else { // 显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}

		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
		mHintView.setVisibility(View.GONE);
		mRefreshTime.setVisibility(View.GONE);
		mArrowImageView.setVisibility(View.GONE);

		switch (state) {
			case STATE_NORMAL:
				if (mState == STATE_READY) {
					mArrowImageView.startAnimation(mRotateDownAnim);
				}
				if (mState == STATE_LOADING) {
					mArrowImageView.clearAnimation();
				}
				mHintView.setText(normalTip);
				mHintView.setVisibility(View.VISIBLE);
				break;
			case STATE_READY:
				if (mState != STATE_READY) {
					mArrowImageView.clearAnimation();
					mArrowImageView.startAnimation(mRotateUpAnim);
					mHintView.setText(R.string.xlistview_footer_hint_ready);
				}
				mHintView.setVisibility(View.VISIBLE);
				mArrowImageView.setVisibility(View.VISIBLE);
				if (null != mRefreshLable) {
					mRefreshTime.setVisibility(View.VISIBLE);
				}
				break;
			case STATE_LOADING:
				mHintView.setText(R.string.xlistview_header_hint_loading);
				mHintView.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.VISIBLE);
				if (null != mRefreshLable) {
					mRefreshTime.setVisibility(View.VISIBLE);
				}
				break;
			default:
		}

	}

	public void setState(int state) {
		if (state == mState) {
			return;
		}
		setViewByState(state);
		mState = state;

	}

	public void setBottomMargin(int height) {
		if (height < 0) {
			return;
		}
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	public void setHintViewText(String tv) {
		mHintView.setText(tv);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		mRefreshTime = (TextView) findViewById(R.id.xlistview_footer_time);
		mArrowImageView = (ImageView) findViewById(R.id.xlistview_footer_arrow);
		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
		mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
		mComplete = (TextView) moreView.findViewById(R.id.completeTV);
		mComplete.setVisibility(View.GONE);
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
		setViewByState(STATE_NORMAL);
		normalTip = context.getResources().getString(R.string.xlistview_footer_hint_normal);

	}

	public void loadDataComplete() {
		mComplete.setVisibility(View.VISIBLE);
		mContentView.setVisibility(View.GONE);

		// mComplete.setVisibility(View.VISIBLE);
		// LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)
		// mContentView.getLayoutParams();
		// lp.height = LayoutParams.WRAP_CONTENT;
		// mContentView.setLayoutParams(lp);
	}

	public void setLoadMoreTip(String text) {
		String temp = normalTip;
		if (TextUtils.isEmpty(text)) {
			normalTip = getResources().getString(R.string.xlistview_footer_hint_normal);
		}
		else {
			normalTip = text;
		}
		if (temp.equals(mHintView.getText().toString())) {
			mHintView.setText(normalTip);
		}

	}

	public void setCompleteTip(String text) {

		if (TextUtils.isEmpty(text)) {
			mComplete.setText(getResources().getString(R.string.xlistview_footer_hint_nomoredata));
		}
		else {
			mComplete.setText(text);
		}

	}

	public void loadDataUnComplete() {
		mComplete.setVisibility(View.GONE);
		mContentView.setVisibility(View.VISIBLE);

		// LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)
		// mContentView.getLayoutParams();
		// lp.height = LayoutParams.WRAP_CONTENT;
		// mContentView.setLayoutParams(lp);
	}

	public void setRefreshTime(String lable) {
		if (null == mRefreshTime) {
			return;
		}

		if (TextUtils.isEmpty(lable)) {
			mRefreshLable = null;
			mRefreshTime.setText(null);
			mRefreshTime.setVisibility(View.GONE);
		}
		else {
			this.mRefreshLable = lable;
			mRefreshTime.setText(this.mRefreshLable);
		}
	}
}
