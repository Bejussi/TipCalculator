package com.bejussi.tipcalculator.presentation.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bejussi.tipcalculator.BuildConfig
import com.bejussi.tipcalculator.R
import com.bejussi.tipcalculator.core.makeToast
import com.bejussi.tipcalculator.databinding.FragmentSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModels()

    private var selectedThemeIndex = 0
    private var selectedLanguageIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.getTheme.observe(viewLifecycleOwner) { isDarkMode ->
            when (isDarkMode) {
                true -> {
                    selectedThemeIndex = 1
                }
                false -> {
                    selectedThemeIndex = 0
                }
            }
        }

        settingsViewModel.getLanguage.observe(viewLifecycleOwner) { language ->
            when (language) {
                "en" -> {
                    selectedLanguageIndex = 0
                }
                "ru" -> {
                    selectedLanguageIndex = 1
                }
                "uk" -> {
                    selectedLanguageIndex = 2
                }
            }
        }

        binding.themeLayout.setOnClickListener {
            showThemeDialog()
        }

        binding.languageLayout.setOnClickListener {
            showLanguageDialog()
        }

        binding.rateLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                    "https://play.google.com/store/apps/details?id=com.bejussi.tipcalculator"
                )
                setPackage("com.android.vending")
            }
            startActivity(intent)
        }

        binding.contactLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
                .setData(Uri.parse("mailto:?subject=Tip Calculator&body=Feedback for Tip Calculator&to=bejussiapp@gmail.com"))
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }

        binding.privacyLayout.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragmentToWebFragment("privacy")
            findNavController().navigate(action)
        }

        binding.termsLayout.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragmentToWebFragment("terms")
            findNavController().navigate(action)
        }

        binding.version.text = BuildConfig.VERSION_NAME
    }

    private fun showThemeDialog() {
        val themes = resources.getStringArray(R.array.themes)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.select_theme))
            .setSingleChoiceItems(themes, selectedThemeIndex) { dialog_, which ->
                selectedThemeIndex = which
            }
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                setTheme(selectedThemeIndex)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showLanguageDialog() {
        val languages = resources.getStringArray(R.array.language)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.select_language))
            .setSingleChoiceItems(languages, selectedLanguageIndex) { dialog_, which ->
                selectedLanguageIndex = which
            }
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                setLanguage(selectedLanguageIndex)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setLanguage(selectedLanguageIndex: Int) {
        lifecycleScope.launch {
            when (selectedLanguageIndex) {
                0 -> {
                    settingsViewModel.setLanguage("en")
                }
                1 -> {
                    settingsViewModel.setLanguage("ru")
                }
                2 -> {
                    settingsViewModel.setLanguage("uk")
                }
            }
        }
    }

    private fun setTheme(selectedThemeIndex: Int) {
        lifecycleScope.launch {
            when (selectedThemeIndex) {
                0 -> {
                    settingsViewModel.setTheme(false)
                }
                1 -> {
                    settingsViewModel.setTheme(true)
                }
            }
        }
    }
}