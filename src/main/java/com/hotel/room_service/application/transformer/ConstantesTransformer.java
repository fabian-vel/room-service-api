package com.hotel.room_service.application.transformer;

import com.hotel.room_service.controller.dto.ConstantesResponse;
import com.hotel.room_service.domain.model.Constantes;

public class ConstantesTransformer {
    private ConstantesTransformer() {}

    public static ConstantesResponse toResponse(Constantes domain) {
        return new ConstantesResponse(
                domain.getConsId(),
                domain.getConsLlave(),
                domain.getConsValor(),
                domain.getConsDescripcion(),
                domain.getConsEstado()
        );
    }
}
