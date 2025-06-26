package com.example.cashflowquest.ui.accounts

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.cashflowquest.R
import com.example.cashflowquest.data.model.Account

class AddAccountDialog(
    private val onAccountAdded: (Account) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_account, null)
        val nameInput = view.findViewById<EditText>(R.id.accountNameInput)
        val balanceInput = view.findViewById<EditText>(R.id.accountBalanceInput)

        return AlertDialog.Builder(requireContext())
            .setTitle("Add Account")
            .setView(view)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString()
                val balance = balanceInput.text.toString().toDoubleOrNull() ?: 0.0
                if (name.isNotBlank()) {
                    onAccountAdded(Account(name = name, balance = balance))
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}
