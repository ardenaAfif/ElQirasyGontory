package com.gontory.elqirasygontory.ui.rate

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat.getSystemService
import com.gontory.elqirasygontory.R
import com.gontory.elqirasygontory.data.Rating
import com.google.firebase.firestore.FirebaseFirestore


class RateUsDialog(context: Context) : AppCompatDialog(context) {

    // Declare variables
    private var ratingEmo: ImageView
    private var ratingBar: RatingBar
    private var message: EditText
    private var btnSubmit: Button
    private var btnClose: ImageView
    private var progressBar: ProgressBar

    init {

        // Initialize views
        setContentView(R.layout.rate_us_dialog)
        ratingEmo = findViewById(R.id.ivEmoRating)!!
        ratingBar = findViewById(R.id.ratingBar)!!
        message = findViewById(R.id.etMessage)!!
        btnSubmit = findViewById(R.id.btnSubmit)!!
        btnClose = findViewById(R.id.btnClose)!!
        progressBar = findViewById(R.id.progressBar)!!

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            updateEmojiRating(rating)
        }

        btnClose.setOnClickListener {
            dismiss()
        }

        btnSubmit.setOnClickListener {
            submitFeedback()
        }

        // Play animation
        playAnim(ratingEmo)
    }

    private fun submitFeedback() {
        val userRating = ratingBar.rating.toLong()
        val message = message.text.toString()

        // Cek rating dan message tidak kosong
        if (userRating > 0 && message.isNotEmpty()) {
            val feedback = Rating(userRating, message)

            // Akses collection Firestore
            val db = FirebaseFirestore.getInstance()

            btnSubmit.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            db.collection("feedback")
                .add(feedback)
                .addOnSuccessListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Feedback sent! Thank you", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                .addOnFailureListener {
                    progressBar.visibility = View.GONE
                    btnSubmit.visibility = View.VISIBLE
                    Toast.makeText(context, "Failed to sent feedback, check your connection!", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Please fill a feedback completely", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playAnim(ratingEmo: ImageView) {
        val scaleAnim = ScaleAnimation(
            0f,
            1f,
            0f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )

        scaleAnim.fillAfter = true
        scaleAnim.duration = 200
        ratingEmo.startAnimation(scaleAnim)
    }

    private fun updateEmojiRating(rating: Float) {
        when {
            rating <= 1 -> ratingEmo.setImageResource(R.drawable.rate_one)
            rating <= 2 -> ratingEmo.setImageResource(R.drawable.rate_two)
            rating <= 3 -> ratingEmo.setImageResource(R.drawable.rate_three)
            rating <= 4 -> ratingEmo.setImageResource(R.drawable.rate_four)
            else -> ratingEmo.setImageResource(R.drawable.rate_five)
        }
    }
}