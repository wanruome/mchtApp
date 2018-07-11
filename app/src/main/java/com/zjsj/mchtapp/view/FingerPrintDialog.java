package com.zjsj.mchtapp.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.view.dialog.BaseDialogUserConfig;
import com.zjsj.mchtapp.R;

public class FingerPrintDialog extends BaseDialogUserConfig {
    public FingerPrintDialog(Context mContext) {
        super(mContext, R.layout.fingerprint_dialog_lay, com.ruomm.resource.R.style.dialogStyle_floating_bgdark);
        setCancelable(true);
         setDialogLayoutParams(DisplayUtil.getDispalyWidth(mContext) * 3 / 4,
         ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public FingerPrintDialog(Context mContext, boolean isCancelcale) {
        this(mContext);
        setCancelable(isCancelcale);

    }

    private FingerPrintDialog(Context mContext, int layoutId, int dialogStyle) {
        super(mContext, layoutId, dialogStyle);

        // TODO Auto-generated constructor stub
    }

    public void setMessageContent( CharSequence contentString) {
        if (TextUtils.isEmpty(contentString)) {
            setText(com.ruomm.resource.R.id.dialog_content, contentString);
            findViewById(com.ruomm.resource.R.id.dialog_content).setVisibility(View.GONE);
        } else {
            setText(com.ruomm.resource.R.id.dialog_content, contentString);
            findViewById(com.ruomm.resource.R.id.dialog_content).setVisibility(View.VISIBLE);
        }
    }


}
