package com.example.cashflowquest.ui.accounts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cashflowquest.R
import com.example.cashflowquest.data.model.Account

class AccountsAdapter(
    private val onClick: (Account) -> Unit
) : ListAdapter<Account, AccountsAdapter.AccountViewHolder>(AccountDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_account, parent, false)
        return AccountViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AccountViewHolder(
        itemView: View,
        private val onClick: (Account) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.accountName)
        private val balanceTextView: TextView = itemView.findViewById(R.id.accountBalance)

        fun bind(account: Account) {
            nameTextView.text = account.name
            balanceTextView.text = "$${String.format("%.2f", account.balance)}"

            // Set color based on balance
            val colorRes = if (account.balance >= 0) R.color.green_500 else R.color.red_500
            balanceTextView.setTextColor(ContextCompat.getColor(itemView.context, colorRes))

            itemView.setOnClickListener { onClick(account) }
        }
    }

    class AccountDiffCallback : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem == newItem
        }
    }
}