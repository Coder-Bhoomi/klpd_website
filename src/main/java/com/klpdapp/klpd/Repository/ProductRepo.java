package com.klpdapp.klpd.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.klpdapp.klpd.model.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {

        List<Product> findTop4ByOrderByCreatedAtDesc();

        List<Product> findTop4ByOrderByHitsDesc();

        @Modifying
        @Query("UPDATE Product p SET p.hits = p.hits + 1 WHERE p.pid = :pid")
        void incrementHits(@Param("pid") Integer pid);

        @Modifying
        @Query("Update Product p Set p.sales=p.sales + 1 where p.pid = :pid")
        void incrementSales(int pid);

        @Query(value = """
                            SELECT DISTINCT
                               CONCAT(
                                   TRIM(GROUP_CONCAT(a.attribute_value ORDER BY a.attribute_name SEPARATOR ' ')),
                                   CASE
                                       WHEN LOWER(p.prod_name) LIKE '%wide%' THEN ' Wide'
                                       WHEN LOWER(p.prod_name) LIKE '%tall%' THEN ' Tall'
                                       ELSE ''
                                   END
                               ) AS size_variant
                            FROM products p
                            JOIN attribute_table a ON p.pid = a.pid
                            WHERE p.subcategory_id = :subcategoryId
                              AND a.attribute_name IN ('capacity','diameter','color')
                            GROUP BY p.pid, p.prod_name
                            HAVING (
                                  (:isInduction = FALSE AND LOWER(p.prod_name) NOT LIKE '%induction%')

                               OR (:isInduction = TRUE  AND LOWER(p.prod_name) LIKE '%induction%')
                            )
                            ORDER BY size_variant
                        """, nativeQuery = true)
        List<String> findDistinctSizeVariants(
                        @Param("subcategoryId") String subcategoryId,
                        @Param("isInduction") Boolean isInduction);

        @Query(value = """
                            SELECT p.*
                            FROM products p
                            WHERE p.subcategory_id = :subcategoryId
                              AND (
                                  (:induction = TRUE AND LOWER(p.prod_name) LIKE '%induction%')
                                  OR (:induction = FALSE AND LOWER(p.prod_name) NOT LIKE '%induction%')
                              )
                              AND (
                                  CONCAT(
                                      (SELECT GROUP_CONCAT(DISTINCT at.attribute_value ORDER BY at.attribute_name SEPARATOR ' ')
                                       FROM attribute_table at
                                       WHERE at.pid = p.pid
                                         AND at.attribute_name IN ('capacity','diameter','color')),
                                      CASE
                                          WHEN LOWER(p.prod_name) LIKE '%wide%' THEN ' Wide'
                                          WHEN LOWER(p.prod_name) LIKE '%tall%' THEN ' Tall'
                                          ELSE ''
                                      END
                                  ) = :size
                              )
                        """, nativeQuery = true)
        List<Product> findProductsBySizeAndSubcategory(
                        @Param("size") String size,
                        @Param("subcategoryId") String subcategoryId,
                        @Param("induction") boolean induction);

        Product findBySubcategory_SubcategoryId(String subcategoryId);

        List<Product> findTop4BySubcategorySubcategoryIdAndPidNot(String subcategoryId, int pid);

        List<Product> findTop4ByOrderBySalesDesc();

        List<Product> findTop4ByOrderByStockAsc();

        List<Product> findBySubcategory_Category_CategoryId(String categoryId);

        @Query("SELECT p FROM Product p JOIN FETCH p.subcategory sc JOIN FETCH sc.category WHERE p.id IN :productIds")
        List<Product> findProductsWithSubcategoryAndCategory(@Param("productIds") List<Integer> pids);

}
