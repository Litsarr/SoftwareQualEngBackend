package com.sqe.finals.service;

import com.sqe.finals.entity.ProductSize;
import com.sqe.finals.repository.ProductSizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductSizeService {

    @Autowired
    private ProductSizeRepository productSizeRepository;

    public List<ProductSize> findAll() {
        return productSizeRepository.findAll();
    }

    public ProductSize save(ProductSize productSize) {
        return productSizeRepository.save(productSize);
    }

    public void deleteById(Long id) {
        productSizeRepository.deleteById(id);
    }
}

