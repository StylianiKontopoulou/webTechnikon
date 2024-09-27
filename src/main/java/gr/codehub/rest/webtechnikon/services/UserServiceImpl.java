package gr.codehub.rest.webtechnikon.services;

import gr.codehub.rest.webtechnikon.exceptions.InvalidInputException;
import gr.codehub.rest.webtechnikon.exceptions.MissingInputException;
import gr.codehub.rest.webtechnikon.exceptions.OwnerNotFoundException;
import gr.codehub.rest.webtechnikon.exceptions.UserExistsException;

import gr.codehub.rest.webtechnikon.models.User;
import gr.codehub.rest.webtechnikon.repositories.UserRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;
import lombok.Data;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class UserServiceImpl implements UserService{

    @Inject
    private UserRepository userRepository;

    @Override
    public User get(Long id) throws OwnerNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()){
            throw new OwnerNotFoundException("This is not an existing owner");
        } else {
            return optionalUser.get();
        }
    }

    public User signInOwner(String email, String password) throws OwnerNotFoundException, InvalidInputException{

            User user = userRepository.searchByEmail(email);
            if (user.getPassword().equals(password)) {
                return user;
            }
            throw new InvalidInputException("You entered the wrong password");
    }

    @Override
    public User create(User user) throws UserExistsException, InvalidInputException, MissingInputException {
        try {
            if (!PatternService.EMAIL_PATTERN.matcher(user.getEmail().trim()).matches()) {
                throw new InvalidInputException("This is not a valid email");
            }
            if (!PatternService.PASSWORD_PATTERN.matcher(user.getPassword().trim()).matches()) {
                throw new InvalidInputException("This is not a valid password");
            }
            if (!PatternService.VAT_PATTERN.matcher(user.getVat().trim()).matches()) {
                throw new InvalidInputException("This is not a valid vat number");
            }
            if (!PatternService.PHONE_NUMBER_PATTERN.matcher(user.getPhoneNumber().trim()).matches()) {
                throw new InvalidInputException("This is not a valid phone number");
            }

            User userEntity = User.builder()
                    .vat(user.getVat().trim())
                    .phoneNumber(user.getPhoneNumber().trim())
                    .address(user.getAddress())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .password(user.getPassword())
                    .userName(user.getUserName())
                    .email(user.getEmail())
                    .userType(user.getUserType())
                    .isActive(true)
                    .build();

            // throws a persistence exception
            userRepository.create(userEntity);
            return userEntity;
        } catch (PersistenceException e) {
            log.error(e.getMessage());
            // a null value was passed
            if (e.getMessage().contains("PropertyValueException")) {
                throw new MissingInputException("There's input missing");

            // the user already exists
            } else {
                throw new UserExistsException("This user already exists");
            }
        }
    }

    
    public void deleteOwner(Long owner_Id) throws OwnerNotFoundException{
        User owner = get(owner_Id);
        try {
            userRepository.delete(owner);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new OwnerNotFoundException("This is not an existing user");
        }
    }

    public void safeDelete(User propertyOwner) throws PersistenceException {

        User existingOwner = get(propertyOwner.getId());
        try {
            existingOwner.setIsActive(false);
            userRepository.update(existingOwner);

            // throws an exception if something goes wrong with the userRepository.update
        } catch (PersistenceException e) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }
    }

    @Override
    public void update(User updatedUser) throws InvalidInputException, UserExistsException, OwnerNotFoundException {

        // checks if the id of the updatedUser belongs to an existing user. Throws an exception otherwise
        User existingOwner = get(updatedUser.getId());
        try {

            // only three fields can be updated
            if (!existingOwner.getFirstName().equals(updatedUser.getFirstName()) ||
                    !existingOwner.getLastName().equals(updatedUser.getLastName()) ||
                    !existingOwner.getPhoneNumber().equals(updatedUser.getPhoneNumber()) ||
                    !existingOwner.getVat().equals(updatedUser.getVat()) ||
                    !existingOwner.getUserName().equals(updatedUser.getUserName()) ||
                     !Objects.equals(existingOwner.getIsActive(), updatedUser.getIsActive())){

                throw new InvalidInputException("You tried to update an unmodifiable field. You can only update the email, password and address");
            }

            // if the address is changed
            if (!existingOwner.getAddress().equals(updatedUser.getAddress())) {
                existingOwner.setAddress(updatedUser.getAddress());
            }

            // if the password is changed and of a certain pattern
            if (!existingOwner.getPassword().equals(updatedUser.getPassword())) {
                if (!PatternService.PASSWORD_PATTERN.matcher(updatedUser.getPassword().trim()).matches()) {
                    throw new InvalidInputException("This is not a valid password");
                }
                existingOwner.setPassword(updatedUser.getPassword());

            }

            // if the email is changed and of a certain pattern
            if (!existingOwner.getEmail().equals(updatedUser.getEmail())) {
                if (!PatternService.EMAIL_PATTERN.matcher(updatedUser.getEmail().trim()).matches()) {
                    throw new InvalidInputException("This is not a valid email");
                }
                // if the email exists in the database it throws an exception

                try {
                    userRepository.searchByEmail(updatedUser.getEmail());
                    throw new UserExistsException("This email is already being used");
                } catch (OwnerNotFoundException e) {
                    existingOwner.setEmail(updatedUser.getEmail());
                }
            }

            userRepository.update(existingOwner);

        // throws an exception if something goes wrong with the userRepository.update
        } catch (PersistenceException e) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
