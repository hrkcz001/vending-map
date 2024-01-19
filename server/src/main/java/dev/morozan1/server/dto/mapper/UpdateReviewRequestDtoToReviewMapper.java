package dev.morozan1.server.dto.mapper;

import dev.morozan1.server.dto.UpdateReviewRequestDto;
import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.entity.Review;
import dev.morozan1.server.exception.BadRequestException;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

@Component
public class UpdateReviewRequestDtoToReviewMapper extends AbstractConverter<UpdateReviewRequestDto, Review> {

    @Override
    protected Review convert(UpdateReviewRequestDto source) {
        try {
            Review review = new Review();

            review.setRating(Short.parseShort(source.getRating()));

            //Escape HTML
            review.setComment(HtmlUtils.htmlEscape(source.getComment()));

            return review;
        } catch (NumberFormatException e) {
            throw new BadRequestException("Rating must be a number between 1 and 5");
        }
    }
}