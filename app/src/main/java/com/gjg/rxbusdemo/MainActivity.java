package com.gjg.rxbusdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.bt_sendmsg)
    Button bt_sendmsg;
    @BindView(R.id.bt_startactivity)
    Button btStartactivity;
    @BindView(R.id.tv_result)
    TextView tvResult;

    private RxBus rxBus = RxBus.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        rxBus.toObserverableOnMainThread("first", new RxBusResult() {
            @Override
            public void onRxBusResult(Object o) {
                final String msg = (String)o;
                tvResult.setText("first收到消息;" + msg);
                Toast.makeText(MainActivity.this, "收到消息;" + msg, Toast.LENGTH_SHORT).show();
            }
        });

        rxBus.toObserverableOnMainThread("second", new RxBusResult() {
            @Override
            public void onRxBusResult(Object o) {
                String msg = (String)o;
                tvResult.setText("second收到消息;" + msg);
                Toast.makeText(MainActivity.this, "second收到消息;" + msg, Toast.LENGTH_SHORT).show();
            }
        });

    }
    ////发送消息
    @OnClick(R.id.bt_sendmsg)
    public void bt_sendmsg(View view){
        rxBus.post("first", new String("hello rxbus"));
    }

    //跳转界面
    @OnClick(R.id.bt_startactivity)
    public void bt_startActivity(View view){
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        MainActivity.this.startActivity(intent);
    }

    /**
     * 退出时，释放rxbus
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxBus.release();
    }


}
