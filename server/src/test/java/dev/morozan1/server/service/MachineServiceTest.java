package dev.morozan1.server.service;

import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.repository.MachineRepository;
import dev.morozan1.server.service.MachineService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class MachineServiceTest {

    @Mock
    private MachineRepository machineRepository;

    @InjectMocks
    private MachineService machineService;

    private final long MACHINE_ID = 1;

    @Test
    public void testGetMachines() {
        Mockito.when(machineRepository.findAll()).thenReturn(List.of(new Machine(), new Machine()));

        List<Machine> machines = machineService.getMachines();

        assertEquals(2, machines.size());
    }

    @Test
    public void testGetMachine() {
        Machine mockMachine = new Machine();
        Mockito.when(machineRepository.findById(MACHINE_ID)).thenReturn(Optional.of(mockMachine));

        Machine machine = machineService.getMachine(MACHINE_ID);

        assertNotNull(machine);
    }

    @Test
    public void testGetMachinesInRadius() {
        double testLatitude = 50.0755;
        double testLongitude = 14.4378 ;
        double radius = 1000;
        double radiusInDegrees = radius / 111320;
        double minLatitude = testLatitude - radiusInDegrees;
        double maxLatitude = testLatitude + radiusInDegrees;
        double minLongitude = testLongitude - (radiusInDegrees / Math.cos(testLatitude));
        double maxLongitude = testLongitude + (radiusInDegrees / Math.cos(testLatitude));

        // Inside of radius
        Machine machineInRadius = new Machine();
        machineInRadius.setLatitude(testLatitude + 0.0005);
        machineInRadius.setLongitude(testLongitude + 0.0005);

        // Outside of radius
        Machine machineOutsideRadius = new Machine();
        machineOutsideRadius.setLatitude(testLatitude + 0.01);
        machineOutsideRadius.setLongitude(testLongitude + 0.01);

        List<Machine> allMachines = Arrays.asList(machineInRadius, machineOutsideRadius);
        Mockito.when(machineRepository.findInSquare(minLatitude, maxLatitude, minLongitude, maxLongitude)).thenReturn(allMachines);

        List<Machine> result = machineService.getMachinesInRadius(testLatitude, testLongitude, radius);

        assertEquals(1, result.size());
        assertEquals(machineInRadius, result.getFirst());
    }

    @Test
    public void testCreateMachine() {
        Machine machineToCreate = new Machine();
        machineToCreate.setMachineId(MACHINE_ID);
        Mockito.when(machineRepository.save(machineToCreate)).thenReturn(machineToCreate);

        Machine createdMachine = machineService.createMachine(machineToCreate);

        assertEquals(machineToCreate, createdMachine);
    }

    @Test
    public void testUpdateMachine() {
        Machine oldMachine = new Machine();
        oldMachine.setAddress("Old address");
        oldMachine.setCoordinates(Pair.of(50.0755, 14.437));
        Machine newMachine = new Machine();
        newMachine.setAddress("New address");
        newMachine.setCoordinates(Pair.of(50.07551, 14.4371));
        Mockito.when(machineRepository.findById(MACHINE_ID)).thenReturn(Optional.of(oldMachine));
        Mockito.when(machineRepository.save(oldMachine)).thenReturn(oldMachine);

        Machine updatedMachine = machineService.updateMachine(MACHINE_ID, newMachine);

        assertEquals(newMachine.getAddress(), updatedMachine.getAddress());
        assertEquals(newMachine.getCoordinates(), updatedMachine.getCoordinates());
    }

    @Test
    public void testDeleteMachine() {
        Mockito.when(machineRepository.findById(MACHINE_ID)).thenReturn(Optional.of(new Machine()));

        machineService.deleteMachine(MACHINE_ID);

        Mockito.verify(machineRepository, times(1)).deleteById(MACHINE_ID);
    }
}