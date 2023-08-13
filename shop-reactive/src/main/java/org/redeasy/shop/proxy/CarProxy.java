package org.redeasy.shop.proxy;

import java.util.List;

import io.smallrye.common.annotation.NonBlocking;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.redeasy.shop.entity.Car;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
//@RegisterRestClient(baseUri = "http://localhost:8080/")
@RegisterRestClient(baseUri = "stork://car-service")
public interface CarProxy {

    @GET
    @Path("shop/{idShop}")
    List<Car> getCarsByShopId(@PathParam("idShop") Long idShop);

    // metodo reactivo
    @GET
    @Path("shop/{idShop}/reactive")
    @NonBlocking
    Uni<List<Car>> getCarsByShopIdReactive(@PathParam("idShop") Long idShop);

}
