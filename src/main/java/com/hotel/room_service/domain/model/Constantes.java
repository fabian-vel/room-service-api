package com.hotel.room_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Constantes {
    private Short consId;
    private String consLlave;
    private String consValor;
    private String consDescripcion;
    private String consEstado;
}
