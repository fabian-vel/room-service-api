package com.hotel.room_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagina<T> {
    private List<T> items;
    private long total;
    private int page;
    private int size;
}
