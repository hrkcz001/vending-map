package dev.morozan1.server.service;

import dev.morozan1.server.entity.MachineProduct;
import dev.morozan1.server.entity.Product;
import dev.morozan1.server.repository.ProductRepository;
import dev.morozan1.server.repository.MachineProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, MachineProductRepository machineProductRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsExcludeMachine(long excludedMachineId) {
        return productRepository.findProductsThatNotIn(excludedMachineId);
    }

    public Product getProduct(Long idValue) {
        return productRepository.findById(idValue).orElseThrow();
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Objects.requireNonNull(product);
        Product productToUpdate = productRepository.findById(id).orElseThrow();
        productToUpdate.setName(product.getName());
        productToUpdate.setPicture(product.getPicture());
        return productRepository.save(productToUpdate);
    }

    public void deleteProduct(Long idValue) {
        productRepository.findById(idValue).orElseThrow();
        productRepository.deleteById(idValue);
    }
}
