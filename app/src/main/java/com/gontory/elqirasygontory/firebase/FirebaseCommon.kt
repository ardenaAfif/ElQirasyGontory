package com.gontory.elqirasygontory.firebase

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FirebaseCommon {

    private val fireStore = FirebaseFirestore.getInstance()

    suspend fun getQuizQuestions(quizId: String): QuerySnapshot? {
        return fireStore.collection("mutholaah")
            .document(quizId)
            .collection("quiz")
            .get().await()
    }

    suspend fun submitQuizResult(
        quizId: String,
        result: HashMap<String, Any?>
    ): Void? {
//        fireStore.collection("mutholaah").document(quizId)

        fireStore.collection("mutholaah").document(quizId)
            .update("taken", FieldValue.increment(1)).await()

        return fireStore.collection("mutholaah").document(quizId)
            .collection("results").document().set(result).await()
    }
}