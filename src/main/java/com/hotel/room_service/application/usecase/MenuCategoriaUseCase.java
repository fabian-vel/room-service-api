package com.hotel.room_service.application.usecase;

import com.hotel.room_service.domain.exception.BusinessException;
import com.hotel.room_service.domain.model.MenuCategoria;
import com.hotel.room_service.domain.port.MenuCategoriaRepository;
import com.hotel.room_service.infrastructure.model.MenuCategoriaRow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuCategoriaUseCase {

    private final MenuCategoriaRepository menuCategoriaRepository;

    public Mono<List<MenuCategoria>> consultarCategorias() {
        return menuCategoriaRepository.consultarCategorias()
                .map(this::agruparCategorias);
    }

    public Mono<List<MenuCategoria>> consultarCategoriasTodas() {
        return menuCategoriaRepository.consultarCategoriasTodas();
    }

    public Mono<Short> insertarCategoria(MenuCategoria categoria) {
        if (categoria.getMecaLlaveMst() == null || categoria.getMecaLlaveMst().isBlank()) {
            return Mono.error(new BusinessException("La llave de la categoría es obligatoria"));
        }
        if (categoria.getMecaNombre() == null || categoria.getMecaNombre().isBlank()) {
            return Mono.error(new BusinessException("El nombre de la categoría es obligatorio"));
        }
        return menuCategoriaRepository.insertarCategoria(categoria);
    }

    public Mono<Long> actualizarCategoria(MenuCategoria categoria) {
        if (categoria.getMecaId() == null) {
            return Mono.error(new BusinessException("El id de la categoría es obligatorio"));
        }
        if (categoria.getMecaLlaveMst() == null || categoria.getMecaLlaveMst().isBlank()) {
            return Mono.error(new BusinessException("La llave de la categoría es obligatoria"));
        }
        if (categoria.getMecaNombre() == null || categoria.getMecaNombre().isBlank()) {
            return Mono.error(new BusinessException("El nombre de la categoría es obligatorio"));
        }
        return menuCategoriaRepository.actualizarCategoria(categoria)
                .flatMap(rows -> {
                    if (rows == 0) return Mono.error(new BusinessException("Categoría no encontrada"));
                    return Mono.just(rows);
                });
    }

    public Mono<Long> eliminarCategoria(Short mecaId) {
        if (mecaId == null) {
            return Mono.error(new BusinessException("El id de la categoría es obligatorio"));
        }
        return menuCategoriaRepository.eliminarCategoria(mecaId)
                .flatMap(rows -> {
                    if (rows == 0) return Mono.error(new BusinessException("Categoría no encontrada"));
                    return Mono.just(rows);
                });
    }

    private List<MenuCategoria> agruparCategorias(List<MenuCategoriaRow> rows) {

        Map<Short, MenuCategoria> padresMap = new LinkedHashMap<>();

        for (MenuCategoriaRow row : rows) {

            MenuCategoria padre =
                    padresMap.computeIfAbsent(
                            row.getPadreId(),
                            id -> MenuCategoria.builder()
                                    .mecaId(id)
                                    .mecaNombre(row.getPadreNombre())
                                    .mecaDescripcion(row.getPadreDescripcion())
                                    .mecaImagenUrl(row.getPadreImagenUrl())
                                    .mecaParentId(null)
                                    .subCategorias(new LinkedList<>())
                                    .build()
                    );

            if (row.getHijoId() != null) {

                padre.getSubCategorias().add(
                        MenuCategoria.builder()
                                .mecaId(row.getHijoId())
                                .mecaNombre(row.getHijoNombre())
                                .mecaDescripcion(row.getHijoDescripcion())
                                .mecaImagenUrl(row.getHijoImagenUrl())
                                .mecaParentId(row.getPadreId())
                                .subCategorias(new LinkedList<>())
                                .build()
                );
            }
        }

        return new ArrayList<>(padresMap.values());
    }
}
