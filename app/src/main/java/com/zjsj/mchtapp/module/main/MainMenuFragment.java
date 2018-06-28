package com.zjsj.mchtapp.module.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruomm.base.ioc.activity.BaseFragment;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.tools.TelePhoneUtil;
import com.ruomm.base.view.percentview.FrameLayout_PercentHeight;
import com.ruomm.base.view.percentview.RelativeLayout_PercentHeight;
import com.ruomm.base.view.swiplayout.SwipeLayout;
import com.ruomm.resource.ui.AppFragment;
import com.zjsj.mchtapp.R;

import org.w3c.dom.Text;

public class MainMenuFragment extends AppFragment {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.ly_userinfo)
        RelativeLayout_PercentHeight ly_userinfo;
        @InjectView(id=R.id.img_header)
        ImageView img_header;
        @InjectView(id=R.id.text_name)
        TextView text_name;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.main_menu_lay, null);
        BaseUtil.initInjectAll(this,mView);
        views.ly_userinfo.setHeightPercent(0.6f);
        int height=DisplayUtil.getNavigationBarHeight(mContext);
        LinearLayout menu_exit_app=mView.findViewById(R.id.menu_exit_app);
        LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams)menu_exit_app.getLayoutParams();
        layoutParams.bottomMargin=height;
//        menu_exit_app.requestLayout();

        return mView;

    }
}
