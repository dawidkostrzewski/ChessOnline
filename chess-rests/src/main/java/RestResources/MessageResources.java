package RestResources;

import dataStore.Message;
import servicesImpl.MessageServicesImpl;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/messages")
@Stateless
@LocalBean
@Produces(MediaType.APPLICATION_JSON)
public class MessageResources {

    @EJB
    private MessageServicesImpl messageServices;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewMessage(Message message){
        try{
            return Response.status(Response.Status.OK).entity(messageServices.createNewMessage(message)).build();
        } catch (EJBTransactionRolledbackException e){
            return Response.serverError().entity(e.getMessage()).build();
        } catch (JMSException e){
            return  Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getMessagesByReciverId(@PathParam("id") Long id){
        return Response.status(Response.Status.OK).entity(messageServices.getMessageListByReciverId(id)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMessage(@PathParam("id") Long id){
        try{
            messageServices.deleteMessage(id);
            return Response.status(Response.Status.OK).build();
        } catch (EJBTransactionRolledbackException e){
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
