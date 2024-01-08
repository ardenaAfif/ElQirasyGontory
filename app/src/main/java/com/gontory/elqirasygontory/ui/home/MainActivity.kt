package com.gontory.elqirasygontory.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.gontory.elqirasygontory.R
import com.gontory.elqirasygontory.data.Mutholaah
import com.gontory.elqirasygontory.data.Question
import com.gontory.elqirasygontory.ui.quiz.IkhtibarFragmentDirections

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent?.let { handleIntent(it) }
    }

    private fun handleIntent(intent: Intent) {
        val navigateTo = intent.getStringExtra("EXTRA_NAVIGATE_TO")
        if (navigateTo == "QuizFragment") {
            val mutholaahData = intent.getParcelableExtra("EXTRA_MUTHOLAAH_DATA") as? Mutholaah
            mutholaahData.let { data ->
                val action = data?.let {
                    IkhtibarFragmentDirections.actionIkhtibarFragmentToQuizFragment(
                        it
                    )
                }
                if (action != null) {
                    findNavController(R.id.main_nav).navigate(action)
                }
            }
        }
    }
}
