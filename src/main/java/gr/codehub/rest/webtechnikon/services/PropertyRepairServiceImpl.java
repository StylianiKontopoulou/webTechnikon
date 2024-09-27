package gr.codehub.rest.webtechnikon.services;

import gr.codehub.rest.webtechnikon.exceptions.InvalidInputException;
import gr.codehub.rest.webtechnikon.exceptions.OwnerNotFoundException;
import gr.codehub.rest.webtechnikon.exceptions.UserExistsException;
import gr.codehub.rest.webtechnikon.models.Property;
import gr.codehub.rest.webtechnikon.models.PropertyRepair;
import gr.codehub.rest.webtechnikon.models.StatusOfRepairEnum;
import gr.codehub.rest.webtechnikon.models.TypeOfRepairEnum;
import gr.codehub.rest.webtechnikon.models.User;
import gr.codehub.rest.webtechnikon.repositories.PropertyRepairRepository;
import gr.codehub.rest.webtechnikon.repositories.PropertyRepository;
import gr.codehub.rest.webtechnikon.repositories.UserRepository;
import jakarta.el.PropertyNotFoundException;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyRepairServiceImpl implements PropertyRepairService {

    @Inject
    private  PropertyRepairRepository propertyRepairRepository;
    @Inject
    private PropertyRepository propertyRepository;
    @Inject
    private UserRepository userRepository;
   
    @Override
    public void initiateRepair(Long ownerId, Long propertyId, TypeOfRepairEnum typeOfRepair, String shortDescription, String fullDescription) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException("Owner with id " + ownerId + " not found"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property with id " + propertyId + " not found"));

        PropertyRepair repair = PropertyRepair.builder()
                .shortDescription(shortDescription)
                .property(property)
                .typeOfRepair(typeOfRepair)
                .submissionDate(LocalDate.now())
                .description(fullDescription)
                .status(StatusOfRepairEnum.PENDING)
                .isActive(true)
                .build();
        propertyRepairRepository.create(repair);
    }

    @Override
    public Boolean acceptRepair(PropertyRepair propertyRepair) throws UserExistsException {
        try {
            if (propertyRepair != null) {
                if (propertyRepair.isOwnerAcceptance() == true) {
                    propertyRepair.setStatus(StatusOfRepairEnum.INPROGRESS);
                    propertyRepair.setActualStartDate(propertyRepair.getProposedStartDate());
                    propertyRepair.setActualEndDate(propertyRepair.getProposedEndDate());
                    propertyRepairRepository.update(propertyRepair);
                    return true;
                }
                propertyRepair.setStatus(StatusOfRepairEnum.DECLINED);
                /*-------------------Stay null--------------*/
                propertyRepairRepository.update(propertyRepair);
            }
        } catch (PersistenceException e) {
            throw new OwnerNotFoundException("There is no property to repair");
        }
        return false;

    }

    @Override
    public List<PropertyRepair> findRepairsInProgressByOwner(Long ownerId) {
        return propertyRepairRepository.findAll().stream()
                .filter(repair -> StatusOfRepairEnum.INPROGRESS.equals(repair.getStatus()) && ownerId.equals(repair.getProperty().getUser().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertyRepair> findRepairsCompletedByOwner(Long ownerId) {
        return propertyRepairRepository.findAll().stream()
                .filter(repair -> StatusOfRepairEnum.COMPLETE.equals(repair.getStatus()) && ownerId.equals(repair.getProperty().getUser().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deletePendingRepair(Long repairId) {
        propertyRepairRepository.findById(repairId).ifPresent(repair -> {
            if (StatusOfRepairEnum.PENDING.equals(repair.getStatus())) {
                propertyRepairRepository.delete(repair);
            }
        });
    }

    @Override
    public <T> void updateRepair(PropertyRepair propertyRepair, T value, int choose) {//any type of data
        try {
            if (propertyRepair.getStatus().equals(StatusOfRepairEnum.PENDING) && propertyRepair.getIsActive()) {
                switch (choose) {
                    case 1 -> {
                        if (value instanceof TypeOfRepairEnum typeOfRepairStr) {
                            propertyRepair.setTypeOfRepair(typeOfRepairStr);
                        }
                    }
                    case 2 -> {
                        if (value instanceof String shortDesciptionStr) {
                            propertyRepair.setShortDescription(shortDesciptionStr);
                        }
                        //auta isws ginoun throws h exceptions
                    }
                    case 3 -> {
                        if (value instanceof String descriptionStr) {
                            propertyRepair.setDescription(descriptionStr);
                        }
                    }

                }

                propertyRepairRepository.update(propertyRepair);
            }
        } catch (PersistenceException e) {
            throw new InvalidInputException("Your input is Invalid");
        }

    }

    @Override
    public List<PropertyRepair> searchRepairsByDateRage(LocalDate startDate, LocalDate endDate) {
        return propertyRepairRepository.searchByDateRange(startDate, endDate);
    }

    @Override
    public List<PropertyRepair> searchRepairsBySubmissionDate(LocalDate submissionDate) {
        return propertyRepairRepository.searchBySubmissionDate(submissionDate);
    }

    @Override
    public void softDelete(Long repairId) throws UserExistsException {
          PropertyRepair repair = propertyRepairRepository.findById(repairId)
            .orElseThrow(() -> new IllegalStateException("Repair with id " + repairId + " does not exist"));

        if (repair.getStatus().equals(StatusOfRepairEnum.PENDING)) {
            repair.setIsActive(false); 
            propertyRepairRepository.update(repair);
        } else {
            throw new IllegalStateException("Repair is not pending and cannot be deleted");
        }
    }
}

