package fi.sulku.sulkumail.viewmodels

import kotlinx.serialization.Serializable

@Serializable
data class UnifiedEmail(
    //todo pagetoken for the mail?
    val id: String,
    var sender: String? = null,
    val subject: String? = null,
    val snippet: String? = null,
    var senderImage: String? = null
)
