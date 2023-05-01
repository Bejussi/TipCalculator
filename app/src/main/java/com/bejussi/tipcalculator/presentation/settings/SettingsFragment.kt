package com.bejussi.tipcalculator.presentation.settings

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.billingclient.api.ProductDetails
import com.bejussi.tipcalculator.BuildConfig
import com.bejussi.tipcalculator.R
import com.bejussi.tipcalculator.databinding.FragmentSettingsBinding
import com.bejussi.tipcalculator.presentation.settings.billing.BillingActionListener
import com.bejussi.tipcalculator.presentation.settings.billing.BillingAdapter
import com.bejussi.tipcalculator.presentation.settings.billing.BillingViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val billingViewModel: BillingViewModel by viewModels()

    private lateinit var adapter: BillingAdapter

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

        setupRecyclerView()

        billingViewModel.productsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        billingViewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progress.isVisible = loading
            binding.donateChoose.isVisible = !loading
        }

        settingsViewModel.getTheme.observe(viewLifecycleOwner) { themeMode ->
            when (themeMode) {
                "light" -> {
                    selectedThemeIndex = 0
                }
                "dark" -> {
                    selectedThemeIndex = 1
                }
                "system" -> {
                    selectedThemeIndex = 2
                }
            }
        }

        settingsViewModel.getLanguage.observe(viewLifecycleOwner) { language ->
            when (language) {
                "en" -> {
                    selectedLanguageIndex = 0
                }
                "uk" -> {
                    selectedLanguageIndex = 1
                }
                "ru" -> {
                    selectedLanguageIndex = 2
                }
                "de" -> {
                    selectedLanguageIndex = 3
                }
                "fr" -> {
                    selectedLanguageIndex = 4
                }
                "es" -> {
                    selectedLanguageIndex = 5
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

    private fun setupRecyclerView() {
        adapter = BillingAdapter(object : BillingActionListener {
            override fun startBilling(productDetails: ProductDetails) {
                billingViewModel.launchBillingFlow(requireActivity(), productDetails)
            }
        })
        binding.donateChoose.adapter = adapter
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
                    settingsViewModel.setLanguage("uk")
                }
                2 -> {
                    settingsViewModel.setLanguage("ru")
                }
                3 -> {
                    settingsViewModel.setLanguage("de")
                }
                4 -> {
                    settingsViewModel.setLanguage("fr")
                }
                5 -> {
                    settingsViewModel.setLanguage("es")
                }
            }
        }
    }

    private fun setTheme(selectedThemeIndex: Int) {
        lifecycleScope.launch {
            when (selectedThemeIndex) {
                0 -> {
                    settingsViewModel.setTheme("light")
                }
                1 -> {
                    settingsViewModel.setTheme("dark")
                }
                2 -> {
                    settingsViewModel.setTheme("system")
                }
            }
        }
    }
}
