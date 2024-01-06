package com.gontory.elqirasygontory.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gontory.elqirasygontory.R
import com.gontory.elqirasygontory.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var correct: Int = 0
    private lateinit var binding: FragmentResultBinding

    private val args: ResultFragmentArgs by navArgs()
    private val resultViewModel: ResultViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        resultViewModel.fetchQuizResult(args.QuizData)

        binding = FragmentResultBinding.inflate(inflater, container, false).apply {
            viewModel = resultViewModel
            lifecycleOwner = viewLifecycleOwner
            quizDetail = args.QuizData
        }

        val fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        val fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out)

        resultViewModel.correctScore.observe(viewLifecycleOwner, Observer {
            binding.resultsContent.startAnimation(fadeInAnimation)
//            binding.resultLoadProgress.startAnimation(fadeOutAnimation)

            binding.resultsScore.text = getString(R.string.score_over, it, args.QuizData.questions)

            correct = it
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = args.QuizData

        binding.apply {
            mutholaahText.text = result.title
            btnRetry.setOnClickListener {
                findNavController().navigate(
                    ResultFragmentDirections.actionResultFragmentToQuizFragment(
                        result
                    )
                )
            }
            btnHome.setOnClickListener {
                findNavController().navigate(ResultFragmentDirections.actionResultFragmentToHomeFragment())
            }
        }
    }

}