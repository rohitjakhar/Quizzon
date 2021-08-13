package com.rohit.quizzon.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.android.billingclient.api.SkuDetails
import com.rohit.quizzon.ui.viewholder.InappPurchaseViewHolder

class InappPurchaseAdapter(private val onButClicked: (SkuDetails) -> Unit) :
    ListAdapter<SkuDetails, InappPurchaseViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InappPurchaseViewHolder {
        return InappPurchaseViewHolder.create(parent, onButClicked)
    }

    override fun onBindViewHolder(holder: InappPurchaseViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<SkuDetails>() {
            override fun areContentsTheSame(
                oldItem: SkuDetails,
                newItem: SkuDetails
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: SkuDetails,
                newItem: SkuDetails
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
