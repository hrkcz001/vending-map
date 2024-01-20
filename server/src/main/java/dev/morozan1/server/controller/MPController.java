package dev.morozan1.server.controller;

import dev.morozan1.server.dto.request.CUMachineProductRequestDto;
import dev.morozan1.server.dto.response.MachineProductResponseDto;
import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.entity.MachineProduct;
import dev.morozan1.server.exception.BadIdException;
import dev.morozan1.server.service.MPService;
import dev.morozan1.server.service.MachineService;
import dev.morozan1.server.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machines/{machineId}/products")
public class MPController {

    private final MachineService machineService;
    private final ProductService productService;
    private final MPService machineProductService;
    private final ModelMapper modelMapper;

    @Autowired
    public MPController(MachineService machineService,
                        ProductService productService,
                        MPService machineProductService,
                        ModelMapper modelMapper) {
        this.machineService = machineService;
        this.productService = productService;
        this.machineProductService = machineProductService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public ResponseEntity<List<MachineProductResponseDto>> getProductsByMachine(@PathVariable String machineId) {
        if (machineId == null) throw new BadIdException();
        try {
            long machineIdValue = Long.parseLong(machineId);

            Machine machine = machineService.getMachine(machineIdValue);
            List<MachineProductResponseDto> machineProductResponseDtoList = machine.getMachineProducts().stream()
                    .map(machineProduct -> modelMapper.map(machineProduct, MachineProductResponseDto.class))
                    .toList();

            return new ResponseEntity<>(machineProductResponseDtoList, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<MachineProductResponseDto> getProductByMachine(@PathVariable String machineId, @PathVariable String productId) {
        if (machineId == null || productId == null) throw new BadIdException();
        try {
            long machineIdValue = Long.parseLong(machineId);
            long productIdValue = Long.parseLong(productId);

            MachineProduct machineProduct = machineProductService.getMachineProduct(machineIdValue, productIdValue);
            MachineProductResponseDto machineProductResponseDto = modelMapper.map(machineProduct, MachineProductResponseDto.class);

            return new ResponseEntity<>(machineProductResponseDto, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @PostMapping("/{productId}")
    public ResponseEntity<MachineProductResponseDto> addProductToMachine(@PathVariable String machineId,
                                                                         @PathVariable String productId,
                                                                         @Validated @RequestBody CUMachineProductRequestDto machineProductRequestDto) {
        if (machineId == null || productId == null) throw new BadIdException();
        try {
            long machineIdValue = Long.parseLong(machineId);
            long productIdValue = Long.parseLong(productId);

            MachineProduct machineProduct = modelMapper.map(machineProductRequestDto, MachineProduct.class);
            machineProduct.setMachine(machineService.getMachine(machineIdValue));
            machineProduct.setProduct(productService.getProduct(productIdValue));
            MachineProduct createdMachineProduct = machineProductService.createMachineProduct(machineProduct);
            MachineProductResponseDto machineProductResponseDto = modelMapper.map(createdMachineProduct, MachineProductResponseDto.class);

            return new ResponseEntity<>(machineProductResponseDto, HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<MachineProductResponseDto> updateProductInMachine(@PathVariable String machineId,
                                                                            @PathVariable String productId,
                                                                            @Validated @RequestBody CUMachineProductRequestDto machineProductRequestDto) {
        if (machineId == null || productId == null) throw new BadIdException();
        try {
            long machineIdValue = Long.parseLong(machineId);
            long productIdValue = Long.parseLong(productId);

            MachineProduct machineProduct = modelMapper.map(machineProductRequestDto, MachineProduct.class);
            machineProduct.setMachine(machineService.getMachine(machineIdValue));
            machineProduct.setProduct(productService.getProduct(productIdValue));
            MachineProduct updatedMachineProduct = machineProductService.updateMachineProduct(machineProduct);
            MachineProductResponseDto machineProductResponseDto = modelMapper.map(updatedMachineProduct, MachineProductResponseDto.class);

            return new ResponseEntity<>(machineProductResponseDto, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductFromMachine(@PathVariable String machineId, @PathVariable String productId) {
        if (machineId == null || productId == null) throw new BadIdException();
        try {
            long machineIdValue = Long.parseLong(machineId);
            long productIdValue = Long.parseLong(productId);

            machineProductService.deleteMachineProduct(machineIdValue, productIdValue);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }
}
