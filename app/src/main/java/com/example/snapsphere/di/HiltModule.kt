package com.example.snapsphere.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    // function for providing firebase authentication
    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    // function for providing firebase firestore
    @Provides
    @Singleton
    fun providesFirebaseFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }

    // function for providing firebase storage
    @Provides
    @Singleton
    fun providesFirebaseStorage(): FirebaseStorage {
        return Firebase.storage
    }
}