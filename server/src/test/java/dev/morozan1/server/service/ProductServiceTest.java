package dev.morozan1.server.service;

import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.entity.MachineProduct;
import dev.morozan1.server.entity.Product;
import dev.morozan1.server.repository.ProductRepository;
import dev.morozan1.server.repository.MachineProductRepository;
import dev.morozan1.server.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private final long PRODUCT_ID = 1;

    @Test
    public void testGetProducts() {
        Mockito.when(productRepository.findAll()).thenReturn(List.of(new Product(), new Product()));

        List<Product> products = productService.getProducts();

        assertEquals(2, products.size());
    }

    @Test
    public void testGetProduct() {
        Product mockProduct = new Product();
        Mockito.when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(mockProduct));

        Product product = productService.getProduct(PRODUCT_ID);

        assertNotNull(product);
    }

    @Test
    public void testCreateProduct() {
        Product productToCreate = new Product();
        productToCreate.setProductId(PRODUCT_ID);
        Mockito.when(productRepository.save(productToCreate)).thenReturn(productToCreate);

        Product createdProduct = productService.createProduct(productToCreate);

        assertEquals(productToCreate, createdProduct);
    }

    @Test
    public void testUpdateProduct() {
        Product oldProduct = new Product();
        oldProduct.setName("Old Name");
        oldProduct.setPicture("Old Picture");

        Product newProduct = new Product();
        newProduct.setName("New Name");
        newProduct.setPicture("New Picture");

        Mockito.when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(oldProduct));
        Mockito.when(productRepository.save(oldProduct)).thenReturn(oldProduct);

        Product updatedProduct = productService.updateProduct(PRODUCT_ID, newProduct);

        assertEquals(newProduct.getName(), updatedProduct.getName());
        assertEquals(newProduct.getPicture(), updatedProduct.getPicture());
    }

    @Test
    public void testDeleteProduct() {
        Mockito.when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(new Product()));

        productService.deleteProduct(PRODUCT_ID);

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(PRODUCT_ID);
    }
}