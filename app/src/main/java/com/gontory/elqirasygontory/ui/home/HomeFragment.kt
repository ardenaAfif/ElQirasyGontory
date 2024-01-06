package com.gontory.elqirasygontory.ui.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gontory.elqirasygontory.R
import com.gontory.elqirasygontory.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playAnim()

        binding.apply {
            muthoalaah.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_mutholaahFragment)
            }

            ikhtibar.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_ikhtibarFragment)
//                Toast.makeText(requireContext(), "Fitur ini akan segera hadir!", Toast.LENGTH_SHORT).show()
            }

            about.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_aboutFragment)
//                Toast.makeText(requireContext(), "Fitur ini akan segera hadir!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playAnim() {
        val scaleX = ObjectAnimator.ofFloat(binding.ivLogo, View.SCALE_X, 0f, 1f).setDuration(600)
        val scaleY = ObjectAnimator.ofFloat(binding.ivLogo, View.SCALE_Y, 0f, 1f).setDuration(600)

        val mutholaah = ObjectAnimator.ofFloat(binding.muthoalaah, View.ALPHA, 1f).setDuration(300)
        val ikhtibar = ObjectAnimator.ofFloat(binding.ikhtibar, View.ALPHA, 1f).setDuration(300)
        val about = ObjectAnimator.ofFloat(binding.about, View.ALPHA, 1f).setDuration(300)

        val logoAnim = AnimatorSet().apply {
            playTogether(scaleX, scaleY)
        }

        AnimatorSet().apply {
            playSequentially(logoAnim, mutholaah, ikhtibar, about)
            start()
        }

        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -70f, 80f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}