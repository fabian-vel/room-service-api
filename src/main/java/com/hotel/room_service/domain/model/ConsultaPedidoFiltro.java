package com.hotel.room_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaPedidoFiltro {
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Short estadoPedido;
    private String habitacion;
    private String producto;
    private int page;
    private int size;
}
