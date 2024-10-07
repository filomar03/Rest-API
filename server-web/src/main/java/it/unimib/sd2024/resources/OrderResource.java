package it.unimib.sd2024.resources;

import it.unimib.sd2024.LogicHandler;
import it.unimib.sd2024.responses.OrdersResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("orders")
public class OrderResource {
    static LogicHandler logicHandler = LogicHandler.getInstance();

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserOrders(@PathParam("userId") String userId) {
        OrdersResponse orders = logicHandler.fetchUserOrders(userId);

        if (orders == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if (!orders.getUserId().equals("user not found")) {
            return Response.ok(orders).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(orders).build();
        }
    }
}