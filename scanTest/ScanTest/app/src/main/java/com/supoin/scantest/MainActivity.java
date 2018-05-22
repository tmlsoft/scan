package com.supoin.scantest;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import static com.supoin.broadcast.SystemBroadCast.SCN_CUST_ACTION_CANCEL;
import static com.supoin.broadcast.SystemBroadCast.SCN_CUST_ACTION_SCODE;
import static com.supoin.broadcast.SystemBroadCast.SCN_CUST_ACTION_START;
import static com.supoin.broadcast.SystemBroadCast.SCN_CUST_EX_SCODE;

public class MainActivity extends AppCompatActivity {

    private Switch powerSwitch;
//    private Switch pauseSwitch;
    private ListView scanList;
    private EditText etScan;
    private Button btnScan;
    private Button btnClear;
    private ArrayAdapter<Object> arrayAdapter;
    private TextView tvPower;
    private TextView tvPause;
    private ActionBar actionBar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        actionBar = getActionBar();
//        actionBar.setTitle("Version:2.0");
        initView();
        // Register receiver
        IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        registerReceiver(scanDataReceiver,intentFilter);

        // Initialize the array adapter for the conversation thread
        arrayAdapter = new ArrayAdapter<>(this, R.layout.message);
        scanList.setAdapter(arrayAdapter);

        //button listener
        btnScan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    onTouchButton();
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    onReleaseButton();
                }
                return false;
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               arrayAdapter.clear();
            }
        });



    }

    private void onTouchButton() {
        Intent scannerIntent = new Intent(SCN_CUST_ACTION_START);
        sendBroadcast(scannerIntent);
    }

    private void onReleaseButton() {
        Intent scannerIntent = new Intent(SCN_CUST_ACTION_CANCEL);
        sendBroadcast(scannerIntent);
    }

    private void initView() {
        powerSwitch = findViewById(R.id.powerSwitch);
//        pauseSwitch = findViewById(R.id.pauseSwitch);
        tvPower = findViewById(R.id.tv_power);
//        tvPause = findViewById(R.id.tv_pause);
        scanList = findViewById(R.id.scanList);
        etScan = findViewById(R.id.et_scan);
        btnScan = findViewById(R.id.btn_scan);
        btnClear = findViewById(R.id.btn_clear);
    }

    private BroadcastReceiver scanDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SCN_CUST_ACTION_SCODE)){
                try {
                    String message = "";
                    message = intent.getStringExtra(SCN_CUST_EX_SCODE).toString();
                    arrayAdapter.add(message);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("in",e.toString());
                }


            }
        }
    };


    /*对于通过api控制扫描使能是否有效*/
    public void onPowerClick(View view) {
        final boolean isChecked = powerSwitch.isChecked();
        if (isChecked) {
            sendBroadcast(new Intent("com.android.server.scannerservice.onoff")
                    .putExtra("scanneronoff", 1));

        } else {
            sendBroadcast(new Intent("com.android.server.scannerservice.onoff")
                    .putExtra("scanneronoff", 0));
        }
    }

   /* public void onPauseClick(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            sendBroadcast(new Intent("ACTION_BAR_SCANCFG")
                    .putExtra("EXTRA_SCAN_SUSPEND", 0));

        } else {
            sendBroadcast(new Intent("ACTION_BAR_SCANCFG")
                    .putExtra("EXTRA_SCAN_SUSPEND", 1));
        }
    }*/
}
