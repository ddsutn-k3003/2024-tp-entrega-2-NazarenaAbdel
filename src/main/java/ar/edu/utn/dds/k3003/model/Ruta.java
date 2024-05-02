package ar.edu.utn.dds.k3003.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ruta {

    private Long id;
    private Long colaboradorId;
    private Integer heladeraIdOrigen;
    private Integer heladeraIdDestino;


    public Ruta(Long colaboradorId, Integer heladeraIdOrigen, Integer heladeraIdDestino){
        //el constructor

        this.colaboradorId = colaboradorId;
        this.heladeraIdOrigen = heladeraIdOrigen;
        this.heladeraIdDestino = heladeraIdDestino;
    }


}
