package RestResources;

import dataStore.Game;
import servicesImpl.GameServicesImpl;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/games")
@Stateless
@LocalBean
@Produces(MediaType.APPLICATION_JSON)
public class GameResources {

    @EJB
    private GameServicesImpl gameServices;

    @GET
    @Path("/{id}")
    public Response getGamesForUser(@PathParam("id") Long id){
        return Response.status(Response.Status.OK).entity(gameServices.getGamesForUserByUserId(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewGame(Game game){
        try{
            return Response.status(Response.Status.OK).entity(gameServices.createNewGame(game)).build();
        } catch (EJBTransactionRolledbackException e){
            return Response.serverError().entity(e.getMessage()).build();
        } catch (JMSException e){
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGame(Game game,@QueryParam("userId") Long userId){
        try {
            return Response.status(Response.Status.OK).entity(gameServices.updateGame(game, userId)).build();
        } catch (EJBTransactionRolledbackException e) {
            return Response.serverError().entity(e.getMessage()).build();
        } catch (JMSException e){
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
