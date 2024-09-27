package gr.codehub.rest.webtechnikon.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Property implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long propertyId;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int yearOfConstruction;

    @Column(nullable = false)
    private PropertyType propertyType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PropertyRepair> propertyRepairs;

    @Override
    public String toString() {
        return "Property{" + "id=" + id + ", address=" + address + ", propretyId=" + propertyId + ", yearOfConstruction=" + yearOfConstruction + ", propertyType=" + propertyType + ", userId=" + user.getId() + ", propertyRepairs=" + propertyRepairs + ", isActive=" + isActive + '}';
    }
}
