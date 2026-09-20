package com.hotel.room_service.application.transformer;

import com.hotel.room_service.controller.dto.MenuCategoriaResponse;
import com.hotel.room_service.domain.model.MenuCategoria;

public class MenuCategoriaTransformer {
    public static MenuCategoriaResponse toResponse(MenuCategoria domain) {
        return new MenuCategoriaResponse(
                domain.getMecaId(),
                domain.getMecaNombre(),
                domain.getMecaDescripcion(),
                domain.getMecaImagenUrl(),
                domain.getMecaParentId(),
                domain.getMecaEstado(),
                domain.getSubCategorias().stream()
                        .map(MenuCategoriaTransformer::toResponse)
                        .toList()
        );
    }
}
