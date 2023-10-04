package com.compose.readerapp.screens.details

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.compose.readerapp.components.ReaderAppBar
import com.compose.readerapp.components.RoundedButton
import com.compose.readerapp.data.Resource
import com.compose.readerapp.model.Book
import com.compose.readerapp.model.Item
import com.compose.readerapp.model.MBook
import com.compose.readerapp.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookDetailsScreen(navController: NavController, bookId: String,
                      viewModel: DetailsViewModel = hiltViewModel()){

    Scaffold(topBar = {
        ReaderAppBar(title = "Book Details",
        Icons.Default.ArrowBack,
        showProfile = false,
            navController = navController){
            navController.navigate(ReaderScreens.SearchScreen.name)
        }
    }) {
        Surface (modifier = Modifier
            .padding(top = it.calculateTopPadding(), start = 12.dp, end = 12.dp)
            .fillMaxSize()){
            Column(modifier = Modifier.padding(top = 12.dp ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {

                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()){
                    value = viewModel.getBookInfo(bookId)
                }.value

                if(bookInfo.data == null){
                    Row {

                        LinearProgressIndicator()
                        Text(text = "Loading...")
                    }
                }else{
                    ShowBookDetails(bookInfo, navController)

                }
            }
        }
    }
}

@Composable
fun ShowBookDetails(bookInfo : Resource<Item>, navController: NavController){
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id

    Card(modifier = Modifier.padding(34.dp),
        shape = CircleShape, elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)) {

        Image(
            rememberImagePainter(data = bookData?.imageLinks?.thumbnail), "Book Image", Modifier
                .width(90.dp)
                .height(90.dp)
                .padding(1.dp)
        )
    }
    Text(text = bookData?.title.toString(), style = MaterialTheme.typography.titleLarge,
        overflow = TextOverflow.Ellipsis,
        maxLines = 19)

    Text(text = "Authors: ${bookData?.authors.toString()}")
    Text(text = "Page Count: ${bookData?.pageCount.toString()}")
    Text(text = "Categories: ${bookData?.categories.toString()}",
        style = MaterialTheme.typography.titleMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis)
    Text(text = "Published: ${bookData?.publishedDate.toString()}",
        style = MaterialTheme.typography.titleMedium)

    Spacer(modifier = Modifier.height(5.dp))


    val cleanDescription = HtmlCompat.fromHtml(bookData?.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    val localDims = LocalContext.current.resources.displayMetrics

    Surface (modifier = Modifier
        .height(localDims.heightPixels.dp.times(0.09f))
        .padding(4.dp)
        .fillMaxWidth(),
        shape = RectangleShape,
        border = BorderStroke(1.dp, Color.Gray)
    ){

        LazyColumn(modifier = Modifier.padding(3.dp)) {
            item {

                Text(text = cleanDescription)
            }
        }
    }

    //Buttons
    Row (modifier = Modifier.padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround){
        RoundedButton(label = "Save"){

            val book = MBook(
                title = bookData?.title.toString(),
                authors = bookData?.authors.toString(),
                description = bookData?.description.toString(),
                categories = bookData?.categories.toString(),
                notes = "",
                photoUrl = bookData?.imageLinks?.thumbnail,
                publishedDate = bookData?.publishedDate,
                pageCount = bookData?.pageCount.toString(),
                rating = 0.0,
                googleBookId = googleBookId,
                userId = FirebaseAuth.getInstance().currentUser?.uid.toString())

            saveToFirebase(book,navController)
        }
        Spacer(modifier = Modifier.width(25.dp))
        RoundedButton(label = "Cancel"){
            navController.popBackStack()
        }
    }

}

fun saveToFirebase(book: MBook, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if(book.toString().isNotEmpty()){
        dbCollection.add(book)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            navController.popBackStack()
                        }
                    }.addOnFailureListener {
                        Log.w("TAG", "saveToFirebase: Error updating $it\""   )
                    }

            }
    }else{

    }
}
