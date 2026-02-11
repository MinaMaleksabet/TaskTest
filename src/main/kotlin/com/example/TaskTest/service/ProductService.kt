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
}
