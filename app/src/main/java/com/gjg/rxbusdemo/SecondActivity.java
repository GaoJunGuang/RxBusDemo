package com.gjg.rxbusdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecondActivity extends AppCompatActivity {

    @BindView(R.id.bt_sendmsg_to_main)
    Button btSendmsgToMain;

    private RxBus rxBus = RxBus.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.bt_sendmsg_to_main)
    public void sendMsgToMain(){
        rxBus.post("second", new String("这是来自第二个页面的数据"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxBus.removeObserverable("first");
    }
}
