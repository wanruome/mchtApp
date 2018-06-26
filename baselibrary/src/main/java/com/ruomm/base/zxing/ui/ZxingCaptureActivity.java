package com.ruomm.base.zxing.ui;

import java.io.IOException;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruomm.base.tools.FileUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.view.upimg.UpImageActivity;
import com.ruomm.base.view.upimg.UpImgHelper;
import com.ruomm.base.zxing.camera.CameraManager;
import com.ruomm.base.zxing.decoding.CaptureActivityHandler;
import com.ruomm.base.zxing.decoding.InactivityTimer;
import com.ruomm.base.zxing.view.ViewZxingGalleryView;
import com.ruomm.base.zxing.view.ViewfinderView;
import com.ruomm.base.zxing.view.ZxingImageCallBack;
import com.ruomm.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

public abstract class ZxingCaptureActivity extends Activity implements Callback, ZxingImageCallBack {

	private CaptureActivityHandler handler;

	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	private Context mContext;

	class Views {
		ViewfinderView viewfinderView;
		ImageView btn_zxing_cancel;
		ImageView btn_zxing_Light;
		TextView btn_zxing_gallery;
		FrameLayout zxing_frame;
		TextView zxing_textdes;
		ViewZxingGalleryView zxinggallry_view;

	}

	private final Views views = new Views();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zxing_camera);
		mContext = this;
		CameraManager.init(getApplication());
		int height = CameraManager.get().getFramingHeight();

		views.zxing_frame = (FrameLayout) findViewById(R.id.zxing_frame);
		views.zxing_textdes = (TextView) findViewById(R.id.zxing_textdes);
		views.zxing_frame.getLayoutParams().height = height;
		views.zxing_frame.requestLayout();
		views.viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		views.btn_zxing_Light = (ImageView) this.findViewById(R.id.zxing_btn_Light);
		views.btn_zxing_gallery = (TextView) this.findViewById(R.id.zxing_btn_gallery);
		views.btn_zxing_cancel = (ImageView) this.findViewById(R.id.zxing_btn_cancel);
		views.zxinggallry_view = (ViewZxingGalleryView) findViewById(R.id.zxing_galleryview);
		views.zxinggallry_view.setZxingImageCallBack(this);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		views.btn_zxing_Light.setOnClickListener(myClickListener);
		views.btn_zxing_gallery.setOnClickListener(myClickListener);
		views.btn_zxing_cancel.setOnClickListener(myClickListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		}
		else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		if (null != views.viewfinderView) {
			views.viewfinderView.recycleLineDrawable();
		}
		super.onDestroy();
	}

	private final OnClickListener myClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int vID = v.getId();
			if (vID == R.id.zxing_btn_cancel) {
				finish();
			}
			else if (vID == R.id.zxing_btn_Light) {
				if (views.btn_zxing_Light.isSelected()) {
					views.btn_zxing_Light.setSelected(false);
					CameraManager.get().lightOff();
				}
				else {
					views.btn_zxing_Light.setSelected(true);
					CameraManager.get().lightOpen();
				}
			}
			else if (vID == R.id.zxing_btn_gallery) {
				setIsOnDecodeImage(true);
				UpImgHelper.getInstance().initialize(true, 320,
						FileUtils.getPathContext(mContext, UpImgHelper.PATH_CACHE_COMPRESS),
						FileUtils.getPathExternalFile(mContext, UpImgHelper.PATH_CACHE_COMPRESS));

				Intent intent = new Intent(ZxingCaptureActivity.this, UpImageActivity.class);
				startActivityForResult(intent, UpImgHelper.RequestCode_GET_PICTURES);

			}

		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		UpImgHelper.getInstance().onUpImgActivityResult(this, requestCode, resultCode, data);
		if (requestCode == UpImgHelper.RequestCode_CROP_IMAGE && resultCode == RESULT_OK) {
			// String imagePath =
			// UpImgHelper.getImageFile(UpImgHelper.drr.get(0)).getAbsolutePath();
			String imagePath = UpImgHelper.getInstance().getImageFile(UpImgHelper.getInstance().drr.get(0))
					.getAbsolutePath();
			views.zxinggallry_view.startDecoding(imagePath);

		}
		else if (requestCode == UpImgHelper.RequestCode_GET_PICTURES && resultCode != RESULT_OK
				|| requestCode == UpImgHelper.RequestCode_CROP_IMAGE) {
			setIsOnDecodeImage(false);
		}
	};

	/**
	 * Handler scan result
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {

		inactivityTimer.onActivity();
		if (views.btn_zxing_gallery.isSelected()) {
			return;
		}
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		// FIXME
		if (TextUtils.isEmpty(resultString)) {
			Toast.makeText(ZxingCaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			ZxingCaptureActivity.this.finish();
		}
		else {
			// System.out.println("Result:"+resultString);
			doZxingAction(resultString);

		}

	}

	@Override
	public void hanleDecodeGallery(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		// FIXME
		if (TextUtils.isEmpty(resultString)) {
			Toast.makeText(ZxingCaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			ZxingCaptureActivity.this.finish();
		}
		else {
			doZxingAction(resultString);

		}
	}

	@Override
	public void hanleDecodeGalleryError(String imagePath) {
		setIsOnDecodeImage(false);

	}

	public void setIsOnDecodeImage(boolean isDecodingImage) {
		if (isDecodingImage) {
			views.btn_zxing_gallery.setSelected(true);
			views.viewfinderView.setShowScanRect(false);
			views.zxing_textdes.setVisibility(View.GONE);
			views.viewfinderView.invalidate();
		}
		else {
			views.btn_zxing_gallery.setSelected(false);
			views.viewfinderView.setShowScanRect(true);
			views.zxing_textdes.setVisibility(View.VISIBLE);
			views.viewfinderView.invalidate();
			if (null != handler) {
				handler.restartPreviewAndDecode();
			}
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		}
		catch (IOException ioe) {
			return;
		}
		catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return views.viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		views.viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.zxing_beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			}
			catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@SuppressLint("DefaultLocale")
	private void doZxingAction(String resultString) {

		if (TextUtils.isEmpty(resultString)) {
			ToastUtil.makeFailToastThr(this, "数据解析异常");
			return;
		}
		String resultLowerCase = resultString.toLowerCase();
		if (resultLowerCase.startsWith("http://")) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(resultString);
			intent.setData(content_url);
			startActivity(intent);
			ZxingCaptureActivity.this.finish();
		}
		else {
			doZxingUserAction(resultString);
			ZxingCaptureActivity.this.finish();
		}
	}

	protected abstract void doZxingUserAction(String resultString);
}
