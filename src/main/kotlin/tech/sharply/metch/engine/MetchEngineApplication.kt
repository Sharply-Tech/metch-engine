package tech.sharply.metch.engine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MetchEngineApplication

fun main(args: Array<String>) {
    runApplication<MetchEngineApplication>(*args)
}
