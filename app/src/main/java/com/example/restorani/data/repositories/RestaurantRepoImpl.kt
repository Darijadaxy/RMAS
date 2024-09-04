package com.example.restorani.data.repositories

import android.net.Uri
import com.example.restorani.data.models.Restaurant
import com.example.restorani.data.models.User
import com.example.restorani.data.services.DatabaseService
import com.example.restorani.data.services.StorageService
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class RestaurantRepoImpl : RestaurantRepo {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val storageInstance = FirebaseStorage.getInstance()

    private val databaseService = DatabaseService(firestoreInstance)
    private val storageService = StorageService(storageInstance)


override suspend fun saveRestaurant(
    location: LatLng,
    name: String,
    description: String,
    restaurantImages: List<Uri>,
    date: Timestamp
): Resource<String> {
    return try {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val restaurantImagesUrls = storageService.uploadRestaurantImages(restaurantImages)
            val geoLocation = GeoPoint(
                location.latitude,
                location.longitude
            )
            // Kreiranje novog objekta restorana
            val newRestaurant = Restaurant(
                userId = currentUser.uid,
                location = geoLocation,
                name = name,
                description = description,
                restaurantImages = restaurantImagesUrls,
                date = date
            )
            // Čuvanje restorana u bazi
            val saveResult = databaseService.saveRestaurant(newRestaurant)
            if (saveResult is Resource.Success) {
                // Ako je restoran uspešno sačuvan, ažuriraj korisničke poene
                val pointsUpdateResult = databaseService.updateUserPoints(currentUser.uid, 10)
                if (pointsUpdateResult is Resource.Failure) {
                    return Resource.Failure(Exception("Restoran je sačuvan, ali ažuriranje poena nije uspelo"))
                }
            }
            saveResult // Vraća rezultat čuvanja restorana
        } else {
            Resource.Failure(Exception("Korisnik nije prijavljen"))
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.Failure(e)
    }
}

    override suspend fun getAllRestaurants(): Resource<List<Restaurant>> {
        return try{
            val snapshot = firestoreInstance.collection("restaurants").get().await()
            val restaurants = snapshot.toObjects(Restaurant::class.java)
            Resource.Success(restaurants)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getAllUsers(): Resource<List<User>> {
        return try{
            val snapshot = firestoreInstance.collection("users").get().await()
            val users = snapshot.toObjects(User::class.java)
            Resource.Success(users)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUsersRestaurants(uid: String): Resource<List<Restaurant>> {
        return try {
            val snapshot = firestoreInstance.collection("restaurants")
                .whereEqualTo("userId", uid)
                .get()
                .await()
            val beaches = snapshot.toObjects(Restaurant::class.java)
            Resource.Success(beaches)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addCommentToRestaurant(
        restaurantId: String,
        comment: String,
        uid: String // Dodaj uid kao parametar
    ): Resource<String> {
       // return databaseService.addCommentToRestaurant(restaurantId, comment)
    val commentResult = databaseService.addCommentToRestaurant(restaurantId, comment)
    if (commentResult is Resource.Success) {
        // Ako je komentar uspešno dodat, ažuriraj korisničke poene
        val pointsUpdateResult = databaseService.updateUserPoints(uid, 5) // Primer: dodavanje 3 poena
        if (pointsUpdateResult is Resource.Failure) {
            return Resource.Failure(Exception("Komentar je dodat, ali ažuriranje poena nije uspelo"))
        }
    }
    return commentResult
    }

    override suspend fun addRatingToRestaurant(
        restaurantId: String,
        rating: Int,
        uid: String
    ): Resource<String> {
        val ratingResult = databaseService.addRatingToRestaurant(restaurantId, rating)
        if (ratingResult is Resource.Success) {
            // Ako je ocena uspešno dodata, ažuriraj korisničke poene
            val pointsUpdateResult = databaseService.updateUserPoints(uid, 3) // Primer: dodavanje 3 poena
            if (pointsUpdateResult is Resource.Failure) {
                return Resource.Failure(Exception("Ocena je dodata, ali ažuriranje poena nije uspelo"))
            }
        }
        return ratingResult
    }




}