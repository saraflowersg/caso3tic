public class Gestor extends Thread {
    

    private Buzon buzon;
    private ZonaEspera zonaEspera;

    public Gestor (Buzon buzon, ZonaEspera zonaEspera) {

        this.buzon = buzon;
        this.zonaEspera = zonaEspera;
    }


    @Override 
    public void run() {


        if (buzon.noHaySolicitudesUsuarios() || buzon.haySolicitudes()) { //no hay solicitudes pero si usuarios o hay solicitudes


            if (buzon.noHaySolicitudesUsuarios()) { // no hay solicitudes pero si usuarios

                while (buzon.noHaySolicitudesUsuarios()) {

                    Thread.yield();
                }
            }

            while (buzon.haySolicitudesUsuarios()) { //mientras haya solicitudes o usuarios
                
                if (!zonaEspera.superaUmbral()) {

                    Solicitud solicitud = buzon.sacarSolicitud();

                    if (solicitud != null) {

                    
                    MaquinaVirtual maquina = new MaquinaVirtual(solicitud);
                    maquina.start();}
                    else {

                        while (buzon.noHaySolicitudesUsuarios()) {

                            Thread.yield();
                }
                    }

                } else { //si si la supera

                    while (zonaEspera.hayMaquinasVirtuales()) {

                        Thread.yield();

                    }
                }

            }

        }


    }


}