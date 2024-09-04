package com.example.restorani.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.restorani.data.models.User
import com.example.restorani.data.repositories.Resource
import com.example.restorani.view_models.RestaurantViewModel


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.restorani.data.models.Restaurant
import com.example.restorani.screens.components.ColorPalette


@Composable
fun RangScreen(
    restaurantViewModel: RestaurantViewModel
) {
    // Pratite stanje liste korisnika iz ViewModel-a
    val usersState by restaurantViewModel.users.collectAsState()

    LaunchedEffect(Unit) {
        restaurantViewModel.getAllUsers()
    }
    // Glavna pozadina ekrana
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPalette.BackgroundMain) // Boja pozadine
            .padding(16.dp)
    ) {
        // Prikazivanje liste korisnika
        when (val users = usersState) {
            is Resource.Success -> {
                val sortedUsers = users.result.sortedByDescending { it.points }

                LazyColumn {
                    items(sortedUsers) { user ->
                        UserItem(user = user)
                    }
                }
            }
            is Resource.Loading -> {
                // Prikazivanje indikatora učitavanja
                CircularProgressIndicator(color = ColorPalette.Purple200)
            }
            else -> {
                // Prikazivanje praznog stanja ili druge alternative
                Text(
                    text = "Nema dostupnih podataka",
                    color = ColorPalette.White
                )
            }
        }
    }
}
@Composable
fun UserItem(user: User) {
    // Prikazivanje korisničkih podataka (fullName i points)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(ColorPalette.Purple200)//.copy(alpha = 0.2f)) // Blaga pozadina za svaki item
            .padding(16.dp)
    ) {
        Text(
            text = user.fullName,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.subtitle1,
            color = ColorPalette.White // Boja teksta
        )
        Text(
            text = user.points.toString(),
            style = MaterialTheme.typography.subtitle1,
            color = ColorPalette.White // Boja poena
        )
    }
    Divider(color = ColorPalette.White.copy(alpha = 0.3f)) // Boja razdvojne linije
}
