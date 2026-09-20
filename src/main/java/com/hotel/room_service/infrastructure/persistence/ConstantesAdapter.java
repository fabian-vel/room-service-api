package com.hotel.room_service.infrastructure.persistence;

import com.hotel.room_service.domain.model.Constantes;
import com.hotel.room_service.domain.port.ConstantesRepository;
import com.hotel.room_service.infrastructure.mapper.ConstantesRowMapper;
import com.hotel.room_service.shared.util.AdapterErrorUtil;
import com.hotel.room_service.shared.util.SqlLoaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ConstantesAdapter implements ConstantesRepository {
    private final DatabaseClient databaseClient;
    private final ConstantesRowMapper constantesRowMapper;
    private final String sql = SqlLoaderUtil.load("querys/consulta-constantes.sql");
    private final String consultarTodos = SqlLoaderUtil.load("querys/consultar-constantes-todos.sql");
    private final String insertar = SqlLoaderUtil.load("querys/insertar-constante.sql");
    private final String actualizar = SqlLoaderUtil.load("querys/actualizar-constante.sql");
    private final String eliminar = SqlLoaderUtil.load("querys/eliminar-constante.sql");

    @Override
    public Mono<Map<String, String>> consultaConstantes(List<String> llaves) {
        return databaseClient.sql(sql)
                .bind("LLAVES", llaves.toArray(new String[0]))
                .map((row, metadata) -> Map.entry(
                        Objects.requireNonNull(row.get("cons_llave", String.class)),
                        Objects.requireNonNull(row.get("cons_valor", String.class))
                ))
                .all()
                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                .onErrorResume(AdapterErrorUtil.mapError("Error al consultar constantes"));
    }

    @Override
    public Mono<List<Constantes>> consultarConstantes() {
        return databaseClient.sql(consultarTodos)
                .map((row, metadata) -> constantesRowMapper.mapRow(row))
                .all()
                .collectList()
                .onErrorResume(AdapterErrorUtil.mapError("Error al consultar constantes en BD"));
    }

    @Override
    public Mono<Short> insertarConstante(Constantes constante) {
        return databaseClient.sql(insertar)
                .bind("llave", constante.getConsLlave())
                .bind("valor", constante.getConsValor())
                .bind("descripcion", constante.getConsDescripcion())
                .map((row, metadata) -> row.get("cons_id", Short.class))
                .one()
                .onErrorResume(AdapterErrorUtil.mapError("Error al insertar constante en BD"));
    }

    @Override
    public Mono<Long> actualizarConstante(Constantes constante) {
        return databaseClient.sql(actualizar)
                .bind("consId", constante.getConsId())
                .bind("llave", constante.getConsLlave())
                .bind("valor", constante.getConsValor())
                .bind("descripcion", constante.getConsDescripcion())
                .fetch()
                .rowsUpdated()
                .onErrorResume(AdapterErrorUtil.mapError("Error al actualizar constante en BD"));
    }

    @Override
    public Mono<Long> eliminarConstante(Short consId) {
        return databaseClient.sql(eliminar)
                .bind("consId", consId)
                .fetch()
                .rowsUpdated()
                .onErrorResume(AdapterErrorUtil.mapError("Error al eliminar constante en BD"));
    }
}
