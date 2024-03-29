package dev.morozan1.server.dto.mapper;

import dev.morozan1.server.dto.request.CUReviewRequestDto;
import dev.morozan1.server.entity.Review;
import dev.morozan1.server.exception.BadRequestException;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

@Component
public class CUReviewRequestDtoToReviewMapper extends AbstractConverter<CUReviewRequestDto, Review> {

    @Override
    protected Review convert(CUReviewRequestDto source) {
        try {
            Review review = new Review();

            review.setRating(source.getRating());

            //Escape HTML
            review.setComment(HtmlUtils.htmlEscape(source.getComment()));

            return review;
        } catch (NumberFormatException e) {
            throw new BadRequestException("Rating must be a number between 1 and 5");
        }
    }
}