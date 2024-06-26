package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.Controller.RutaController;
import ar.edu.utn.dds.k3003.Controller.TrasladoController;
import ar.edu.utn.dds.k3003.facades.dtos.Constants;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class WebApp {

    public static void main(String[] args) {

        var env = System.getenv();
        var URL_VIANDAS = env.get("URL_VIANDAS");

        var objectMapper = createObjectMapper();
        var fachada = new Fachada();

        fachada.setViandasProxy(new ar.edu.utn.dds.k3003.clients.ViandasProxy(objectMapper));

        var port = Integer.parseInt(env.getOrDefault("PORT", "8080"));

        var app = Javalin.create().start(port);

        var rutaController = new RutaController(fachada);
        var trasladosController = new TrasladoController(fachada);

        app.post("/rutas", rutaController::agregar);
        app.get("/rutas", rutaController::obtenerTodas);
        app.post("/traslados", trasladosController::asignar);
        app.get("/traslados/{id}", trasladosController::obtener);
        app.patch("/traslados/{id}", trasladosController::modificarEstado);
    }

    public static ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        configureObjectMapper(objectMapper);
        return objectMapper;
    }

    public static void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var sdf = new SimpleDateFormat(Constants.DEFAULT_SERIALIZATION_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(sdf);
    }
}