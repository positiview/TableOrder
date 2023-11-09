package com.example.mytableorder.repository

import android.net.Uri
import android.util.Log
import com.example.mytableorder.Db.db
import com.example.mytableorder.Db.storage
import com.example.mytableorder.model.UserDTO
import com.example.mytableorder.model.UpdateUser
import com.example.mytableorder.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class AuthRepositoryImpl : AuthRepository {



    val auth: FirebaseAuth = Firebase.auth
    private val storageRef: StorageReference = storage.reference

    val user = Firebase.auth.currentUser
    override suspend fun login(
        email: String,
        password: String,
        result: (Resource<Map<String, Any>?>) -> Unit
    ) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currentUser = auth.currentUser
                if(currentUser!!.isEmailVerified){
                    Log.d("$$", "버그 찾기 uid : ${auth.currentUser?.uid}")
                    db.collection("users")
                        .document(currentUser.uid)
                        .get()
                        .addOnSuccessListener {
                            val userInfo = it.data
                            result.invoke(Resource.Success(userInfo))
                            Log.d("$$","로그인 성공!!")
//                            getUserImg()
                        }
                        .addOnFailureListener { exception ->
                            result.invoke(Resource.Error("사용자 정보를 가져오는 중에 오류가 발생했습니다: ${exception.message}"))
                        }
                }else{
                    result.invoke(Resource.Error("이메일 인증을 먼저해주세요"))
                }
            }.addOnFailureListener {
                result.invoke(Resource.Error("회원 정보를 찾지 못했습니다."))
            }

    }
    override suspend fun getUserInfo(result: (Resource<Map<String, Any>?>) -> Unit) {
        val currentUser = auth.currentUser
        Log.d("$$","get User Info check: $currentUser")
        if (currentUser != null) {
            db.collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener {
                    val user = it.data

                    result.invoke(Resource.Success(user))

                }
        }else{
            result.invoke(Resource.Error("TableOrder에 오신것을 환영합니다."))
        }
    }
    override suspend fun setUserInfo(user: UpdateUser, result: (Resource<Map<String, Any>?>) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users")
                .document(uid)
                .set(user, SetOptions.merge())
                .addOnSuccessListener {
                    getUser(result)
                }.addOnFailureListener {
                    result.invoke(Resource.Error("사용자 정보 업데이트에 실패했습니다."))
                }
        }else{
            result.invoke(Resource.Error("회원정보를 찾을 수가 없습니다."))
        }
    }
    private fun getUser(result: (Resource<Map<String, Any>?>) -> Unit) {
        val uid = auth.currentUser?.uid
        Log.d("$$","currentUser uid : $uid")
        if(uid != null) {
            db.collection("users")
                .document(uid!!)
                .get()
                .addOnSuccessListener {
                    val userInfo = it.data
                    result.invoke(Resource.Success(userInfo))
                }
                .addOnFailureListener { exception ->
                    result.invoke(Resource.Error("사용자 정보를 가져오는 중에 오류가 발생했습니다: ${exception.message}"))
                }
        }else{
            result.invoke(Resource.Error("회원 정보를 찾을 수가 없습니다."))
        }
    }
    override suspend fun getUserImage(result: (Resource<Uri>) -> Unit) {
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        Log.d("$$","uid 여기.. : $uid")
        if(uid != null){
            val imgRef: StorageReference = storageRef.child("user/$uid")
            imgRef.downloadUrl.addOnSuccessListener {
                Log.d("$$","check downloadUrl : $it")
                result.invoke(Resource.Success(it))
            }
        }else{
            result.invoke(Resource.Error("회원 이미지를 찾올 수 없습니다"))

        }
    }

   /* private fun getUserImg() : Uri {
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        var uri: Uri = null
        Log.d("$$","uid 여기.. : $uid")
        if(uid != null){
            val imgRef: StorageReference = storageRef.child("user/$uid")
            imgRef.downloadUrl.addOnSuccessListener {
                Log.d("$$","check downloadUrl : $it")
                uri = it
            }
        }else{


        }
        return uri
    }*/
    override suspend fun setUserImage(imagePath: Uri, result: (Resource<Uri>) -> Unit) {
        val uid = auth.currentUser?.uid
        if(uid != null){
            Log.d("$$", "uid : $uid")
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
            result.invoke(Resource.Error("회원 정보를 찾을 수 없습니다"))

        }
    }
    override suspend fun deleteUserImage(result: (Resource<Uri?>) -> Unit) {
        val uid = auth.currentUser?.uid
        if(uid != null){
            val desertRef : StorageReference = storageRef.child("user/$uid")

            desertRef.delete().addOnSuccessListener{
                val uri: Uri? = null
                result.invoke(Resource.Success(uri))
            }.addOnFailureListener {
                result.invoke(Resource.Error("이미지 삭제에 실패했습니다."))
            }
        }else{
            result.invoke(Resource.Error("회원정보를 찾을 수가 없습니다."))
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        userDTO: UserDTO,
        result: (Resource<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                auth.currentUser?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        db.collection("users")
                            .document(auth.uid.toString())
                            .set(userDTO)
                            .addOnSuccessListener {
                                result.invoke(Resource.Success("인증링크가 성공적으로 생성되었습니다.\n 이메일을 확인해주세요"))
                            }
                    }
            }
            .addOnFailureListener {
                result.invoke(Resource.Error(it.message.toString()))
            }
    }





    override suspend fun updateAuthEmail(email: String) {
        TODO("Not yet implemented")
    }
}