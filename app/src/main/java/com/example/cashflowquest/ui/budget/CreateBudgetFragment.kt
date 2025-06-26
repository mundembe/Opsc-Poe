package com.example.cashflowquest.ui.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cashflowquest.databinding.FragmentCreateBudgetBinding
import com.example.cashflowquest.ui.budget.BudgetViewModel


@AndroidEntryPoint
class CreateBudgetFragment : Fragment() {
    private var _binding: FragmentCreateBudgetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BudgetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            val category = binding.etCategory.text.toString()
            val limit = binding.etLimit.text.toString().toDoubleOrNull() ?: 0.0

            if (category.isBlank()) {
                binding.etCategory.error = "Category cannot be empty"
                return@setOnClickListener
            }

            if (limit <= 0) {
                binding.etLimit.error = "Limit must be positive"
                return@setOnClickListener
            }

            viewModel.createBudget(category, limit)
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}