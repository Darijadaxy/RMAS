package com.example.restorani.data.repositories

import android.net.Uri
import com.example.restorani.data.models.User
import com.google.firebase.auth.FirebaseUser

interface IAuthRepo {
    val user: FirebaseUser?

    suspend fun signUp(fullName: String, phoneNumber: String, profileImage: Uri, email: String, password: String): Resource<FirebaseUser>
    suspend fun signIn(email: String, password: String): Resource<FirebaseUser>
    fun signOut()

    suspend fun getUser(): Resource<User>
    suspend fun getAllUsers(): Resource<List<User>>
}