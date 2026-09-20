package com.hotel.room_service.infrastructure.mapper;

import com.hotel.room_service.domain.model.Constantes;
import io.r2dbc.spi.Row;
import org.springframework.stereotype.Component;

@Component
public class ConstantesRowMapper {
    public Constantes mapRow(Row row) {
        return Constantes.builder()
                .consId(row.get("cons_id", Short.class))
                .consLlave(row.get("cons_llave", String.class))
                .consValor(row.get("cons_valor", String.class))
                .consDescripcion(row.get("cons_descripcion", String.class))
                .consEstado(row.get("cons_estado", String.class))
                .build();
    }
}
