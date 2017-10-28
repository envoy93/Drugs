package com.shashov.drugs.features.drugs.presentation.adapters;

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shashov.drugs.R
import com.shashov.drugs.features.drugs.data.local.Drug
import com.shashov.drugs.features.drugs.data.local.Drugs
import com.shashov.drugs.features.drugs.data.local.ISearchItem
import com.shashov.drugs.features.drugs.data.local.Substance
import com.shashov.drugs.features.drugs.presentation.ColorGenerator
import kotlinx.android.synthetic.main.drugs_searchlist.view.*
import kotlinx.android.synthetic.main.substance_searchlist.view.*

class SearchListAdapter(val drugs: List<ISearchItem>, var onClick: (text: String?) -> Unit) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                if (viewType == 1)
                    R.layout.drugs_searchlist
                else
                    R.layout.substance_searchlist,
                parent, false)
        return ViewHolder(v, onClick)
    }

    override fun getItemCount(): Int {
        return drugs.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (drugs[position] is Drug) 1 else 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == 1)
            holder.bindDrugs(drugs[position] as Drug)
        else
            holder.bindSubstance(drugs[position] as Substance)
    }

    class ViewHolder(itemView: View, var onClick: (text: String?) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindDrugs(drugs: Drug) {
            with(drugs) {
                itemView.drugsName.text = name
                itemView.drugsSubstance.text = substance
                itemView.drugsImageView.letter = name?.first().toString()
                itemView.drugsImageView.shapeColor = ColorGenerator.getRandomColor().toInt()
                itemView.drugsItem.setOnClickListener({ _ ->
                    onClick(substance)
                })
            }
        }

        fun bindSubstance(drugs: Substance) {
            with(drugs) {
                itemView.substanceName.text = substance
                itemView.substanceImageView.letter = substance?.first().toString()
                itemView.substanceImageView.shapeColor = ColorGenerator.getRandomColor().toInt()
                itemView.substanceItem.setOnClickListener({ _ ->
                    onClick(substance)
                })
            }
        }
    }
}
