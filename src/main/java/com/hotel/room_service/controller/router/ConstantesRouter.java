package com.hotel.room_service.controller.router;

import com.hotel.room_service.controller.handler.ConstantesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ConstantesRouter {

    @Bean
    public RouterFunction<ServerResponse> constantesRoutes(ConstantesHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/constantes"), handler::consultarConstantes)
                .andRoute(POST("/api/v1/constantes"), handler::insertarConstante)
                .andRoute(PUT("/api/v1/constantes/{id}"), handler::actualizarConstante)
                .andRoute(DELETE("/api/v1/constantes/{id}"), handler::eliminarConstante);
    }
}
