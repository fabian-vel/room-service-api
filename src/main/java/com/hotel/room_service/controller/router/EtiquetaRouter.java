package com.hotel.room_service.controller.router;

import com.hotel.room_service.controller.handler.EtiquetaHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class EtiquetaRouter {

    @Bean
    public RouterFunction<ServerResponse> etiquetaRoutes(EtiquetaHandler handler) {
        return RouterFunctions
                .route(POST("/api/v1/etiquetas"), handler::insertarEtiqueta)
                .andRoute(GET("/api/v1/etiquetas"), handler::consultarEtiquetas)
                .andRoute(POST("/api/v1/etiquetas/consulta"), handler::consultaEtiqueta)
                .andRoute(PUT("/api/v1/etiquetas/{id}"), handler::actualizarEtiqueta)
                .andRoute(DELETE("/api/v1/etiquetas/{id}"), handler::eliminarEtiqueta);
    }
}
