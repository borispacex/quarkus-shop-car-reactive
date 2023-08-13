package org.redeasy.car.register;

import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.consul.ConsulClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Registration {

    // Info: Localizacion de consul
    @ConfigProperty(name = "consul.host") String host;
    @ConfigProperty(name = "consul.port") int port;
    // Info: Registra el puerto donde se ejectura e servicio a registrar
    @ConfigProperty(name = "quarkus.http.port", defaultValue = "8080") int car1Port;

    public void init(@Observes StartupEvent ev, Vertx vertx) {
        ConsulClient client = ConsulClient.create(vertx, new ConsulClientOptions()
                .setHost(host)
                .setPort(port));

        client.registerServiceAndAwait(new ServiceOptions()
                .setPort(car1Port)
                .setAddress("localhost") // 192.168.56.1
                .setName("car-service")
                .setId("car-1"));
    }

}
