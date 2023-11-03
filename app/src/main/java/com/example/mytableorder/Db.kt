package com.example.mytableorder

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

object Db {

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    var reatimedb : FirebaseDatabase = FirebaseDatabase.getInstance()

    fun getInstance() : FirebaseFirestore{
        return db
    }

    fun getRealtimeInstance() :FirebaseDatabase{
        return reatimedb
    }
}