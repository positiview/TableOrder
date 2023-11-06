package com.example.mytableorder.repository

import com.example.mytableorder.model.User
import com.example.mytableorder.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

import javax.inject.Inject

class AuthRepositoryImpl : AuthRepository {


    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth

    override suspend fun login(
        email: String,
        password: String,
        result: (Resource<String>) -> Unit
    ) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currentUser = auth.currentUser
                if(currentUser!!.isEmailVerified){
                    db.collection("users")
                        .document(currentUser.uid)
                        .get()
                        .addOnSuccessListener {
                            val userType = it.get("user_type") as String

                            when (userType) {
                                "Admin" -> {
                                    result.invoke(Resource.Success("admin"))
                                }

                                else -> {
                                    result.invoke(Resource.Success("user"))
                                }
                            }
                        }
                }else{
                    result.invoke(Resource.Error("Email not verified"))
                }
            }.addOnFailureListener {
                result.invoke(Resource.Error(it.message.toString()))
            }

    }

    override suspend fun register(
        email: String,
        password: String,
        user: User,
        result: (Resource<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                auth.currentUser?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        db.collection("users")
                            .document(auth.uid.toString())
                            .set(user)
                            .addOnSuccessListener {
                                result.invoke(Resource.Success("Account Created Successfully\n Check your email for verification Link"))
                            }
                    }
            }
            .addOnFailureListener {
                result.invoke(Resource.Error(it.message.toString()))
            }
    }

    override suspend fun logout(result: () -> Unit) {
        auth.signOut()
        result.invoke()
    }
}