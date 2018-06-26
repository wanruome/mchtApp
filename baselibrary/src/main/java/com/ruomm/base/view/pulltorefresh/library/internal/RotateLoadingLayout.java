/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.ruomm.base.view.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.ruomm.base.view.pulltorefresh.library.PullToRefreshBase.Mode;
import com.ruomm.base.view.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.ruomm.R;

public class RotateLoadingLayout extends LoadingLayout {

	public RotateLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);

	}

	@Override
	public void onLoadingDrawableSet(Drawable imageDrawable) {

	}

	@Override
	protected void onPullImpl(float scaleOfLayout) {

	}

	@Override
	protected void refreshingImpl() {
		// mHeaderImage.startAnimation(mRotateAnimation);
		mHeaderImage.setVisibility(View.INVISIBLE);
		mHeaderProgress.setVisibility(View.VISIBLE);
	}

	@Override
	protected void resetImpl() {
		// mHeaderImage.clearAnimation();
		mHeaderImage.setVisibility(View.INVISIBLE);
		mHeaderProgress.setVisibility(View.INVISIBLE);
		resetImageRotation();
	}

	private void resetImageRotation() {
		// if (null != mHeaderImageMatrix) {
		// mHeaderImageMatrix.reset();
		// mHeaderImage.setImageMatrix(mHeaderImageMatrix);
		// }
	}

	@Override
	protected void pullToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected void releaseToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.pulltorefresh_default_ptr_rotate;
	}

}
