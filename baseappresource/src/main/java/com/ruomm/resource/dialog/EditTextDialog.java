package com.ruomm.resource.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.ruomm.base.view.dialog.BaseDialogUserConfig;
import com.ruomm.resource.R;

public class EditTextDialog extends BaseDialogUserConfig {
    private EditText editText;
    public EditTextDialog(Context mContext) {
        super(mContext, R.layout.dialog_edittext_input, com.ruomm.resource.R.style.dialogStyle_floating_bgdark);
        setListenerCancle(com.ruomm.resource.R.id.dialog_cancle);
        setListener(com.ruomm.resource.R.id.dialog_confirm);
        setDialogLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        setCancelable(false);
        editText=(EditText)findViewById(R.id.dialog_content);

        // LinearLayout layout=(LinearLayout) findViewById(R.id.dialog_container);
        // layout.set
        // setDialogLayoutParams(DisplayUtil.getDispalyWidth(mContext) * 3 / 4,
        // LayoutParams.WRAP_CONTENT);
    }

    public EditTextDialog(Context mContext, boolean isCancelcale) {
        this(mContext);
        setCancelable(isCancelcale);

    }

    private EditTextDialog(Context mContext, int layoutId, int dialogStyle) {
        super(mContext, layoutId, dialogStyle);

        // TODO Auto-generated constructor stub
    }


    public void setDialogTitle(CharSequence titleString) {
        if (TextUtils.isEmpty(titleString)) {
            setText(com.ruomm.resource.R.id.dialog_title, "输入密码");
        }
        else {
            setText(com.ruomm.resource.R.id.dialog_title, titleString);
        }
    }

    private void setMessageButton(CharSequence cancaleText, CharSequence confirmText) {
        if (TextUtils.isEmpty(cancaleText)) {
            setText(com.ruomm.resource.R.id.dialog_cancle, "取消");
        }
        else {
            setText(com.ruomm.resource.R.id.dialog_cancle, cancaleText);
        }
        if (TextUtils.isEmpty(confirmText)) {
            setText(com.ruomm.resource.R.id.dialog_confirm, "确定");
        }
        else {
            setText(com.ruomm.resource.R.id.dialog_confirm, confirmText);
        }

    }


    @Override
    protected void onItemClick(View v, int vID) {
        super.onItemClick(v, vID);
        String data=editText.getText().toString();
        v.setTag(data);
    }
    // public int getDispalyWidth(Context mContext) {
    // DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
    // int displaywidth = dm.widthPixels;
    // return displaywidth;
    // }
    //
    // public int getDispalyHeight(Context mContext) {
    // DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
    // int displayheight = dm.heightPixels;
    // return displayheight;
    // }

}