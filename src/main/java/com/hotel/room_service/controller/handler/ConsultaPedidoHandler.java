package com.hotel.room_service.controller.handler;

import com.hotel.room_service.application.transformer.ConsultaPedidoTransformer;
import com.hotel.room_service.application.usecase.ConsultaPedidoUseCase;
import com.hotel.room_service.controller.dto.PaginaResponse;
import com.hotel.room_service.domain.exception.BusinessException;
import com.hotel.room_service.domain.model.ConsultaPedidoFiltro;
import com.hotel.room_service.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ConsultaPedidoHandler {
    private final ConsultaPedidoUseCase consultaPedidoUseCase;
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 20;

    public Mono<ServerResponse> consultarPedidoPorUsuario() {
        return consultaPedidoUseCase.consultarPedidoPorUsuario()
                .map(pedidos -> pedidos.stream()
                        .map(ConsultaPedidoTransformer::toResponse)
                        .toList()
                )
                .flatMap(pedidos -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Pedidos consultados exitosamente",
                                pedidos,
                                pedidos.size()
                        ))
                );
    }

    public Mono<ServerResponse> consultarPedidoFechaActual() {
        return consultaPedidoUseCase.consultarPedidoFechaActual()
                .map(pedidos -> pedidos.stream()
                        .map(ConsultaPedidoTransformer::toResponse)
                        .toList()
                )
                .flatMap(pedidos -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Pedidos del dia consultados exitosamente",
                                pedidos,
                                pedidos.size()
                        ))
                );
    }

    public Mono<ServerResponse> consultarPedidoFiltro(ServerRequest request) {
        return Mono.fromCallable(() -> toFiltro(request))
                .flatMap(consultaPedidoUseCase::consultarPedidoFiltro)
                .map(pagina -> new PaginaResponse<>(
                        pagina.getItems().stream()
                                .map(ConsultaPedidoTransformer::toResponse)
                                .toList(),
                        pagina.getTotal(),
                        pagina.getPage(),
                        pagina.getSize()
                ))
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.ok(
                                "Pedidos consultados exitosamente",
                                response,
                                response.items().size()
                        ))
                );
    }

    private ConsultaPedidoFiltro toFiltro(ServerRequest request) {
        return ConsultaPedidoFiltro.builder()
                .fechaDesde(param(request, "fechaDesde").map(v -> parse(v, "fechaDesde", LocalDate::parse)).orElse(null))
                .fechaHasta(param(request, "fechaHasta").map(v -> parse(v, "fechaHasta", LocalDate::parse)).orElse(null))
                .estadoPedido(param(request, "estadoPedido").map(v -> parse(v, "estadoPedido", Short::parseShort)).orElse(null))
                .habitacion(param(request, "habitacion").orElse(null))
                .producto(param(request, "producto").orElse(null))
                .page(param(request, "page").map(v -> parse(v, "page", Integer::parseInt)).orElse(DEFAULT_PAGE))
                .size(param(request, "size").map(v -> parse(v, "size", Integer::parseInt)).orElse(DEFAULT_SIZE))
                .build();
    }

    private Optional<String> param(ServerRequest request, String nombre) {
        return request.queryParam(nombre)
                .map(String::trim)
                .filter(v -> !v.isEmpty());
    }

    private <T> T parse(String valor, String nombre, Function<String, T> parser) {
        try {
            return parser.apply(valor);
        } catch (RuntimeException e) {
            throw new BusinessException("El parámetro " + nombre + " no es válido");
        }
    }
}
