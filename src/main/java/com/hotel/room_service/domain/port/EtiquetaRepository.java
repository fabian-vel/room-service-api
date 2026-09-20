package com.hotel.room_service.domain.port;

import com.hotel.room_service.domain.model.Etiqueta;
import com.hotel.room_service.domain.model.EtiquetaRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EtiquetaRepository {
    Mono<List<Etiqueta>> consultaEtiqueta(EtiquetaRequest request);
    Mono<List<Etiqueta>> consultarEtiquetas();
    Mono<Short> insertarEtiqueta(Etiqueta etiqueta);
    Mono<Long> actualizarEtiqueta(Etiqueta etiqueta);
    Mono<Long> eliminarEtiqueta(Short etiqId);
}
