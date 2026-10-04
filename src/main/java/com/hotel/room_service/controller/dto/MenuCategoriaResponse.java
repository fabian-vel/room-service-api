package com.hotel.room_service.controller.dto;

import java.util.List;

public record MenuCategoriaResponse(
        Short mecaId,
        String mecaLlaveMst,
        String mecaNombre,
        String mecaDescripcion,
        String mecaImagenUrl,
        Short mecaParentId,
        String mecaEstado,
        List<MenuCategoriaResponse> subCategorias
) {
}

