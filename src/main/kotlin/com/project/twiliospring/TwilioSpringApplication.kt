package com.project.twiliospring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TwilioSpringApplication

fun main(args: Array<String>) {
    runApplication<TwilioSpringApplication>(*args)
}
