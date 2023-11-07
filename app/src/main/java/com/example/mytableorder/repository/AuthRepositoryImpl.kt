package com.example.mytableorder.repository

import android.net.Uri
import android.util.Log
import com.example.mytableorder.Db.db
import com.example.mytableorder.Db.storage
import com.example.mytableorder.model.User
import com.example.mytableorder.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileInputStream

import javax.inject.Inject

class AuthRepositoryImpl : AuthRepository {


//    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth
    private val storageRef: StorageReference = storage.reference

    private val uid = auth.currentUser?.uid
    override suspend fun login(
        email: String,
        password: String,
        result: (Resource<Map<String, Any>?>) -> Unit
    ) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currentUser = auth.currentUser
                if(currentUser!!.isEmailVerified){
                    db.collection("users")
                        .document(currentUser.uid)
                        .get()
                        .addOnSuccessListener {
                            val user = it.data
//                            val userType = it.get("user_type") as String
                            result.invoke(Resource.Success(user))

                        }
                }else{
                    result.invoke(Resource.Error("Email not verified"))
                }
            }.addOnFailureListener {
                result.invoke(Resource.Error(it.message.toString()))
            }

    }

    override suspend fun getUserImage(result: (Resource<Uri>) -> Unit) {

        if(uid != null){
            val imgRef: StorageReference = storageRef.child("user/$uid")
            imgRef.downloadUrl.addOnSuccessListener {

                result.invoke(Resource.Success(it))
            }
        }else{
            result.invoke(Resource.Error("회원 가입을 먼저 해주세요"))

        }
    }

    override suspend fun setUserImage(imagePath: Uri, result: (Resource<Uri>) -> Unit) {

        if(uid != null){
            val imgRef: StorageReference = storageRef.child("user/$uid")
            val uploadTask = imgRef.putFile(imagePath)
            uploadTask.addOnFailureListener{
                Log.d("$$","파이어베이스 upload fail...")
            }.addOnSuccessListener {


                Log.d("$$","파이어베이스 upload success...")
            }
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imgRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    result.invoke(Resource.Success(downloadUri))
                    Log.d("$$","Firebase upload success, download Uri: $downloadUri")
                } else {
                    // Handle failures
                    // ...
                }
            }
        }else{
            result.invoke(Resource.Error("회원 가입을 먼저 해주세요"))

        }
    }

    override suspend fun getUserInfo(result: (Resource<Map<String, Any>?>) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener {
                    val user = it.data
    //                            val userType = it.get("user_type") as String
                    result.invoke(Resource.Success(user))

                }
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
                                result.invoke(Resource.Success("인증링크가 성공적으로 생성되었습니다.\n 이메일을 확인해주세요"))
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