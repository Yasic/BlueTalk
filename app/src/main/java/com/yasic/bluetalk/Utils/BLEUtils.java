package com.yasic.bluetalk.Utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ESIR on 2016/1/3.
 */
public class BLEUtils{

    private static final long SCAN_TIMEOUT = 10000;

    /**
     * 空构造函数
     */
    private BLEUtils(){}

    public static BLEUtils getInstance(){
        return BLEUtilsHolder.sInstance;
    }

    private static class BLEUtilsHolder{
        private static final BLEUtils sInstance = new BLEUtils();
    }

    /**
     * 获取蓝牙适配器
     * @param context 传入上下文
     * @return 返回蓝牙适配器
     */
    public BluetoothAdapter getBluetoothAdapter(Context context){
        BluetoothManager bluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        return bluetoothManager.getAdapter();
    }

    public void scanBleDevice(boolean enable, Handler handler, final BluetoothAdapter bluetoothAdapter){
        if(enable){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            },SCAN_TIMEOUT);
            bluetoothAdapter.startLeScan(leScanCallback);
        }else {
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i("device", device+"");
        }
    };
}
