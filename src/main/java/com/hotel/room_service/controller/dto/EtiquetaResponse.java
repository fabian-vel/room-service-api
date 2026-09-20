package com.hotel.room_service.controller.dto;

public record EtiquetaResponse(
        Short etiqId,
        String etiqLlaveMst,
        String etiqNombre,
        String etiqDescripcion,
        String etiqEstado
) {
}
