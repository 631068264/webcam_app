package com.example.wuyuxi.webcam.core;

import android.app.Activity;
import android.os.Bundle;

import com.example.wuyuxi.webcam.util.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * @Annotation //
 */
public class BaseActivity extends Activity {
    protected Set<BaseDialogFragment> mDialogContainer = new HashSet<>();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /***
     * Dialog fragment Manager
     */
    public void showDialog(BaseDialogFragment fragment) {
        if (mDialogContainer.contains(fragment)) {
            Logger.e(String.format("FragmentDialog %s has already shown in current activity",
                    fragment.toString()));
        } else {
            mDialogContainer.add(fragment);
            fragment.show(getFragmentManager(), fragment.getClass().getName());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //进入后台模式时调用
        //关闭并不保留所有DialogFragment状态
        for (BaseDialogFragment fragment : mDialogContainer) {
            if (fragment != null) {
                fragment.dismissAllowingStateLoss();
            }
        }
        mDialogContainer.clear();
        super.onSaveInstanceState(outState);
    }
}
