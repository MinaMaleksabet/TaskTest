package com.example.TaskTest.service

import com.example.TaskTest.domain.Product
import com.example.TaskTest.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getAllProducts(): List<Product> = productRepository.findAll()

    fun addProduct(product: Product) {
        productRepository.save(product)
    }

    fun searchProductsByTitle(query: String): List<Product> =
        if (query.isBlank()) {
            productRepository.findAll()
        } else {
            productRepository.findByTitleContainingIgnoreCase(query.trim())
        }

    fun getProductById(id: Long): Product? = productRepository.findById(id)

    fun updateProduct(product: Product) {
        productRepository.update(product)
    }
}