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

import static dev.morozan1.server.util.MachineDtoMapper.convertToDto;

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
    public ResponseEntity<List<MachineResponseDto>> getMachines(@RequestParam(required = false) Double latitude,
                                                                @RequestParam(required = false) Double longitude,
                                                                @RequestParam(required = false) Double radius) {
        List<Machine> machines = machineService.getMachines(latitude, longitude, radius);

        List<MachineResponseDto> machineResponseDto = machines.stream()
                .map(machine -> convertToDto(machine, modelMapper))
                .toList();

        return ResponseEntity.ok(machineResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineResponseDto> getMachine(@PathVariable Long id) {
        Machine machine = machineService.getMachine(id);

        MachineResponseDto machineResponseDto = convertToDto(machine, modelMapper);

        return ResponseEntity.ok(machineResponseDto);
    }

    @PostMapping
    public ResponseEntity<MachineResponseDto> createMachine(@Validated @RequestBody CreateMachineRequestDto machineRequestDto) {
        Machine machine = modelMapper.map(machineRequestDto, Machine.class);
        Machine createdMachine = machineService.createMachine(machine);
        MachineResponseDto machineResponseDto = convertToDto(createdMachine, modelMapper);
        return ResponseEntity.ok(machineResponseDto);
    }
}
