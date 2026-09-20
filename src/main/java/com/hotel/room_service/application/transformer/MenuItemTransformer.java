package com.hotel.room_service.application.transformer;

import com.hotel.room_service.controller.dto.EtiquetaResponse;
import com.hotel.room_service.controller.dto.MenuItemResponse;
import com.hotel.room_service.domain.model.MenuItem;

import java.util.List;

public class MenuItemTransformer {
    public static MenuItemResponse toResponse(MenuItem domain) {
        return new MenuItemResponse(
                domain.getMeitId(),
                domain.getMeitNombre(),
                domain.getMeitDescripcion(),
                domain.getMeitPrecio(),
                domain.getMeitImagenUrl(),
                domain.getMecaId(),
                domain.getMecaParentId(),
                domain.getEtiquetas() != null
                        ? domain.getEtiquetas().stream()
                        .map(e -> new EtiquetaResponse(
                                e.getEtiqId(),
                                e.getEtiqLlaveMst(),
                                e.getEtiqNombre(),
                                e.getEtiqDescripcion(),
                                null))
                        .toList()
                        : List.of()
        );
    }
}
