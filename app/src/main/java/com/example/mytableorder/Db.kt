package com.example.mytableorder

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

object Db {

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    var rtdb : FirebaseDatabase = FirebaseDatabase.getInstance()

    fun getInstance() : FirebaseFirestore{
        return db
    }

    fun getRtDbInstance() :FirebaseDatabase{
        return rtdb
    }
}