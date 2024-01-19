package dev.morozan1.server.controller;

import dev.morozan1.server.dto.CUProductRequestDto;
import dev.morozan1.server.dto.ProductResponseDto;
import dev.morozan1.server.entity.Product;
import dev.morozan1.server.exception.BadIdException;
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
    public ResponseEntity<List<ProductResponseDto>> getProducts() {
        List<Product> products = productService.getProducts();
        List<ProductResponseDto> productResponseDtoList = products.stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();
        return new ResponseEntity<>(productResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable String id) {
        if (id == null) throw new BadIdException();
        try {
            Long idValue = Long.parseLong(id);
            Product product = productService.getProduct(idValue);
            ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
            return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @PostMapping()
    public ResponseEntity<ProductResponseDto> createProduct(@Validated @RequestBody CUProductRequestDto productRequestDto) {
        Product product = modelMapper.map(productRequestDto, Product.class);
        Product createdProduct = productService.createProduct(product);
        ProductResponseDto productResponseDto = modelMapper.map(createdProduct, ProductResponseDto.class);
        return new ResponseEntity<>(productResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable String id, @Validated @RequestBody CUProductRequestDto productRequestDto) {
        if (id == null) throw new BadIdException();
        try {
            Long idValue = Long.parseLong(id);
            Product product = modelMapper.map(productRequestDto, Product.class);
            Product updatedProduct = productService.updateProduct(idValue, product);
            ProductResponseDto productResponseDto = modelMapper.map(updatedProduct, ProductResponseDto.class);
            return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        if (id == null) throw new BadIdException();
        try {
            Long idValue = Long.parseLong(id);
            productService.deleteProduct(idValue);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }
}
