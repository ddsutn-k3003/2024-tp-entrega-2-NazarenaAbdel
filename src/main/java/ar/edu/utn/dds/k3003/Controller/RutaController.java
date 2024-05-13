package ar.edu.utn.dds.k3003.Controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.model.Ruta;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

public class RutaController {

    private Fachada fachada;
    public RutaController(Fachada fachada) {
        this.fachada = fachada;
    }

    public void agregar(Context context) {
        var rutaDTO = context.bodyAsClass(RutaDTO.class);
        var rutaDTORta = this.fachada.agregar(rutaDTO);
        context.json(rutaDTORta);
        context.status(HttpStatus.CREATED);
    }

    public void obtenerTodas(Context context) {
        var rutas = this.fachada.obtenerTodasLasRutas();
        context.json(rutas);
        context.status(HttpStatus.OK);
    }


}
