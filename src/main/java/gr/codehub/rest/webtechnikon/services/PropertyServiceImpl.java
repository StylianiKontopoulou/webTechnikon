package gr.codehub.rest.webtechnikon.services;

import gr.codehub.rest.webtechnikon.exceptions.DuplicateEntryException;
import gr.codehub.rest.webtechnikon.exceptions.InvalidYearException;
import gr.codehub.rest.webtechnikon.exceptions.OwnerNotFoundException;
import gr.codehub.rest.webtechnikon.exceptions.ResourceNotFoundException;
import gr.codehub.rest.webtechnikon.models.Property;
import gr.codehub.rest.webtechnikon.models.PropertyType;
import gr.codehub.rest.webtechnikon.models.User;
import gr.codehub.rest.webtechnikon.repositories.PropertyRepository;
import gr.codehub.rest.webtechnikon.repositories.UserRepository;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

public class PropertyServiceImpl implements PropertyService {

    @Inject
    private PropertyRepository propertyRepository;
    @Inject
    private UserRepository userRepository;

    @Override
    public Optional<Property> findByPropertyIdNumber(Long propertyIdNumber) {
        return propertyRepository.findByPropertyIdNumber(propertyIdNumber);
    }

    @Override
    public List<Property> findByOwnerVatNumber(Long vatNumber) {
        return propertyRepository.findByOwnerVatNumber(vatNumber);
    }
    
    
    @Override
    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    @Override
    public Property createProperty(Property property) {

        //check if owner exists
        User owner = userRepository.findById(property.getUser().getId())
                .orElseThrow(() -> new OwnerNotFoundException("Owner with ID " + property.getUser().getId() + " does not exist."));
        
        //check if property id (e9) is unique
        Optional<Property> existingProperty = propertyRepository.findByPropertyIdNumber(property.getId());

        if (existingProperty.isPresent()) {
            throw new DuplicateEntryException("Property with ID " + property.getId() + " already exists.");
        }

        Property newEntity = Property.builder()
                .propertyId(property.getPropertyId())
                .address(property.getAddress())
                .yearOfConstruction(property.getYearOfConstruction())
                .propertyType(property.getPropertyType())
                .user(owner)
                .isActive(true)
                .build();

        return propertyRepository.create(newEntity);
    }

    @Override
    public Property updateProperty(Long id, Long propertyIdNumber, String address, int yearOfConstruction, PropertyType propertyType, Long propertyOwnerId) {
        Optional<Property> propertyToUpdateCheck = propertyRepository.findById(id);
        if (!propertyToUpdateCheck.isPresent() || !propertyToUpdateCheck.get().getIsActive()) {
            throw new ResourceNotFoundException("Property with ID " + id + " does not exist or is inactive.");
        }

        Property propertyToUpdate = propertyToUpdateCheck.get();

        //check if its going to be changed and validate that the new property id will be different from the previous one if its going to be changed
        if (propertyIdNumber != null && !propertyIdNumber.equals(propertyToUpdate.getPropertyId())) {
            //check if the propertyId already exists
            Optional<Property> existingPropertyWithSameIdNumber = findByPropertyIdNumber(propertyIdNumber);
            if (existingPropertyWithSameIdNumber.isPresent()) {
                throw new DuplicateEntryException("Property with identification number " + propertyIdNumber + " already exists.");
            }
            propertyToUpdate.setPropertyId(propertyIdNumber);
        }

        if (address != null) {
            propertyToUpdate.setAddress(address);
        }

        if (yearOfConstruction > 1800 && yearOfConstruction <= java.time.Year.now().getValue()) {
            propertyToUpdate.setYearOfConstruction(yearOfConstruction);
        } else if (yearOfConstruction != 0) {
            throw new InvalidYearException("Year of construction must be between 1800 and the current year.");
        }

        if (propertyType != null) {
            propertyToUpdate.setPropertyType(propertyType);
        }

        if (propertyOwnerId != null) {
            Optional<User> owner = userRepository.findById(propertyOwnerId);
            if (!owner.isPresent()) {
                throw new OwnerNotFoundException("Owner with ID " + propertyOwnerId + " does not exist.");
            }
            propertyToUpdate.setUser(owner.get());
        }

        propertyRepository.update(propertyToUpdate);

        return propertyToUpdate;

    }

    @Override
    public Property deleteProperty(Long id) {
        Property propertyToDelete = propertyRepository.findById(id).orElseThrow(
         ()-> new ResourceNotFoundException("Property with ID " + id + " does not exist"));
        
        if (!propertyToDelete.getIsActive()) {
            throw new ResourceNotFoundException("Property with ID " + id + " is inactive.");
        }

        propertyRepository.delete(propertyToDelete);
        return propertyToDelete;
    }

    @Override
    public Property softDeleteProperty(Long id) {
        Optional<Property> propertyToDeleteCheck = propertyRepository.findById(id);
        if (!propertyToDeleteCheck.isPresent() || !propertyToDeleteCheck.get().getIsActive()) {
            throw new ResourceNotFoundException("Property with ID " + id + " does not exist or is inactive.");
        }

        Property propertyToDelete = propertyToDeleteCheck.get();
        propertyRepository.softDelete(propertyToDelete);
        return propertyToDelete;
    }

}
