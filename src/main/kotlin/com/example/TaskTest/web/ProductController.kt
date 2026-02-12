package com.example.TaskTest.web

import com.example.TaskTest.domain.Product
import com.example.TaskTest.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class ProductController(private val productService: ProductService) {

    @GetMapping("/")
    fun mainPage(): String {
        return "index"
    }

    @GetMapping("/products/load")
    fun loadProducts(model: Model): String {
        val products = productService.getAllProducts()
        model.addAttribute("products", products)
        return "fragments/products :: productsTableWithButton"
    }

    @PostMapping("/products/add")
    fun addProduct(
        @RequestParam id: Long,
        @RequestParam title: String,
        @RequestParam vendor: String,
        @RequestParam productType: String,
        @RequestParam price: Double,
        model: Model
    ): String {
        val product = Product(id, title, vendor, productType, price)
        productService.addProduct(product)

        val products = productService.getAllProducts()
        model.addAttribute("products", products)

        return "fragments/products :: productsTableWithButton"
    }

    @GetMapping("/fragments/product-form")
    fun productForm(): String {
        return "fragments/product-form :: productForm"
    }

    @GetMapping("/products/search")
    fun searchProducts(
        @RequestParam("query", required = false, defaultValue = "") query: String,
        model: Model
    ): String {
        val products = productService.searchProductsByTitle(query)
        model.addAttribute("products", products)
        return "fragments/products :: productsTableWithButton"
    }

    @GetMapping("/products/search-page")
    fun productSearchPage(): String {
        return "product-search"
    }

    @GetMapping("/products/{id}/edit")
    fun editProductPage(@PathVariable id: Long, model: Model): String {
        val product = productService.getProductById(id)
            ?: return "redirect:/"
        model.addAttribute("product", product)
        return "product-edit"
    }

    @PostMapping("/products/update")
    fun updateProduct(
        @RequestParam id: Long,
        @RequestParam title: String,
        @RequestParam vendor: String,
        @RequestParam productType: String,
        @RequestParam price: Double
    ): String {
        val product = Product(id, title, vendor, productType, price)
        productService.updateProduct(product)
        return "redirect:/products/list"
    }

    @GetMapping("/products/list")
    fun productsListPage(model: Model): String {
        model.addAttribute("products", productService.getAllProducts())
        return "products-list"
    }
}