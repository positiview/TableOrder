package com.example.mytableorder

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

object Db {

    var firestore_database : FirebaseFirestore = FirebaseFirestore.getInstance()
    var realtime_database : FirebaseDatabase = FirebaseDatabase.getInstance()

    fun getInstance() : FirebaseFirestore{
        return firestore_database
    }

    fun getRealtimeInstance() :FirebaseDatabase{
        return realtime_database
    }
}