package org.redeasy.shop.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.redeasy.shop.entity.Shop;

@ApplicationScoped
public class ShopRepository implements PanacheRepository<Shop> {

}
