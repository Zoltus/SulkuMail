package fi.sulku.sulkumail.composables.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fi.sulku.sulkumail.viewmodels.AuthViewModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.Identity
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Settings() {
    val authVm = koinViewModel<AuthViewModel>()
    Column(
        modifier = Modifier.padding(25.dp)
    ) {
        Text(
            fontSize = 30.sp,
            text = "Settings"
        )
        Spacer(Modifier.height(50.dp))

        val authState = authVm.supabase.composeAuth.rememberSignInWithGoogle(

            onResult = {
                when (it) { //handle errors
                    NativeSignInResult.ClosedByUser -> {
                        println("ClosedByUser")
                    }

                    is NativeSignInResult.Error -> {
                        println("Error")
                    }

                    is NativeSignInResult.NetworkError -> {
                        println("NetworkError")
                    }

                    NativeSignInResult.Success -> {
                        println("Success")

                    }
                }
            }
        )

        Button(onClick = { authState.startFlow() }) {
            Text("Sign in with Google")
        }

        Button(onClick = { authVm.signOut() }) {
            Text("linkIdentitety")
        }

        Button(onClick = {
            //get all identities linked to a user
            val identities = authVm.supabase.auth.currentIdentitiesOrNull() ?: emptyList()

//find the google identity linked to the user
            val googleIdentity: Identity = identities.first { it.provider == "google" }

        }) {
            Text("Print")
        }


    }
}