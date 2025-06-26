import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cashflowquest.R
import com.example.cashflowquest.data.model.Budget
import com.example.cashflowquest.databinding.ItemBudgetBinding
import com.example.cashflowquest.ui.budget.BudgetProgress

class BudgetAdapter(
    private val onBudgetClick: (BudgetProgress) -> Unit,
    private val onBudgetLongClick: (BudgetProgress) -> Unit
) : ListAdapter<BudgetProgress, BudgetAdapter.BudgetViewHolder>(BudgetDiffCallback()) {

    // Initialize with empty list to prevent NPE
    init {
        submitList(emptyList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        Log.d("BudgetAdapter", "Creating new view holder")
        val binding = ItemBudgetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BudgetViewHolder(binding, onBudgetClick, onBudgetLongClick)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val item = getItem(position)
        Log.d("BudgetAdapter", "Binding item at position $position: ${item.category}")
        holder.bind(item)
    }

    override fun submitList(list: List<BudgetProgress>?) {
        Log.d("BudgetAdapter", "Submitting new list with ${list?.size ?: 0} items")
        super.submitList(list)
    }

    class BudgetViewHolder(
        private val binding: ItemBudgetBinding,
        private val onClick: (BudgetProgress) -> Unit,
        private val onLongClick: (BudgetProgress) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BudgetProgress) {
            with(binding) {
                // Set text values
                textCategory.text = item.category
                textRemaining.text = root.context.getString(
                    R.string.budget_remaining,
                    item.remaining
                )

                // Set progress bar
                progressBar.max = 100
                progressBar.progress = (item.progress * 100).toInt()

                // Set color based on budget status
                val colorRes = when {
                    item.isOverBudget -> R.color.budget_over
                    item.progress > 0.8f -> R.color.budget_warning
                    else -> R.color.budget_ok
                }
                progressBar.progressTintList = ContextCompat.getColorStateList(
                    root.context,
                    colorRes
                )

                // Set click listeners
                root.setOnClickListener { onClick(item) }
                root.setOnLongClickListener {
                    onLongClick(item)
                    true
                }
            }
        }
    }
}

class BudgetDiffCallback : DiffUtil.ItemCallback<BudgetProgress>() {
    override fun areItemsTheSame(oldItem: BudgetProgress, newItem: BudgetProgress): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BudgetProgress, newItem: BudgetProgress): Boolean {
        return oldItem == newItem
    }
}