package com.rohit.quizzon.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.SkuDetails
import com.rohit.quizzon.databinding.ItemInappPurchaseBinding

class InappPurchaseViewHolder(
    private val binding: ItemInappPurchaseBinding,
    private val onButButtonClicked: (SkuDetails) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(inappPurchaseModel: SkuDetails) = binding.apply {
        tvItemText.text = inappPurchaseModel.title
        btnBuy.setOnClickListener {
            onButButtonClicked(inappPurchaseModel)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onButClicked: (SkuDetails) -> Unit): InappPurchaseViewHolder {
            val mView = LayoutInflater.from(parent.context)
            val binding = ItemInappPurchaseBinding.inflate(mView, parent, false)
            return InappPurchaseViewHolder(binding = binding, onButClicked)
        }
    }
}
