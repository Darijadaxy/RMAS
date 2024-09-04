package com.example.restorani.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.restorani.R
import com.example.restorani.data.models.Restaurant
//import java.lang.reflect.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter


import androidx.compose.ui.draw.clip // Import for clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.restorani.screens.components.ColorPalette
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.example.restorani.screens.components.DashedLineBackground
import com.example.restorani.screens.components.Header
import com.example.restorani.screens.components.InputFieldLabel

@Composable
fun RestaurantDetScreen(
    restaurant: Restaurant,
    onBack: () -> Unit,
   // onAddComment: (String) -> Unit,
   // onAddRating: (Int) -> Unit
    onAddComment: (String, String) -> Unit, // Dodaj uid kao parametar
    onAddRating: (Int, String) -> Unit, // Dodaj uid kao parametar
    uid: String
) {

    val commentText = remember { mutableStateOf("") }
    val ratingText = remember { mutableStateOf("") }
    val comments = remember { mutableStateOf(restaurant.comments) } // Track local comment state
    val averageRating = remember { mutableStateOf(restaurant.averageRating) }

    DashedLineBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Header(header_text = restaurant.name)

            Text(
                text = "Opis:",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            InputFieldLabel(label = restaurant.description)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Slike:",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(modifier = Modifier.padding(bottom = 16.dp)) {
                items(restaurant.restaurantImages) { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Slika restorana",
                        modifier = Modifier
                            .size(120.dp)
                            .padding(end = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Display average rating
            Text(
                text = "Prosečna ocena: ${averageRating.value}",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Rating input and add button
            Row(modifier = Modifier.padding(top = 16.dp)) {
                TextField(
                    value = ratingText.value,
                    onValueChange = { ratingText.value = it },
                    placeholder = {
                        Text(
                            text = "Dodaj ocenu (1-5)...",
                            style = TextStyle(fontSize = 14.sp, color = Color.White)
                        )
                    },
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                Button(
                    onClick = {
                        val rating = ratingText.value.toIntOrNull()
                        if (rating != null && rating in 1..5) {
                            onAddRating(rating, uid) // Pass uid
                            ratingText.value = ""
                            // Update local average rating
                            val newAverageRating = (averageRating.value * restaurant.ratings.size + rating) / (restaurant.ratings.size + 1)
                            averageRating.value = newAverageRating
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterVertically),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ColorPalette.Purple200,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Dodaj ocenu")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Komentari:",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Display comments
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(comments.value) { comment ->
                    Text(
                        text = comment,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    )
                }
            }

            // Comment input and add button
            Row(modifier = Modifier.padding(top = 16.dp)) {
                TextField(
                    value = commentText.value,
                    onValueChange = { commentText.value = it },
                    placeholder = {
                        Text(
                            text = "Dodaj komentar...",
                            style = TextStyle(fontSize = 14.sp, color = Color.White)
                        )
                    },
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                Button(
                    onClick = {
                        onAddComment(commentText.value, uid) // Pass uid
                        comments.value = comments.value + commentText.value // Update local comments
                        commentText.value = "" // Reset input field
                    },
                    modifier = Modifier.align(Alignment.CenterVertically),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ColorPalette.Purple200,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Dodaj")
                }
            }
        }
    }
//    val commentText = remember { mutableStateOf("") }
//    val ratingText = remember { mutableStateOf("") }
//    val comments = restaurant.comments // Pretpostavljamo da je ovo dinamički podatak
//    val averageRating = restaurant.averageRating // Pretpostavljamo da je ovo dinamički podatak
//
//    DashedLineBackground {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Header(header_text = restaurant.name)
//
//            Text(
//                text = "Opis:",
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                ),
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            InputFieldLabel(label = restaurant.description)
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            Text(
//                text = "Slike:",
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                ),
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            LazyRow(modifier = Modifier.padding(bottom = 16.dp)) {
//                items(restaurant.restaurantImages) { imageUrl ->
//                    AsyncImage(
//                        model = imageUrl,
//                        contentDescription = "Slika restorana",
//                        modifier = Modifier
//                            .size(120.dp)
//                            .padding(end = 8.dp),
//                        contentScale = ContentScale.Crop
//                    )
//                }
//            }
//
//            Text(
//                text = "Prosečna ocena: ${averageRating}",
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                ),
//                modifier = Modifier.padding(vertical = 8.dp)
//            )
//
//            Row(modifier = Modifier.padding(top = 16.dp)) {
//                TextField(
//                    value = ratingText.value,
//                    onValueChange = { ratingText.value = it },
//                    placeholder = {
//                        Text(
//                            text = "Dodaj ocenu (1-5)...",
//                            style = TextStyle(fontSize = 14.sp, color = Color.White)
//                        )
//                    },
//                    textStyle = TextStyle(color = Color.White),
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(end = 8.dp)
//                )
//                Button(
//                    onClick = {
//                        val rating = ratingText.value.toIntOrNull()
//                        if (rating != null && rating in 1..5) {
//                            onAddRating(rating, uid) // Prosledi uid
//                            ratingText.value = ""
//                        }
//                    },
//                    modifier = Modifier.align(Alignment.CenterVertically),
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = ColorPalette.Purple200,
//                        contentColor = Color.White
//                    )
//                ) {
//                    Text(text = "Dodaj ocenu")
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "Komentari:",
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                ),
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            Row(modifier = Modifier.padding(top = 16.dp)) {
//                TextField(
//                    value = commentText.value,
//                    onValueChange = { commentText.value = it },
//                    placeholder = {
//                        Text(
//                            text = "Dodaj komentar...",
//                            style = TextStyle(fontSize = 14.sp, color = Color.White)
//                        )
//                    },
//                    textStyle = TextStyle(color = Color.White),
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(end = 8.dp)
//                )
//                Button(
//                    onClick = {
//                        onAddComment(commentText.value, uid) // Prosledi uid
//                        commentText.value = "" // Resetuj polje za unos
//                    },
//                    modifier = Modifier.align(Alignment.CenterVertically),
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = ColorPalette.Purple200,
//                        contentColor = Color.White
//                    )
//                ) {
//                    Text(text = "Dodaj")
//                }
//            }
//
//            // Prikaz komentara
//            LazyColumn(
//                modifier = Modifier.padding(top = 16.dp)
//            ) {
//                items(comments) { comment ->
//                    Text(
//                        text = comment,
//                        style = TextStyle(color = Color.White),
//                        modifier = Modifier.padding(bottom = 8.dp)
//                    )
//                }
//            }
//        }
//    }
}
//najbolji
//    val commentText = remember { mutableStateOf("") }
//    val ratingText = remember { mutableStateOf("")}
//    val comments = remember { mutableStateOf(restaurant.comments) } // Pratimo lokalno stanje komentara
//    val averageRating = remember { mutableStateOf(restaurant.averageRating) }
//
//
//        DashedLineBackground {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Header(header_text = restaurant.name)
//
//            Text(
//                text = "Opis:",
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                ),
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            InputFieldLabel(label = restaurant.description)
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            Text(
//                text = "Slike:",
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                ),
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            LazyRow(modifier = Modifier.padding(bottom = 16.dp)) {
//                items(restaurant.restaurantImages) { imageUrl ->
//                    AsyncImage(
//                        model = imageUrl,
//                        contentDescription = "Slika restorana",
//                        modifier = Modifier
//                            .size(120.dp)
//                            .padding(end = 8.dp),
//                        contentScale = ContentScale.Crop
//                    )
//                }
//            }
//
//            // Prikaz prosečne ocene
//            Text(
//                text = "Prosečna ocena: ${averageRating.value}",
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                ),
//                modifier = Modifier.padding(vertical = 8.dp)
//            )
//
////            // Polje za unos nove ocene i dugme za dodavanje
////            Row(modifier = Modifier.padding(top = 16.dp)) {
////                TextField(
////                    value = ratingText.value,
////                    onValueChange = { ratingText.value = it },
////                    placeholder = {
////                        Text(
////                            text = "Dodaj ocenu (1-5)...",
////                            style = TextStyle(fontSize = 14.sp, color = Color.White)
////                        )
////                    },
////                    textStyle = TextStyle(color = Color.White),
////                    modifier = Modifier
////                        .weight(1f)
////                        .padding(end = 8.dp)
////                )
////                Button(
////                    onClick = {
////                        val rating = ratingText.value.toIntOrNull()
////                        if (rating != null && rating in 1..5) {
////                            onAddRating(rating)
////                            ratingText.value = ""
////                            // Ažuriraj prosečnu ocenu lokalno
////                            val newAverageRating = (averageRating.value * restaurant.ratings.size + rating) / (restaurant.ratings.size + 1)
////                            averageRating.value = newAverageRating
////                        }
////                    },
////                    modifier = Modifier.align(Alignment.CenterVertically),
////                    colors = ButtonDefaults.buttonColors(
////                        backgroundColor = ColorPalette.Purple200,
////                        contentColor = Color.White
////                    )
////                ) {
////                    Text(text = "Dodaj ocenu")
////                }
////            }
//            // Polje za unos nove ocene i dugme za dodavanje
//            Row(modifier = Modifier.padding(top = 16.dp)) {
//                TextField(
//                    value = ratingText.value,
//                    onValueChange = { ratingText.value = it },
//                    placeholder = {
//                        Text(
//                            text = "Dodaj ocenu (1-5)...",
//                            style = TextStyle(fontSize = 14.sp, color = Color.White)
//                        )
//                    },
//                    textStyle = TextStyle(color = Color.White),
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(end = 8.dp)
//                )
//                Button(
//                    onClick = {
//                        val rating = ratingText.value.toIntOrNull()
//                        if (rating != null && rating in 1..5) {
//                            onAddRating(rating, uid) // Prosledi uid
//                            ratingText.value = ""
//                        }
//                    },
//                    modifier = Modifier.align(Alignment.CenterVertically),
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = ColorPalette.Purple200,
//                        contentColor = Color.White
//                    )
//                ) {
//                    Text(text = "Dodaj ocenu")
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "Komentari:",
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                ),
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
////            // Prikaz komentara koristeći lokalno stanje
////            LazyColumn(modifier = Modifier.weight(1f)) {
////                items(comments.value) { comment ->
////                    Text(
////                        text = comment,
////                        style = TextStyle(
////                            fontSize = 14.sp,
////                            color = Color.DarkGray
////                        ),
////                        modifier = Modifier
////                            .fillMaxWidth()
////                            .padding(4.dp)
////                            .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
////                            .padding(8.dp)
////                    )
////                }
////            }
////
////            Row(modifier = Modifier.padding(top = 16.dp)) {
////                TextField(
////                    value = commentText.value,
////                    onValueChange = { commentText.value = it },
////                    placeholder = {
////                        Text(
////                            text = "Dodaj komentar...",
////                            style = TextStyle(fontSize = 14.sp, color = Color.White)
////                        )
////                    },
////                    textStyle = TextStyle(color = Color.White),
////                    modifier = Modifier
////                        .weight(1f)
////                        .padding(end = 8.dp)
////                )
////                Button(
////                    onClick = {
////                        onAddComment(commentText.value) // Pozivamo funkciju da dodamo komentar u Firebase
////                        comments.value = comments.value + commentText.value // Ažuriramo lokalno stanje
////                        commentText.value = "" // Resetujemo polje za unos
////                    },
////                    modifier = Modifier.align(Alignment.CenterVertically),
////                    colors = ButtonDefaults.buttonColors(
////                        backgroundColor = ColorPalette.Purple200,
////                        contentColor = Color.White
////                    )
////                ) {
////                    Text(text = "Dodaj")
//            Row(modifier = Modifier.padding(top = 16.dp)) {
//                TextField(
//                    value = commentText.value,
//                    onValueChange = { commentText.value = it },
//                    placeholder = {
//                        Text(
//                            text = "Dodaj komentar...",
//                            style = TextStyle(fontSize = 14.sp, color = Color.White)
//                        )
//                    },
//                    textStyle = TextStyle(color = Color.White),
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(end = 8.dp)
//                )
//                Button(
//                    onClick = {
//                        onAddComment(commentText.value, uid) // Prosledi uid
//                        comments.value = comments.value + commentText.value // Ažuriraj lokalno stanje
//                        commentText.value = "" // Resetuj polje za unos
//                    },
//                    modifier = Modifier.align(Alignment.CenterVertically),
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = ColorPalette.Purple200,
//                        contentColor = Color.White
//                    )
//                ) {
//                    Text(text = "Dodaj")
//                }
//            }
//        }
//    }
//}


//    val commentText = remember { mutableStateOf("") }
//    DashedLineBackground {
//
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//
//            Header(header_text = restaurant.name)
//
////        Text(
////            text = restaurant.name,
////            style = TextStyle(
////                fontSize = 24.sp,
////                fontWeight = FontWeight.Bold
////            ),
////            modifier = Modifier.padding(bottom = 8.dp)
////        )
//
//            Text(
//                text = "Opis:",
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                ),
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            // Prikaz opisa restorana
////            Text(
////                text = restaurant.description,
////                style = TextStyle(
////                    fontSize = 16.sp,
////                    color = Color.Gray
////                ),
////                modifier = Modifier.padding(bottom = 16.dp)
////            )
//            InputFieldLabel(label = restaurant.description)
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            Text(
//                text = "Slike:",
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                ),
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            // Prikaz slika restorana
//            LazyRow(modifier = Modifier.padding(bottom = 16.dp)) {
//                items(restaurant.restaurantImages) { imageUrl ->
//                    AsyncImage(
//                        model = imageUrl,
//                        contentDescription = "Slika restorana",
//                        modifier = Modifier
//                            .size(120.dp)
//                            .padding(end = 8.dp),
//                        contentScale = ContentScale.Crop
//                    )
//                }
//            }
//
//            // Prikaz naslova komentara
//            Text(
//                text = "Komentari:",
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                ),
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            // Prikaz komentara
//            LazyColumn(modifier = Modifier.weight(1f)) {
//                items(restaurant.comments) { comment ->
//                    Text(
//                        text = comment,
//                        style = TextStyle(
//                            fontSize = 14.sp,
//                            color = Color.DarkGray
//                        ),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(4.dp)
//                            .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
//                            .padding(8.dp)
//                    )
//                }
//            }
//
//            // Polje za unos novog komentara i dugme za dodavanje
//            Row(modifier = Modifier.padding(top = 16.dp)) {
//                TextField(
//                    value = commentText.value,
//                    onValueChange = { commentText.value = it },
//                    placeholder = { Text(text = "Dodaj komentar...",style = TextStyle(
//                        fontSize = 14.sp,
//                        color = Color.White
//                    ),) },
//                   // textStyle = TextStyle(color = Color.White),
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(end = 8.dp)
//                )
//                Button(
//                    onClick = {
//                        onAddComment(commentText.value)
//                        commentText.value = ""
//                    },
//                    modifier = Modifier.align(Alignment.CenterVertically),
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = ColorPalette.Purple200,
//                        contentColor = Color.White
//                    )
//                ) {
//                    Text(text = "Dodaj")
//                }
//            }
//        }
//    }
//}




















//@Composable
//fun RestaurantDetScreen(
//    restaurant: Restaurant,
//    onBack: () -> Unit,
//    onAddComment: (String) -> Unit
//) {
//    val commentText = remember { mutableStateOf("") }
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .padding(16.dp)) {
//
//        Text(
//            text = restaurant.name,
//            style = TextStyle(
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold
//            ),
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//
//        Text(
//            text = restaurant.description,
//            style = TextStyle(
//                fontSize = 16.sp,
//                color = Color.Gray
//            ),
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        LazyRow(modifier = Modifier.padding(bottom = 16.dp)) {
//            items(restaurant.restaurantImages) { imageUrl ->
//                AsyncImage(
//                    model = imageUrl,
//                    contentDescription = "Slika restorana",
//                    modifier = Modifier
//                        .size(120.dp)
//                        .padding(end = 8.dp),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
//
//        Text(
//            text = "Komentari:",
//            style = TextStyle(
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold
//            ),
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//
//        LazyColumn(modifier = Modifier.weight(1f)) {
//            items(restaurant.comments) { comment ->
//                Text(
//                    text = comment,
//                    style = TextStyle(
//                        fontSize = 14.sp,
//                        color = Color.DarkGray
//                    ),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(4.dp)
//                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
//                        .padding(8.dp)
//                )
//            }
//        }
//
//        Row(modifier = Modifier.padding(top = 16.dp)) {
//            TextField(
//                value = commentText.value,
//                onValueChange = { commentText.value = it },
//                placeholder = { Text(text = "Dodaj komentar...") },
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(end = 8.dp)
//            )
//            Button(
//                onClick = {
//                    onAddComment(commentText.value)
//                    commentText.value = ""
//                },
//                modifier = Modifier.align(Alignment.CenterVertically),
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = ColorPalette.Purple200,
//                    contentColor = Color.White
//                )
//            ) {
//                Text(text = "Dodaj")
//            }
//        }
//    }
//}
////@Composable
////fun RestaurantDetailScreen(
////    restaurant: Restaurant,
////    onBack: () -> Unit,
////    onAddComment: (String) -> Unit
////) {
////    val commentText = remember { mutableStateOf("") }
////
////    Column(modifier = Modifier
////        .fillMaxSize()
////        .padding(16.dp)) {
////        Text(
////            text = restaurant.name,
////            style = TextStyle(
////                fontSize = 24.sp,
////                fontWeight = FontWeight.Bold
////            ),
////            modifier = Modifier.padding(bottom = 8.dp)
////        )
////        Text(
////            text = restaurant.description,
////            style = TextStyle(
////                fontSize = 16.sp,
////                color = Color.Gray
////            ),
////            modifier = Modifier.padding(bottom = 16.dp)
////        )
////
////        LazyRow(modifier = Modifier.padding(bottom = 16.dp)) {
////            items(restaurant.restaurantImages) { imageUrl ->
////                AsyncImage(
////                    model = imageUrl,
////                    contentDescription = "Slika restorana",
////                    modifier = Modifier
////                        .size(120.dp)
////                        .padding(end = 8.dp),
////                    contentScale = ContentScale.Crop
////                )
////            }
////        }
////
////        Text(
////            text = "Komentari:",
////            style = TextStyle(
////                fontSize = 20.sp,
////                fontWeight = FontWeight.Bold
////            ),
////            modifier = Modifier.padding(bottom = 8.dp)
////        )
////        LazyColumn(modifier = Modifier.weight(1f)) {
////            items(restaurant.comments) { comment ->
////                Text(
////                    text = comment,
////                    style = TextStyle(
////                        fontSize = 14.sp,
////                        color = Color.DarkGray
////                    ),
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .padding(4.dp)
////                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
////                        .padding(8.dp)
////                )
////            }
////        }
////
////        Row(modifier = Modifier.padding(top = 16.dp)) {
////            TextField(
////                value = commentText.value,
////                onValueChange = { commentText.value = it },
////                placeholder = { Text(text = "Dodaj komentar...") },
////                modifier = Modifier
////                    .weight(1f)
////                    .padding(end = 8.dp)
////            )
////            Button(
////                onClick = {
////                    onAddComment(commentText.value)
////                    commentText.value = ""
////                },
////                modifier = Modifier.align(Alignment.CenterVertically)
////            ) {
////                Text(text = "Dodaj")
////            }
////        }
////    }
////}
//
//
//
//
//
//
//
////@Composable
////fun RestaurantDetScreen(
////    restaurant: Restaurant,
////    onBack: () -> Unit
////) {
////
////    Scaffold(
////        topBar = {
////            TopAppBar(
////                title = { Text(text = restaurant.name) },
////                backgroundColor =ColorPalette.Purple200,
////                contentColor = Color.White,
////                navigationIcon = {
////                    IconButton(onClick = onBack) {
////                        Icon(
////                            painter = painterResource(id = R.drawable.slika), // Postavi ikonu za nazad
////                            contentDescription = "Nazad"
////                        )
////                    }
////                }
////            )
////        },
////        content = { paddingValues -> // Dodajemo paddingValues parametar
////            Column(
////                modifier = Modifier
////                    .fillMaxSize()
////                    .padding(paddingValues) // Primeni padding
////                    .padding(16.dp) // Dodatni padding
////            ) {
//////                // Prikaz slike restorana
//////                if (restaurant.restaurantImages.isNotEmpty()) {
//////                    Image(
//////                        painter = rememberAsyncImagePainter(restaurant.restaurantImages.first()),
//////                        contentDescription = "Slika restorana",
//////                        modifier = Modifier
//////                            .fillMaxWidth()
//////                            .height(200.dp)
//////                            .padding(bottom = 16.dp)
//////                            .clip(RoundedCornerShape(10.dp)),
//////                        contentScale = ContentScale.Crop
//////                    )
//////                }
////
////                // Prikaz imena restorana
////                Text(
////                    text = "Ime restorana: ${restaurant.name}",
////                    style = MaterialTheme.typography.h6,
////                    modifier = Modifier.padding(bottom = 8.dp)
////                )
////
////                // Prikaz opisa restorana
////                Text(
////                    text = "Opis: ${restaurant.description}",
////                    style = MaterialTheme.typography.body1,
////                    modifier = Modifier.padding(bottom = 16.dp)
////                )
////
////                // Prikaz svih slika
////                restaurant.restaurantImages.forEach { imageUrl ->
////                    Image(
////                        painter = rememberAsyncImagePainter(imageUrl),
////                        contentDescription = "Slika restorana",
////                        modifier = Modifier
////                            .fillMaxWidth()
////                            .height(200.dp)
////                            .padding(bottom = 16.dp)
////                            .clip(RoundedCornerShape(10.dp)),
////                        contentScale = ContentScale.Crop
////                    )
////                }
////
////                // Dodavanje dugmeta ili linka za dodatne akcije
////                Spacer(modifier = Modifier.height(24.dp))
////                Button(
////                    onClick = { /* Ovde možeš dodati dodatnu funkcionalnost */ },//dodaj komentar
////                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorPalette.Purple200) //MaterialTheme.colors.primary)
////                ) {
////                    Text("Ostavi komentar", color = Color.White)
////                }
////            }
////        }
////    )
////}
//
//
