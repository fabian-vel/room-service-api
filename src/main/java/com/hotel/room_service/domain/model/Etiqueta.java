package com.hotel.room_service.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Etiqueta {
    @JsonProperty("etiqId")
    private Short etiqId;
    @JsonProperty("etiqLlaveMst")
    private String etiqLlaveMst;
    @JsonProperty("etiqNombre")
    private String etiqNombre;
    @JsonProperty("etiqDescripcion")
    private String etiqDescripcion;
    @JsonProperty("etiqEstado")
    private String etiqEstado;
}
