package dev.morozan1.vendingmap.entity;

import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "MACHINE")
public class Machine {

    @Id
    @Column(name = "MACHINE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long machineId;

    //@PlaceInPrague(message = "Machine must be in Prague")
    @Column(name = "COORDINATE", columnDefinition = "Geometry(Point)", nullable = false)
    private Point coordinate;

    //@NotBlank(message = "Machine address must not be empty")
    @Column(name = "ADDRESS", nullable = false)
    private String address;

    //@NullOrNotBlank(message = "Machine description must not be whitespace-only")
    @Column(name = "DESCRIPTION")
    private String description;

    //@TimePeriod(message = "Machine available time must be in format HH:MM-HH:MM")
    @Column(name = "AVAILABLE_TIME")
    private String availableTime;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews;

    @ManyToMany
    @JoinTable(name = "MACHINE_PRODUCT")
    @MapKeyJoinColumn(name = "AVAILABILITY")
    private Map<Product, Boolean> products;

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        Objects.requireNonNull(machineId, "Machine id must not be null");
        this.machineId = machineId;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point coordinate) {
        Objects.requireNonNull(coordinate, "Machine coordinate must not be null");
        if (!isInPragueValid(coordinate)) {
            throw new IllegalArgumentException("Machine must be in Prague");
        }
        this.coordinate = coordinate;
    }

    public static Boolean isInPragueValid(Point coordinate) {
        double pragueMinLatitude = 49.9417;
        double pragueMaxLatitude = 50.1768;
        double pragueMinLongitude = 14.2257;
        double pragueMaxLongitude = 14.7070;

        double latitude = coordinate.getY();
        double longitude = coordinate.getX();

        return latitude >= pragueMinLatitude && latitude <= pragueMaxLatitude &&
                longitude >= pragueMinLongitude && longitude <= pragueMaxLongitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        Objects.requireNonNull(address, "Machine address must not be null");
        if (address.isBlank()) {
            throw new IllegalArgumentException("Machine address must not be empty");
        }
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.isBlank()) {
            throw new IllegalArgumentException("Machine description must not be empty");
        }
        this.description = description;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(String availableTime) {
        Objects.requireNonNull(availableTime, "Machine available time must not be null");
        if (!isTimePeriodValid(availableTime)) {
            throw new IllegalArgumentException("Machine available time must be in format HH:MM-HH:MM");
        }
        this.availableTime = availableTime;
    }

    public static Boolean isTimePeriodValid(String timePeriod){
        return timePeriod.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]-([01]?[0-9]|2[0-3]):[0-5][0-9]");
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        Objects.requireNonNull(review, "Review must not be null");

        if (reviews == null) {
            reviews = new HashSet<>();
        }

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

    public Map<Product, Boolean> getProducts() {
        return products;
    }

    public void addProduct(Product product, Boolean availability) {
        Objects.requireNonNull(product, "Product must not be null");

        if (products == null) {
            products = new HashMap<>();
        }

        if (products.containsKey(product)) {
            throw new IllegalArgumentException("Product is already in machine");
        }

        products.put(product, availability);
    }

    public void updateProduct(Product product, Boolean availability) {
        Objects.requireNonNull(product, "Product must not be null");

        if (products == null || !products.containsKey(product)) {
            throw new IllegalArgumentException("Product is not in machine");
        }

        products.put(product, availability);
    }

    public void removeProduct(Product product) {
        Objects.requireNonNull(product, "Product must not be null");
        if (products == null) return;
        products.remove(product);
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
                ", coordinate=" + coordinate +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", availableTime='" + availableTime + '\'' +
                ", reviews=" + reviews +
                ", products=" + products +
                '}';
    }
}