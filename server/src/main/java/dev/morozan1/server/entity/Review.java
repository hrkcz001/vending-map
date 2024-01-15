package dev.morozan1.server.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "REVIEW")
public class Review {

    @Id
    @Column(name = "REVIEW_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "MACHINE_ID", nullable = false)
    private Machine machine;

    @Column(name = "RATING", nullable = false)
    private Short rating;

    @Column(name = "COMMENT", length = 300)
    private String comment;

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        Objects.requireNonNull(reviewId, "Review id must not be null");
        this.reviewId = reviewId;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        Objects.requireNonNull(machine, "Machine must not be null");
        this.machine = machine;
    }

    public Short getRating() {
        return rating;
    }

    public void setRating(Short rating) {
        Objects.requireNonNull(rating, "Rating must not be null");
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if (comment != null && comment.isBlank()) {
            throw new IllegalArgumentException("Comment must not be whitespace-only");
        }
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review review)) return false;
        return reviewId != null && reviewId.equals(review.reviewId);
    }

    @Override
    public int hashCode() {
        return reviewId.hashCode();
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
