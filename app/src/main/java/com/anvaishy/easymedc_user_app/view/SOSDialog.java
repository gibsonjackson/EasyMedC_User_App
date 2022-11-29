package com.anvaishy.easymedc_user_app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.anvaishy.easymedc_user_app.R;

public class SOSDialog extends Dialog {

    private Context mContext;
    private View.OnClickListener listener;
    Button send;

    public SOSDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public void setListener(View.OnClickListener l) {
        this.listener = l;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sos_dialog);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(listener);
    }


}
