package gr.codehub.rest.webtechnikon.resources;

import gr.codehub.rest.webtechnikon.models.User;
import gr.codehub.rest.webtechnikon.services.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Path("Users")
@Slf4j     
public class UserResource {
    
    @Inject
    private UserService userService;
    
   @Path("Users/admin/property-owners")
   @GET
   @Produces("text/json")
    public List<User> getOwners(){
        return userService.getUsers();
    }
    
//    
//   @Path("Users/admin/property-owners/{propertyOwnerId}")
//   @DELETE
//   @Consumes("application/json")
//   @Produces("application/json")   
//  public boolean deleteOwner(@PathParam ("ownerId") long ownerId){
//      return  userService.deleteOwner(ownerId);
//  }
//  
//  @Path("Users/property-owners/properties/{propertyID}")
//  @GET
//  @Produces("text/json")
//  
}
