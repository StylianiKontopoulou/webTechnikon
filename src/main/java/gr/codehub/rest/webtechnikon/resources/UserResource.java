package gr.codehub.rest.webtechnikon.resources;

import gr.codehub.rest.webtechnikon.models.Property;
import gr.codehub.rest.webtechnikon.models.PropertyRepair;
import gr.codehub.rest.webtechnikon.services.PropertyRepairService;
import gr.codehub.rest.webtechnikon.services.PropertyService;
import gr.codehub.rest.webtechnikon.services.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Path("owner")
@Slf4j     
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    
    @Inject
    private UserService userService;
    @Inject
    private PropertyService propertyService;
    @Inject
    private PropertyRepairService propertyRepairService;
    
//  @POST
//    @Path("properties")
//    public Response createProperty(Property req) {
//        Property property = propertyService.createProperty(req);
//        return Response.status(Response.Status.CREATED).entity(property).build();
//    }
    
     // GET owner's properties
    @GET
    @Path("/properties")
    public Response getOwnerProperties(@QueryParam("ownerId") Long ownerId) {
        List<Property> properties = propertyService.findByOwnerVatNumber(ownerId);
        return Response.ok(properties).build();
    }
    
     // GET repairs for a specific property
    @GET
    @Path("/properties/{propertyId}/repairs")
    public Response getPropertyRepairs(@PathParam("propertyId") Long propertyId) {
        List<PropertyRepair> repairs = propertyRepairService.findRepairsByProperty(propertyId);
        return Response.ok(repairs).build();
    }
    // Add repair for a specific property
//    @POST
//    @Path("/properties/{propertyId}/repairs")
//    public Response addRepair(@HeaderParam("ownerId") Long ownerId,@PathParam("propertyId") Long propertyId, PropertyRepair repair) {
//        propertyRepairService.initiateRepair(οwnerId, propertyId, repair.getTypeOfRepair(), repair.getShortDescription(), repair.getDescription());
//        return Response.status(Response.Status.CREATED).build();
//    }
    // Delete repair (only if it is not complete)
    @DELETE
    @Path("/repairs/{repairId}")
    public Response deleteRepair(@PathParam("repairId") Long repairId) {
        propertyRepairService.deletePendingRepair(repairId);
        return Response.noContent().build();
    }
 
}
