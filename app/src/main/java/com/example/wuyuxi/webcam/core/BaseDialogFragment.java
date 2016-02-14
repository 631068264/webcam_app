package com.example.wuyuxi.webcam.core;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import com.example.wuyuxi.webcam.R;

/**
 * @Annotation
 */
public class BaseDialogFragment extends DialogFragment {
    protected Context mWrappedContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //按外围关闭Dialog
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_Transparent);
    }

    protected Context getWrappedContext() {
        if (mWrappedContext == null) {
            mWrappedContext = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
        }
        return mWrappedContext;
    }

}
