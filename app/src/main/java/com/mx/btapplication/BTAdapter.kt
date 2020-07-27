package com.mx.btapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_bt.view.*

class BTAdapter(private val list: MutableList<DeviceBT>): RecyclerView.Adapter<BTAdapter.BTViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BTViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        return BTViewHolder(layoutInflater.inflate(R.layout.item_bt, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BTViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class BTViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        fun bind(deviceBT: DeviceBT) {
           with(view) {
               tv_item_name.text = deviceBT.name
               tv_item_address.text = deviceBT.mac
               tv_item_rssi.text = deviceBT.rssi.toString()
           }
        }

    }

}