package com.example.virtualhaus.models

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

fun Query.addDocumentSnapshotListener(listener: (documents: List<DocumentSnapshot>) -> Unit) {
    addSnapshotListener { snapshot, error ->
        if (error != null) {
            error.message?.let { Log.e(this::class.simpleName, it) }
            return@addSnapshotListener
        }

        if (snapshot == null) {
            Log.e(this::class.simpleName, "Document update snapshot was NULL")
            return@addSnapshotListener
        }

        listener(snapshot.documents)
    }
}