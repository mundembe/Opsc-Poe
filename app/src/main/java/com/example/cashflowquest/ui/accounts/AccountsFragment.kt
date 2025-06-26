package com.example.cashflowquest.ui.accounts

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashflowquest.R
import com.example.cashflowquest.data.database.AppDatabase
import com.example.cashflowquest.data.model.Account
import com.example.cashflowquest.databinding.FragmentAccountsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AccountsFragment : Fragment() {
    private lateinit var viewModel: AccountsViewModel
    private lateinit var adapter: AccountsAdapter
    private lateinit var binding: FragmentAccountsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getInstance(requireContext())
        val viewModelFactory = AccountsViewModelFactory(
            database.accountDao(),
            database.transactionDao()
        )

        viewModel = ViewModelProvider(this, viewModelFactory)[AccountsViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        adapter = AccountsAdapter { account ->
            // Handle account click
            Toast.makeText(requireContext(), "${account.name}: $${account.balance}", Toast.LENGTH_SHORT).show()
        }
        binding.accountsRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        binding.accountsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AccountsFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun setupObservers() {
        viewModel.allAccounts.observe(viewLifecycleOwner) { accounts ->
            adapter.submitList(accounts)
            updateTotalBalance(accounts)
        }
    }

    private fun updateTotalBalance(accounts: List<Account>) {
        val totalBalance = accounts.sumOf { it.balance }
        binding.totalBalanceTextView.text = "Total Balance: $${String.format("%.2f", totalBalance)}"
    }

    private fun setupClickListeners() {
        binding.addAccountButton.setOnClickListener {
            showAddAccountDialog()
        }

        binding.fabAddTransaction.setOnClickListener {
            navigateToAddTransaction()
        }
    }

    private fun showAddAccountDialog() {
        AddAccountDialog { account ->
            viewModel.insert(account)
        }.show(parentFragmentManager, "AddAccountDialog")
    }

    private fun navigateToAddTransaction() {
        findNavController().navigate(R.id.action_accountsFragment_to_addTransactionFragment)
    }
}