package dev.morozan1.server.controller;

import dev.morozan1.server.dto.request.CUMachineRequestDto;
import dev.morozan1.server.dto.response.MachineResponseDto;
import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.service.MachineService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<Machine> machines;
        if (latitude != null && longitude != null && radius != null) {
               machines = machineService.getMachinesInRadius(latitude, longitude, radius);
        }else {
            machines = machineService.getMachines();
        }

        List<MachineResponseDto> machineResponseDtoList = machines.stream()
                .map(machine -> modelMapper.map(machine, MachineResponseDto.class))
                .toList();

        return new ResponseEntity<>(machineResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineResponseDto> getMachine(@PathVariable long id) {
        Machine machine = machineService.getMachine(id);
        MachineResponseDto machineResponseDto = modelMapper.map(machine, MachineResponseDto.class);
        return new ResponseEntity<>(machineResponseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MachineResponseDto> createMachine(@Validated @RequestBody CUMachineRequestDto machineRequestDto) {
        Machine machine = modelMapper.map(machineRequestDto, Machine.class);
        Machine createdMachine = machineService.createMachine(machine);
        MachineResponseDto machineResponseDto = modelMapper.map(createdMachine, MachineResponseDto.class);
        return new ResponseEntity<>(machineResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MachineResponseDto> updateMachine(@PathVariable long id,
                                                            @Validated @RequestBody CUMachineRequestDto machineRequestDto) {
        Machine machine = modelMapper.map(machineRequestDto, Machine.class);
        Machine updatedMachine = machineService.updateMachine(id, machine);
        MachineResponseDto machineResponseDto = modelMapper.map(updatedMachine, MachineResponseDto.class);
        return new ResponseEntity<>(machineResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMachine(@PathVariable long id) {
        machineService.deleteMachine(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}