package fi.sulku.sulkumail

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import fi.sulku.sulkumail.di.initKoin

fun main() = application {
    initKoin()
    Window(
        title = "SulkuMail",
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            width = 1200.dp,
            height = 800.dp,
            position = WindowPosition(Alignment.Center)
        ),
    ) {
         App()
    }
}

/*
@Serializable
data class City(
    val id: Int,
    val name: String,
    val url: String,
    val slug: String,
)

        val a = rememberCoroutineScope()
        a.launch {
            val json = Json {
                ignoreUnknownKeys = true
                isLenient = true // Allows handling minor deviations in JSON format
            }

            val client = HttpClient(CIO) {
                install(ContentNegotiation) { json }
            }


            val response = client.get("https://monitoring.localzero.net/api/cities?format=json") {
                headers {
                    append(HttpHeaders.Accept, "application/json")
                    append(HttpHeaders.ContentType, "application/json;charset=UTF-8")
                }
            }
            // Deserialize into a List of Cities
            val cities: List<City> = json.decodeFromString(response.bodyAsText())

            cities.forEach { city -> println(city) }


            cities.forEach { city ->
                println("City: ${city.name}")
                val resp = client.get("https://monitoring.localzero.net/api/cities/${city.slug}/tasks") {
                    headers {
                        append(HttpHeaders.Accept, "application/json")
                        append(HttpHeaders.ContentType, "application/json;charset=UTF-8")
                    }
                }.bodyAsText()

                val jsonArray = Json.parseToJsonElement(resp).jsonArray

                // Function to recursively handle child nodes
                fun processChildren(children: JsonArray) {
                    for (childElement in children) {
                        val childJsonObject = childElement.jsonObject

                        val source = childJsonObject["source"]?.jsonPrimitive?.int

                        if (source != null && source == 0) {
                            val title = childJsonObject["title"]?.jsonPrimitive?.content
                            val slug = childJsonObject["slugs"]?.jsonPrimitive?.content
                            val url = "https://monitoring.localzero.net/${city.slug}/massnahmen/$slug"
                            println("   Title: $title")
                            println("   URL: $url")
                            println("   Source: $source")
                        }
                        // Check if this child has more children
                        val grandChildren = childJsonObject["children"]?.jsonArray
                        if (grandChildren != null && grandChildren.isNotEmpty()) {
                            processChildren(grandChildren)
                        }
                    }
                }

                // Start processing the top-level children
                processChildren(jsonArray)


            }
        }*/
