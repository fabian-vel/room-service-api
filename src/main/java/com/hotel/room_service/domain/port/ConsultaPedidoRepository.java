package com.hotel.room_service.domain.port;

import com.hotel.room_service.domain.model.ConsultaPedido;
import com.hotel.room_service.domain.model.ConsultaPedidoFiltro;
import com.hotel.room_service.domain.model.Pagina;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ConsultaPedidoRepository {
    Mono<List<ConsultaPedido>> consultarPedidoPorUsuario(Short haacId);
    Mono<List<ConsultaPedido>> consultarPedidoFechaActual();
    Mono<Pagina<ConsultaPedido>> consultarPedidoFiltro(ConsultaPedidoFiltro filtro);
}
