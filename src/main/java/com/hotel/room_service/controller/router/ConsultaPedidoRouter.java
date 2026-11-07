package com.hotel.room_service.controller.router;

import com.hotel.room_service.controller.handler.ConsultaPedidoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class ConsultaPedidoRouter {

    @Bean
    public RouterFunction<ServerResponse> consultarPedidoPorUsuarioRoutes(ConsultaPedidoHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/consulta-pedidos"), request -> handler.consultarPedidoPorUsuario());
    }

    @Bean
    public RouterFunction<ServerResponse> consultarPedidoFechaActualRoutes(ConsultaPedidoHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/consulta-pedidos-fecha-actual"), request -> handler.consultarPedidoFechaActual());
    }

    @Bean
    public RouterFunction<ServerResponse> consultarPedidoFiltroRoutes(ConsultaPedidoHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/pedidos"), handler::consultarPedidoFiltro);
    }
}
