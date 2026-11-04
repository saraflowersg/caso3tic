import controladores.controladorAlmacenamiento;
import controladores.controladorMemoria;
import controladores.ControladorProcesadores;

public class MaquinaVirtual extends Thread {

    // Los static son compartidos por todas las MVs
    private static ZonaEspera zonaEspera;
    private static ControladorProcesadores ctrlProcesador;
    private static controladorMemoria ctrlMemoria;
    private static controladorAlmacenamiento ctrlAlmacenamiento;
    private static int activas = 0;// Maquinas Virtuales que no han terminado su ejecución

    private Solicitud solicitud;
    private String id;

    public static void configurar(ZonaEspera zona, ControladorProcesadores procesador,
                                  controladorMemoria memoria, controladorAlmacenamiento almacenamiento) {
        zonaEspera = zona;
        ctrlProcesador = procesador;
        ctrlMemoria = memoria;
        ctrlAlmacenamiento = almacenamiento;
    }

    public MaquinaVirtual(Solicitud solicitud) {
        this.solicitud = solicitud;
        this.id = "MV-" + solicitud.getNombre();
        sumarActiva();
    }

    @Override
    public void run() {
        try {

            synchronized (zonaEspera) {
                while (reservarTodo() == false) {
                    System.out.println(id + " no consiguio recursos, tiene que esperar");
                    zonaEspera.agregarMaquinaVirtual(this);
                    zonaEspera.wait();
                    zonaEspera.sacarMaquinaVirtual(this);
                }
            }

            int tiempo = 4000 + (int) (Math.random() * 36001); // Calculo tiempo random entre 4000 y 40000 ms, se pone *36001 porque math.random da valores entre 0 y 0.999 y se redondea conviertiendolo en entero
            System.out.println(id + " se ejecuta en " + tiempo + " ms");
            Thread.sleep(tiempo);

            synchronized (zonaEspera) {
                ctrlAlmacenamiento.liberar(id);
                ctrlMemoria.liberar(id);
                ctrlProcesador.liberar(id);
                zonaEspera.notifyAll();
            }
            System.out.println(id + " termino");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            restarActiva();
        }
    }

    private boolean reservarTodo() {
        if (ctrlProcesador.reservar(id, solicitud.getNumProcesadores()) == false) {
            return false;

        }
        if (ctrlMemoria.reservar(id, solicitud.getNumMemoria()) == false) {
            ctrlProcesador.liberar(id);
            return false;
        }
        if (ctrlAlmacenamiento.reservar(id, solicitud.getEspacioDisco()) == false) {
            ctrlMemoria.liberar(id);
            ctrlProcesador.liberar(id);
            return false;
        }
        return true;
    }

    private static synchronized void sumarActiva() {

        activas++;
    }

    private static synchronized void restarActiva() {

        activas = activas - 1;
        MaquinaVirtual.class.notifyAll();
    }

    public static synchronized void esperarTodas() throws InterruptedException {
        while (activas > 0) {
            MaquinaVirtual.class.wait();
        }
    }
}