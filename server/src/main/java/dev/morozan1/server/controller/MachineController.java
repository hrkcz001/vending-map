package dev.morozan1.server.controller;

import dev.morozan1.server.dto.CreateMachineRequestDto;
import dev.morozan1.server.dto.MachineResponseDto;
import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.service.MachineService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static dev.morozan1.server.dto.mapper.MachineToMachineResponseDtoMapper.convertToDto;

@RestController
@RequestMapping("/api/machines")
public class MachineController {

    private final MachineService machineService;

    private final ModelMapper modelMapper;

    @Autowired
    public MachineController(MachineService machineService, ModelMapper modelMapper) {
        this.machineService = machineService;
        this.modelMapper = modelMapper;
    }


    @GetMapping
    public ResponseEntity<?> getMachines(@RequestParam(required = false) Double latitude,
                                                                @RequestParam(required = false) Double longitude,
                                                                @RequestParam(required = false) Double radius) {
        try {
            List<Machine> machines = machineService.getMachines(latitude, longitude, radius);


            List<MachineResponseDto> machineResponseDto = machines.stream()
                    .map(machine -> convertToDto(machine, modelMapper))
                    .toList();

            return ResponseEntity.ok(machineResponseDto);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMachine(@PathVariable Long id) {
        try {
            Machine machine = machineService.getMachine(id);
            MachineResponseDto machineResponseDto = convertToDto(machine, modelMapper);
            return ResponseEntity.ok(machineResponseDto);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createMachine(@Validated @RequestBody CreateMachineRequestDto machineRequestDto) {
        try {
            Machine machine = modelMapper.map(machineRequestDto, Machine.class);
            Machine createdMachine = machineService.createMachine(machine);
            MachineResponseDto machineResponseDto = convertToDto(createdMachine, modelMapper);
            return ResponseEntity.ok(machineResponseDto);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
