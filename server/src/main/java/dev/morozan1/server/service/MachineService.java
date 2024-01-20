package dev.morozan1.server.service;

import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.repository.MachineProductRepository;
import dev.morozan1.server.repository.MachineRepository;
import dev.morozan1.server.repository.ProductRepository;
import dev.morozan1.server.utils.GeoUtils;
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

    public List<Machine> getMachines() {
        return machineRepository.findAll();
    }

    public List<Machine> getMachinesInRadius(double latitude, double longitude, double radius) {
        double minLatitude = latitude - (radius / 111.32);
        double maxLatitude = latitude + (radius / 111.32);
        double minLongitude = longitude - (radius / (111.32 * Math.cos(latitude)));
        double maxLongitude = longitude + (radius / (111.32 * Math.cos(latitude)));

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
