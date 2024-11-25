package com.sqe.finals.datainit;

import com.sqe.finals.entity.Category;

import com.sqe.finals.repository.CategoryRepository;
import com.sqe.finals.repository.ProductSizeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataInitializationService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);

    @PostConstruct
    public void init() {
        // Populate Categories if empty
        if (categoryRepository.count() == 0) {
            logger.info("Initializing categories...");
            categoryRepository.save(new Category("Islander Classics"));
            categoryRepository.save(new Category("Islander Outdoors"));
            categoryRepository.save(new Category("Women"));
            categoryRepository.save(new Category("Kids"));
            logger.info("Categories initialized.");
        } else {
            logger.info("Categories already initialized.");
        }

        // Log all categories to verify
        List<Category> categories = categoryRepository.findAll();
        categories.forEach(category -> logger.info("Category: {}", category.getName()));
    }
}