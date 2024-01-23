package dev.morozan1.server.controller;

import dev.morozan1.server.dto.request.CUProductRequestDto;
import dev.morozan1.server.dto.response.ProductResponseDto;
import dev.morozan1.server.entity.Product;
import dev.morozan1.server.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponseDto>> getProducts(@RequestParam(required = false) Long excludeMachineId ) {

        List<Product> products;
        if (excludeMachineId != null) {
            products = productService.getProductsExcludeMachine(excludeMachineId);
        } else {
            products = productService.getProducts();
        }
        List<ProductResponseDto> productResponseDtoList = products.stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();
        return new ResponseEntity<>(productResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable long id) {
        Product product = productService.getProduct(id);
        ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ProductResponseDto> createProduct(@Validated @RequestBody CUProductRequestDto productRequestDto) {
        Product product = modelMapper.map(productRequestDto, Product.class);
        Product createdProduct = productService.createProduct(product);
        ProductResponseDto productResponseDto = modelMapper.map(createdProduct, ProductResponseDto.class);
        return new ResponseEntity<>(productResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable long id,
                                                            @Validated @RequestBody CUProductRequestDto productRequestDto) {
        Product product = modelMapper.map(productRequestDto, Product.class);
        Product updatedProduct = productService.updateProduct(id, product);
        ProductResponseDto productResponseDto = modelMapper.map(updatedProduct, ProductResponseDto.class);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
