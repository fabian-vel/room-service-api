package com.hotel.room_service.controller.handler;

import com.hotel.room_service.application.usecase.ConstantesUseCase;
import com.hotel.room_service.application.transformer.ConstantesTransformer;
import com.hotel.room_service.domain.model.Constantes;
import com.hotel.room_service.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ConstantesHandler {
    private final ConstantesUseCase constantesUseCase;

    public Mono<ServerResponse> consultarConstantes(ServerRequest request) {
        return constantesUseCase.consultarConstantes()
                .map(constantes -> constantes.stream()
                        .map(ConstantesTransformer::toResponse)
                        .toList()
                )
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Constantes consultadas exitosamente",
                                response,
                                response.size()
                        ))
                );
    }

    public Mono<ServerResponse> insertarConstante(ServerRequest request) {
        return request.bodyToMono(Constantes.class)
                .flatMap(constantesUseCase::insertarConstante)
                .flatMap(id -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Constante creada exitosamente",
                                id,
                                1
                        ))
                );
    }

    public Mono<ServerResponse> actualizarConstante(ServerRequest request) {
        Mono<Short> idMono = Mono.just(Short.parseShort(request.pathVariable("id")));
        return idMono.flatMap(id ->
                request.bodyToMono(Constantes.class)
                        .map(constante -> {
                            constante.setConsId(id);
                            return constante;
                        })
        )
                .flatMap(constantesUseCase::actualizarConstante)
                .flatMap(rows -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Constante actualizada exitosamente",
                                rows,
                                1
                        ))
                );
    }

    public Mono<ServerResponse> eliminarConstante(ServerRequest request) {
        Short consId = Short.parseShort(request.pathVariable("id"));
        return constantesUseCase.eliminarConstante(consId)
                .flatMap(rows -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Constante eliminada exitosamente",
                                rows,
                                1
                        ))
                );
    }
}
