package com.example.mytableorder

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object Db {

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    var reatimedb : FirebaseDatabase = FirebaseDatabase.getInstance()
    var storage : FirebaseStorage = FirebaseStorage.getInstance()

    fun getInstance() : FirebaseFirestore{
        return db
    }

    fun getRealtimeInstance() :FirebaseDatabase{
        return reatimedb
    }

    fun getRef() : StorageReference{
        return storage.reference
    }


}