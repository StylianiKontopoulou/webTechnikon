package gr.codehub.rest.webtechnikon.services;

import gr.codehub.rest.webtechnikon.models.PropertyRepair;
import gr.codehub.rest.webtechnikon.models.TypeOfRepairEnum;
import java.util.List;
import java.util.Optional;


public interface PropertyRepairService {

    List<PropertyRepair> getAllRepairs();

    List<PropertyRepair> searchRepairs(String description, String status);

    PropertyRepair getRepairById(Long repairId);

    PropertyRepair createRepair(PropertyRepair repair);

    void updateRepair(Long repairId, PropertyRepair repair);

    void deleteRepair(Long repairId);

    List<PropertyRepair> getRepairsByPropertyId(Long propertyId);
}