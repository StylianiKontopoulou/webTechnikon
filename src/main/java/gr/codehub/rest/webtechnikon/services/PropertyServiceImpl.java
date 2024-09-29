package gr.codehub.rest.webtechnikon.services;

import gr.codehub.rest.webtechnikon.models.Property;
import gr.codehub.rest.webtechnikon.repositories.PropertyRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.util.List;

@RequestScoped
public class PropertyServiceImpl implements PropertyService {

    @Inject
    private PropertyRepository propertyRepository;

    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @Override
    public List<Property> searchProperties(String location, String type) {
        return propertyRepository.findPropertiesByCriteria(location, type);
    }

    @Override
    public Property getPropertyById(Long propertyId) {
        return propertyRepository.findById(propertyId).orElse(null);
    }

    @Override
    public Property createProperty(Property property) {
        return propertyRepository.create(property);
    }

    @Override
    public void updateProperty(Long propertyId, Property property) {
        Property existingProperty = propertyRepository.findById(propertyId).orElse(null);
        if (existingProperty != null) {
            existingProperty.setPropertyId(property.getPropertyId());
            existingProperty.setAddress(property.getAddress());
            existingProperty.setYearOfConstruction(property.getYearOfConstruction());
            existingProperty.setPropertyType(property.getPropertyType());
            existingProperty.setUser(property.getUser());
            existingProperty.setIsActive(property.getIsActive());
            propertyRepository.update(existingProperty);
        }
    }

    @Override
    public void deleteProperty(Long propertyId) {
        propertyRepository.softDelete(propertyId);
    }

    @Override
    public List<Property> getPropertiesByUserId(Long userId) {
        return propertyRepository.findPropertiesByUserId(userId);
    }
}