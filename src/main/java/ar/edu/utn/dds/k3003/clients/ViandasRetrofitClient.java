package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import kotlin.PublishedApi;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ViandasRetrofitClient {

    @GET("viandas/{qr}")
    Call<ViandaDTO> get(@Path("qr") String qr);

    @GET("/viandas/search/findByColaboradorIdAndAnioAndMes")
    Call<List<ViandaDTO>> buscarViandasColaborador(@Path("colabId") Long colabId, @Path("mes") Integer mes, @Path("anio") Integer anio);

    @GET("/viandas/{qr}/vencida")
    Call<Boolean> viandaVencida(@Path("qr") String qr);

    @POST("/viandas/")
    Call<ViandaDTO> agregarVianda(@Body ViandaDTO viandaDTO);

    @PATCH("viandas/{qrVianda}")
    Call<ViandaDTO> modificarEstadoVianda(@Path("qrVianda") String qrVianda, @Path("nuevoEstado") EstadoViandaEnum nuevoEstado);

    @PATCH("viandas/{qrVianda}")
    Call<ViandaDTO> modificarHeladeraVianda(@Path("qrVianda") String qrVianda, @Path("idHeladera") Integer idHeladera);
}