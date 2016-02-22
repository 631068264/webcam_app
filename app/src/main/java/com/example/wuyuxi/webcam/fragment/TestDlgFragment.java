package com.example.wuyuxi.webcam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wuyuxi.webcam.R;
import com.example.wuyuxi.webcam.core.BaseDialogFragment;
import com.example.wuyuxi.webcam.events.UrlEvent;

import de.greenrobot.event.EventBus;

/**
 * @Annotation //
 */
public class TestDlgFragment extends BaseDialogFragment {

    public static TestDlgFragment newInstance(String url) {
        TestDlgFragment fragment = new TestDlgFragment();
        fragment.setUrl(url);
        return fragment;
    }

    private LinearLayout mLayout;
    private EditText mText;
    private View mSeturl;

    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.cloneInContext(getWrappedContext())
                .inflate(R.layout.fragment_location, container, false);

        mLayout = (LinearLayout) view.findViewById(R.id.view_layout);
        mText = (EditText) view.findViewById(R.id.url);
        mSeturl = view.findViewById(R.id.set_url);

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mText.setText(url);
//        mText.requestFocus();
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(mText, InputMethodManager.SHOW_FORCED);

        mSeturl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mText.getText().toString())) {
                    Toast.makeText(getActivity(), "请填写地址", Toast.LENGTH_SHORT).show();
                } else {
                    dismiss();
                    EventBus.getDefault().post(new UrlEvent(mText.getText().toString()));
                }
            }
        });


        return view;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
