package dev.morozan1.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morozan1.server.dto.CoordinatesDto;
import dev.morozan1.server.dto.request.CUMachineProductRequestDto;
import dev.morozan1.server.dto.request.CUMachineRequestDto;
import dev.morozan1.server.dto.request.CUProductRequestDto;
import dev.morozan1.server.dto.response.MachineProductResponseDto;
import dev.morozan1.server.dto.response.MachineResponseDto;
import dev.morozan1.server.dto.response.ProductResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class MPControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCRUDMachineProduct() throws Exception {
        // Create a machine
        CUMachineRequestDto machineRequestDto = new CUMachineRequestDto();
        CoordinatesDto coordinatesDto = new CoordinatesDto();
        coordinatesDto.setLatitude(50.0755);
        coordinatesDto.setLongitude(14.4378);
        machineRequestDto.setCoordinates(coordinatesDto);
        machineRequestDto.setAddress("Test Address");

        MvcResult createMachineResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/machines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(machineRequestDto)))
                .andReturn();

        assertEquals(201, createMachineResponse.getResponse().getStatus());
        assertNotNull(createMachineResponse.getResponse().getContentAsString());

        MachineResponseDto createdMachine = objectMapper.readValue(createMachineResponse.getResponse().getContentAsString(),
                MachineResponseDto.class);
        assertNotNull(createdMachine);

        long machineId = createdMachine.getMachineId();

        // Create a product
        CUProductRequestDto productRequestDto = new CUProductRequestDto();
        productRequestDto.setName("Test Product");

        MvcResult createProductResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andReturn();

        assertEquals(201, createProductResponse.getResponse().getStatus());
        assertNotNull(createProductResponse.getResponse().getContentAsString());

        ProductResponseDto createdProduct = objectMapper.readValue(createProductResponse.getResponse().getContentAsString(),
                ProductResponseDto.class);
        assertNotNull(createdProduct);

        long productId = createdProduct.getProductId();

        // Add product to machine
        CUMachineProductRequestDto addProductRequestDto = new CUMachineProductRequestDto();
        addProductRequestDto.setIsAvailable(true);
        addProductRequestDto.setPrice(100);

        MvcResult addProductResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/machines/" + machineId + "/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addProductRequestDto)))
                .andReturn();

        assertEquals(201, addProductResponse.getResponse().getStatus());
        assertNotNull(addProductResponse.getResponse().getContentAsString());

        MachineProductResponseDto addedProductResponseDto = objectMapper.readValue(addProductResponse.getResponse().getContentAsString(),
                MachineProductResponseDto.class);

        assertNotNull(addedProductResponseDto);
        assertEquals(addProductRequestDto.getIsAvailable(), addedProductResponseDto.getIsAvailable());
        assertEquals(addProductRequestDto.getPrice(), addedProductResponseDto.getPrice());

        // Get product from machine
        MvcResult getProductResponse = mockMvc.perform(MockMvcRequestBuilders.get("/api/machines/" + machineId + "/products/" + productId))
                .andReturn();

        assertEquals(200, getProductResponse.getResponse().getStatus());
        assertNotNull(getProductResponse.getResponse().getContentAsString());

        MachineProductResponseDto getProductResponseDto = objectMapper.readValue(getProductResponse.getResponse().getContentAsString(),
                MachineProductResponseDto.class);

        assertNotNull(getProductResponseDto);
        assertEquals(addedProductResponseDto.getProduct().getProductId(), getProductResponseDto.getProduct().getProductId());
        assertEquals(addedProductResponseDto.getIsAvailable(), getProductResponseDto.getIsAvailable());
        assertEquals(addedProductResponseDto.getPrice(), getProductResponseDto.getPrice());

        // Get all products from machine
        MvcResult getAllProductsResponse = mockMvc.perform(MockMvcRequestBuilders.get("/api/machines/" + machineId + "/products"))
                .andReturn();

        assertEquals(200, getAllProductsResponse.getResponse().getStatus());
        assertNotNull(getAllProductsResponse.getResponse().getContentAsString());

        MachineProductResponseDto[] allProductsResponseDtoArray = objectMapper.readValue(getAllProductsResponse.getResponse().getContentAsString(),
                MachineProductResponseDto[].class);

        assertEquals(1, allProductsResponseDtoArray.length);

        // Update product in machine
        CUMachineProductRequestDto updateProductRequestDto = new CUMachineProductRequestDto();
        updateProductRequestDto.setIsAvailable(false);
        updateProductRequestDto.setPrice(150);

        MvcResult updateProductResponse = mockMvc.perform(MockMvcRequestBuilders.put("/api/machines/" + machineId + "/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductRequestDto)))
                .andReturn();

        assertEquals(200, updateProductResponse.getResponse().getStatus());
        assertNotNull(updateProductResponse.getResponse().getContentAsString());

        MachineProductResponseDto updateProductResponseDto = objectMapper.readValue(updateProductResponse.getResponse().getContentAsString(),
                MachineProductResponseDto.class);

        assertNotNull(updateProductResponseDto);
        assertEquals(addedProductResponseDto.getProduct().getProductId(), updateProductResponseDto.getProduct().getProductId());
        assertEquals(updateProductRequestDto.getIsAvailable(), updateProductResponseDto.getIsAvailable());
        assertEquals(updateProductRequestDto.getPrice(), updateProductResponseDto.getPrice());

        // Delete product from machine
        MvcResult deleteProductResponse = mockMvc.perform(MockMvcRequestBuilders.delete("/api/machines/" + machineId + "/products/" + productId))
                .andReturn();

        assertEquals(204, deleteProductResponse.getResponse().getStatus());

        MvcResult getProductResponseAfterDelete = mockMvc.perform(MockMvcRequestBuilders.get("/api/machines/" + machineId + "/products/" + productId))
                .andReturn();

        assertEquals(404, getProductResponseAfterDelete.getResponse().getStatus());

        // Delete machine and product
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/machines/" + machineId)).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/" + productId)).andReturn();

    }
}