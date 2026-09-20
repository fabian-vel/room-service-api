package com.hotel.room_service.infrastructure.mapper;

import com.hotel.room_service.domain.model.Etiqueta;
import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EtiquetaRowMapper {
    public Etiqueta mapRow(Row row) {
        return Etiqueta.builder()
                .etiqId(row.get("etiq_id", Short.class))
                .etiqLlaveMst(row.get("etic_llave_mst", String.class))
                .etiqNombre(row.get("etiq_nombre", String.class))
                .etiqDescripcion(row.get("etiq_descripcion", String.class))
                .etiqEstado(row.get("etiq_estado", String.class))
                .build();
    }
}
