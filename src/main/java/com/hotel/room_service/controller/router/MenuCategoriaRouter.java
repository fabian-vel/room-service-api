package com.hotel.room_service.controller.router;

import com.hotel.room_service.controller.handler.MenuCategoriaHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class MenuCategoriaRouter {

    @Bean
    public RouterFunction<ServerResponse> categoriasRoutes(MenuCategoriaHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/categorias"), handler::consultarCategorias)
                .andRoute(GET("/api/v1/categorias/todas"), handler::consultarCategoriasTodas)
                .andRoute(POST("/api/v1/categorias"), handler::insertarCategoria)
                .andRoute(PUT("/api/v1/categorias/{id}"), handler::actualizarCategoria)
                .andRoute(DELETE("/api/v1/categorias/{id}"), handler::eliminarCategoria);
    }
}
