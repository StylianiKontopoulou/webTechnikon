package gr.codehub.rest.webtechnikon.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyRepair implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repairId;

    @ManyToOne
    @JoinColumn(name = "property")
    @JsonIgnore
    private Property property;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfRepairEnum typeOfRepair; //Enum

    @Column(nullable = false)
    private String shortDescription;

    @Column(nullable = false)
    private LocalDate submissionDate;

    @Column(nullable = false)
    private String description;

    private LocalDate proposedStartDate;

    private LocalDate proposedEndDate;

    private int proposedCost;

    private boolean ownerAcceptance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusOfRepairEnum status;

    private LocalDate actualStartDate;

    private LocalDate actualEndDate;
    // Constructors, Getters, Setters, toString() from lombok
    
    @Column(name = "isActive", nullable = false)
    private Boolean isActive;

    @Override
    public String toString() {
        return "PropertyRepair{" + "repairId=" + repairId + ", property=" + property.getId() + ", typeOfRepair=" + typeOfRepair + ", shortDescription=" + shortDescription + ", submissionDate=" + submissionDate + ", description=" + description + ", proposedStartDate=" + proposedStartDate + ", proposedEndDate=" + proposedEndDate + ", proposedCost=" + proposedCost + ", ownerAcceptance=" + ownerAcceptance + ", status=" + status + ", actualStartDate=" + actualStartDate + ", actualEndDate=" + actualEndDate + ", isActive=" + isActive +'}';
    }
}
