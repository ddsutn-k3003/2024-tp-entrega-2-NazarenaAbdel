package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.FachadaColaboradores;
import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.webmocks.ViandaTestServer;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

public class ViandasMain{
    private static FachadaViandas fachadaViandas;
    private static FachadaHeladeras fachadaHeladeras;
    private static FachadaColaboradores fachadaColaboradores;
    private static FachadaLogistica fachadaLogistica;
    public static void main(String[] args) throws Exception {

        ViandaTestServer.main(new String[]{});


    }
}
