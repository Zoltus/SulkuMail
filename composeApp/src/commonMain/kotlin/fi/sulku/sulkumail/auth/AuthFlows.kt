package fi.sulku.sulkumail.auth

import fi.sulku.sulkumail.Token

//Todo proper authHandling
internal expect suspend fun startGoogleAuthFlow(scopes: List<String>): Token