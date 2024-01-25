package dev.morozan1.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morozan1.server.dto.request.CUProductRequestDto;
import dev.morozan1.server.dto.response.ProductResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCRUDProduct() throws Exception {
        //Create
        CUProductRequestDto requestDto = new CUProductRequestDto();
        requestDto.setName("Test Product");
        requestDto.setPicture("test_picture_url");

        assertNotNull(requestDto);

        MvcResult createResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andReturn();

        assertEquals(201, createResponse.getResponse().getStatus());
        assertNotNull(createResponse.getResponse().getContentAsString());

        ProductResponseDto createResponseDto = objectMapper.readValue(createResponse.getResponse().getContentAsString(),
                ProductResponseDto.class);

        assertNotNull(createResponseDto);
        assertEquals(requestDto.getName(), createResponseDto.getName());
        assertEquals(requestDto.getPicture(), createResponseDto.getPicture());

        //Read
        long productId = createResponseDto.getProductId();

        MvcResult getResponse = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + productId))
                .andReturn();

        assertEquals(200, getResponse.getResponse().getStatus());
        assertNotNull(getResponse.getResponse().getContentAsString());

        ProductResponseDto getResponseDto = objectMapper.readValue(getResponse.getResponse().getContentAsString(),
                ProductResponseDto.class);

        assertNotNull(getResponseDto);
        assertEquals(productId, getResponseDto.getProductId());
        assertEquals(createResponseDto.getName(), getResponseDto.getName());
        assertEquals(createResponseDto.getPicture(), getResponseDto.getPicture());

        //Read all
        MvcResult getAllResponse = mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andReturn();

        assertEquals(200, getAllResponse.getResponse().getStatus());
        assertNotNull(getAllResponse.getResponse().getContentAsString());

        ProductResponseDto[] getAllResponseDto = objectMapper.readValue(getAllResponse.getResponse().getContentAsString(),
                ProductResponseDto[].class);

        assertNotNull(getAllResponseDto);
        assertEquals(1, getAllResponseDto.length);

        //Update
        CUProductRequestDto updateRequestDto = new CUProductRequestDto();
        updateRequestDto.setName("Updated Product");
        updateRequestDto.setPicture("updated_picture_url");

        MvcResult updateResponse = mockMvc.perform(MockMvcRequestBuilders.put("/api/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andReturn();

        assertEquals(200, updateResponse.getResponse().getStatus());
        assertNotNull(updateResponse.getResponse().getContentAsString());

        ProductResponseDto updateResponseDto = objectMapper.readValue(updateResponse.getResponse().getContentAsString(),
                ProductResponseDto.class);

        assertNotNull(updateResponseDto);
        assertEquals(productId, updateResponseDto.getProductId());
        assertEquals(updateRequestDto.getName(), updateResponseDto.getName());
        assertEquals(updateRequestDto.getPicture(), updateResponseDto.getPicture());

        //Delete
        MvcResult deleteResponse = mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/" + productId))
                .andReturn();

        assertEquals(204, deleteResponse.getResponse().getStatus());

        MvcResult getResponseAfterDelete = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + productId))
                .andReturn();

        assertEquals(404, getResponseAfterDelete.getResponse().getStatus());
    }
}