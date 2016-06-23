package RestResources;

import dataStore.Friends;
import servicesImpl.FriendsServicesImpl;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/friends")
@Stateless
@LocalBean
@Produces(MediaType.APPLICATION_JSON)
public class FriendsResources {

    @EJB
    private FriendsServicesImpl friendsServices;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addToFriendsList(Friends friends){
        try{
            return Response.status(Response.Status.CREATED).entity(friendsServices.createFriend(friends)).build();
        } catch (EJBTransactionRolledbackException e){
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getFriendsList(@PathParam("id") Long id){
        return  Response.status(Response.Status.OK).entity(friendsServices.getFriendsForUser(id)).build();
    }

    @GET
    @Path("/check")
    public Response checkIfCanAdd(@QueryParam("userId") Long userId,
                                  @QueryParam("friendId") Long friendId){
        return Response.status(Response.Status.OK).entity(friendsServices.checkIfCanAdd(userId,friendId)).build();
    }

    @DELETE
    @Path("/remove")
    public Response removeFriend(@QueryParam("userId") Long userId,
                                 @QueryParam("friendId") Long friendId){
        try{
            friendsServices.removeFriend(userId, friendId);
            return Response.status(Response.Status.OK).build();
        } catch (EJBTransactionRolledbackException e){
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
