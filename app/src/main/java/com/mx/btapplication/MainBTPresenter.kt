package com.mx.btapplication

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainBTPresenter(private val context: Context, private val view: MainBTView) {

    private val REQUEST_ENABLE_BT: Int = 100
    private val REQUEST_ENABLE_LOCATION: Int = 101
    private val SCAN_PERIOD: Long = 10000
    private val devicesList: MutableList<DeviceBT> = arrayListOf()

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return@lazy bluetoothManager.adapter
    }

    fun initBTAdapter() {

        if (bluetoothAdapter == null) {

            view.showErrorBTNotSupport()

        } else if (bluetoothAdapter?.isEnabled == false){

            view.requestPermissionBTEnable()

        } else if (!hasLocationPermission()) {

            requestForLocationPermissions()

        } else {
            view.showLoading()
            startBT()

        }

    }

    private fun hasLocationPermission(): Boolean {

        return ContextCompat.checkSelfPermission(
            view.getCurrentActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    }

    private fun requestForLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(view.getCurrentActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Request the permissions
            ActivityCompat.requestPermissions(view.getCurrentActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ENABLE_LOCATION)
        } else {
            // Request the permissions, the result will be received in onRequestPermissionResult
            ActivityCompat.requestPermissions(view.getCurrentActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ENABLE_LOCATION)
        }
    }

    fun requestPermissionResult(requestCode: Int, permissions: Array<out String>,
                                grantResults: IntArray) {

        when (requestCode) {

            REQUEST_ENABLE_LOCATION -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startBT()
                } else {
                    view.showError(context.getString(R.string.message_location_required)) { requestForLocationPermissions() }
                }

            }

        }

    }

    fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {

            REQUEST_ENABLE_BT -> {

                when (resultCode) {

                    Activity.RESULT_OK -> {

                        if (!hasLocationPermission()) {
                            requestForLocationPermissions()
                        } else {
                            startBT()
                        }

                    }

                    Activity.RESULT_CANCELED -> {
                        view.showError(context.getString(R.string.message_bt_required)) {view.requestPermissionBTEnable()}
                    }

                }

            }

        }
    }

    fun startBT() {

        val leScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, _ ->
            view.getCurrentActivity().runOnUiThread {
                val nameDevice: String = if (device.name.isNullOrEmpty()) "SN" else device.name
                devicesList.add(DeviceBT(nameDevice, device.address, rssi))
                view.initAdapter(devicesList)
            }
        }
        Handler().postDelayed({
            bluetoothAdapter?.stopLeScan(leScanCallback)
        }, SCAN_PERIOD)

        bluetoothAdapter?.startLeScan(leScanCallback)

    }

}