package com.hotel.room_service.application.usecase;

import com.hotel.room_service.domain.exception.BusinessException;
import com.hotel.room_service.domain.model.Constantes;
import com.hotel.room_service.domain.port.ConstantesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConstantesUseCase {
    private final ConstantesRepository constantesRepository;

    public Mono<List<Constantes>> consultarConstantes() {
        return constantesRepository.consultarConstantes();
    }

    public Mono<Short> insertarConstante(Constantes constante) {
        if (constante.getConsLlave() == null || constante.getConsLlave().isBlank()) {
            return Mono.error(new BusinessException("La llave de la constante es obligatoria"));
        }
        if (constante.getConsValor() == null || constante.getConsValor().isBlank()) {
            return Mono.error(new BusinessException("El valor de la constante es obligatorio"));
        }
        return constantesRepository.insertarConstante(constante);
    }

    public Mono<Long> actualizarConstante(Constantes constante) {
        if (constante.getConsId() == null) {
            return Mono.error(new BusinessException("El id de la constante es obligatorio"));
        }
        if (constante.getConsLlave() == null || constante.getConsLlave().isBlank()) {
            return Mono.error(new BusinessException("La llave de la constante es obligatoria"));
        }
        if (constante.getConsValor() == null || constante.getConsValor().isBlank()) {
            return Mono.error(new BusinessException("El valor de la constante es obligatorio"));
        }
        return constantesRepository.actualizarConstante(constante)
                .flatMap(rows -> {
                    if (rows == 0) return Mono.error(new BusinessException("Constante no encontrada"));
                    return Mono.just(rows);
                });
    }

    public Mono<Long> eliminarConstante(Short consId) {
        if (consId == null) {
            return Mono.error(new BusinessException("El id de la constante es obligatorio"));
        }
        return constantesRepository.eliminarConstante(consId)
                .flatMap(rows -> {
                    if (rows == 0) return Mono.error(new BusinessException("Constante no encontrada"));
                    return Mono.just(rows);
                });
    }
}
