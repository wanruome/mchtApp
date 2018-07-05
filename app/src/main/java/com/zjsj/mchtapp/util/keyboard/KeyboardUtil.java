package com.zjsj.mchtapp.util.keyboard;
import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ruomm.base.tools.DisplayUtil;
import com.zjsj.mchtapp.R;

public class KeyboardUtil {

    private Keyboard k1;// 字母键盘
    private Keyboard k2;// 数字键盘
    private Keyboard k3;// 符号键盘
    private boolean isUpper = false;// 是否大写
    // 字母键盘
    private static final int BOARD_LETTER=-11;
    // 数字键盘
    private static final int BOARD_NUMBER=-12;
    // 符号键盘
    private static final int BOARD_SYMBOL=-13;
    private static final int BOARD_SHIFT=-14;
    private static final int ACTION_DELETE=-15;
    private static final int ACTION_DONE=-16;
    private static final int ACTION_LETF=-17;
    private static final int ACTION_RIGHT=-18;
    //€
    private static final int MONEY_RMB=-21;
    private static final int MONEY_OY=-22;

    private static final int MONEY_YB=-23;


    private ViewGroup rootView;
    private View keyboradContainer;
    private KeyboardView keyboardView;
    private EditText ed;
    private LayoutInflater  mLayoutInflater;



    private KeyboardUtil(Activity activity, EditText edit) {
        this.ed = edit;
        k1 = new Keyboard(activity, R.xml.key_letter);
        k2 = new Keyboard(activity,R.xml.key_number);
        k3 = new Keyboard(activity,R.xml.key_symbol);
        mLayoutInflater=LayoutInflater.from(activity);

//        keyboardView = new KeyboardView(activity, null);
        rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content));
        keyboradContainer=mLayoutInflater.inflate(R.layout.lyt_keyboard,null);
        int height= DisplayUtil.getNavigationBarHeight(activity);
        if(height>0)
        {
           keyboradContainer.setPadding(0,0,0,height);
        }
        keyboardView = keyboradContainer.findViewById(R.id.kv_lyt_keyboard);
        keyboardView.setKeyboard(k3);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(onKeyboardActionListener);
    }

    OnKeyboardActionListener onKeyboardActionListener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                hideKeyboard();
            } else if (primaryCode == ACTION_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == BOARD_LETTER) {// 大小写切换

                keyboardView.setKeyboard(k1);
            }
            else if(primaryCode==BOARD_SHIFT)
            {
                isUpper = !isUpper;
                k1.setShifted(isUpper);
                keyboardView.invalidateAllKeys();
            }
            else if (primaryCode == BOARD_NUMBER) {// 符号键盘
                keyboardView.setKeyboard(k2);
            }
            else if (primaryCode == BOARD_SYMBOL) {// 符号键盘
                keyboardView.setKeyboard(k3);
            } else if (primaryCode == ACTION_LETF) { //向左
                if (start > 0) {
                    ed.setSelection(start - 1);
                }
            } else if (primaryCode == ACTION_RIGHT) { // 向右
                if (start < ed.length()) {
                    ed.setSelection(start + 1);
                }
            } else {
                String str = Character.toString((char) primaryCode);
                if (isWord(str)) {
                    if (isUpper) {
                        str = str.toUpperCase();
                    } else {
                        str = str.toLowerCase();
                    }
                }
                editable.insert(start, str);

            }
        }
    };

    private boolean isShow = false;

    public void showKeyboard() {
        if (!isShow) {
            rootView.addView(keyboradContainer);
            isShow = true;
        }
    }

    private void hideKeyboard() {
        if (rootView != null && keyboardView != null && isShow) {
            isShow = false;
            rootView.removeView(keyboradContainer);
        }
        mInstance = null;
    }

    private boolean isWord(String str) {
        return str.matches("[a-zA-Z]");
    }

    private static KeyboardUtil mInstance;

    public static KeyboardUtil shared(Activity activity, EditText edit) {
        if (mInstance == null) {
            mInstance = new KeyboardUtil(activity, edit);
        }
        mInstance.ed = edit;
        return mInstance;
    }
}
