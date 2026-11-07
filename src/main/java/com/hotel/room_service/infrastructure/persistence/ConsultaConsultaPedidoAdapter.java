package com.hotel.room_service.infrastructure.persistence;

import com.hotel.room_service.domain.model.ConsultaPedido;
import com.hotel.room_service.domain.model.ConsultaPedidoFiltro;
import com.hotel.room_service.domain.model.Pagina;
import com.hotel.room_service.domain.port.ConsultaPedidoRepository;
import com.hotel.room_service.infrastructure.mapper.ConsultaPedidoRowMapper;
import com.hotel.room_service.shared.util.AdapterErrorUtil;
import com.hotel.room_service.shared.util.SqlLoaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ConsultaConsultaPedidoAdapter implements ConsultaPedidoRepository {
    private final DatabaseClient databaseClient;
    private final ConsultaPedidoRowMapper consultaPedidoRowMapper;
    private final String sql = SqlLoaderUtil.load("querys/consulta_pedidos.sql");
    private final String sqlFechaActual = SqlLoaderUtil.load("querys/consulta-pedidos-actual.sql");
    private final String sqlFiltro = SqlLoaderUtil.load("querys/consulta-pedidos-filtro.sql");
    private static final String MARCADOR_FILTROS = "/*FILTROS*/";

    @Override
    public Mono<List<ConsultaPedido>> consultarPedidoPorUsuario(Short haacId) {
        return databaseClient.sql(sql)
                .bind("haacId", haacId)
                .map((row, metadata) -> consultaPedidoRowMapper.mapRow(row))
                .all()
                .collectList()
                .onErrorResume(AdapterErrorUtil.mapError("Error al consultar pedidos en BD"));
    }

    @Override
    public Mono<List<ConsultaPedido>> consultarPedidoFechaActual() {
        return databaseClient.sql(sqlFechaActual)
                .map((row, metadata) -> consultaPedidoRowMapper.mapRow(row))
                .all()
                .collectList()
                .onErrorResume(AdapterErrorUtil.mapError("Error al consultar pedidos por fecha actual en BD"));
    }

    @Override
    public Mono<Pagina<ConsultaPedido>> consultarPedidoFiltro(ConsultaPedidoFiltro filtro) {
        // Solo se agregan al WHERE las condiciones de los filtros que llegan con valor
        List<String> condiciones = new ArrayList<>();
        Map<String, Object> parametros = new LinkedHashMap<>();

        if (filtro.getFechaDesde() != null) {
            condiciones.add("P.pedi_fecha_creacion >= CAST(:fechaDesde AS date)");
            parametros.put("fechaDesde", filtro.getFechaDesde());
        }
        if (filtro.getFechaHasta() != null) {
            // menor al día siguiente para incluir todo el día de fechaHasta
            condiciones.add("P.pedi_fecha_creacion < CAST(:fechaHasta AS date)");
            parametros.put("fechaHasta", filtro.getFechaHasta().plusDays(1));
        }
        if (filtro.getEstadoPedido() != null) {
            condiciones.add("P.pedi_estado_pedido = :estadoPedido");
            parametros.put("estadoPedido", filtro.getEstadoPedido());
        }
        if (filtro.getHabitacion() != null) {
            condiciones.add("P.pedi_habitacion = :habitacion");
            parametros.put("habitacion", filtro.getHabitacion());
        }
        if (filtro.getProducto() != null) {
            condiciones.add("EXISTS (SELECT 1"
                    + " FROM tbl_pedido_detalle PDF"
                    + " INNER JOIN tbl_menu_items MIF ON MIF.meit_id = PDF.meit_id"
                    + " WHERE PDF.pedi_id = P.pedi_id"
                    + " AND PDF.pede_estado = 'A'"
                    + " AND PDF.pede_fecha_eliminacion IS NULL"
                    + " AND MIF.meit_nombre ILIKE :producto)");
            parametros.put("producto", "%" + filtro.getProducto() + "%");
        }

        StringBuilder filtros = new StringBuilder();
        condiciones.forEach(condicion -> filtros.append("  AND ").append(condicion).append("\n"));

        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(sqlFiltro.replace(MARCADOR_FILTROS, filtros))
                .bind("limite", filtro.getSize())
                .bind("desplazamiento", (filtro.getPage() - 1) * filtro.getSize());
        for (Map.Entry<String, Object> parametro : parametros.entrySet()) {
            spec = spec.bind(parametro.getKey(), parametro.getValue());
        }

        return spec
                .map((row, metadata) -> Map.entry(
                        consultaPedidoRowMapper.mapRow(row),
                        row.get("total_registros", Long.class)
                ))
                .all()
                .collectList()
                .map(filas -> Pagina.<ConsultaPedido>builder()
                        .items(filas.stream().map(Map.Entry::getKey).toList())
                        .total(filas.isEmpty() ? 0 : filas.get(0).getValue())
                        .page(filtro.getPage())
                        .size(filtro.getSize())
                        .build()
                )
                .onErrorResume(AdapterErrorUtil.mapError("Error al consultar pedidos con filtros en BD"));
    }
}
