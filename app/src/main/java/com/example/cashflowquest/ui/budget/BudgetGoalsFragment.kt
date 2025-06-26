package com.example.cashflowquest.ui.budget

import BudgetAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cashflowquest.R
import com.example.cashflowquest.databinding.FragmentBudgetGoalsBinding
import com.example.cashflowquest.ui.budget.BudgetViewModel
import com.google.android.material.snackbar.Snackbar
//import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BudgetGoalsFragment : Fragment() {

    private var _binding: FragmentBudgetGoalsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BudgetViewModel by viewModels()
    private lateinit var budgetAdapter: BudgetAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.addTestData()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        budgetAdapter = BudgetAdapter(
            onBudgetClick = { budgetProgress ->
                navigateToBudgetDetail(budgetProgress.id)
            },
            onBudgetLongClick = { budgetProgress ->
                showDeleteBudgetDialog(budgetProgress.id)
            }
        )

        binding.recyclerViewBudgets.apply {
            adapter = budgetAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                ).apply {
                    ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
                        setDrawable(it)
                    }
                }
            )
        }
    }

    private fun setupObservers() {
        // Observe budget progress
        viewModel.budgetProgress.observe(viewLifecycleOwner) { budgets ->
            budgetAdapter.submitList(budgets)
            binding.emptyState.visibility = if (budgets.isEmpty()) View.VISIBLE else View.GONE
        }

        // Observe snackbar messages (using LiveData instead of collectLatestLifecycleFlow)
        viewModel.snackbarMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                showSnackbar(it)
                viewModel.clearSnackbar()
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabAddBudget.setOnClickListener {
            // Ensure this uses the correct action ID from your nav graph
            findNavController().navigate(
                R.id.action_budgetGoalsFragment_to_createBudgetFragment
            )
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshBudgets()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun navigateToBudgetDetail(budgetId: Int) {
        findNavController().navigate(
            R.id.budgetDetailFragment,
            Bundle().apply { putInt("budgetId", budgetId) }
        )
    }

    private fun navigateToCreateBudget() {
        findNavController().navigate(R.id.createBudgetFragment)
    }


    private fun showDeleteBudgetDialog(budgetId: Int) {
        context?.let {
            androidx.appcompat.app.AlertDialog.Builder(it)
                .setTitle("Delete Budget")
                .setMessage("Are you sure you want to delete this budget?")
                .setPositiveButton("Delete") { _, _ ->
                    viewModel.deleteBudget(budgetId)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

annotation class AndroidEntryPoint
