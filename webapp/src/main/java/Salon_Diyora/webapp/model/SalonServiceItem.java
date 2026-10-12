package Salon_Diyora.webapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "services")
public class SalonServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private double price;
    private int durationMinutes;

    public SalonServiceItem() {}

    public SalonServiceItem(String serviceName, double price, int durationMinutes) {
        this.serviceName = serviceName;
        this.price = price;
        this.durationMinutes = durationMinutes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
}