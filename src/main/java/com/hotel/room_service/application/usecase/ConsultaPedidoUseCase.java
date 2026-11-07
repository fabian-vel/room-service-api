package com.hotel.room_service.application.usecase;

import com.hotel.room_service.domain.exception.BusinessException;
import com.hotel.room_service.domain.model.ConsultaPedido;
import com.hotel.room_service.domain.model.ConsultaPedidoFiltro;
import com.hotel.room_service.domain.model.Pagina;
import com.hotel.room_service.domain.port.ConsultaPedidoRepository;
import com.hotel.room_service.shared.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultaPedidoUseCase {
    private final ConsultaPedidoRepository consultaPedidoRepository;
    private final TokenUtil tokenUtil;
    private static final int MAX_SIZE = 100;

    public Mono<List<ConsultaPedido>> consultarPedidoPorUsuario() {
        return tokenUtil.getHaacId()
                .flatMap(consultaPedidoRepository::consultarPedidoPorUsuario);
    }

    public Mono<List<ConsultaPedido>> consultarPedidoFechaActual() {
        return consultaPedidoRepository.consultarPedidoFechaActual();
    }

    public Mono<Pagina<ConsultaPedido>> consultarPedidoFiltro(ConsultaPedidoFiltro filtro) {
        if (filtro.getPage() < 1) {
            return Mono.error(new BusinessException("La página debe ser mayor o igual a 1"));
        }
        if (filtro.getSize() < 1 || filtro.getSize() > MAX_SIZE) {
            return Mono.error(new BusinessException("El tamaño de página debe estar entre 1 y " + MAX_SIZE));
        }
        if (filtro.getFechaDesde() != null && filtro.getFechaHasta() != null
                && filtro.getFechaDesde().isAfter(filtro.getFechaHasta())) {
            return Mono.error(new BusinessException("La fecha inicial no puede ser mayor a la fecha final"));
        }

        return consultaPedidoRepository.consultarPedidoFiltro(filtro);
    }
}
