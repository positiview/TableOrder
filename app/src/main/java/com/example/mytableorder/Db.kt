package com.example.mytableorder

import com.google.firebase.firestore.FirebaseFirestore

object Db {

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getInstance() : FirebaseFirestore{
        return db
    }
}