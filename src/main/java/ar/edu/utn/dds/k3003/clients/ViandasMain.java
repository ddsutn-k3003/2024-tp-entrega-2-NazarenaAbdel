package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

public class ViandasMain {
     public static void main(String[] args) {

         //System.out.println(LocalDateTime.now());

         ViandasProxy instanciaViandasApp = ViandasProxy.getInstancia();

         System.out.println("agregar una vianda");

         ViandaDTO vianda = new ViandaDTO("1234jfowj33", LocalDateTime.of(2024, 5, 15, 12, 0), EstadoViandaEnum.DEPOSITADA, 12L, 2);

         instanciaViandasApp.agregar(vianda);



    }
}
