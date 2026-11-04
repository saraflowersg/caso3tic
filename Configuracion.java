public class Configuracion {
    //este es el main
    public static int valorBase;
    public static int cantUsuarios;
    public static int cantGestores;
    public static int procesadores;
    public static Float capacidadMemoria;
    public static Float capacidadAlmacenamiento;
    public static final int tam1 = 5; //capacidadBuzon
    public static final int tam2= 5; //capacidadZonaEspera

    public static void main(String[] args) {

        Buzon buzon = new Buzon(cantUsuarios);
        ZonaEspera zonaEspera = new ZonaEspera();

        for (int i =0; i<cantUsuarios; i ++) {

            Usuario usuario = new Usuario((i+1),valorBase * (i+1), buzon);
            usuario.start();
        }

        for (int i = 0; i < cantGestores; i ++) {

            Gestor gestor = new Gestor(buzon, zonaEspera);
            gestor.start();

        }



    }
    
}
