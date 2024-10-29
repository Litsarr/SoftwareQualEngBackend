package com.sqe.finals.datainit;

import com.sqe.finals.entity.Category;

import com.sqe.finals.repository.CategoryRepository;
import com.sqe.finals.repository.ProductSizeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataInitializationService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        // Populate Categories if empty
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("Islander Classics"));
            categoryRepository.save(new Category("Islander Outdoors"));
            categoryRepository.save(new Category("Women"));
            categoryRepository.save(new Category("Kids"));
        }
    }


}