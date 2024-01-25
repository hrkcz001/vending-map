package dev.morozan1.server.service;

import dev.morozan1.server.entity.MachineProduct;
import dev.morozan1.server.exception.BadRequestException;
import dev.morozan1.server.repository.MachineProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MPService {

        private final MachineProductRepository machineProductRepository;

        @Autowired
        public MPService(MachineProductRepository machineProductRepository) {
            this.machineProductRepository = machineProductRepository;
        }

        public MachineProduct getMachineProduct(long machineId, long productId) {
            return machineProductRepository.findByMachineAndProduct(machineId, productId).orElseThrow();
        }

        public MachineProduct createMachineProduct(MachineProduct machineProduct) {
            Objects.requireNonNull(machineProduct);
            if (machineProductRepository.findByMachineAndProduct(machineProduct.getMachine().getMachineId(),
                                                                 machineProduct.getProduct().getProductId())
                    .isPresent()) {
                throw new BadRequestException("Binding already exists");
            }
            return machineProductRepository.save(machineProduct);
        }

        public MachineProduct updateMachineProduct(MachineProduct machineProduct) {
            Objects.requireNonNull(machineProduct);
            MachineProduct machineProductToUpdate = machineProductRepository
                    .findByMachineAndProduct(machineProduct.getMachine().getMachineId(),
                                             machineProduct.getProduct().getProductId())
                    .orElseThrow();
            machineProductToUpdate.setAvailability(machineProduct.getAvailability());
            machineProductToUpdate.setPrice(machineProduct.getPrice());
            return machineProductRepository.save(machineProductToUpdate);
        }

        public void deleteMachineProduct(long machineId, long productId) {
            MachineProduct machineProduct = machineProductRepository.findByMachineAndProduct(machineId, productId).orElseThrow();
            machineProductRepository.delete(machineProduct);
        }
}
