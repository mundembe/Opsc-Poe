package com.example.cashflowquest

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cashflowquest.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // ✅ only this one

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
            return
        }

        Handler(Looper.getMainLooper()).post {
            val navController = findNavController(R.id.nav_host_fragment)
            binding.bottomNav.setupWithNavController(navController) // ✅ use binding
        }
    }

    fun setSelectedTab(position: Int) {
        binding.bottomNav.selectedItemId = when (position) {
            0 -> R.id.accountsFragment
            1 -> R.id.budgetGoalsFragment
            2 -> R.id.challengesFragment
            else -> R.id.accountsFragment
        }
    }
}
