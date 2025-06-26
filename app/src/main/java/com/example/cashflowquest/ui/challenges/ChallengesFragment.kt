package com.example.cashflowquest.ui.challenges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cashflowquest.databinding.FragmentChallengesBinding

class ChallengesFragment : Fragment() {

    private var _binding: FragmentChallengesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChallengesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ChallengesAdapter(getMockChallenges())
        binding.recyclerChallenges.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerChallenges.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getMockChallenges(): List<Challenge> {
        return listOf(
            Challenge("No Spend Weekend", "Avoid spending money this weekend."),
            Challenge("Cook at Home", "Prepare all meals at home for 7 days."),
            Challenge("Track Every Expense", "Log every purchase for a full week."),
            Challenge("Cut One Subscription", "Cancel one unused subscription service.")
        )
    }
}
