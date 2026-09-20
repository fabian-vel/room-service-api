package com.hotel.room_service.domain.port;

import com.hotel.room_service.domain.model.Constantes;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface ConstantesRepository {
    Mono<Map<String, String>> consultaConstantes(List<String> llaves);
    Mono<List<Constantes>> consultarConstantes();
    Mono<Short> insertarConstante(Constantes constante);
    Mono<Long> actualizarConstante(Constantes constante);
    Mono<Long> eliminarConstante(Short consId);
}
