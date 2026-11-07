package com.hotel.room_service.controller.dto;

import java.util.List;

public record PaginaResponse<T>(
        List<T> items,
        long total,
        int page,
        int pageSize
) {
}
