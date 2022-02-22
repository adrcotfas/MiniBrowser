package com.adrcotfas.minibrowser.ui.history

import androidx.recyclerview.widget.RecyclerView
import com.adrcotfas.minibrowser.ui.history.HistoryAdapter.HistoryViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.adrcotfas.minibrowser.R
import android.widget.LinearLayout
import android.widget.TextView
import java.util.ArrayList

class HistoryAdapter internal constructor(private val listener: Listener) :
    RecyclerView.Adapter<HistoryViewHolder>() {
    internal interface Listener {
        fun onClick(url: String)
    }

    private var data: List<String> = ArrayList()
    fun setData(data: List<String>) {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(data[position])
        holder.root.setOnClickListener { listener.onClick(data[position]) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root: LinearLayout = itemView.findViewById(R.id.root)
        private val initial: TextView = itemView.findViewById(R.id.initial)
        private val url: TextView = itemView.findViewById(R.id.url)
        fun bind(data: String) {
            initial.text = data[0].toString()
            url.text = data
        }

    }
}