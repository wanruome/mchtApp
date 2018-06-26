/**	MediaPlayer声音播放控制类
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年11月16日 上午11:15:39 
 */
package com.ruomm.base.tools;

import com.ruomm.base.tools.listener.MPlayListener;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class PlayUtil {
	private MediaPlayer mPlayer = null;
	private String mPlayingPath = null;
	private boolean isPause = false;
	private MPlayListener mPlayListener;

	public PlayUtil() {
		super();

	}

	public MPlayListener getMPlayListener() {
		return mPlayListener;
	}

	public void setMPlayListener(MPlayListener mPlayListener) {
		this.mPlayListener = mPlayListener;
	}

	public void startPlaying(String filePath) {
		if (filePath == null) {
			return;
		}
		boolean isStartError = false;
		if (filePath.equals(mPlayingPath)) {
			this.mPlayingPath = filePath;
			if (null != mPlayer && mPlayer.isPlaying()) {
				isPause = false;
				return;
			}
			else if (null != mPlayer && isPause) {
				isPause = false;
				try {
					mPlayer.start();
				}
				catch (Exception e) {
					stopPlaying();
					isStartError = true;
				}
			}
			else {
				stopPlaying();
				mPlayingPath = filePath;
				mPlayer = new MediaPlayer();
				try {
					mPlayer.setDataSource(filePath);
					mPlayer.prepare();
					mPlayer.start();
					mPlayer.setOnCompletionListener(mplayCompletionListener);

				}
				catch (Exception e) {
					stopPlaying();
					isStartError = true;
				}
			}
		}
		else {
			this.mPlayingPath = filePath;
			stopPlaying();
			mPlayingPath = filePath;
			mPlayer = new MediaPlayer();
			try {
				mPlayer.setDataSource(filePath);
				mPlayer.prepare();
				mPlayer.start();
				mPlayer.setOnCompletionListener(mplayCompletionListener);
			}
			catch (Exception e) {
				stopPlaying();
				isStartError = true;
			}
		}
		if (isStartError == true) {
			if (null != mPlayListener) {
				mPlayListener.onMediaPlayStartError(this.mPlayingPath);
			}
		}

	}

	private final OnCompletionListener mplayCompletionListener = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {

			if (null != mPlayListener) {
				mPlayListener.onMediaPlayStop(mPlayingPath);
			}

		}
	};

	public void pausePlaying() {
		if (null != mPlayer && mPlayer.isPlaying()) {
			mPlayer.pause();
			isPause = true;
		}
	}

	public void stopPlaying() {
		isPause = false;
		if (mPlayer != null) {
			if (mPlayer.isPlaying()) {
				mPlayer.stop();
			}
			mPlayer.release();
			mPlayer = null;
		}
	}

}
