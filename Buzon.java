import java.io.ObjectInputFilter.Config;
import java.util.ArrayList;

public class Buzon {



    public ArrayList<Solicitud> solicitudes;

    public Buzon() {

        this.solicitudes = new ArrayList<Solicitud>();
    }

    public static synchronized void disminuirUsuario() {

        Configuracion.cantUsuarios = Configuracion.cantUsuarios -1;
        
    }

    public synchronized  void agregarSolicitud(Solicitud soli, Thread t) {

        while (solicitudes.size() == 0) {

                t.yield();
        }

        
        }

    }
