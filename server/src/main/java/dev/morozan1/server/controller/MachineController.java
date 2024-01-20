package dev.morozan1.server.controller;

import dev.morozan1.server.dto.CUMachineRequestDto;
import dev.morozan1.server.dto.CUReviewRequestDto;
import dev.morozan1.server.dto.MachineResponseDto;
import dev.morozan1.server.dto.ReviewResponseDto;
import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.entity.Review;
import dev.morozan1.server.exception.BadRequestException;
import dev.morozan1.server.exception.BadIdException;
import dev.morozan1.server.service.MachineService;
import dev.morozan1.server.service.ReviewService;
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
    private final ReviewService reviewService;

    private final ModelMapper modelMapper;

    @Autowired
    public MachineController(MachineService machineService, ReviewService reviewService, ModelMapper modelMapper) {
        this.machineService = machineService;
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<MachineResponseDto>> getMachines(@RequestParam(required = false) String latitude,
                                                                @RequestParam(required = false) String longitude,
                                                                @RequestParam(required = false) String radius) {
        try {
            List<Machine> machines;
            if (latitude != null && longitude != null && radius != null) {
                double latitudeValue = Double.parseDouble(latitude);
                double longitudeValue = Double.parseDouble(longitude);
                double radiusValue = Double.parseDouble(radius);
                machines = machineService.getMachinesInRadius(latitudeValue, longitudeValue, radiusValue);
            }else {
                machines = machineService.getMachines();
            }

            List<MachineResponseDto> machineResponseDtoList = machines.stream()
                    .map(machine -> modelMapper.map(machine, MachineResponseDto.class))
                    .toList();

            return new ResponseEntity<>(machineResponseDtoList, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Latitude, longitude and radius must be numbers in double format");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineResponseDto> getMachine(@PathVariable String id) {
        if (id == null) throw new BadIdException();
        try {
            long idValue = Long.parseLong(id);
            Machine machine = machineService.getMachine(idValue);
            MachineResponseDto machineResponseDto = modelMapper.map(machine, MachineResponseDto.class);
            return new ResponseEntity<>(machineResponseDto, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
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
        if (id == null) throw new BadIdException();
        try {
            long idValue = Long.parseLong(id);
            Machine machine = modelMapper.map(machineRequestDto, Machine.class);
            Machine updatedMachine = machineService.updateMachine(idValue, machine);
            MachineResponseDto machineResponseDto = modelMapper.map(updatedMachine, MachineResponseDto.class);
            return new ResponseEntity<>(machineResponseDto, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMachine(@PathVariable String id) {
        if (id == null) throw new BadIdException();
        try {
            long idValue = Long.parseLong(id);
            machineService.deleteMachine(idValue);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }
}