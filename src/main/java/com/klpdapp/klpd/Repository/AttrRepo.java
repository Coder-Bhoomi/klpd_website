package com.klpdapp.klpd.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.klpdapp.klpd.model.Attribute;
import com.klpdapp.klpd.model.Product;

public interface AttrRepo extends JpaRepository<Attribute, Integer> {
    
    List<Attribute> findByAttributeName(String name);

    List<Attribute> findByProduct(Product prod);

    @Query("SELECT DISTINCT a.attributeName FROM Attribute a")
    List<String> findDistinctAttributeName();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("DELETE FROM Attribute a WHERE a.product = :product AND a.attribute_id NOT IN :ids")
    void deleteByProductAndAttributeIdNotIn(@Param("product") Product product, @Param("ids") List<Integer> ids);
    
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("DELETE FROM Attribute a WHERE a.product = :product")
    void deleteByProduct(@Param("product") Product product);
    
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Attribute a SET a.attributeName = :name, a.attributeValue = :value WHERE a.attribute_id = :id")
    void updateAttribute(@Param("id") int id, @Param("name") String name, @Param("value") String value);

}
