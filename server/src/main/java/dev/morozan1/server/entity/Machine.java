package dev.morozan1.server.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import jakarta.persistence.*;
import java.sql.Time;
import java.util.*;

@Entity
@Getter
@Table(name = "MACHINE")
public class Machine {

    @Id
    @Column(name = "MACHINE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long machineId;

    @Column(name = "LATITUDE", nullable = false)
    private Double latitude;

    @Column(name = "LONGITUDE", nullable = false)
    private Double longitude;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "DESCRIPTION")
    private String description;

    @Setter
    @Column(name = "AVAILABLE_FROM")
    private Time availableFrom;

    @Setter
    @Column(name = "AVAILABLE_TO")
    private Time availableTo;

    @Setter
    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    @Setter
    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MachineProduct> machineProducts = new HashSet<>();


    public void setMachineId(Long machineId) {
        Objects.requireNonNull(machineId, "Machine id must not be null");
        this.machineId = machineId;
    }

    public void setLatitude(Double latitude) {
        Objects.requireNonNull(latitude, "Machine latitude must not be null");
        if (this.longitude != null && !isInPrague(latitude, this.longitude)) {
            throw new IllegalArgumentException("Machine must be in Prague");
        }
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        Objects.requireNonNull(longitude, "Machine longitude must not be null");
        if (this.latitude != null && !isInPrague(this.latitude, longitude)) {
            throw new IllegalArgumentException("Machine must be in Prague");
        }
        this.longitude = longitude;
    }

    public void setCoordinates(Pair<Double, Double> coordinates) {
        Objects.requireNonNull(coordinates, "Machine coordinates must not be null");
        Objects.requireNonNull(coordinates.getFirst(), "Machine latitude must not be null");
        Objects.requireNonNull(coordinates.getSecond(), "Machine longitude must not be null");
        if (!isInPrague(coordinates.getFirst(), coordinates.getSecond())) {
            throw new IllegalArgumentException("Machine must be in Prague");
        }
        this.latitude = coordinates.getFirst();
        this.longitude = coordinates.getSecond();
    }

    public static Boolean isInPrague(double latitude, double longitude) {
        double pragueMinLatitude = 49.9417;
        double pragueMaxLatitude = 50.1768;
        double pragueMinLongitude = 14.2257;
        double pragueMaxLongitude = 14.7070;

        return latitude >= pragueMinLatitude && latitude <= pragueMaxLatitude &&
                longitude >= pragueMinLongitude && longitude <= pragueMaxLongitude;
    }

    public void setAddress(String address) {
        Objects.requireNonNull(address, "Machine address must not be null");
        if (address.isBlank()) {
            throw new IllegalArgumentException("Machine address must not be empty");
        }
        this.address = address;
    }

    public void setDescription(String description) {
        if (description != null && description.isBlank()) {
            throw new IllegalArgumentException("Machine description must not be empty");
        }
        this.description = description;
    }

    public void setAvailableTime(Pair<Time, Time> availableTime) {
        if (availableTime != null) {
            Objects.requireNonNull(availableTime.getFirst(), "Machine availableFrom must not be null");
            Objects.requireNonNull(availableTime.getSecond(), "Machine availableTo must not be null");
            if (availableTime.getFirst().after(availableTime.getSecond())) {
                throw new IllegalArgumentException("Machine availableFrom must be before availableTo");
            }
            this.availableFrom = availableTime.getFirst();
            this.availableTo = availableTime.getSecond();
        }
        else {
            this.availableFrom = null;
            this.availableTo = null;
        }
    }

    public Double calculateRating(Set<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return null;
        }

        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public void addReview(Review review) {
        Objects.requireNonNull(review, "Review must not be null");

        if (reviews.contains(review)) {
            throw new IllegalArgumentException("Review is already in machine");
        }

        reviews.add(review);
        review.setMachine(this);
    }

    public void removeReview(Review review) {
        Objects.requireNonNull(review, "Review must not be null");
        if (reviews == null) return;

        reviews.remove(review);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Machine machine)) return false;
        return machineId != null && machineId.equals(machine.machineId);
    }

    @Override
    public int hashCode() {
        return machineId.hashCode();
    }

    @Override
    public String toString() {
        return "Machine{" +
                "machineId=" + machineId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", availableFrom=" + availableFrom +
                ", availableTo=" + availableTo +
                ", reviews=" + reviews +
                ", machineProducts=" + machineProducts +
                '}';
    }
}