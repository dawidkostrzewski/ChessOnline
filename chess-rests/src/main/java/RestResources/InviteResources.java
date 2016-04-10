package RestResources;

import dataStore.Invite;
import servicesImpl.InviteServicesImpl;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/invites")
@Stateless
@LocalBean
@Produces(MediaType.APPLICATION_JSON)
public class InviteResources {

    @EJB
    private InviteServicesImpl inviteServices;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewInvite(Invite invite){
        try{
            return Response.status(Response.Status.OK).entity(inviteServices.createNewInvite(invite)).build();
        } catch (Exception e){
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateInvite(Invite invite){
        try{
            return Response.status(Response.Status.OK).entity(inviteServices.updateInvite(invite)).build();
        } catch (EJBTransactionRolledbackException e){
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getInvitesByReciverId(@PathParam("id") Long id){
        return Response.status(Response.Status.OK).entity(inviteServices.getInvitesByReciverId(id)).build();
    }
}
