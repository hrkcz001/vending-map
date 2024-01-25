package dev.morozan1.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morozan1.server.dto.request.CUMachineRequestDto;
import dev.morozan1.server.dto.CoordinatesDto;
import dev.morozan1.server.dto.response.MachineResponseDto;
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
public class MachineControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCRUDMachine() throws Exception {
        //Create
        CUMachineRequestDto requestDto = new CUMachineRequestDto();
        CoordinatesDto coordinatesDto = new CoordinatesDto();
        coordinatesDto.setLatitude(50.0755);
        coordinatesDto.setLongitude(14.4378);
        requestDto.setCoordinates(coordinatesDto);
        requestDto.setAddress("Test Address");

        assertNotNull(requestDto);

        MvcResult createResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/machines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andReturn();

        assertEquals(201, createResponse.getResponse().getStatus());
        assertNotNull(createResponse.getResponse().getContentAsString());

        MachineResponseDto createResponseDto = objectMapper.readValue(createResponse.getResponse().getContentAsString(),
                MachineResponseDto.class);

        assertNotNull(createResponseDto);
        assertEquals(requestDto.getAddress(), createResponseDto.getAddress());
        assertEquals(requestDto.getCoordinates(), createResponseDto.getCoordinates());

        //Read
        long machineId = createResponseDto.getMachineId();

        MvcResult getResponse = mockMvc.perform(MockMvcRequestBuilders.get("/api/machines/" + machineId))
                .andReturn();

        assertEquals(200, getResponse.getResponse().getStatus());
        assertNotNull(getResponse.getResponse().getContentAsString());

        MachineResponseDto getResponseDto = objectMapper.readValue(getResponse.getResponse().getContentAsString(),
                MachineResponseDto.class);

        assertNotNull(getResponseDto);
        assertEquals(machineId, getResponseDto.getMachineId());
        assertEquals(createResponseDto.getAddress(), getResponseDto.getAddress());
        assertEquals(createResponseDto.getCoordinates(), getResponseDto.getCoordinates());

        //Read all
        MvcResult getAllResponse = mockMvc.perform(MockMvcRequestBuilders.get("/api/machines"))
                .andReturn();

        assertEquals(200, getAllResponse.getResponse().getStatus());
        assertNotNull(getAllResponse.getResponse().getContentAsString());

        MachineResponseDto[] getAllResponseDto = objectMapper.readValue(getAllResponse.getResponse().getContentAsString(),
                MachineResponseDto[].class);

        assertNotNull(getAllResponseDto);
        assertEquals(1, getAllResponseDto.length);

        //Update
        CUMachineRequestDto updateRequestDto = new CUMachineRequestDto();
        updateRequestDto.setAddress("Updated Address");
        updateRequestDto.setCoordinates(coordinatesDto);

        MvcResult updateResponse = mockMvc.perform(MockMvcRequestBuilders.put("/api/machines/" + machineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andReturn();

        assertEquals(200, updateResponse.getResponse().getStatus());
        assertNotNull(updateResponse.getResponse().getContentAsString());

        MachineResponseDto updateResponseDto = objectMapper.readValue(updateResponse.getResponse().getContentAsString(),
                MachineResponseDto.class);

        assertNotNull(updateResponseDto);
        assertEquals(machineId, updateResponseDto.getMachineId());
        assertEquals(updateRequestDto.getAddress(), updateResponseDto.getAddress());
        assertEquals(updateRequestDto.getCoordinates(), updateResponseDto.getCoordinates());

        //Delete
        MvcResult deleteResponse = mockMvc.perform(MockMvcRequestBuilders.delete("/api/machines/" + machineId))
                .andReturn();

        assertEquals(204, deleteResponse.getResponse().getStatus());

        MvcResult getResponseAfterDelete = mockMvc.perform(MockMvcRequestBuilders.get("/api/machines/" + machineId))
                .andReturn();

        assertEquals(404, getResponseAfterDelete.getResponse().getStatus());
    }
}