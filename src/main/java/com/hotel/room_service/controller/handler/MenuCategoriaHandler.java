package com.hotel.room_service.controller.handler;

import com.hotel.room_service.application.usecase.MenuCategoriaUseCase;
import com.hotel.room_service.application.transformer.MenuCategoriaTransformer;
import com.hotel.room_service.domain.model.MenuCategoria;
import com.hotel.room_service.shared.constant.SuccessMessages;
import com.hotel.room_service.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MenuCategoriaHandler {

    private final MenuCategoriaUseCase menuCategoriaUseCase;

    public Mono<ServerResponse> consultarCategorias(ServerRequest request) {
        return menuCategoriaUseCase.consultarCategorias()
                .map(categorias -> categorias.stream()
                        .map(MenuCategoriaTransformer::toResponse)
                        .toList()
                )
                .flatMap(categorias -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                SuccessMessages.CATEGORIAS_CONSULTADAS.getMessage(),
                                categorias,
                                categorias.size()
                        ))
                );
    }

    public Mono<ServerResponse> consultarCategoriasTodas(ServerRequest request) {
        return menuCategoriaUseCase.consultarCategoriasTodas()
                .map(categorias -> categorias.stream()
                        .map(MenuCategoriaTransformer::toResponse)
                        .toList()
                )
                .flatMap(categorias -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Categorías consultadas exitosamente",
                                categorias,
                                categorias.size()
                        ))
                );
    }

    public Mono<ServerResponse> insertarCategoria(ServerRequest request) {
        return request.bodyToMono(MenuCategoria.class)
                .flatMap(menuCategoriaUseCase::insertarCategoria)
                .flatMap(id -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Categoría creada exitosamente",
                                id,
                                1
                        ))
                );
    }

    public Mono<ServerResponse> actualizarCategoria(ServerRequest request) {
        Mono<Short> idMono = Mono.just(Short.parseShort(request.pathVariable("id")));
        return idMono.flatMap(id ->
                request.bodyToMono(MenuCategoria.class)
                        .map(categoria -> {
                            categoria.setMecaId(id);
                            return categoria;
                        })
        )
                .flatMap(menuCategoriaUseCase::actualizarCategoria)
                .flatMap(rows -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Categoría actualizada exitosamente",
                                rows,
                                1
                        ))
                );
    }

    public Mono<ServerResponse> eliminarCategoria(ServerRequest request) {
        Short mecaId = Short.parseShort(request.pathVariable("id"));
        return menuCategoriaUseCase.eliminarCategoria(mecaId)
                .flatMap(rows -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Categoría eliminada exitosamente",
                                rows,
                                1
                        ))
                );
    }
}
