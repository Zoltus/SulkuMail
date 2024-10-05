package fi.sulku.sulkumail.composables.login

data class Validation(val errorMsg: String, val validate: ((String) -> Boolean))