package RestResources;




import servicesImpl.UserServicesImpl;
import dataStore.User;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Stateless
@LocalBean
@Produces(MediaType.APPLICATION_JSON)
public class UserResources {

    @EJB
    private UserServicesImpl userServices;

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id){
        return  Response.status(Response.Status.OK).entity(userServices.getUserById(id)).build();
    }

    @GET
    @Path("/login/{login}")
    public Response getUserByLogin(@PathParam("login") String login){
        return Response.status(Response.Status.OK).entity(userServices.getUserByLogin(login)).build();
    }

    @GET
    @Path("/")
    public Response getUsersList(){
        return Response.status(Response.Status.OK).entity(userServices.getUsersList()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewUser(User user){
        try{
           return Response.status(Response.Status.OK).entity(userServices.addNewUser(user)).build();
        } catch (EJBTransactionRolledbackException e){
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(User user){
        try{
            return Response.status(Response.Status.OK).entity(userServices.updateUser(user)).build();
        } catch (EJBTransactionRolledbackException e){
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
