package com.ruomm.base.view.dragsortgridview;

import com.ruomm.base.view.dragsortgridview.ReorderArray.onMoveListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

public class DragSortGridView extends GridView implements onMoveListener {

	private static final boolean DEBUG = false;
	private static final String TAG = "DragSortGridView";
	private static SparseArray<String> DRAG_EVENT_ACTION;

	static {
		if (DEBUG) {
			DRAG_EVENT_ACTION = new SparseArray<String>();
			DRAG_EVENT_ACTION.put(DragEvent.ACTION_DRAG_STARTED, "ACTION_DRAG_STARTED");
			DRAG_EVENT_ACTION.put(DragEvent.ACTION_DRAG_ENTERED, "ACTION_DRAG_ENTERED");
			DRAG_EVENT_ACTION.put(DragEvent.ACTION_DRAG_LOCATION, "ACTION_DRAG_LOCATION");
			DRAG_EVENT_ACTION.put(DragEvent.ACTION_DRAG_EXITED, "ACTION_DRAG_EXITED");
			DRAG_EVENT_ACTION.put(DragEvent.ACTION_DRAG_ENDED, "ACTION_DRAG_ENDED");
			DRAG_EVENT_ACTION.put(DragEvent.ACTION_DROP, "ACTION_DROP");
		}
	}

	private static final int FLAG_DRAW_APPEARING = 0x1;
	private static final int FLAG_DRAW_DISAPPEARING = 0x2;

	private boolean mDragSortEnabled;

	private int mLastDragPosition;
	private int mLastTargetPosition;
	private int mAppearingPosition;
	private int mDisappearingPosition;
	private int mMovingChildViewsNum;
	private int mPrivateFlags;

	private Drawable mAppearingDrawable;
	private Drawable mDisappearingDrawable;
	private Animation mFadeOutAnimation;
	private ReorderArray mReorderArray;
	private final Handler mHandler = new Handler();
	private OnReorderContentListener mOnReorderListener;

	public DragSortGridView(Context context) {
		super(context);
		init(context);
	}

	public DragSortGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DragSortGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	@SuppressLint("NewApi")
	private final void init(Context context) {

		mDragSortEnabled = true;

		mFadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
		mFadeOutAnimation.setDuration(150);
		mFadeOutAnimation.setFillEnabled(true);
		mFadeOutAnimation.setFillAfter(true);
		mFadeOutAnimation.setInterpolator(new AccelerateInterpolator());

		setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				ListAdapter adapter = getAdapter();
				int dragStartPos = 0;
				int dragEndPos = adapter.getCount() - 1;
				if (adapter instanceof DragSortAdapter) {
					DragSortAdapter dragSortAdapter = (DragSortAdapter) adapter;
					dragStartPos = dragSortAdapter.getDragStartPositon();
					dragEndPos = dragSortAdapter.getDragEndPostion();
					if (dragStartPos < 0) {
						dragStartPos = 0;
					}
					if (dragEndPos > adapter.getCount() - 1) {
						dragEndPos = adapter.getCount() - 1;
					}
				}

				if (position < dragStartPos || position > dragEndPos) {
					return false;
				}
				if (mDragSortEnabled) {
					view.startDrag(null, new DragShadowBuilder(view), position, 0);
					return true;
				}
				return false;
			}

		});

		setOnDragListener(new OnDragListener() {

			@Override
			public boolean onDrag(View v, DragEvent event) {

				final int x = Math.round(event.getX());
				final int y = Math.round(event.getY());

				final int action = event.getAction();

				if (DEBUG) {
					Log.d("adapter", "event action:" + DRAG_EVENT_ACTION.get(action) + "---------x:" + x + "y:" + y);
				}

				ListAdapter adapter = getAdapter();

				switch (action) {
					case DragEvent.ACTION_DRAG_STARTED:

						if (adapter instanceof DragSortAdapter) {
							DragSortAdapter dragSortAdapter = (DragSortAdapter) adapter;
							mReorderArray = dragSortAdapter.getReorderArray();
						}

						if (mReorderArray == null) {
							mReorderArray = new ReorderArray(adapter.getCount());
						}

						mReorderArray.setOnMoveListener(DragSortGridView.this);

						int actualPos = (Integer) event.getLocalState();
						View dragView = getView(actualPos);
						dragView.startAnimation(mFadeOutAnimation);

						mPrivateFlags |= FLAG_DRAW_DISAPPEARING;

						mLastDragPosition = actualPos;
						mDisappearingPosition = actualPos;
						mLastTargetPosition = -1;

						break;
					case DragEvent.ACTION_DRAG_ENTERED:
						break;
					case DragEvent.ACTION_DRAG_LOCATION:

						if (mMovingChildViewsNum > 0) {
							break;
						}

						// �õ�����ԭʼλ��
						int originalPos = getOriginalPos(x, y);
						// Ŀ��ʵ��λ��
						int targetPos = originalPos == -1 ? -1 : mReorderArray.indexOf(originalPos);
						int dragStartPos = 0;
						int dragEndPos = adapter.getCount() - 1;
						if (adapter instanceof DragSortAdapter) {
							DragSortAdapter dragSortAdapter = (DragSortAdapter) adapter;
							dragStartPos = dragSortAdapter.getDragStartPositon();
							dragEndPos = dragSortAdapter.getDragEndPostion();
							if (dragStartPos < 0) {
								dragStartPos = 0;
							}
							if (dragEndPos > adapter.getCount() - 1) {
								dragEndPos = adapter.getCount() - 1;
							}
						}
						if (targetPos < dragStartPos) {
							targetPos = dragStartPos;
						}
						else if (targetPos > dragEndPos) {
							targetPos = dragEndPos;
						}

						if (-1 != targetPos && targetPos != mLastTargetPosition) {

							if (DEBUG) {
								Log.d(TAG, "targetPos:" + targetPos);
							}

							// ���Ŀ��λ�ú��϶�λ����ͬ������Ŀ����ʾAppearing
							if (targetPos != mLastDragPosition) {
								mPrivateFlags &= ~FLAG_DRAW_DISAPPEARING;
								mPrivateFlags |= FLAG_DRAW_APPEARING;
								mAppearingPosition = targetPos;
							}

							mLastTargetPosition = targetPos;

							int dragPositon = mLastDragPosition;

							reOrderViews(dragPositon, targetPos);

							mLastDragPosition = targetPos;

						}
						// MLog.i(originalPos + ":" + targetPos);

						break;
					case DragEvent.ACTION_DRAG_EXITED:
						break;
					case DragEvent.ACTION_DRAG_ENDED:

						int fromPos = mReorderArray.get(mLastTargetPosition);
						int toPos = mLastTargetPosition;

						int dragViewPos = toPos == -1 ? mLastDragPosition : toPos;

						View lastDragView = getView(dragViewPos);
						lastDragView.clearAnimation();

						NotifyExecutor executor = new NotifyExecutor(fromPos, toPos);
						mHandler.post(executor);

						mLastDragPosition = -1;
						mLastTargetPosition = -1;
						break;
					case DragEvent.ACTION_DROP:

						break;
				}

				return true;
			}
		});

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Log.d(TAG, "onDraw");

		if ((mPrivateFlags & FLAG_DRAW_APPEARING) != 0) {

			Drawable drawable = mAppearingDrawable;
			int position = mAppearingPosition;
			if (position != -1 && drawable != null) {

				drawIndicator(canvas, position, drawable);
			}
		}
		else if ((mPrivateFlags & FLAG_DRAW_DISAPPEARING) != 0) {
			Drawable drawable = mDisappearingDrawable;
			int position = mDisappearingPosition;
			if (position != -1 && drawable != null) {

				drawIndicator(canvas, position, drawable);
			}
		}
	}

	// ��positionλ����ʾ��Ҫ���item�ı�־
	protected void showAppearingAtPosition(int position) {
		mPrivateFlags |= FLAG_DRAW_APPEARING;
		this.mAppearingPosition = position;
	}

	// ��positionλ����ʾ��Ҫ�Ƴ�item�ı�־
	protected void showDisappearingAtPosition(int position) {
		mPrivateFlags |= FLAG_DRAW_DISAPPEARING;
		this.mDisappearingPosition = position;
	}

	protected void hideAppearing() {
		mPrivateFlags &= ~FLAG_DRAW_APPEARING;
	}

	protected void hideDisappearing() {
		mPrivateFlags &= ~FLAG_DRAW_DISAPPEARING;
	}

	public void setDragSortEnabled(boolean enabled) {
		this.mDragSortEnabled = enabled;
	}

	protected void drawIndicator(Canvas canvas, int position, Drawable drawable) {
		if (position == -1) {
			return;
		}
		View view = getView(position);
		if (view == null) {
			return;
		}
		canvas.save();
		int l = view.getLeft();
		int t = view.getTop();
		canvas.translate(l, t);
		drawable.setBounds(0, 0, view.getWidth(), view.getHeight());
		drawable.draw(canvas);
		canvas.restore();
	}

	// �ڶ�����ɺ󷢳�˳��ı��֪ͨ
	private final void notifyAfterMoveView(int fromPos, int toPos) {

		if (-1 == toPos || fromPos == toPos) {
			return;
		}

		if (mOnReorderListener != null) {
			mOnReorderListener.onReOrderContent(fromPos, toPos);
		}
		else {
			ListAdapter adapter = getAdapter();
			if (adapter instanceof DragSortAdapter) {
				DragSortAdapter dragSortAdapter = (DragSortAdapter) adapter;
				dragSortAdapter.onReOrderContent(fromPos, toPos);
			}
		}
	}

	private class NotifyExecutor implements Runnable {

		private final int mFromPos;
		private final int mToPos;

		public NotifyExecutor(int fromPos, int toPos) {
			this.mFromPos = fromPos;
			this.mToPos = toPos;
		}

		@Override
		public void run() {

			if (mMovingChildViewsNum > 0) {
				mHandler.postDelayed(this, 200);
			}
			else {
				mPrivateFlags &= ~FLAG_DRAW_APPEARING;
				notifyAfterMoveView(mFromPos, mToPos);
			}
		}

	}

	private void reOrderViews(int fromPos, int toPos) {

		Log.d(TAG, "reOrder:" + fromPos + "<-->" + fromPos);

		if (fromPos == toPos) {
			return;
		}

		// ���ƶ�View��ǰ��ȡ���϶���View���Լ�Ŀ��λ��
		final View fromView = getView(fromPos);
		final View toView = getView(toPos);
		final Rect toRect = new Rect();
		getRect(toView, toRect);

		mReorderArray.reOrder(fromPos, toPos);

		fromView.layout(toRect.left, toRect.top, toRect.right, toRect.bottom);

	}

	@Override
	public void onMove(int fromPos, int toPos) {

		if (DEBUG) {
			Log.d(TAG, fromPos + " move to " + toPos);
		}

		moveView(fromPos, toPos);

	}

	// ����ʵ�ʵĶ���ʵ��λ��
	private void moveView(int fromPos, int toPos) {

		final View fromView = getView(fromPos);
		final View toView = getView(toPos);

		final Rect fromRect = new Rect();
		final Rect toRect = new Rect();

		getRect(fromView, fromRect);
		getRect(toView, toRect);

		int tx = toRect.left - fromRect.left;
		int ty = toRect.top - fromRect.top;

		Animation translate = new TranslateAnimation(0, tx, 0, ty);

		translate.setDuration(150);
		translate.setFillEnabled(true);
		translate.setFillAfter(true);
		translate.setAnimationListener(new MoveViewAnimationListener(fromView, toView.getLeft(), toView.getTop()));

		fromView.startAnimation(translate);
		mMovingChildViewsNum++;
	}

	// ����ʵ��position��View����
	protected View getView(int actualPos) {
		// ��ʵ��positionת����ԭʼposition
		int originalPos = actualPos;
		ReorderArray array = mReorderArray;
		if (array != null) {
			originalPos = array.get(actualPos);
		}
		return getChildAt(originalPos - getFirstVisiblePosition());
	}

	protected void getRect(View view, Rect rect) {
		rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
	}

	// ��������View�����ԭʼλ��
	private int getOriginalPos(int x, int y) {
		return pointToPosition(x, y);
	}

	public void setOnReorderedListener(OnReorderContentListener listener) {
		this.mOnReorderListener = listener;
	}

	public void setAppearingDrawable(int id) {
		mAppearingDrawable = getResources().getDrawable(id);
	}

	public void setDisappearingDrawable(int id) {
		mDisappearingDrawable = getResources().getDrawable(id);
	}

	protected Drawable getAppearingDrawable() {
		return mAppearingDrawable;
	}

	protected Drawable getDisappearingDrawable() {
		return mDisappearingDrawable;
	}

	public interface OnReorderContentListener {
		/**
		 * implement this to reorder your content data and call notifyDataSetChanged() in this
		 * function
		 */
		public void onReOrderContent(int fromPos, int toPos);
	}

	private class MoveViewAnimationListener implements Animation.AnimationListener {

		private final View mTarget;
		private final int mNewX, mNewY;

		public MoveViewAnimationListener(View target, int newX, int newY) {
			this.mTarget = target;
			this.mNewX = newX;
			this.mNewY = newY;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mTarget.layout(mNewX, mNewY, mNewX + mTarget.getWidth(), mNewY + mTarget.getHeight());
			mTarget.clearAnimation();
			mMovingChildViewsNum--;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

	}

}
