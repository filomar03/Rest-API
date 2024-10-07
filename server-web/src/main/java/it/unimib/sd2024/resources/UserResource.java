package it.unimib.sd2024.resources;

import it.unimib.sd2024.LogicHandler;
import it.unimib.sd2024.models.User;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("users")
public class UserResource {
    static LogicHandler logicHandler = LogicHandler.getInstance();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User request) {
        boolean success = logicHandler.registerUser(request);

        JsonObject response = Json.createObjectBuilder()
            .add("user", success ? "registered" : "not registered")
            .build();
        
        if (success) {
            return Response.ok(response).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(response).build();
        }
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkUsernameAvailability(@PathParam("userId") String userId) {
        boolean success = logicHandler.verifyUser(userId) == 1;

        JsonObject response = Json.createObjectBuilder()
            .add("user", success ? "verified" : "not verified")
            .build();
        
        if (success) {
            return Response.ok(response).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }
    }
}