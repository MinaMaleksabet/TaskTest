package com.example.TaskTest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TaskTestApplication

fun main(args: Array<String>) {
	runApplication<TaskTestApplication>(*args)
}
