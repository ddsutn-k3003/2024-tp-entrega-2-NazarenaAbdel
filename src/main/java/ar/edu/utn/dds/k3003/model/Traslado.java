package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Traslado {
    private Long id;
    private String qrVianda;
    private EstadoTrasladoEnum estado;
    private LocalDateTime fechaTraslado;
    private Ruta ruta;


    public Traslado(String qr, Ruta ruta, EstadoTrasladoEnum estado, LocalDateTime fecha){
            this.qrVianda = qr;
            this.estado = estado;
            this.fechaTraslado = fecha;
    }


}
