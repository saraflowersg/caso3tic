import java.util.ArrayList;

public class Buzon {

    public ArrayList<Solicitud> solicitudes;
    public int cantUsuarios;

    public Buzon(int cantUsuarios) {

        this.solicitudes = new ArrayList<Solicitud>();
        this.cantUsuarios = cantUsuarios;
    }

    public synchronized void disminuirUsuario() {

        cantUsuarios -=1;
        
    }

    public synchronized Boolean noHaySolicitudesUsuarios() {

        if (solicitudes.size() == 0 && cantUsuarios > 0) {

            return true;
        } else {

            return false;
        }
    }
    public synchronized Boolean haySolicitudesUsuarios() {

        if (solicitudes.size() > 0 || cantUsuarios > 0) {

            return true;
        } else {

            return false;
        }
    }
    public synchronized Boolean haySolicitudes() {

        if (solicitudes.size() > 0) {

            return true;
        } else {

            return false;
        }
    }

    public synchronized Boolean isBuzonTerminado() {

        if (solicitudes.size() == 0 && cantUsuarios == 0) {
            return true;
        } else {

            return false;
        }
    }

    public synchronized  void agregarSolicitud(Solicitud soli) {


        while (solicitudes.size() >= Configuracion.tam1) {

            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        solicitudes.add(soli);
        notify();

        }

    public synchronized Solicitud sacarSolicitud() {

        Solicitud i = null;
        if (solicitudes.size() > 0) {
        i = solicitudes.remove(0);
        notify();
        }
        return i;
    }

}