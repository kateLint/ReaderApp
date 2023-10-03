package com.compose.readerapp.screens.details

import androidx.lifecycle.ViewModel
import com.compose.readerapp.data.Resource
import com.compose.readerapp.model.Item
import com.compose.readerapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(val repository: BookRepository): ViewModel(){
    suspend fun getBookInfo(bookId: String): Resource<Item>{
        return repository.getBookInfo(bookId)
    }
}