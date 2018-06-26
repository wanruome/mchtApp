/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2014-10-31 上午10:55:29 
 */
package com.ruomm.base.basecall;

import java.io.IOException;

import android.content.Context;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 调用系统录音辅助类
 * 
 * @author Ruby
 */
public class BaseCallMediaRecorder {
	private final Context mContext;
	/**
	 * 系统录音机
	 */
	private MediaRecorder mRecorder;
	/**
	 * 录音存储路径
	 */
	private String path;
	/**
	 * 录音开始时间
	 */
	private long time_start = 0;
	/**
	 * 录音结束时间
	 */
	private long time_end = 0;
	/**
	 * 是否正在录音中
	 */
	public boolean isRecording = false;

	/**
	 * 构造函数
	 * 
	 * @param mContext
	 *            调用录音Context；
	 * @param filepath
	 *            录音存储文件路径；
	 */
	public BaseCallMediaRecorder(Context mContext, String filepath) {
		super();
		this.mContext = mContext;
		this.path = filepath;
	}

	/**
	 * 开始录音
	 */
	@SuppressWarnings("deprecation")
	public void StartRecorder() {
		if (!TextUtils.isEmpty(path)) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			mRecorder.setOutputFile(path);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			try {
				mRecorder.prepare();
				mRecorder.start();
				isRecording = true;
				time_start = System.currentTimeMillis();
			}
			catch (IOException e) {
				mRecorder.release();
				isRecording = false;
				mRecorder = null;
			}
		}
		else {
			Toast.makeText(mContext, "录音失败", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 停止录音
	 */
	public void StopRecorder() {
		if (null != mRecorder) {
			time_end = System.currentTimeMillis();
			isRecording = false;
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
		else {
			isRecording = false;
			path = null;
		}
	}

	/**
	 * 录音时长
	 * 
	 * @return 录音时长，毫秒计数，强制转换为Integer类型了；
	 */
	public int getRecorderTime() {
		long recordertime;
		if (time_start > 0 && time_end > 0) {
			recordertime = time_end - time_start;
		}
		else {
			recordertime = 0;
		}
		return (int) recordertime;
	}

	/**
	 * @return 录音文件的路径；
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 录音时候的音量百分比的大小，0为最小，100为最大，在需要显示录音波形图等时候可以调用此方法更新数据
	 * 
	 * @return 录音时候的音量大小，范围为0-100；
	 */
	public int getCurrentVolume() {
		int temp = 0;
		if (isRecording) {
			temp = mRecorder.getMaxAmplitude() * 200 / 32768;
		}
		if (temp > 100) {
			temp = 100;
		}
		return temp;
	}
}
