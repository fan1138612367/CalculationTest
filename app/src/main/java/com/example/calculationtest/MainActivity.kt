package com.example.calculationtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.calculationtest.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val navController by lazy { binding.fragmentContainerView.getFragment<NavHostFragment>().navController }
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        onBackPressedDispatcher.addCallback(this) {
            if (navController.currentDestination?.id == R.id.titleFragment) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.toast_quit),
                    Toast.LENGTH_SHORT
                ).show()
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.toast_quit),
                    Toast.LENGTH_SHORT
                ).show()
                isEnabled = false
                lifecycleScope.launch {
                    delay(1500)
                    isEnabled = true
                }
            } else {
                onSupportNavigateUp()
            }
        }
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (navController.currentDestination?.id == R.id.questionFragment) {
            MaterialAlertDialogBuilder(this@MainActivity)
                .setTitle(R.string.dialog_quit_title)
                .setPositiveButton(R.string.dialog_positive_message) { _, _ ->
                    navController.navigateUp()
                }
                .setNegativeButton(R.string.dialog_negative_message) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        } else {
            navController.navigateUp()
        }
        return super.onSupportNavigateUp()
    }
}