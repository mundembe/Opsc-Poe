package com.example.cashflowquest.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.cashflowquest.MainActivity
import com.example.cashflowquest.R
import com.example.cashflowquest.data.database.AppDatabase
import com.example.cashflowquest.data.model.Account
import com.example.cashflowquest.data.model.Transaction
import com.example.cashflowquest.ui.accounts.AccountsViewModel
import com.example.cashflowquest.ui.accounts.AccountsViewModelFactory
import com.example.cashflowquest.viewmodel.TransactionViewModel

class AddTransactionFragment : Fragment() {

    private lateinit var viewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_transaction, container, false)

        val database = AppDatabase.getInstance(requireContext())
        val transactionDao = database.transactionDao()
        val accountDao = database.accountDao()

        // Update the factory to include accountDao
        val transactionFactory = TransactionViewModelFactory(transactionDao, accountDao, requireActivity().application)
        viewModel = ViewModelProvider(this, transactionFactory)[TransactionViewModel::class.java]


        val application = requireActivity().application

        viewModel = ViewModelProvider(this, transactionFactory)[TransactionViewModel::class.java]

        val accountsFactory = AccountsViewModelFactory(accountDao, transactionDao)
        val accountsViewModel =
            ViewModelProvider(this, accountsFactory)[AccountsViewModel::class.java]

        val accountSpinner = view.findViewById<Spinner>(R.id.accountSpinner)
        val typeSpinner = view.findViewById<Spinner>(R.id.typeSpinner)
        val amountEdit = view.findViewById<EditText>(R.id.amountInput)
        val categoryEdit = view.findViewById<EditText>(R.id.categoryInput)
        val noteEdit = view.findViewById<EditText>(R.id.noteInput)
        val submitBtn = view.findViewById<Button>(R.id.submitTransactionBtn)

        val types = TransactionType.values().map { it.label }
        typeSpinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, types)

        // 👇 Observe account list to populate the spinner
        accountsViewModel.allAccounts.observe(viewLifecycleOwner) { accounts ->
            val adapter = object : ArrayAdapter<Account>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                accounts
            ) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    val account = getItem(position)
                    (view as TextView).text = "${account?.name} (${String.format("$%.2f", account?.balance ?: 0.0)})"

                    return view
                }

                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    (view as TextView).text = getItem(position)?.name ?: ""
                    return view
                }
            }
            accountSpinner.adapter = adapter

        }

        submitBtn.setOnClickListener {
            val amount = amountEdit.text.toString().toDoubleOrNull() ?: 0.0
            val category = categoryEdit.text.toString()
            val note = noteEdit.text.toString()
            val type = typeSpinner.selectedItem.toString()

            val selectedAccount = accountSpinner.selectedItem as? Account
            val selectedAccountId = selectedAccount?.id?.toLong() ?: -1L

            if (category.isBlank() || amount <= 0.0 || selectedAccountId == -1L) {
                Toast.makeText(requireContext(), "Please enter valid data", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val transaction = Transaction(
                accountId = selectedAccountId,
                amount = amount,
                type = type,
                category = category,
                note = note
            )

            viewModel.insert(transaction)

            Toast.makeText(context, "Transaction added!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        return view
    }
}
