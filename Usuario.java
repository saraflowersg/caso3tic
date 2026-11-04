public class Usuario extends Thread {
    
    private int numSolicitudes;
    private int nUsuario;
    private Buzon buzon;


    public Usuario(int nUsuario, int numSolicitudes, Buzon buzon) {

        this.numSolicitudes = numSolicitudes;
        this.nUsuario = nUsuario;
        this.buzon = buzon;
    }

    @Override 
    public void run() {

        for (int i = 0; i < numSolicitudes; i ++) {

            Solicitud solicitud = new Solicitud("U" + nUsuario + "S" + i);

            buzon.agregarSolicitud(solicitud);


        }


        buzon.disminuirUsuario();
    }
}