package gr.codehub.rest.webtechnikon.services;

import gr.codehub.rest.webtechnikon.models.PropertyRepair;
import gr.codehub.rest.webtechnikon.repositories.PropertyRepairRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.util.List;

@RequestScoped
public class PropertyRepairServiceImpl implements PropertyRepairService {

    @Inject
    private PropertyRepairRepository propertyRepairRepository;

    @Override
    public List<PropertyRepair> getAllRepairs() {
        return propertyRepairRepository.findAll();
    }

    @Override
    public List<PropertyRepair> searchRepairs(String description, String status) {
        return propertyRepairRepository.findRepairsByCriteria(description, status);
    }

    @Override
    public PropertyRepair getRepairById(Long repairId) {
        return propertyRepairRepository.findById(repairId).orElse(null);
    }

    @Override
    public PropertyRepair createRepair(PropertyRepair repair) {
        return propertyRepairRepository.create(repair);
    }

    @Override
    public void updateRepair(Long repairId, PropertyRepair repair) {
        PropertyRepair existingRepair = propertyRepairRepository.findById(repairId).orElse(null);
        if (existingRepair != null) {
            existingRepair.setProperty(repair.getProperty());
            existingRepair.setTypeOfRepair(repair.getTypeOfRepair());
            existingRepair.setShortDescription(repair.getShortDescription());
            existingRepair.setSubmissionDate(repair.getSubmissionDate());
            existingRepair.setDescription(repair.getDescription());
            existingRepair.setProposedStartDate(repair.getProposedStartDate());
            existingRepair.setProposedEndDate(repair.getProposedEndDate());
            existingRepair.setProposedCost(repair.getProposedCost());
            existingRepair.setOwnerAcceptance(repair.isOwnerAcceptance());
            existingRepair.setStatus(repair.getStatus());
            existingRepair.setActualStartDate(repair.getActualStartDate());
            existingRepair.setActualEndDate(repair.getActualEndDate());
            existingRepair.setIsActive(repair.getIsActive());
            propertyRepairRepository.update(existingRepair);
        }
    }

    @Override
    public void deleteRepair(Long repairId) {
        propertyRepairRepository.deleteById(repairId);
    }

    @Override
    public List<PropertyRepair> getRepairsByPropertyId(Long propertyId) {
        return propertyRepairRepository.findRepairsByPropertyId(propertyId);
    }
}
