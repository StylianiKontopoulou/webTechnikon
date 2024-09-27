package gr.codehub.rest.webtechnikon.repositories;

import gr.codehub.rest.webtechnikon.exceptions.OwnerNotFoundException;
import gr.codehub.rest.webtechnikon.models.User;
import gr.codehub.rest.webtechnikon.models.UserTypeEnum;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.Data;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@RequestScoped
public class UserRepository implements Repository<User> {

    @PersistenceContext(unitName = "Persistence")
    private EntityManager entityManager;

    @Override
    @Transactional
    public User create(User user) throws PersistenceException {
        entityManager.persist(user);
        return user;
    }

    @Override
    @Transactional
    public void update(User user) throws PersistenceException {
        entityManager.merge(user);
        entityManager.persist(user);
    }

    @Override
    @Transactional
    public void delete(User user) throws IllegalArgumentException, NullPointerException {
        entityManager.remove(user);
        entityManager.persist(user);

    }

    public void deleteOwner(User user) throws IllegalArgumentException, NullPointerException {
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM User u WHERE u.userType =: userType", User.class)
                .setParameter("userType", UserTypeEnum.PROPERTY_OWNER);
        entityManager.getTransaction().commit();
    }

    @Override
    @Transactional
    public <V> Optional<User> findById(V id) {
        try {
            User owner = entityManager.find(User.class, id);
            if (owner.getIsActive() == false) {
                return Optional.empty();
            }
            return Optional.of(owner);
        } catch (Exception e) {
            log.debug("Owner not found");
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        TypedQuery<User> query
                = entityManager.createQuery("SELECT u FROM User u WHERE u.isActive=true", User.class);
        return query.getResultList();
    }

    public List<User> findPropertyOwners() {
        TypedQuery<User> query
                = entityManager.createQuery("SELECT u FROM User u WHERE u.userType =: userType", User.class)
                        .setParameter("userType", UserTypeEnum.PROPERTY_OWNER);
        return query.getResultList();
    }

    public User searchByEmail(String email) {

        List<User> owner = entityManager.createQuery("SELECT u FROM User u WHERE u.email LIKE: givenEmail")
                .setParameter("givenEmail", email)
                .getResultList();

        if (owner.isEmpty()) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }

        if (owner.get(0).getIsActive() == false) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }

        return (User) owner.get(0);
    }

    public User searchByVat(Long vat) {

        List<User> owner = entityManager.createQuery("SELECT u FROM User u WHERE u.vat LIKE ?1")
                .setParameter(1, vat)
                .getResultList();

        if (owner.isEmpty()) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }

        if (owner.get(0).getIsActive() == false) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }

        return (User) owner.get(0);
    }
    
    @Transactional
    public void deleteById(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            user.setIsActive(Boolean.FALSE);
        }
        entityManager.persist(user);
    }
}
