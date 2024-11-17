package com.sqe.finals.entity;

import java.util.Map;

public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Map<String, Integer> sizes;
    private String imageSideUrl;
    private String imageTopUrl;
    private CategoryDTO category;

    // Getters and setters
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

    public Map<String, Integer> getSizes() {
        return sizes;
    }

    public void setSizes(Map<String, Integer> sizes) {
        this.sizes = sizes;
    }

    public String getImageSideUrl() {
        return imageSideUrl;
    }

    public void setImageSideUrl(String imageSideUrl) {
        this.imageSideUrl = imageSideUrl;
    }

    public String getImageTopUrl() {
        return imageTopUrl;
    }

    public void setImageTopUrl(String imageTopUrl) {
        this.imageTopUrl = imageTopUrl;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }
}

