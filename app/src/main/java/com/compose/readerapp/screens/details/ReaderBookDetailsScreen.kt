package com.compose.readerapp.screens.details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.compose.readerapp.components.ReaderAppBar
import com.compose.readerapp.data.Resource
import com.compose.readerapp.model.Item
import com.compose.readerapp.navigation.ReaderScreens

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
            .padding(it.calculateTopPadding())
            .fillMaxSize()){
            Column(modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {

                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()){
                    value = viewModel.getBookInfo(bookId)
                }.value

                if(bookInfo.data == null){
                    LinearProgressIndicator()
                }else{
                    Text(text = "Book id: ${bookInfo.data?.volumeInfo?.title}")
                }
            }
        }
    }




}