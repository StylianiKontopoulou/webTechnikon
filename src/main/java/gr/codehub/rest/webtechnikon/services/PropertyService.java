package gr.codehub.rest.webtechnikon.services;

import gr.codehub.rest.webtechnikon.models.Property;
import gr.codehub.rest.webtechnikon.models.PropertyType;
import java.util.List;
import java.util.Optional;


public interface PropertyService {
    Property createProperty(Property property);
    Property updateProperty(Long id, Long propertyIdNumber, String address, int yearOfConstruction, PropertyType propertyType, Long propertyOwnerId);
    Optional<Property> findByPropertyIdNumber(Long propertyIdNumber);
    List<Property> findByOwnerVatNumber(Long vatNumber);
    List<Property> findAll();
    Property deleteProperty(Long id);
    Property softDeleteProperty(Long id);
}
