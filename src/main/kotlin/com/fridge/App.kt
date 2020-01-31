package com.fridge

import com.fridge.domain.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, System.getenv("PORT").toInt(), module = Application::mainModule).start()
}


fun Application.mainModule() {
    routing {
        route()
    }
}

fun Routing.route() {
    install(StatusPages) {
        exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    val repository: Repository<String, Item> = ItemRepository()

    get("/") {
        call.respond(mapOf("status" to "Text Message"))
    }

    post("/items") {
        val request = call.receive<Item>()
        val item = repository.add(request)
        call.respond(HttpStatusCode.Created, item)
    }

    delete("/items/{id}") {
        call.parameters["id"]?.let { id ->
            repository.remove(id)
            call.respond(HttpStatusCode.OK)
        } ?: call.respond(HttpStatusCode.NotFound)
    }

    get("/items") {
        call.respond(HttpStatusCode.OK, repository.getAll())
    }
}
