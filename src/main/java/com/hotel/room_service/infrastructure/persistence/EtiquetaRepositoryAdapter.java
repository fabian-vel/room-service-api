package com.hotel.room_service.infrastructure.persistence;

import com.hotel.room_service.domain.model.Etiqueta;
import com.hotel.room_service.domain.model.EtiquetaRequest;
import com.hotel.room_service.domain.port.EtiquetaRepository;
import com.hotel.room_service.infrastructure.mapper.EtiquetaRowMapper;
import com.hotel.room_service.shared.util.AdapterErrorUtil;
import com.hotel.room_service.shared.util.SqlLoaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EtiquetaRepositoryAdapter implements EtiquetaRepository {
    private final DatabaseClient databaseClient;
    private final EtiquetaRowMapper etiquetaRowMapper;
    private final String consultaPorItem = SqlLoaderUtil.load("querys/consulta-etiqueta-x-item.sql");
    private final String consultaPorCategoria = SqlLoaderUtil.load("querys/consulta-etiqueta-x-categoria.sql");
    private final String consultarTodos = SqlLoaderUtil.load("querys/consultar-etiquetas-todos.sql");
    private final String insertar = SqlLoaderUtil.load("querys/insertar-etiqueta.sql");
    private final String actualizar = SqlLoaderUtil.load("querys/actualizar-etiqueta.sql");
    private final String eliminar = SqlLoaderUtil.load("querys/eliminar-etiqueta.sql");

    @Override
    public Mono<List<Etiqueta>> consultaEtiqueta(EtiquetaRequest request) {
        String sql;
        String paramNombre;
        Short paramValor;

        if (request.isConsultaPorCategoria()) {
            sql = consultaPorCategoria;
            paramNombre = "MECA_ID";
            paramValor = request.getMecaId();
        } else {
            sql = consultaPorItem;
            paramNombre = "MEIT_ID";
            paramValor =  request.getMeitId();
        }

        return databaseClient.sql(sql)
                .bind(paramNombre, paramValor)
                .map((row, metadata) -> etiquetaRowMapper.mapRow(row))
                .all()
                .collectList()
                .onErrorResume(AdapterErrorUtil.mapError("Error al consultar etiquetas"));

    }

    @Override
    public Mono<List<Etiqueta>> consultarEtiquetas() {
        return databaseClient.sql(consultarTodos)
                .map((row, metadata) -> etiquetaRowMapper.mapRow(row))
                .all()
                .collectList()
                .onErrorResume(AdapterErrorUtil.mapError("Error al consultar etiquetas en BD"));
    }

    @Override
    public Mono<Short> insertarEtiqueta(Etiqueta etiqueta) {
        return databaseClient.sql(insertar)
                .bind("llaveMst", etiqueta.getEtiqLlaveMst())
                .bind("nombre", etiqueta.getEtiqNombre())
                .bind("descripcion", etiqueta.getEtiqDescripcion())
                .map((row, metadata) -> row.get("etiq_id", Short.class))
                .one()
                .onErrorResume(AdapterErrorUtil.mapError("Error al insertar etiqueta en BD"));
    }

    @Override
    public Mono<Long> actualizarEtiqueta(Etiqueta etiqueta) {
        return databaseClient.sql(actualizar)
                .bind("etiqId", etiqueta.getEtiqId())
                .bind("llaveMst", etiqueta.getEtiqLlaveMst())
                .bind("nombre", etiqueta.getEtiqNombre())
                .bind("descripcion", etiqueta.getEtiqDescripcion())
                .fetch()
                .rowsUpdated()
                .onErrorResume(AdapterErrorUtil.mapError("Error al actualizar etiqueta en BD"));
    }

    @Override
    public Mono<Long> eliminarEtiqueta(Short etiqId) {
        return databaseClient.sql(eliminar)
                .bind("etiqId", etiqId)
                .fetch()
                .rowsUpdated()
                .onErrorResume(AdapterErrorUtil.mapError("Error al eliminar etiqueta en BD"));
    }
}
