package com.zjsj.mchtapp.keyboard;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;

import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.dialog.BaseDialogUserConfig;
import com.ruomm.base.view.dialog.BasePopupWindow;
import com.ruomm.base.view.dialog.BasePopupWindowUserConfig;
import com.zjsj.mchtapp.R;

public class KeyBoradDialog extends Dialog {
    private Context mContext;
    public KeyBoradDialog(Context mContext) {
        super(mContext, com.ruomm.R.style.dialogStyle_floating);
        this.mContext = mContext;
        setContentView(R.layout.keyboard_layout);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
    }
}
