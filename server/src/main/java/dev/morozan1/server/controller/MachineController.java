package dev.morozan1.server.controller;

import dev.morozan1.server.dto.CUMachineRequestDto;
import dev.morozan1.server.dto.MachineResponseDto;
import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.exception.NoIdException;
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
        List<Machine> machines = machineService.getMachines(latitude, longitude, radius);


        List<MachineResponseDto> machineResponseDtoList = machines.stream()
                    .map(machine -> modelMapper.map(machine, MachineResponseDto.class))
                    .toList();

        return new ResponseEntity<>(machineResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineResponseDto> getMachine(@PathVariable String id) {
        if (id == null) throw new NoIdException();
        try {
            Long idValue = Long.parseLong(id);
            Machine machine = machineService.getMachine(idValue);
            MachineResponseDto machineResponseDto = modelMapper.map(machine, MachineResponseDto.class);
            return new ResponseEntity<>(machineResponseDto, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new NoIdException();
        }
    }

    @PostMapping
    public ResponseEntity<MachineResponseDto> createMachine(@Validated @RequestBody CUMachineRequestDto machineRequestDto) {
        Machine machine = modelMapper.map(machineRequestDto, Machine.class);
        Machine createdMachine = machineService.createMachine(machine);
        MachineResponseDto machineResponseDto = modelMapper.map(createdMachine, MachineResponseDto.class);
        return new ResponseEntity<>(machineResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MachineResponseDto> updateMachine(@PathVariable String id, @Validated @RequestBody CUMachineRequestDto machineRequestDto) {
        if (id == null) throw new NoIdException();
        try {
            Long idValue = Long.parseLong(id);
            Machine machine = modelMapper.map(machineRequestDto, Machine.class);
            Machine updatedMachine = machineService.updateMachine(idValue, machine);
            MachineResponseDto machineResponseDto = modelMapper.map(updatedMachine, MachineResponseDto.class);
            return new ResponseEntity<>(machineResponseDto, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new NoIdException();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMachine(@PathVariable String id) {
        if (id == null) throw new NoIdException();
        try {
            Long idValue = Long.parseLong(id);
            machineService.deleteMachine(idValue);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new NoIdException();
        }
    }
}