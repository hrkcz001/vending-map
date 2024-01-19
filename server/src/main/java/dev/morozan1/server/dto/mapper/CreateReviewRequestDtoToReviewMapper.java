package dev.morozan1.server.dto.mapper;

import dev.morozan1.server.dto.CreateReviewRequestDto;
import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.entity.Review;
import dev.morozan1.server.exception.BadRequestException;
import dev.morozan1.server.repository.MachineRepository;
import org.modelmapper.AbstractConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

@Component
public class CreateReviewRequestDtoToReviewMapper extends AbstractConverter<CreateReviewRequestDto, Review> {

    private final MachineRepository machineRepository;

    @Autowired
    public CreateReviewRequestDtoToReviewMapper(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @Override
    protected Review convert(CreateReviewRequestDto source) {
        try {
            Review review = new Review();
            review.setRating(Short.parseShort(source.getRating()));

            //Escape HTML
            review.setComment(HtmlUtils.htmlEscape(source.getComment()));

            Machine machine = machineRepository.findById(Long.parseLong(source.getMachineId())).orElseThrow();
            review.setMachine(machine);

            return review;
        } catch (NumberFormatException e) {
            throw new BadRequestException("Rating must be a number between 1 and 5");
        }
    }
}