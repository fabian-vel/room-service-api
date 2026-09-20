package com.hotel.room_service.application.transformer;

import com.hotel.room_service.controller.dto.EtiquetaResponse;
import com.hotel.room_service.domain.model.Etiqueta;

public class EtiquetaTransformer {
    public static EtiquetaResponse toResponse(Etiqueta domain) {
        return new EtiquetaResponse(
                domain.getEtiqId(),
                domain.getEtiqLlaveMst(),
                domain.getEtiqNombre(),
                domain.getEtiqDescripcion(),
                domain.getEtiqEstado()
        );
    }
}
