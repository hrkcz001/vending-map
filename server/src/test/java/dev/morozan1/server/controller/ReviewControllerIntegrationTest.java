package dev.morozan1.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morozan1.server.dto.CoordinatesDto;
import dev.morozan1.server.dto.request.CUMachineRequestDto;
import dev.morozan1.server.dto.request.CUReviewRequestDto;
import dev.morozan1.server.dto.response.MachineResponseDto;
import dev.morozan1.server.dto.response.ReviewResponseDto;
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
public class ReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCRUDReview() throws Exception {
        // Create a machine to associate the review
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

        // Create
        CUReviewRequestDto requestDto = new CUReviewRequestDto();
        requestDto.setRating((short) 5);
        requestDto.setComment("This is a test review content");

        assertNotNull(requestDto);

        MvcResult createResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/machines/" + machineId + "/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andReturn();

        assertEquals(201, createResponse.getResponse().getStatus());
        assertNotNull(createResponse.getResponse().getContentAsString());

        ReviewResponseDto createResponseDto = objectMapper.readValue(createResponse.getResponse().getContentAsString(),
                ReviewResponseDto.class);

        assertNotNull(createResponseDto);
        assertEquals(requestDto.getRating(), createResponseDto.getRating());
        assertEquals(requestDto.getComment(), createResponseDto.getComment());

        // Read
        long reviewId = createResponseDto.getReviewId();

        MvcResult getResponse = mockMvc.perform(MockMvcRequestBuilders.get("/api/machines/" + machineId + "/reviews/" + reviewId))
                .andReturn();

        assertEquals(200, getResponse.getResponse().getStatus());
        assertNotNull(getResponse.getResponse().getContentAsString());

        ReviewResponseDto getResponseDto = objectMapper.readValue(getResponse.getResponse().getContentAsString(),
                ReviewResponseDto.class);

        assertNotNull(getResponseDto);
        assertEquals(reviewId, getResponseDto.getReviewId());
        assertEquals(createResponseDto.getRating(), getResponseDto.getRating());
        assertEquals(createResponseDto.getComment(), getResponseDto.getComment());

        // Read all
        MvcResult getAllResponse = mockMvc.perform(MockMvcRequestBuilders.get("/api/machines/" + machineId + "/reviews"))
                .andReturn();

        assertEquals(200, getAllResponse.getResponse().getStatus());
        assertNotNull(getAllResponse.getResponse().getContentAsString());

        ReviewResponseDto[] getAllResponseDto = objectMapper.readValue(getAllResponse.getResponse().getContentAsString(),
                ReviewResponseDto[].class);

        assertNotNull(getAllResponseDto);
        assertEquals(1, getAllResponseDto.length);

        // Update
        CUReviewRequestDto updateRequestDto = new CUReviewRequestDto();
        updateRequestDto.setRating((short) 4);
        updateRequestDto.setComment("This is an updated test review content");

        MvcResult updateResponse = mockMvc.perform(MockMvcRequestBuilders.put("/api/machines/" + machineId + "/reviews/" + reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andReturn();

        assertEquals(200, updateResponse.getResponse().getStatus());
        assertNotNull(updateResponse.getResponse().getContentAsString());

        ReviewResponseDto updateResponseDto = objectMapper.readValue(updateResponse.getResponse().getContentAsString(),
                ReviewResponseDto.class);

        assertNotNull(updateResponseDto);
        assertEquals(reviewId, updateResponseDto.getReviewId());
        assertEquals(updateRequestDto.getRating(), updateResponseDto.getRating());
        assertEquals(updateRequestDto.getComment(), updateResponseDto.getComment());

        // Delete
        MvcResult deleteResponse = mockMvc.perform(MockMvcRequestBuilders.delete("/api/machines/" + machineId + "/reviews/" + reviewId))
                .andReturn();

        assertEquals(204, deleteResponse.getResponse().getStatus());

        MvcResult getResponseAfterDelete = mockMvc.perform(MockMvcRequestBuilders.get("/api/machines/" + machineId + "/reviews/" + reviewId))
                .andReturn();

        assertEquals(404, getResponseAfterDelete.getResponse().getStatus());

        // Delete machine
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/machines/" + machineId)).andReturn();
    }
}