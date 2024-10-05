package fi.sulku.sulkumail

import kotlinx.serialization.Serializable


@Serializable
data class UserData(
    val id: String,
    val username: String,
    val avatar: String,
    val global_name: String,
    val avatar_decoration_data: String?,
    val locale: String,
    val email: String,
    val verified: Boolean
)
/*
{"id":"198099414089203717",
"username":"zoltus",
"avatar":"412d2908887a3c382c9f80724dd9cd1e",
"discriminator":"0",
"public_flags":256,
"flags":256,
"banner":null,
"accent_color":8447,
"global_name":"Zoltus",
"avatar_decoration_data":null,
"banner_color":"#0020ff",
"clan":null,
"mfa_enabled":true,
"locale":"en-US",
"premium_type":0,
"email":"zoltus@outlook.com",
"verified":true}
 */