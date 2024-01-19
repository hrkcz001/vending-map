package dev.morozan1.server.validator;

import dev.morozan1.server.annotation.IsMachineId;
import dev.morozan1.server.repository.MachineRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;

public class IsMachineIdValidator implements ConstraintValidator<IsMachineId, String> {

    private final MachineRepository machineRepository;

    @Autowired
    public IsMachineIdValidator(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @Override
    public void initialize(IsMachineId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String idString, ConstraintValidatorContext constraintValidatorContext) {
        if (idString == null) return false;
        try{
            Long id = Long.parseLong(idString);
            return machineRepository.existsById(id);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
