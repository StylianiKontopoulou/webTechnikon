package gr.codehub.rest.webtechnikon.resources;

import gr.codehub.rest.webtechnikon.models.Property;
import gr.codehub.rest.webtechnikon.models.User;
import gr.codehub.rest.webtechnikon.services.PropertyRepairService;
import gr.codehub.rest.webtechnikon.services.PropertyService;
import gr.codehub.rest.webtechnikon.services.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

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

    // GET all owners and their properties
    @GET
    @Path("/users")
    public Response getUsers() {
        List<User> owners = userService.getUsers();
        return Response.ok(owners).build();
    }

    // Add new user
    @POST
    @Path("/users")
    public Response createUser(User req) {
        User user = userService.create(req);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    // Delete user
    @DELETE
    @Path("/users/{userId}")
    public Response deleteUser(@PathParam("userId") Long userId) {
        userService.delete(userId);
        return Response.noContent().build();
    }

    @GET
    @Path("properties")
    public Response getProperties() {
        List<Property> properties = propertyService.findAll();
        return Response.ok(properties).build();
    }

    @POST
    @Path("properties")
    public Response createProperty(Property req) {
        Property property = propertyService.createProperty(req);
        return Response.status(Response.Status.CREATED).entity(property).build();
    }
    
    @DELETE
    @Path("properties/{propertyId}")
    public Response deleteProperty(@PathParam("propertyId") Long propertyId) {
        propertyService.deleteProperty(propertyId);
        return Response.noContent().build();
    }
//
//    // Search repairs by date range or submission date
//    @GET
//    @Path("/repairs")
//    public Response searchRepairs(
//            @QueryParam("startDate") String startDate, 
//            @QueryParam("endDate") String endDate, 
//            @QueryParam("submissionDate") String submissionDate
//    ) {
//
//        if (submissionDate != null) {
//            List<PropertyRepair> repairs = propertyRepairService.searchRepairsBySubmissionDate(LocalDate.parse(submissionDate));
//            return Response.ok(repairs).build();
//        } else if (startDate != null && endDate != null) {
//            List<PropertyRepair> repairs = propertyRepairService.searchRepairsByDateRage(LocalDate.parse(startDate), LocalDate.parse(endDate));
//            return Response.ok(repairs).build();
//        }
//        return Response.status(Response.Status.BAD_REQUEST).entity("Invalid query parameters").build();
//    }

//    // Update repair status and propose dates
//    @PUT
//    @Path("/repairs/{repairId}")
//    public Response updateRepairStatus(
//        @PathParam("repairId") Long repairId, 
//        @QueryParam("status") StatusOfRepairEnum status, 
//        @QueryParam("proposedStartDate") LocalDate proposedStartDate,
//        @QueryParam("proposedEndDate") LocalDate proposedEndDate
//    ) {
//        PropertyRepair updatedRepair = propertyRepairService.updateRepairStatus(repairId, status, proposedStartDate, proposedEndDate);
//        return Response.ok(updatedRepair).build();
//    }
}
