package ar.edi.itn.dds.k3003.model;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.Ruta;
import ar.edu.utn.dds.k3003.tests.TestTP;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class testsEntrega1 {

    private String QR_VIANDA = "123";
    private Integer HELADERA_ORIGEN = 1;
    private Integer HELADERA_DESTINO = 2;

    Fachada instanciaLogistica;

    @Mock
    FachadaViandas fachadaViandas;
    @Mock
    FachadaHeladeras fachadaHeladeras;

    public testsEntrega1() {

    }

    @BeforeEach
    void setUp() {
        this.instanciaLogistica = new Fachada();
        this.instanciaLogistica.setHeladerasProxy(this.fachadaHeladeras);
        this.instanciaLogistica.setViandasProxy(this.fachadaViandas);
    }

    @Test
    void  testAgregarRuta() {

        RutaDTO rutaDto = new RutaDTO(14L, 1,2);
        rutaDto.setId(1L);
        this.instanciaLogistica.agregar(rutaDto);
        var result =  this.instanciaLogistica.agregar(rutaDto);

        assertEquals(rutaDto, result);
    }

    @Test
    void  testAsignarTraslado() throws TrasladoNoAsignableException {
        RutaDTO rutaDto = new RutaDTO(14L, 1,2);
        rutaDto.setId(1L);
        this.instanciaLogistica.agregar(rutaDto);

        ViandaDTO viandaDto = new ViandaDTO("123", LocalDateTime.now(), EstadoViandaEnum.PREPARADA, 15L, 1);

        TrasladoDTO trasladoDto = new TrasladoDTO(viandaDto.getCodigoQR(), 1, 2);

        TrasladoDTO result = this.instanciaLogistica.asignarTraslado(trasladoDto);


        assertEquals(EstadoTrasladoEnum.ASIGNADO, result.getStatus());
        assertEquals(14L, result.getColaboradorId());

    }
}

