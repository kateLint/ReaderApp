package com.compose.readerapp.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.compose.readerapp.R
import com.compose.readerapp.components.EmailInput
import com.compose.readerapp.components.PasswordInput
import com.compose.readerapp.components.ReaderLogo
import com.compose.readerapp.components.SubmitButton
import com.compose.readerapp.navigation.ReaderScreens

@Composable
fun ReaderLoginScreen(navController: NavHostController,
                      viewModel: LoginScreenViewModel = viewModel()) {
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ReaderLogo()
            if(showLoginForm.value){
                UserForm(loading = false,isCreateAccount = false){email, password ->
                    //TODO fb login
                    viewModel.signInWithEmailAndPassword(email,password){
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }
                }
            }else{
                UserForm(loading = false, isCreateAccount = true){ email, password ->
                    //TODO create FB account
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }
                }
            }

        }
        Spacer(modifier = Modifier.height(15.dp))
        Row (modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            val text = if( showLoginForm.value){
                "Sign up"
            }else{
                "Login"
            }
            Text(text = "New User?")
            Text(text = text, modifier = Modifier
                .clickable {
                    showLoginForm.value = !showLoginForm.value
                }
                .padding(start = 5.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, pwd ->}

){
    val email = rememberSaveable {
        mutableStateOf("me@me.com")
    }
    val password = rememberSaveable { mutableStateOf("testtest") }

    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value){
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .height(450.dp)
     //   .fillMaxHeight()
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())

    Column(modifier,
        horizontalAlignment = Alignment.CenterHorizontally) {
        if (isCreateAccount) Text(
            text = stringResource(id = R.string.create_acct),
            modifier = Modifier.padding(10.dp)
        ) else Text("")
        EmailInput(
            modifier = Modifier.padding(10.dp),
            emailState = email,

            enabled = !loading,
            onAction = KeyboardActions {
                passwordFocusRequest.requestFocus()
            },
        )

        PasswordInput(
            modifier = Modifier
                .focusRequester(passwordFocusRequest)
                .padding(10.dp),
            passwordState = password,
            labelId = "Password",
            enabled = !loading, //Todo change this
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions

                onDone(email.value.trim(), password.value.trim())
            })

        SubmitButton(
            modifier = Modifier.padding(10.dp),
            textId = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = valid
        ){
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }


}