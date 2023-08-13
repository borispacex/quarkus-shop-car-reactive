package org.redeasy.car.resource;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.redeasy.car.entity.Car;
import org.redeasy.car.entity.Shop;
import org.redeasy.car.repository.CarRepository;
import org.redeasy.car.repository.ShopRepository;

import java.net.URI;
import java.util.List;

@Path("/cars")
public class CarResource {

    @Inject
    CarRepository carRepository;
    @Inject
    ShopRepository shopRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Car> cars = carRepository.listAll();
        return Response.ok(cars).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getById(@PathParam("id") Long id) {
        Car car = carRepository.findById(id);
        if (carRepository.isPersistent(car)) {
            return Response.ok(car).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brand/{brand}")
    public Response getByBrand(@PathParam("brand") String brand) {
        List<Car> cars = carRepository.list("SELECT c FROM Car c WHERE c.brand = ?1 ORDER BY id", brand);
        return Response.ok(cars).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Car car) {
        carRepository.persist(car);
        if (carRepository.isPersistent(car)) {
            return Response.created(URI.create("cars" + car.id)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = carRepository.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();

    }

    @PUT
    @Path("/{idCar}/shop/{idShop}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response addToShop(@PathParam("idCar") Long idCar, @PathParam("idShop") Long idShop) {
        Car car = carRepository.findById(idCar);
        Shop shop = shopRepository.findById(idShop);
        if (car != null && shop != null) {
            car.setShop(shop);
            carRepository.persist(car);
            return Response.ok(car).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // Obtenr el numero de autos
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("size")
    public String getSize() {
        return String.format("{\"size\": %d}", carRepository.count());
    }

    // Metodo que retorna las lista de vehichulos asignado a una tienda
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("shop/{idShop}")
    @Blocking
    public Response getCarsByShopId(@PathParam("idShop") Long idShop) {
        delay(5000L);
        List<Car> cars = carRepository.list("SELECT c FROM Car c WHERE c.shop.id = ?1 ORDER BY id DESC", idShop);
        return Response.ok(cars).build();
    }

    // metodos para reactive

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("shop/{idShop}/reactive")
    @NonBlocking
    public Uni<List<Car>> getCarsByShopIdReactive(@PathParam("idShop") Long idShop) {
        delay(5000L);
        List<Car> cars = carRepository.list("SELECT c FROM Car c WHERE c.shop.id = ?1 ORDER BY id DESC", idShop);
        return (Uni<List<Car>>) cars;
    }

    public void delay(Long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
