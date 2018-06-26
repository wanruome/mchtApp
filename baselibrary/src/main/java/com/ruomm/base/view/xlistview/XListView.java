/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * 		Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.ruomm.base.view.xlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.ruomm.R;

public class XListView extends ListView implements OnScrollListener {

	private float mLastY = -1; // save event y
	private float mDownY = 0;
	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.
	private IXListViewListener mListViewListener;

	// -- header view
	private XListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	// private TextView mHeaderTimeView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	private XListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;

	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
														// at bottom, trigger
														// load more.
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull // feature.
	private boolean isLoadingMore = true;
	private boolean isRefreshing = true;
	private boolean isShowComplete = true;
	private boolean isShowLoadingTime = true;
	private boolean isShowRefreshTime = true;
	private long refreshTime = 0;
	private long loadingTime = 0;
	private boolean isUpTimeUIOk = true;

	/**
	 * @param context
	 */
	public XListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
		addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new XListViewFooter(context);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mHeaderViewHeight = mHeaderViewContent.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
		setRefreshTime();
		setLoadingTime();
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// make sure XListViewFooter is the last footer view, and only add once.
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	/**
	 * enable or disable pull down refresh feature.
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		this.isRefreshing = enable;
		// setPullLoadEnableInit(enable);
		this.mEnablePullRefresh = enable;
		if (!this.mEnablePullRefresh) { // disable, hide the content
			this.mHeaderViewContent.setVisibility(View.INVISIBLE);
		}
		else {
			this.mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	private void setPullRefreshEnableInit(boolean enable) {
		if (this.mEnablePullRefresh == enable) {
			return;
		}
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		}
		else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * enable or disable pull up load more feature.
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		this.isLoadingMore = enable;
		// setPullLoadEnableInit(enable);
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
			// make sure "pull up" don't show a line in bottom when listview with one page
			setFooterDividersEnabled(false);
		}
		else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setViewByState(XListViewFooter.STATE_NORMAL);
			// mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// make sure "pull up" don't show a line in bottom when listview with one page
			setFooterDividersEnabled(true);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(footerOnClickListener);
		}

	}

	private void setPullLoadEnableInit(boolean enable) {
		if (this.mEnablePullLoad == enable) {
			return;
		}
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
			// make sure "pull up" don't show a line in bottom when listview with one page
			setFooterDividersEnabled(false);
		}
		else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// make sure "pull up" don't show a line in bottom when listview with one page
			setFooterDividersEnabled(true);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(footerOnClickListener);
		}
	}

	private final OnClickListener footerOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == mFooterView) {
				startLoadMore();
			}

		}
	};

	/**
	 * stop refresh, reset header view.
	 */
	private void stopRefresh() {
		if (mPullRefreshing == true) {
			setRefreshTime(System.currentTimeMillis());
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	private void stopLoadMore() {
		if (mPullLoading == true) {
			setLoadingTime(System.currentTimeMillis());
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	public void setfootTextView(String tv) {
		mFooterView.setHintViewText(tv);
	}

	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
		setRefreshTime();
	}

	public void setLoadingTime(long loadingTime) {
		this.loadingTime = loadingTime;
		setLoadingTime();
	}

	public void setRefreshTime() {

		if (isShowRefreshTime) {
			setRefreshTimeByString(XListUtil.getRefreshTime(this.refreshTime));
		}
		else {
			setRefreshTimeByString(null);
		}
	}

	public void setLoadingTime() {
		if (isShowLoadingTime) {
			setLoadingTimeByString(XListUtil.getLoadingTime(this.loadingTime));
		}
		else {
			setLoadingTimeByString(null);
		}
	}

	// public void setLastUpdateLabel(long updateTime) {
	// setRefreshTime(XListUtil.getRefreshTime(updateTime));
	// }

	/**
	 * set last refresh time
	 * 
	 * @param time
	 */
	public void setRefreshTimeByString(String lable) {
		if (null != mHeaderView) {
			mHeaderView.setRefreshTime(lable);
		}

	}

	public void setLoadingTimeByString(String lable) {
		if (null != mFooterView) {
			mFooterView.setRefreshTime(lable);
		}
	}

	public void setShowLoadingTime(boolean isShowLoadingTime) {
		this.isShowLoadingTime = isShowLoadingTime;

	}

	public void setShowRefreshTime(boolean isShowRefreshTime) {
		this.isShowRefreshTime = isShowRefreshTime;
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			}
			else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		if (!isUpTimeUIOk) {
			isUpTimeUIOk = true;
			setRefreshTime();
		}
		setSelection(0); // scroll to top each time
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) {
			return;
		}
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
		isUpTimeUIOk = true;
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				mFooterView.setState(XListViewFooter.STATE_READY);
			}
			else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);
		if (!isUpTimeUIOk) {
			isUpTimeUIOk = true;
			setLoadingTime();
		}

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
			invalidate();
		}
		isUpTimeUIOk = true;
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			setPullRefreshEnableInit(false);
			mListViewListener.onLoadMore();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isUpTimeUIOk = false;
				mLastY = ev.getRawY();
				mDownY = ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:

				final float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();
				if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
					// the first item is showing, header has shown or pull down.
					updateHeaderHeight(deltaY / OFFSET_RADIO);
					invokeOnScrolling();
				}
				else if (getLastVisiblePosition() == mTotalItemCount - 1
						&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
					// last item, already pulled up or want to pull up.
					updateFooterHeight(-deltaY / OFFSET_RADIO);
				}
				break;
			default:
				mLastY = -1; // reset

				if (getFirstVisiblePosition() == 0) {
					// invoke refresh
					if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
						mPullRefreshing = true;
						mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
						if (mListViewListener != null) {
							// 自定义代码
							setPullLoadEnableInit(false);
							mListViewListener.onRefresh();
						}
					}
					else if (mHeaderView.getVisiableHeight() == 0) {
						if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA && !mPullLoading) {
							startLoadMore();
						}
						resetFooterHeight();
					}
					resetHeaderHeight();

				}
				else if (getLastVisiblePosition() == mTotalItemCount - 1) {
					// invoke load more.
					if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA && !mPullLoading) {
						startLoadMore();
					}
					resetFooterHeight();
				}
				break;
		}

		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			}
			else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke onXScrolling when
	 * header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();
	}

	public void setDataLoadComplete() {
		mFooterView.loadDataComplete();
	}

	public void setDataLoadUnComplete() {
		mFooterView.loadDataUnComplete();
	}

	public void setFooterTip(String text) {
		if (null != mFooterView) {
			mFooterView.setLoadMoreTip(text);
		}
	}

	public void hideFooterCompleteView() {
		removeFooterView(mFooterView);
	}

	// 自定义方法
	public boolean isLoadingMore() {
		return isLoadingMore;
	}

	public boolean isRefreshing() {
		return isRefreshing;
	}

	public boolean isShowComplete() {
		return isShowComplete;
	}

	public void setShowComplete(boolean isShowComplete) {
		this.isShowComplete = isShowComplete;
	}

	public void setShowComplete(boolean isShowComplete, String textShow) {
		this.isShowComplete = isShowComplete;
		setCompleteTip(textShow);
	}

	private void setCompleteTip(String text) {
		if (null != mFooterView) {
			mFooterView.setCompleteTip(text);
		}
	}

	public void setCanLoading(boolean isLoadingMore) {
		this.isLoadingMore = isLoadingMore;
	}

	public void setCanRefreshing(boolean isRefreshing) {
		this.isRefreshing = isRefreshing;
	}

	public void stopRefreshAndLoading() {
		stopRefresh();
		stopLoadMore();
		setPullLoadEnableInit(isLoadingMore);
		setPullRefreshEnableInit(isRefreshing);
		if (isShowComplete) {
			if (!isLoadingMore) {

				setDataLoadComplete();
			}
			else {
				mFooterView.loadDataUnComplete();
			}
		}
		else {
			mFooterView.loadDataUnComplete();
		}
	}
}
