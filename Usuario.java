public class Usuario extends Thread {
    
    private int numSolicitudes;
    private int id;
    private static int nUsuario;
    private Buzon buzon;


    public Usuario(int id, int numSolicitudes, Buzon buzon) {

        this.numSolicitudes = numSolicitudes;
        this.id = id;
        this.buzon = buzon;
    }

    @Override 
    public void run() {

        for (int i = 0; i < numSolicitudes; i ++) {

            Solicitud solicitud = new Solicitud("U" + nUsuario + "S" + i);

            buzon.agregarSolicitud(solicitud, this);

            numSolicitudes -=1;


        }


        Buzon.disminuirUsuario();
    }
}
