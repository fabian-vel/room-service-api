package com.hotel.room_service.controller.handler;

import com.hotel.room_service.application.usecase.EtiquetaUseCase;
import com.hotel.room_service.application.transformer.EtiquetaTransformer;
import com.hotel.room_service.domain.model.Etiqueta;
import com.hotel.room_service.domain.model.EtiquetaRequest;
import com.hotel.room_service.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EtiquetaHandler {
    private final EtiquetaUseCase etiquetaUseCase;

    public Mono<ServerResponse> consultaEtiqueta(ServerRequest request) {
        return request.bodyToMono(EtiquetaRequest.class)
                .flatMap(etiquetaUseCase::consultaEtiqueta)
                .map(etiqueta -> etiqueta.stream()
                        .map(EtiquetaTransformer::toResponse)
                        .toList()
                )
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Etiquetas consultadas exitosamente",
                                response,
                                response.size()
                        ))
                );
    }

    public Mono<ServerResponse> consultarEtiquetas(ServerRequest request) {
        return etiquetaUseCase.consultarEtiquetas()
                .map(etiquetas -> etiquetas.stream()
                        .map(EtiquetaTransformer::toResponse)
                        .toList()
                )
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Etiquetas consultadas exitosamente",
                                response,
                                response.size()
                        ))
                );
    }

    public Mono<ServerResponse> insertarEtiqueta(ServerRequest request) {
        return request.bodyToMono(Etiqueta.class)
                .flatMap(etiquetaUseCase::insertarEtiqueta)
                .flatMap(id -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Etiqueta creada exitosamente",
                                id,
                                1
                        ))
                );
    }

    public Mono<ServerResponse> actualizarEtiqueta(ServerRequest request) {
        Mono<Short> idMono = Mono.just(Short.parseShort(request.pathVariable("id")));
        return idMono.flatMap(id ->
                request.bodyToMono(Etiqueta.class)
                        .map(etiqueta -> {
                            etiqueta.setEtiqId(id);
                            return etiqueta;
                        })
        )
                .flatMap(etiquetaUseCase::actualizarEtiqueta)
                .flatMap(rows -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Etiqueta actualizada exitosamente",
                                rows,
                                1
                        ))
                );
    }

    public Mono<ServerResponse> eliminarEtiqueta(ServerRequest request) {
        Short etiqId = Short.parseShort(request.pathVariable("id"));
        return etiquetaUseCase.eliminarEtiqueta(etiqId)
                .flatMap(rows -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Etiqueta eliminada exitosamente",
                                rows,
                                1
                        ))
                );
    }
}
