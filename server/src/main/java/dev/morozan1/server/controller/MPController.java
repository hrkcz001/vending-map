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
    public ResponseEntity<List<MachineProductResponseDto>> getProductsByMachine(@PathVariable long machineId) {
        Machine machine = machineService.getMachine(machineId);
        List<MachineProductResponseDto> machineProductResponseDtoList = machine.getMachineProducts().stream()
                .map(machineProduct -> modelMapper.map(machineProduct, MachineProductResponseDto.class))
                .toList();

        return new ResponseEntity<>(machineProductResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<MachineProductResponseDto> getProductByMachine(@PathVariable long machineId,
                                                                         @PathVariable long productId) {
        MachineProduct machineProduct = machineProductService.getMachineProduct(machineId, productId);
        MachineProductResponseDto machineProductResponseDto = modelMapper.map(machineProduct, MachineProductResponseDto.class);

        return new ResponseEntity<>(machineProductResponseDto, HttpStatus.OK);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<MachineProductResponseDto> addProductToMachine(@PathVariable long machineId,
                                                                         @PathVariable long productId,
                                                                         @Validated @RequestBody CUMachineProductRequestDto machineProductRequestDto) {
        MachineProduct machineProduct = modelMapper.map(machineProductRequestDto, MachineProduct.class);
        machineProduct.setMachine(machineService.getMachine(machineId));
        machineProduct.setProduct(productService.getProduct(productId));
        MachineProduct createdMachineProduct = machineProductService.createMachineProduct(machineProduct);
        MachineProductResponseDto machineProductResponseDto = modelMapper.map(createdMachineProduct, MachineProductResponseDto.class);

        return new ResponseEntity<>(machineProductResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<MachineProductResponseDto> updateProductInMachine(@PathVariable long machineId,
                                                                            @PathVariable long productId,
                                                                            @Validated @RequestBody CUMachineProductRequestDto machineProductRequestDto) {
        MachineProduct machineProduct = modelMapper.map(machineProductRequestDto, MachineProduct.class);
        machineProduct.setMachine(machineService.getMachine(machineId));
        machineProduct.setProduct(productService.getProduct(productId));
        MachineProduct updatedMachineProduct = machineProductService.updateMachineProduct(machineProduct);
        MachineProductResponseDto machineProductResponseDto = modelMapper.map(updatedMachineProduct, MachineProductResponseDto.class);

        return new ResponseEntity<>(machineProductResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductFromMachine(@PathVariable Long machineId,
                                                         @PathVariable Long productId) {
        if (machineId == null || productId == null) throw new BadIdException();

        machineProductService.deleteMachineProduct(machineId, productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
