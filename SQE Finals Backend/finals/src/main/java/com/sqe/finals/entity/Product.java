package com.sqe.finals.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties(value = {"products"}, allowSetters = true)
    @JsonBackReference(value = "category-products") // Specify the reference name
    private Category category;

    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "size")
    @Column(name = "quantity")
    private Map<String, Integer> sizes = new HashMap<>();

    private String imageSide;
    private String imageTop;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Map<String, Integer> getSizes() {
        return sizes;
    }

    public void setSizes(Map<String, Integer> sizes) {
        this.sizes = sizes;
    }

    public String getImageSide() {
        return imageSide;
    }

    public void setImageSide(String imageSide) {
        this.imageSide = imageSide;
    }

    public String getImageTop() {
        return imageTop;
    }

    public void setImageTop(String imageTop) {
        this.imageTop = imageTop;
    }
}

