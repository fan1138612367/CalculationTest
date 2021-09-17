package com.example.calculationtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        navController =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
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