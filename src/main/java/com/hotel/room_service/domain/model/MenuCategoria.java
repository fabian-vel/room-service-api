package com.hotel.room_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuCategoria {

    private Short  mecaId;
    private String mecaLlaveMst;
    private String mecaNombre;
    private String mecaDescripcion;
    private String mecaImagenUrl;
    private Short  mecaParentId;
    private String mecaEstado;
    private List<MenuCategoria> subCategorias;
}

