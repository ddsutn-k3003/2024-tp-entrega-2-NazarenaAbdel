package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.Ruta;
import ar.edu.utn.dds.k3003.model.Traslado;
import ar.edu.utn.dds.k3003.repositories.RutaMapper;
import ar.edu.utn.dds.k3003.repositories.RutaRepository;
import ar.edu.utn.dds.k3003.repositories.TrasladoMapper;
import ar.edu.utn.dds.k3003.repositories.TrasladoRepository;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Fachada implements ar.edu.utn.dds.k3003.facades.FachadaLogistica{


    private final RutaRepository rutaRepository;
    private final RutaMapper rutaMapper;
    private final TrasladoRepository trasladoRepository;
    private final TrasladoMapper trasladoMapper;
    private FachadaViandas fachadaViandas;
    private FachadaHeladeras fachadaHeladeras;

    public Fachada() {
        this.rutaRepository = new RutaRepository();
        this.rutaMapper = new RutaMapper();
        this.trasladoMapper = new TrasladoMapper();
        this.trasladoRepository = new TrasladoRepository();
    }


    /*
    *  ViandaDTO t =
        new ViandaDTO(
            QR_VIANDA,
            LocalDateTime.now(),
            EstadoViandaEnum.PREPARADA,
            15L,
            LogisticaTest.HELADERA_ORIGEN);
    when(fachadaViandas.buscarXQR(QR_VIANDA)).thenReturn(t);
    var agregar = instancia.agregar(new RutaDTO(14L, LogisticaTest.HELADERA_ORIGEN, 2));
    instancia.agregar(new RutaDTO(15L, LogisticaTest.HELADERA_ORIGEN, 3));
    assertNotNull(agregar.getId(), "la ruta una vez agregada deberia tener un identificador");

    var traslado = new TrasladoDTO(QR_VIANDA, LogisticaTest.HELADERA_ORIGEN, 2);
    var trasladoDTO = instancia.asignarTraslado(traslado);

    assertEquals(
        EstadoTrasladoEnum.ASIGNADO,
        trasladoDTO.getStatus(),
        "el estado de un traslado debe figurar como asignado luego de una asignación");
    assertEquals(14L, trasladoDTO.getColaboradorId(), "No se asigno el colaborador correcto");*/

    @Override
    public RutaDTO agregar(RutaDTO rutaDTO) {
        Ruta ruta = new Ruta(rutaDTO.getColaboradorId(), rutaDTO.getHeladeraIdOrigen(), rutaDTO.getHeladeraIdDestino());
        ruta = this.rutaRepository.save(ruta);
        return rutaMapper.map(ruta);
    }


    @Override
    public TrasladoDTO buscarXId(Long aLong) throws NoSuchElementException {
        //como lo tengo que buscar, significa que existe entonces lo busco en el repository
        Traslado traslado = trasladoRepository.findById(aLong);

        TrasladoDTO trasDto = trasladoMapper.map(traslado);

        return trasDto;
    }

    @Override
    public TrasladoDTO asignarTraslado(TrasladoDTO trasladoDTO) throws TrasladoNoAsignableException {

        ViandaDTO viandaDTO = fachadaViandas.buscarXQR(trasladoDTO.getQrVianda());

        List<Ruta> rutasPosibles = this.rutaRepository.findByHeladeras(trasladoDTO.getHeladeraOrigen(),
                trasladoDTO.getHeladeraDestino());

        Collections.shuffle(rutasPosibles);
        if (rutasPosibles.isEmpty()) {
            throw new TrasladoNoAsignableException("no se puede asignar traslado porque no hay una ruta");
        }

            Ruta ruta = rutasPosibles.get(0);
            Traslado traslado = trasladoRepository.save(new Traslado(viandaDTO.getCodigoQR(), ruta,
                    EstadoTrasladoEnum.ASIGNADO, trasladoDTO.getFechaTraslado()));


        return this.trasladoMapper.map(traslado);

    }

    @Override
    public List<TrasladoDTO> trasladosDeColaborador(Long aLong, Integer integer, Integer integer1) {
        //(colaboradorId, mes, anio)
        //me tiene que devolver todos los traslados de un colaborados
        //en mi repository tengo todos los traslados y los traslados tienen un colaborador, entonces tengo que matchearlos
        //el traslado tiene una ruto y la ruta tiene el id
        List<Traslado> traslados = trasladoRepository.findAll();

        List<Traslado> trasladosDeColaboradores = traslados.stream().filter(t -> t.getRuta().getColaboradorId().equals(aLong) &&
                                                                            t.getFechaTraslado().getMonthValue() == integer &&
                                                                            t.getFechaTraslado().getYear() == integer1).collect(Collectors.toList());

        //ahora a esa lista la tengo que convertir en una lista de DTOS

         List<TrasladoDTO> trasDtos = trasladosDeColaboradores.stream().map(t -> trasladoMapper.map(t)).collect(Collectors.toList());

        return trasDtos;
    }

    @Override
    public void setHeladerasProxy(FachadaHeladeras fachadaHeladeras) {
            this.fachadaHeladeras = fachadaHeladeras;
    }

    @Override
    public void setViandasProxy(FachadaViandas fachadaViandas) {
        this.fachadaViandas = fachadaViandas;
    }

    @Override
    public void trasladoRetirado(Long aLong) {
        //primero busco el id del traslado
        TrasladoDTO trasladoDto = buscarXId(aLong);

        //genero el retiroDto - el numero de la tarjeta hay que inventarlo por el momento
        RetiroDTO retiroHeladera = new RetiroDTO(trasladoDto.getQrVianda(), "123", trasladoDto.getHeladeraOrigen());

        fachadaHeladeras.retirar(retiroHeladera);

        //ahora cambio el estado de la vianda
        fachadaViandas.modificarEstado(trasladoDto.getQrVianda(), EstadoViandaEnum.EN_TRASLADO);

        // y genero un nuevo traslado
        Ruta ruta = new Ruta(trasladoDto.getColaboradorId(),trasladoDto.getHeladeraOrigen(),trasladoDto.getHeladeraDestino());
        Traslado traslado = new Traslado(trasladoDto.getQrVianda(), ruta, EstadoTrasladoEnum.EN_VIAJE, trasladoDto.getFechaTraslado());
        trasladoRepository.save(traslado);

    }

    @Override
    public void trasladoDepositado(Long aLong) {
        //es un traslado que ya fue creado en trasladoRetirado y ahora lo tengo que buscar
        TrasladoDTO trasladoDTO = buscarXId(aLong);

        //la deposito en la heladera
        fachadaHeladeras.depositar(trasladoDTO.getHeladeraDestino(), trasladoDTO.getQrVianda());

        //cambio el estado de la vianda
        fachadaViandas.modificarEstado(trasladoDTO.getQrVianda(), EstadoViandaEnum.DEPOSITADA);
        fachadaViandas.modificarHeladera(trasladoDTO.getQrVianda(), trasladoDTO.getHeladeraDestino());

        //cambio el estado del traslado
        Traslado traslado = trasladoRepository.findById(aLong);
        traslado.setEstado(EstadoTrasladoEnum.ENTREGADO);


    }

    public List<Ruta> obtenerTodasLasRutas() {
       return this.rutaRepository.getRutas().stream().toList();
    }
}
