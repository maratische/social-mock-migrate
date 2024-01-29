package com.examples.uidcreator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class SocialMockApplication

fun main(args: Array<String>) {
	runApplication<SocialMockApplication>(*args)
}
