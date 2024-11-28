package fi.sulku.sulkumail.di

import SulkuMail.composeApp.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import org.koin.dsl.module

val supabaseModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            defaultLogLevel = LogLevel.DEBUG
            install(Auth.Companion) {
                /*
                The deeplink scheme used for the implicit and PKCE flow. When null, deeplinks won't be used as redirect urls
                Note: Deeplinks are only used as redirect urls on Android and Apple platforms. Other platforms will use their own default redirect url.
                 */
                host = "sulkumail"
                scheme = "login"
                /*
                The default redirect url used for authentication. When null, a platform specific default redirect url will be used.
                On Android and Apple platforms, the default redirect url is the deeplink.
                On Browser platforms, the default redirect url is the current url.
                On Desktop (excluding MacOS) platforms, there is no default redirect url. For OAuth flows, a http callback server will be used and a localhost url will be the redirect url.
                 */
                defaultRedirectUrl = ""
                alwaysAutoRefresh = true
                autoSaveToStorage = true
                autoLoadFromStorage = true
                // platformGoTrueConfig()
                flowType = FlowType.PKCE //tod
                // On Android only, you can set OAuth and SSO logins to open in a custom tab, rather than an external browser:
                // defaultExternalAuthAction = ExternalAuthAction.CustomTabs() //defaults to ExternalAuthAction.ExternalBrowser
                /*
                Whether to stop auto-refresh on focus loss, and resume it on focus again.
                Currently only supported on Android.
                 */
                //enableLifecycleCallbacks = true
            }
            /*install(ComposeAuth) { // ios/android native google auth
                googleNativeLogin(serverClientId = "google-client-id")
                appleNativeLogin()
            }*/
            // install(Postgrest)
            install(Auth.Companion) {

            }
            // install(Realtime)
        }
    }
}