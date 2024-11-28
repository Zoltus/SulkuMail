package fi.sulku.sulkumail.di

/*
                    //https://supabase.com/docs/reference/kotlin/auth-getuser
                   // supabase.auth.signInWith(Discord)
                    // val session = supabase.auth.currentSessionOrNull()
                    //val user = supabase.auth.retrieveUserForCurrentSession(updateSession = true)

  scope.launch {
            supabase.auth.sessionStatus.collect {
                when (it) {
                    is SessionStatus.Authenticated -> {
                        println("Received new authenticated session.")
                        when (it.source) { //Check the source of the session
                            SessionSource.External -> println("External")
                            is SessionSource.Refresh -> println("Refresh")
                            is SessionSource.SignIn -> println("Sign in")
                            is SessionSource.AnonymousSignIn -> println("Anonymous sign in")
                            is SessionSource.SignUp -> println("Sign up")
                            SessionSource.Storage -> println("Storage")
                            SessionSource.Unknown -> println("Unknown")
                            is SessionSource.UserChanged -> println("User changed")
                            is SessionSource.UserIdentitiesChanged -> println("User identities changed")
                        }
                    }

                    SessionStatus.Initializing -> {
                        println("Initializing")
                    }

                    is SessionStatus.RefreshFailure -> {
                        println("Refresh failure ${it.cause}") //Either a network error or a internal server error
                    }

                    is SessionStatus.NotAuthenticated -> {
                        if (it.isSignOut) {
                            println("User signed out")
                        } else {
                            println("User not signed in")
                        }
                    }
                }
            }
        }
 */