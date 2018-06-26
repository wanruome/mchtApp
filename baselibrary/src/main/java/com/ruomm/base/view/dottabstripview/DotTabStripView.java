/**
 *	@copyright 视秀科技-2014 
 * 	@author wanruome  
 * 	@create 2014年8月22日 上午11:53:58 
 */
package com.ruomm.base.view.dottabstripview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.ruomm.R;
import com.ruomm.base.ioc.extend.Thread_CanStop;

public class DotTabStripView extends LinearLayout {
	private final int MSG_WHAT_CYCLE = 200;
	private final Context mContext;
	private final LinearLayout dotContainer;
	private final int imgresId;
	private final int imgsize;
	private int imgmargin_horizontal;
	private int imgmargin_vertical;
	private final int dotOrientation;
	private int dotcount;
	private DotTabStripListener listener;
	private int currentitem = -1;
	private CycleThread cycleThread;
	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_WHAT_CYCLE) {
				int tempindex = (Integer) msg.obj;
				setCurrentItem(tempindex);
			}
		};
	};

	// private DotviewASyncTask myASyncTask;

	public DotTabStripView(Context context) {
		this(context, null);
	}

	public DotTabStripView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint({ "Recycle", "NewApi" })
	public DotTabStripView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		this.dotOrientation = super.getOrientation();
		// 获取样式
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DotTabStripView);
		this.imgsize = a.getDimensionPixelSize(R.styleable.DotTabStripView_dotImageSize, 5);
		this.imgresId = a.getResourceId(R.styleable.DotTabStripView_dotImage, 0);
		if (dotOrientation == LinearLayout.HORIZONTAL) {
			this.imgmargin_horizontal = a.getDimensionPixelSize(R.styleable.DotTabStripView_dotImageMargin, 0);
			this.imgmargin_vertical = 0;
		}
		else {
			this.imgmargin_vertical = a.getDimensionPixelSize(R.styleable.DotTabStripView_dotImageMargin, 0);
			this.imgmargin_horizontal = 0;
		}

		this.dotContainer = new LinearLayout(context);
		this.dotContainer.setOrientation(dotOrientation);
		this.dotContainer.setGravity(Gravity.CENTER);
		this.dotContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(this.dotContainer);
	}

	public void setListener(DotTabStripListener listener) {
		this.listener = listener;
	}

	public void setDotCount(int count) {
		setDotCount(count, 0);
	}

	public void setDotCount(int count, int selectposition) {
		dotContainer.removeAllViews();
		dotcount = count;
		if (count < 1) {
			return;
		}
		if (selectposition > count - 1) {
			currentitem = dotcount - 1;
		}
		else if (selectposition < 0) {
			currentitem = 0;
		}
		else {
			currentitem = selectposition;
		}
		for (int index = 0; index < count; index++) {
			ImageView image = new ImageView(mContext);
			image.setLayoutParams(new LayoutParams(imgsize + 2 * imgmargin_horizontal, imgsize + 2 * imgmargin_vertical));
			image.setPadding(imgmargin_horizontal, imgmargin_vertical, imgmargin_horizontal, imgmargin_vertical);
			image.setScaleType(ScaleType.FIT_XY);
			image.setImageResource(imgresId);
			dotContainer.addView(image, index);
		}
		setCurrentItem(currentitem);

	}

	public void setCurrentItem(int positon) {
		if (dotcount < 1) {
			return;
		}
		if (positon > dotcount - 1) {
			currentitem = dotcount - 1;
		}
		else if (positon < 0) {
			currentitem = 0;
		}
		else {
			currentitem = positon;
		}
		for (int index = 0; index < dotcount; index++) {
			if (currentitem == index) {
				((ImageView) dotContainer.getChildAt(index)).setSelected(true);
			}
			else {
				((ImageView) dotContainer.getChildAt(index)).setSelected(false);
			}
		}
		if (null != listener) {
			listener.onDotViewSelect(currentitem);
		}
	}

	public int getCurrentItem() {
		return currentitem;
	}

	public void setCycleTask(long ms) {
		if (null != cycleThread) {
			cycleThread.stopTask();
			cycleThread = null;
		}
		cycleThread = new CycleThread(ms);
		cycleThread.start();
	}

	class CycleThread extends Thread_CanStop {
		long time = 0;
		int count = 0;
		int index = 0;

		public CycleThread(long time) {
			super();
			this.time = time;
			this.count = dotcount;
			this.index = currentitem;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (true) {
				if (time <= 0 || count < 2 || isStopTask) {
					break;
				}
				SystemClock.sleep(time);
				index = (index + 1) % count;
				Message msg = new Message();
				msg.what = MSG_WHAT_CYCLE;
				msg.obj = index;
				mHandler.sendMessage(msg);
			}

		}
	}

	@Override
	protected void onDetachedFromWindow() {
		if (null != cycleThread) {
			cycleThread.stopTask();
			cycleThread = null;
		}
		super.onDetachedFromWindow();
	}
}
