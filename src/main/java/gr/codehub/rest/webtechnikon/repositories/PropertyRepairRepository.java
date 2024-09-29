package gr.codehub.rest.webtechnikon.repositories;

import gr.codehub.rest.webtechnikon.exceptions.UserNotFoundException;
import gr.codehub.rest.webtechnikon.models.PropertyRepair;
import gr.codehub.rest.webtechnikon.models.StatusOfRepairEnum;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@RequestScoped
public class PropertyRepairRepository extends AbstractRepository<PropertyRepair> {

    public PropertyRepairRepository() {
        super(PropertyRepair.class);
    }

    public List<PropertyRepair> findRepairsByCriteria(String description, String status) {
        String query = "SELECT r FROM PropertyRepair r WHERE 1=1";
        if (description != null && !description.isEmpty()) {
            query += " AND r.description LIKE :description";
        }
        if (status != null && !status.isEmpty()) {
            query += " AND r.status = :status";
        }
        
        TypedQuery<PropertyRepair> q = entityManager.createQuery(query, PropertyRepair.class);
        if (description != null && !description.isEmpty()) {
            q.setParameter("description", "%" + description + "%");
        }
        if (status != null && !status.isEmpty()) {
            q.setParameter("status", StatusOfRepairEnum.valueOf(status));
        }
        
        return q.getResultList();
    }

    public List<PropertyRepair> findRepairsByPropertyId(Long propertyId) {
        return entityManager.createQuery("SELECT r FROM PropertyRepair r WHERE r.property.id = :propertyId", PropertyRepair.class)
                .setParameter("propertyId", propertyId)
                .getResultList();
    }

    public List<PropertyRepair> searchByDateRange(LocalDate startDate, LocalDate endDate) {
        TypedQuery<PropertyRepair> query;
        query = entityManager.createQuery(
                "SELECT r FROM PropertyRepair r WHERE isActive = TRUE AND r.submissionDate BETWEEN :startDate AND :endDate", PropertyRepair.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        if (query.getResultList().isEmpty()) {
            throw new UserNotFoundException("There is no Repair between" + startDate + "and" + endDate);
        }
        return query.getResultList();
    }
}