package com.hotel.room_service.infrastructure.persistence;

import com.hotel.room_service.domain.exception.InternalServerErrorException;
import com.hotel.room_service.domain.model.MenuCategoria;
import com.hotel.room_service.domain.port.MenuCategoriaRepository;
import com.hotel.room_service.infrastructure.mapper.MenuCategoriaRowMapper;
import com.hotel.room_service.infrastructure.model.MenuCategoriaRow;
import com.hotel.room_service.shared.util.AdapterErrorUtil;
import com.hotel.room_service.shared.util.SqlLoaderUtil;
import io.r2dbc.spi.R2dbcException;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuCategoriaRepositoryAdapter implements MenuCategoriaRepository {

    private final DatabaseClient databaseClient;
    private final MenuCategoriaRowMapper menuCategoriaRowMapper;
    private final String sql = SqlLoaderUtil.load("querys/consultar-categoria.sql");
    private final String consultarTodas = SqlLoaderUtil.load("querys/consultar-menu-categorias-todos.sql");
    private final String insertar = SqlLoaderUtil.load("querys/insertar-menu-categoria.sql");
    private final String actualizar = SqlLoaderUtil.load("querys/actualizar-menu-categoria.sql");
    private final String eliminar = SqlLoaderUtil.load("querys/eliminar-menu-categoria.sql");

    @Override
    public Mono<List<MenuCategoriaRow>> consultarCategorias() {
        return databaseClient.sql(sql)
                .map((row, metadata) -> menuCategoriaRowMapper.mapRow(row))
                .all()
                .collectList()
                .onErrorMap(R2dbcException.class, ex ->
                        new InternalServerErrorException("Error al consultar categorías en BD", ex)
                );
    }

    @Override
    public Mono<List<MenuCategoria>> consultarCategoriasTodas() {
        return databaseClient.sql(consultarTodas)
                .map((row, metadata) -> MenuCategoria.builder()
                        .mecaId(row.get("meca_id", Short.class))
                        .mecaLlaveMst(row.get("meca_llave_mst", String.class))
                        .mecaNombre(row.get("meca_nombre", String.class))
                        .mecaDescripcion(row.get("meca_descripcion", String.class))
                        .mecaImagenUrl(row.get("meca_imagen_url", String.class))
                        .mecaParentId(row.get("meca_parent_id", Short.class))
                        .mecaEstado(row.get("meca_estado", String.class))
                        .subCategorias(List.of())
                        .build()
                )
                .all()
                .collectList()
                .onErrorResume(AdapterErrorUtil.mapError("Error al consultar todas las categorías en BD"));
    }

    @Override
    public Mono<Short> insertarCategoria(MenuCategoria categoria) {
        return databaseClient.sql(insertar)
                .bind("llaveMst", categoria.getMecaLlaveMst())
                .bind("nombre", categoria.getMecaNombre())
                .bind("descripcion", categoria.getMecaDescripcion())
                .bind("imagenUrl", categoria.getMecaImagenUrl())
                .bind("parentId", categoria.getMecaParentId())
                .map((row, metadata) -> row.get("meca_id", Short.class))
                .one()
                .onErrorResume(AdapterErrorUtil.mapError("Error al insertar categoría en BD"));
    }

    @Override
    public Mono<Long> actualizarCategoria(MenuCategoria categoria) {
        return databaseClient.sql(actualizar)
                .bind("mecaId", categoria.getMecaId())
                .bind("llaveMst", categoria.getMecaLlaveMst())
                .bind("nombre", categoria.getMecaNombre())
                .bind("descripcion", categoria.getMecaDescripcion())
                .bind("imagenUrl", categoria.getMecaImagenUrl())
                .bind("parentId", categoria.getMecaParentId())
                .fetch()
                .rowsUpdated()
                .onErrorResume(AdapterErrorUtil.mapError("Error al actualizar categoría en BD"));
    }

    @Override
    public Mono<Long> eliminarCategoria(Short mecaId) {
        return databaseClient.sql(eliminar)
                .bind("mecaId", mecaId)
                .fetch()
                .rowsUpdated()
                .onErrorResume(AdapterErrorUtil.mapError("Error al eliminar categoría en BD"));
    }
}
