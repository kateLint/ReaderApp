package com.compose.readerapp.screens.login

data class LoginState(val status: Status, val message: String? = null) {
    companion object{
        val IDLE = LoginState(Status.IDLE)
        val SUCCESS = LoginState(Status.SUCCESS)
        val LOADING = LoginState(Status.LOADING)
        val FAILED = LoginState(Status.FAILED)
    }
    enum class Status{
        SUCCESS,
        FAILED,
        LOADING,
        IDLE
    }
}
