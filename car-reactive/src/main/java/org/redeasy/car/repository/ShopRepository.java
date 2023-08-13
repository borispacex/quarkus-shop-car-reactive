package org.redeasy.car.repository;

import org.redeasy.car.entity.Shop;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShopRepository implements PanacheRepository<Shop> {

}
