package com.example.TaskTest.scheduler

import com.example.TaskTest.domain.Product
import com.example.TaskTest.repository.ProductRepository
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ProductImportJob(
    private val productRepository: ProductRepository
) {

    private val mapper = jacksonObjectMapper()

    @Scheduled(initialDelay = 0, fixedDelay = Long.MAX_VALUE)
    fun importProducts() {

        val inputStream = javaClass.classLoader
            .getResourceAsStream("data/products.json")
            ?: run {
                println("products.json not found")
                return
            }

        if (inputStream.available() == 0) {
            println("products.json is empty")
            return
        }

        val root: JsonNode = mapper.readTree(inputStream)["products"]

        if (root == null || !root.isArray) {
            println("products.json must contain 'products' array")
            return
        }

        root.take(50).forEach { node ->


            val price = node["variants"]
                ?.firstOrNull()
                ?.get("price")
                ?.asDouble()
                ?: 0.0

            val product = Product(
                id = node["id"].asLong(),
                title = node["title"].asText(),
                vendor = node["vendor"].asText(),
                productType = node["product_type"].asText(),
                price = price
            )

            productRepository.save(product)
        }

        println(" Products loaded from JSON")
    }
}