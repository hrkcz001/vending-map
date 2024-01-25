package dev.morozan1.server.service;

import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.repository.MachineProductRepository;
import dev.morozan1.server.repository.MachineRepository;
import dev.morozan1.server.repository.ProductRepository;
import dev.morozan1.server.utils.GeoUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MachineService {

    private final MachineRepository machineRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository, MachineProductRepository machineProductRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.machineRepository = machineRepository;
    }

    public List<Machine> getMachines() {
        return machineRepository.findAll();
    }

    public List<Machine> getMachinesInRadius(double latitude, double longitude, double radius) {
        double radiusInDegrees = radius / 111320;
        double minLatitude = latitude - radiusInDegrees;
        double maxLatitude = latitude + radiusInDegrees;
        double minLongitude = longitude - (radiusInDegrees / Math.cos(latitude));
        double maxLongitude = longitude + (radiusInDegrees / Math.cos(latitude));

        List<Machine> machines = machineRepository.findInSquare(minLatitude, maxLatitude, minLongitude, maxLongitude);
        machines = machines.stream()
                .filter(machine -> GeoUtils.calculateDistance(latitude, longitude, machine.getLatitude(), machine.getLongitude()) <= radius)
                .toList();

        return machines;
    }

    public Machine getMachine(long id) {
        return machineRepository.findById(id).orElseThrow();
    }

    public Machine createMachine(Machine machine) {
        return machineRepository.save(machine);
    }

    public Machine updateMachine(long id, Machine machine) {
        Objects.requireNonNull(machine);
        Machine machineToUpdate = machineRepository.findById(id).orElseThrow();
        machineToUpdate.setAddress(machine.getAddress());
        machineToUpdate.setAvailableTime(machine.getAvailableTime());
        machineToUpdate.setCoordinates(machine.getCoordinates());
        machineToUpdate.setDescription(machine.getDescription());
        return machineRepository.save(machineToUpdate);
    }

    public void deleteMachine(long id) {
        machineRepository.findById(id).orElseThrow();
        machineRepository.deleteById(id);
    }
}
