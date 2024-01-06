package com.gontory.elqirasygontory.ui.about

import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gontory.elqirasygontory.R
import com.gontory.elqirasygontory.databinding.FragmentAboutBinding
import com.gontory.elqirasygontory.ui.rate.RateUsDialog

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            toolbar.tvToolbarName.text = getString(R.string.about)
            toolbar.navBack.setOnClickListener {
                findNavController().navigateUp()
            }

            val versionName = getVersionName()
            version.text = "Version $versionName"

            btnRate.setOnClickListener {
                showRatingDialog()
            }
        }
    }

    private fun showRatingDialog() {
        val rateUsDialog = RateUsDialog(requireContext())
        rateUsDialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparant)))
        rateUsDialog.setCancelable(false)
        rateUsDialog.show()
    }

    private fun getVersionName(): String {
        try {
            val packageInfo =
                requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "Unknown"
    }

}