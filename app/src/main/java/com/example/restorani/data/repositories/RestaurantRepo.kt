package com.example.restorani.data.repositories

import android.net.Uri
import com.example.restorani.data.models.Restaurant
import com.example.restorani.data.models.User
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

interface RestaurantRepo {

    suspend fun saveRestaurant(
        location: LatLng,
        name:String,
        description: String,
        restaurantImages : List<Uri>,
        date: Timestamp
    ): Resource<String>

    suspend fun getAllRestaurants(): Resource<List<Restaurant>> //ovo mozda ne treba
    suspend fun  getAllUsers():Resource<List<User>>

    suspend fun getUsersRestaurants(
        uid: String //kog korisnika
    ): Resource<List<Restaurant>>

    // Dodajte metod za dodavanje komentara
    suspend fun addCommentToRestaurant(
        restaurantId: String,
        comment: String,
        uid: String
    ): Resource<String>

    suspend fun addRatingToRestaurant(restaurantId: String, rating: Int,uid: String): Resource<String>
   // suspend fun updateUserPoints(uid : String, points : Int): Resource<String>


}

