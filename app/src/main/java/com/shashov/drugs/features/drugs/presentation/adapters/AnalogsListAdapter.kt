package com.shashov.drugs.features.drugs.presentation.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shashov.drugs.R
import com.shashov.drugs.features.drugs.data.local.Drugs
import com.shashov.drugs.features.drugs.data.local.ISearchItem
import com.shashov.drugs.features.drugs.presentation.ColorGenerator
import kotlinx.android.synthetic.main.analogs_searchlist.view.*

class AnalogsListAdapter(val drugs: List<Drugs>) : RecyclerView.Adapter<AnalogsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalogsListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.analogs_searchlist, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return drugs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindDrugs(drugs[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindDrugs(drugs: Drugs) {
            with(drugs) {
                itemView.analogName.text = name
                itemView.analogPrice.text = "${price.toString()}р"
                itemView.analogCompany.text = company
                itemView.analogForm.text = form
                itemView.analogCount.text = "${count}шт"
            }
        }
    }
}