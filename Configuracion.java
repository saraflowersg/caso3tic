import controladores.ControladorProcesadores;
import controladores.ControladorAlmacenamiento;
import controladores.ControladorMemoria;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Configuracion {
  
    public static int valorBase;
    public static int cantUsuarios;
    public static int cantGestores;
    public static int procesadores;
    public static Float capacidadMemoria;
    public static int tamPagina; 
    public static Float capacidadAlmacenamiento;
    public static int tamBloque; 
    public static int tam1; //capacidadBuzon
    public static int tam2; // capacidad zona espera

    public static void main(String[] args) throws InterruptedException {

        System.out.println("--- Sistema Gestor de Recursos para MVs --");

        if (args.length != 1) {
            System.out.println("Uso: java Configuracion <archivo de parametros>");
            return;
        }

        Properties config = new Properties();
        try (FileReader reader = new FileReader(args[0])) {
            config.load(reader);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo " + args[0] + ": " + e.getMessage());
            return;
        }

        valorBase = Integer.parseInt(config.getProperty("valorBase").trim());
        cantUsuarios = Integer.parseInt(config.getProperty("cantUsuarios").trim());
        cantGestores = Integer.parseInt(config.getProperty("cantGestores").trim());
        procesadores = Integer.parseInt(config.getProperty("procesadores").trim());
        capacidadMemoria = Float.parseFloat(config.getProperty("capacidadMemoria").trim());
        tamPagina = Integer.parseInt(config.getProperty("tamPagina").trim());
        capacidadAlmacenamiento = Float.parseFloat(config.getProperty("capacidadAlmacenamiento").trim());
        tamBloque = Integer.parseInt(config.getProperty("tamBloque").trim());
        tam1 = Integer.parseInt(config.getProperty("tam1").trim());
        tam2 = Integer.parseInt(config.getProperty("tam2").trim());

        Buzon buzon = new Buzon(cantUsuarios);
        ZonaEspera zonaEspera = new ZonaEspera();
        ControladorProcesadores controladorProcesador = new ControladorProcesadores(procesadores);
        ControladorMemoria controladorMemoria = new ControladorMemoria(capacidadMemoria, tamPagina);
        ControladorAlmacenamiento controladorAlmacenamiento = new ControladorAlmacenamiento(capacidadAlmacenamiento, tamBloque);

        MaquinaVirtual.configurar(zonaEspera, controladorProcesador, controladorMemoria, controladorAlmacenamiento);


        for (int i =0; i<cantUsuarios; i ++) {

            Usuario usuario = new Usuario((i+1),valorBase * (i+1), buzon);
            usuario.start();
        }

        for (int i = 0; i < cantGestores; i ++) {

            Gestor gestor = new Gestor(buzon, zonaEspera);
            gestor.start();

        }
}}
