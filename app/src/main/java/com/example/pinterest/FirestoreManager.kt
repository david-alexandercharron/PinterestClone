package com.example.pinterest

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * FirestoreManager :
 *    We will use this class to manage Firestore.
 *    The structure of the messages is /UserCategories/UserUid/Categories/CategoryName
 **/
class FirestoreManager {

    companion object{

        /** Attributes **/
        private val db = Firebase.firestore
        const val tag = "asdf"

        /**
         * Save Categories to a User
         *
         * @param userUid       FirebaseAuth's current user uid
         * @param categories    Array of the categories selected from the user
         *
         */
        fun saveCategories(userUid: String?, categories: Array<Category>) {

            // If the userUid is null, an error has occurred
            if(userUid != null){

                // Add a new document with a generated ID
                var collection = db.collection("UserCategories")
                    .document(userUid)
                    .collection("Categories")

                for (category in categories){

                    // Save the selected categories to the user
                    val categories = hashMapOf(
                        "categoryNameId" to category.name
                    )

                    collection.add(categories)

                        .addOnSuccessListener { documentReference ->
                            Log.d(tag, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(tag, "Error adding document", e)
                        }
                }

            } else {
                // An error has occurred
                Log.w(tag, "An error occurred while attempting to save the category.")
            }
        }

        /**
         * Get Categories DocumentRef of a user
         *
         * @param userUid       FirebaseAuth's current user uid
         *
         */
        fun getCategoryDocumentRefByUserUid(userUid: String): Task<QuerySnapshot> {

            val docRef = db.collection("UserCategories")
                .document(userUid)
                .collection("Categories")

            return docRef.get()

        }


        /**
         * Clear Categories from a User
         *
         * @param userUid       FirebaseAuth's current user uid
         *
         */
        fun clearCategories(context: Context, userUid: String?) {

            // If the userUid is null, an error has occurred
            if(userUid != null){

                // Add a new document with a generated ID
                db.collection("UserCategories")
                    .document(userUid)
                    .delete()

                    .addOnSuccessListener { _ ->
                        Toast.makeText(context, "Successfully cleared saves", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { _ ->
                        Toast.makeText(context, "An error has occured attempting to clear the categories", Toast.LENGTH_SHORT).show()
                    }



            } else {
                // An error has occurred
                Toast.makeText(context, "An error occurred while attempting to clear the categories", Toast.LENGTH_SHORT).show()
            }
        }







    }
}