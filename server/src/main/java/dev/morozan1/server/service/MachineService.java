package dev.morozan1.server.service;

import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.repository.MachineProductRepository;
import dev.morozan1.server.repository.MachineRepository;
import dev.morozan1.server.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@Service
public class MachineService {

    private final MachineRepository machineRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository, MachineProductRepository machineProductRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.machineRepository = machineRepository;
    }

    private double distance (double latitude1, double longitude1, double latitude2, double longitude2) {
        return Math.sqrt(Math.pow(latitude1 - latitude2, 2) + Math.pow(longitude1 - longitude2, 2));
    }

    public List<Machine> getMachines(Double latitude, Double longitude, Double radius) {
        List<Machine> machines = machineRepository.findAll();
        if (latitude == null || longitude == null || radius == null) return machines;
        machines = machines.stream()
                .filter(machine -> distance(latitude, longitude, machine.getLatitude(), machine.getLongitude()) <= radius)
                .toList();
        return machines;
    }

    public Machine getMachine(Long id) {
        return machineRepository.findById(id).orElseThrow();
    }

    public Machine createMachine(Machine machine) {
        return machineRepository.save(machine);
    }

    public Machine updateMachine(Long id, Machine machine) {
        Machine machineToUpdate = machineRepository.findById(id).orElseThrow();
        machineToUpdate.setAddress(machine.getAddress());
        machineToUpdate.setAvailableTime(machine.getAvailableTime());
        machineToUpdate.setCoordinates(machine.getCoordinates());
        machineToUpdate.setDescription(machine.getDescription());
        return machineRepository.save(machineToUpdate);
    }

    public void deleteMachine(Long id) {
        machineRepository.findById(id).orElseThrow();
        machineRepository.deleteById(id);
    }
}
