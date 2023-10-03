package com.compose.readerapp.repository

import com.compose.readerapp.data.DataOrException
import com.compose.readerapp.data.Resource
import com.compose.readerapp.model.Item
import com.compose.readerapp.network.BooksApi
import com.google.rpc.context.AttributeContext
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksApi) {

    private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()

/*    suspend fun getBooks(searchQuery: String): DataOrException<List<Item>, Boolean,Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data= api.getAllBooks(searchQuery).items
            if(dataOrException.data?.isNotEmpty() == true){
                dataOrException.loading = false
            }
        }catch (e:Exception){
            dataOrException.e = e
        }
        return dataOrException
    }*/


    suspend fun getBooks(searchQuery: String): Resource<List<Item>> {

        return try {
            Resource.Loading(data = true)

            val itemList = api.getAllBooks(searchQuery).items
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)

        }catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }

    }

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        val response = try {
            Resource.Loading(data = true)
            api.getBookInfo(bookId)

        }catch (exception: Exception){
            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }


}