package dev.morozan1.server.service;

import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.entity.MachineProduct;
import dev.morozan1.server.entity.Product;
import dev.morozan1.server.exception.BadRequestException;
import dev.morozan1.server.repository.MachineProductRepository;
import dev.morozan1.server.service.MPService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MPServiceTest {

    @Mock
    private MachineProductRepository machineProductRepository;

    @InjectMocks
    private MPService mpService;

    private final long MACHINE_ID = 1;
    private final long PRODUCT_ID = 2;

    @Test
    public void testGetMachineProduct() {
        MachineProduct mockMachineProduct = new MachineProduct();
        Mockito.when(machineProductRepository.findByMachineAndProduct(MACHINE_ID, PRODUCT_ID)).thenReturn(Optional.of(mockMachineProduct));

        MachineProduct result = mpService.getMachineProduct(MACHINE_ID, PRODUCT_ID);

        assertNotNull(result);
    }

    @Test
    public void testCreateMachineProduct() {
        MachineProduct machineProductToCreate = new MachineProduct();
        Machine machine = new Machine();
        machine.setMachineId(MACHINE_ID);
        Product product = new Product();
        product.setProductId(PRODUCT_ID);
        machineProductToCreate.setMachine(machine);
        machineProductToCreate.setProduct(product);

        Mockito.when(machineProductRepository
                .findByMachineAndProduct(machineProductToCreate.getMachine().getMachineId(),
                                         machineProductToCreate.getProduct().getProductId()))
                .thenReturn(Optional.empty());
        Mockito.when(machineProductRepository.save(machineProductToCreate)).thenReturn(machineProductToCreate);

        MachineProduct createdMachineProduct = mpService.createMachineProduct(machineProductToCreate);

        assertEquals(machineProductToCreate, createdMachineProduct);
    }

    @Test
    public void testCreateMachineProduct_ThrowsBadRequestException() {
        MachineProduct existingMachineProduct = new MachineProduct();
        Machine machine = new Machine();
        machine.setMachineId(MACHINE_ID);
        Product product = new Product();
        product.setProductId(PRODUCT_ID);
        existingMachineProduct.setMachine(machine);
        existingMachineProduct.setProduct(product);

        Mockito.when(machineProductRepository
                .findByMachineAndProduct(existingMachineProduct.getMachine().getMachineId(),
                                         existingMachineProduct.getProduct().getProductId()))
                .thenReturn(Optional.of(existingMachineProduct));

        MachineProduct machineProductToCreate = new MachineProduct();
        machineProductToCreate.setMachine(existingMachineProduct.getMachine());
        machineProductToCreate.setProduct(existingMachineProduct.getProduct());

        assertThrows(BadRequestException.class, () -> mpService.createMachineProduct(machineProductToCreate));
    }

    @Test
    public void testUpdateMachineProduct() {
        MachineProduct existingMachineProduct = new MachineProduct();
        Machine machine = new Machine();
        machine.setMachineId(MACHINE_ID);
        Product product = new Product();
        product.setProductId(PRODUCT_ID);
        existingMachineProduct.setMachine(machine);
        existingMachineProduct.setProduct(product);
        existingMachineProduct.setAvailability(true);
        existingMachineProduct.setPrice(100);

        MachineProduct newMachineProduct = new MachineProduct();
        newMachineProduct.setMachine(existingMachineProduct.getMachine());
        newMachineProduct.setProduct(existingMachineProduct.getProduct());
        newMachineProduct.setAvailability(false);
        newMachineProduct.setPrice(200);

        Mockito.when(machineProductRepository
                .findByMachineAndProduct(existingMachineProduct.getMachine().getMachineId(),
                                         existingMachineProduct.getProduct().getProductId()))
                .thenReturn(Optional.of(existingMachineProduct));
        Mockito.when(machineProductRepository.save(existingMachineProduct)).thenReturn(existingMachineProduct);

        MachineProduct updatedMachineProduct = mpService.updateMachineProduct(newMachineProduct);

        assertEquals(newMachineProduct, updatedMachineProduct);
        assertEquals(newMachineProduct.getAvailability(), updatedMachineProduct.getAvailability());
        assertEquals(newMachineProduct.getPrice(), updatedMachineProduct.getPrice());
    }

    @Test
    public void testDeleteMachineProduct() {
        MachineProduct existingMachineProduct = new MachineProduct();
        Machine machine = new Machine();
        machine.setMachineId(MACHINE_ID);
        Product product = new Product();
        product.setProductId(PRODUCT_ID);
        existingMachineProduct.setMachine(machine);
        existingMachineProduct.setProduct(product);

        Mockito.when(machineProductRepository
                .findByMachineAndProduct(existingMachineProduct.getMachine().getMachineId(),
                                         existingMachineProduct.getProduct().getProductId()))
                .thenReturn(Optional.of(existingMachineProduct));

        mpService.deleteMachineProduct(existingMachineProduct.getMachine().getMachineId(),
                                       existingMachineProduct.getProduct().getProductId());

        Mockito.verify(machineProductRepository, Mockito.times(1)).delete(existingMachineProduct);
    }
}