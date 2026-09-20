package com.hotel.room_service.controller.dto;

public record ConstantesResponse(
        Short consId,
        String consLlave,
        String consValor,
        String consDescripcion,
        String consEstado
) {
}
