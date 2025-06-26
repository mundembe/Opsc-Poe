package com.example.cashflowquest.ui.challenges

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cashflowquest.databinding.ItemChallengeBinding

class ChallengesAdapter(
    private val challenges: List<Challenge>
) : RecyclerView.Adapter<ChallengesAdapter.ChallengeViewHolder>() {

    inner class ChallengeViewHolder(private val binding: ItemChallengeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(challenge: Challenge) {
            binding.textTitle.text = challenge.title
            binding.textDescription.text = challenge.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val binding = ItemChallengeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChallengeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bind(challenges[position])
    }

    override fun getItemCount() = challenges.size
}

