package gr.codehub.rest.webtechnikon.resources;

import gr.codehub.rest.webtechnikon.models.Property;
import gr.codehub.rest.webtechnikon.models.PropertyRepair;
import gr.codehub.rest.webtechnikon.models.User;
import gr.codehub.rest.webtechnikon.models.UserTypeEnum;
import gr.codehub.rest.webtechnikon.services.PropertyRepairService;
import gr.codehub.rest.webtechnikon.services.PropertyService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/repairs")
public class RepairResource {

    @Inject
    private PropertyRepairService propertyRepairService;

    @Inject
    private PropertyService propertyService;

    @Context
    private HttpServletRequest requestContext;

    @GET
    @RolesAllowed("ALL")
    public Response listRepairs(@QueryParam("propertyId") Long propertyId) {
        Property property = propertyService.getPropertyById(propertyId);
        if (property == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        User user = (User) requestContext.getAttribute("currentUser");
        if (user.getUserType().equals(UserTypeEnum.PROPERTY_OWNER) && !property.getUser().getId().equals(user.getId())) {
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to access this resource").build();
        }
        try {
            List<PropertyRepair> repairs = propertyRepairService.getRepairsByPropertyId(propertyId);
            return Response.ok(repairs).build();
        } catch (Exception e) {
            log.error("Error listing repairs", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @RolesAllowed("ALL")
    public Response createRepair(PropertyRepair req) {
        Property property = propertyService.getPropertyById(req.getProperty().getId());
        if (property == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        User user = (User) requestContext.getAttribute("currentUser");
        if (user.getUserType().equals(UserTypeEnum.PROPERTY_OWNER) && !property.getUser().getId().equals(user.getId())) {
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to access this resource").build();
        }
        try {
            PropertyRepair repair = propertyRepairService.createRepair(req);
            return Response.status(Response.Status.CREATED).entity(repair).build();
        } catch (Exception e) {
            log.error("Error creating repair", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{repairId}")
    @RolesAllowed("ALL")
    public Response getRepairDetails(@PathParam("repairId") Long repairId) {
        try {
            PropertyRepair repair = propertyRepairService.getRepairById(repairId);
            if (repair != null) {
                User user = (User) requestContext.getAttribute("currentUser");

                if (user.getUserType().equals(UserTypeEnum.PROPERTY_OWNER) && !repair.getProperty().getUser().getId().equals(user.getId())) {
                    return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to access this resource").build();
                }
                return Response.ok(repair).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error("Error getting repair details", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{repairId}")
    @RolesAllowed("ALL")
    public Response updateRepairDetails(@PathParam("repairId") Long repairId, PropertyRepair repair) {
        try {
            propertyRepairService.updateRepair(repairId, repair);
            return Response.ok().build();
        } catch (Exception e) {
            log.error("Error updating repair details", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{repairId}")
    @RolesAllowed("ADMIN")
    public Response deleteRepair(@PathParam("repairId") Long repairId) {
        PropertyRepair repair = propertyRepairService.getRepairById(repairId);
        if (repair == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        User user = (User) requestContext.getAttribute("currentUser");

        if (user.getUserType().equals(UserTypeEnum.PROPERTY_OWNER) && !repair.getProperty().getUser().getId().equals(user.getId())) {
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to access this resource").build();
        }
        try {
            propertyRepairService.deleteRepair(repairId);
            return Response.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting repair", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
