package gr.codehub.rest.webtechnikon.resources;

import gr.codehub.rest.webtechnikon.models.Property;
import gr.codehub.rest.webtechnikon.models.PropertyRepair;
import gr.codehub.rest.webtechnikon.models.TypeOfRepairEnum;
import gr.codehub.rest.webtechnikon.models.User;
import gr.codehub.rest.webtechnikon.services.PropertyRepairService;
import gr.codehub.rest.webtechnikon.services.PropertyService;
import gr.codehub.rest.webtechnikon.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {

    @Inject
    private UserService userService;

    @Inject
    private PropertyService propertyService;

    @Inject
    private PropertyRepairService propertyRepairService;

    @GET
    @Path("/repairs")
    @RolesAllowed("ADMIN")
    public Response listOrSearchRepairs(@QueryParam("description") String description,
            @QueryParam("status") String status) {
        try {
            List<PropertyRepair> repairs;
            if (description != null || status != null) {
                repairs = propertyRepairService.searchRepairs(description, status);
            } else {
                repairs = propertyRepairService.getAllRepairs();
            }
            return Response.ok(repairs).build();
        } catch (Exception e) {
            log.error("Error listing or searching repairs", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/users")
    @RolesAllowed("ADMIN")
    public Response listOrSearchUsers(@QueryParam("name") String name, @QueryParam("email") String email) {
        try {
            List<User> users;
            if (name != null || email != null) {
                users = userService.searchUsers(name, email);
            } else {
                users = userService.getAllUsers();
            }
            return Response.ok(users).build();
        } catch (Exception e) {
            log.error("Error listing or searching users", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/properties")
    @RolesAllowed("ADMIN")
    public Response listOrSearchProperties(@QueryParam("location") String location,
            @QueryParam("type") String type) {
        try {
            List<Property> properties;
            if (location != null || type != null) {
                properties = propertyService.searchProperties(location, type);
            } else {
                properties = propertyService.getAllProperties();
            }
            return Response.ok(properties).build();
        } catch (Exception e) {
            log.error("Error listing or searching properties", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
