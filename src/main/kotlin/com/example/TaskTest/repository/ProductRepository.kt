package com.example.TaskTest.repository

import com.example.TaskTest.domain.Product
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(
    private val jdbcClient: JdbcClient
) {

    fun save(product: Product) {
        jdbcClient.sql(
            """
            INSERT INTO products (id, title, vendor, product_type, price)
            VALUES (:id, :title, :vendor, :productType, :price)
            ON CONFLICT (id) DO NOTHING
            """
        )
            .param("id", product.id)
            .param("title", product.title)
            .param("vendor", product.vendor)
            .param("productType", product.productType)
            .param("price", product.price)
            .update()
    }

    fun findAll(): List<Product> {
        return jdbcClient.sql(
            """
            SELECT id, title, vendor, product_type, price
            FROM products
            ORDER BY id
            """
        )
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    vendor = rs.getString("vendor"),
                    productType = rs.getString("product_type"),
                    price = rs.getDouble("price")
                )
            }
            .list()
    }

    fun findByTitleContainingIgnoreCase(query: String): List<Product> {
        val pattern = "%${query}%"
        return jdbcClient.sql(
            """
            SELECT id, title, vendor, product_type, price
            FROM products
            WHERE LOWER(title) LIKE LOWER(:pattern)
            ORDER BY id
            """
        )
            .param("pattern", pattern)
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    vendor = rs.getString("vendor"),
                    productType = rs.getString("product_type"),
                    price = rs.getDouble("price")
                )
            }
            .list()
    }

    fun findById(id: Long): Product? {
        return jdbcClient.sql(
            """
            SELECT id, title, vendor, product_type, price
            FROM products
            WHERE id = :id
            """
        )
            .param("id", id)
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    vendor = rs.getString("vendor"),
                    productType = rs.getString("product_type"),
                    price = rs.getDouble("price")
                )
            }
            .optional()
            .orElse(null)
    }

    fun update(product: Product) {
        jdbcClient.sql(
            """
            UPDATE products
            SET title = :title, vendor = :vendor, product_type = :productType, price = :price
            WHERE id = :id
            """
        )
            .param("id", product.id)
            .param("title", product.title)
            .param("vendor", product.vendor)
            .param("productType", product.productType)
            .param("price", product.price)
            .update()
    }


    fun deleteById(id: Long) {
        jdbcClient.sql(
            """
        DELETE FROM products
        WHERE id = :id
        """
        )
            .param("id", id)
            .update()
    }

    fun existsById(id: Long): Boolean {
        return jdbcClient.sql(
            """
        SELECT 1
        FROM products
        WHERE id = :id
        LIMIT 1
        """
        )
            .param("id", id)
            .query { _, _ -> 1 }
            .list()              // get a Kotlin List<Int>
            .isNotEmpty()        // true if at least one row
    }


}