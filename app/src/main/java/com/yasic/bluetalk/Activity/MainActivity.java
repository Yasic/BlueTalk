package com.yasic.bluetalk.Activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.yasic.bluetalk.R;
import com.yasic.bluetalk.Utils.BLEUtils;

import org.apache.http.conn.scheme.HostNameResolver;

public class MainActivity extends AppCompatActivity {

    /**
     * 蓝牙工具实例
     */
    private BLEUtils bleUtils = BLEUtils.getInstance();

    /**
     * 开启蓝牙扫描的开关
     */
    private Button btnBLEScan;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToobar();
        setFAB();
        final BluetoothAdapter bluetoothAdapter = bleUtils.getBluetoothAdapter(this);
        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled()){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("检测到蓝牙尚未开启，是否现在开启");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    bluetoothAdapter.enable();
                }
            });
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "BlueTalk已停止", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            builder.create().show();//显示开启蓝牙对话框
        }
        btnBLEScan = (Button)findViewById(R.id.btn_blescan);
        btnBLEScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleUtils.scanBleDevice(true, handler, bluetoothAdapter);
            }
        });
    }

    /**
     * 设置toolbar
     */
    private void setToobar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * 设置FAB
     */
    private void setFAB(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
