package com.hotel.room_service.application.usecase;

import com.hotel.room_service.domain.exception.BusinessException;
import com.hotel.room_service.domain.model.Etiqueta;
import com.hotel.room_service.domain.model.EtiquetaRequest;
import com.hotel.room_service.domain.port.EtiquetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EtiquetaUseCase {
    private final EtiquetaRepository etiquetaRepository;

    public Mono<List<Etiqueta>> consultaEtiqueta(EtiquetaRequest request) {
        if (request.isConsultaPorCategoria() && request.getMecaId() == null) {
            return Mono.error(new BusinessException("El id de la categoría es obligatorio"));
        } else if (!request.isConsultaPorCategoria() && request.getMeitId() == null) {
            return Mono.error(new BusinessException("El id del item del menú es obligatorio"));
        }

        return etiquetaRepository.consultaEtiqueta(request);
    }

    public Mono<List<Etiqueta>> consultarEtiquetas() {
        return etiquetaRepository.consultarEtiquetas();
    }

    public Mono<Short> insertarEtiqueta(Etiqueta etiqueta) {
        if (etiqueta.getEtiqLlaveMst() == null || etiqueta.getEtiqLlaveMst().isBlank()) {
            return Mono.error(new BusinessException("La llave de la etiqueta es obligatoria"));
        }
        if (etiqueta.getEtiqNombre() == null || etiqueta.getEtiqNombre().isBlank()) {
            return Mono.error(new BusinessException("El nombre de la etiqueta es obligatorio"));
        }
        return etiquetaRepository.insertarEtiqueta(etiqueta);
    }

    public Mono<Long> actualizarEtiqueta(Etiqueta etiqueta) {
        if (etiqueta.getEtiqId() == null) {
            return Mono.error(new BusinessException("El id de la etiqueta es obligatorio"));
        }
        if (etiqueta.getEtiqLlaveMst() == null || etiqueta.getEtiqLlaveMst().isBlank()) {
            return Mono.error(new BusinessException("La llave de la etiqueta es obligatoria"));
        }
        if (etiqueta.getEtiqNombre() == null || etiqueta.getEtiqNombre().isBlank()) {
            return Mono.error(new BusinessException("El nombre de la etiqueta es obligatorio"));
        }
        return etiquetaRepository.actualizarEtiqueta(etiqueta)
                .flatMap(rows -> {
                    if (rows == 0) return Mono.error(new BusinessException("Etiqueta no encontrada"));
                    return Mono.just(rows);
                });
    }

    public Mono<Long> eliminarEtiqueta(Short etiqId) {
        if (etiqId == null) {
            return Mono.error(new BusinessException("El id de la etiqueta es obligatorio"));
        }
        return etiquetaRepository.eliminarEtiqueta(etiqId)
                .flatMap(rows -> {
                    if (rows == 0) return Mono.error(new BusinessException("Etiqueta no encontrada"));
                    return Mono.just(rows);
                });
    }
}
