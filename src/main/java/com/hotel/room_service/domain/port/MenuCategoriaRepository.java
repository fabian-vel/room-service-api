package com.hotel.room_service.domain.port;

import com.hotel.room_service.domain.model.MenuCategoria;
import com.hotel.room_service.infrastructure.model.MenuCategoriaRow;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MenuCategoriaRepository {
    Mono<List<MenuCategoriaRow>> consultarCategorias();
    Mono<List<MenuCategoria>> consultarCategoriasTodas();
    Mono<Short> insertarCategoria(MenuCategoria categoria);
    Mono<Long> actualizarCategoria(MenuCategoria categoria);
    Mono<Long> eliminarCategoria(Short mecaId);
}

