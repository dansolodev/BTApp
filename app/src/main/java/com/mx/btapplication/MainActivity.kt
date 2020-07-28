package com.mx.btapplication

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainBTView {

    private val REQUEST_ENABLE_BT: Int = 100

    private val presenter: MainBTPresenter by lazy { MainBTPresenter(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.initBTAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.bt_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.option_refresh -> {
                showLoading()
                presenter.startBT()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        presenter.requestPermissionResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.activityResult(requestCode, resultCode, data)
    }

    // Methods MainBTView
    override fun getCurrentActivity(): Activity = this

    override fun showErrorBTNotSupport() {
        showAlertDialog(message = getString(R.string.message_bt_not_supported), action =  {finish()})
    }

    override fun showError(message: String, function: () -> Unit) {
        showAlertDialog(message = message, action =  { function() })
    }

    override fun requestPermissionBTEnable() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    override fun showLoading() {
        tv_loading.visibility = View.VISIBLE
        rv_bt_devices.visibility = View.GONE
    }

    override fun initAdapter(elements: MutableList<DeviceBT>?) {
        elements?.let { listWrapped ->

            tv_loading.visibility = View.GONE
            rv_bt_devices.visibility = View.VISIBLE
            rv_bt_devices.layoutManager = LinearLayoutManager(this)
            rv_bt_devices.adapter = BTAdapter(listWrapped)

        }
    }

}