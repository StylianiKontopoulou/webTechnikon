package gr.codehub.rest.webtechnikon.repositories;

import gr.codehub.rest.webtechnikon.models.Property;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
public class PropertyRepository implements Repository<Property> {

    @PersistenceContext(unitName = "Persistence")
    private EntityManager entityManager;

    @Override
    @Transactional
    public Property create(Property property) {
        entityManager.persist(property);
        return property;
    }

    @Override
    @Transactional
    public void update(Property property) {
        entityManager.merge(property);
    }

    @Override
    @Transactional
    public void delete(Property property) {
        // Merge the detached entity and get the managed instance
        Property managedProperty = entityManager.contains(property) ? property : entityManager.merge(property);

        // Now remove the managed instance
        entityManager.remove(managedProperty);
        
        entityManager.flush();
    }

    @Transactional
    public void softDelete(Property property) {
        property.setIsActive(false);
        entityManager.merge(property);
    }

    @Override
    @Transactional
    public <V> Optional<Property> findById(V id) {
        try {
            Property property = entityManager.find(Property.class, id);
            if (property != null && property.getIsActive()) {
                return Optional.of(property);
            }
        } catch (Exception e) {
            log.debug("Property not found");
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public List<Property> findAll() {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.isActive = true", Property.class);
        return query.getResultList();
    }

    public Optional<Property> findByPropertyIdNumber(Long propertyId) {
        try {
            TypedQuery<Property> query = entityManager.createQuery(
                    "SELECT p FROM Property p WHERE p.Id = :propertyId AND p.isActive = true", Property.class);
            query.setParameter("propertyId", propertyId);
            List<Property> result = query.getResultList();
            if (result.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(result.get(0));
            }
        } catch (Exception e) {
            log.debug("Property with this propertyid not found");
        }
        return Optional.empty();
    }

    public List<Property> findByOwnerVatNumber(Long vatNumber) {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.user.vat = :vatNumber AND p.isActive = true", Property.class);
        query.setParameter("vatNumber", vatNumber);
        return query.getResultList();
    }

}
