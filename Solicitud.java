public class Solicitud {
    
    private String nombre;
    private int numProcesadores;
    private int numMemoria; //GiB
    private int espacioDisco; //GiB

    public Solicitud(String nombre) {


        this.nombre = nombre;
        // procesador
        int entero = (int) Math.round(Configuracion.procesadores * 0.75);
        this.numProcesadores = (int) (Math.random() * entero) + 1;

        // memoria

        entero = (int) Math.round(Configuracion.capacidadMemoria * 0.75);
        int valor = (int) (Math.random() * entero) + 1;
        this.numMemoria = Integer.highestOneBit(valor);

        // Almacenamiento 

        entero =  (int) Math.round(Configuracion.capacidadAlmacenamiento * 0.75);
        valor = (int) (Math.random() * entero) + 1;
        this.espacioDisco = Math.max(2, valor - (valor % 2));

    }
    public String getNombre() {
        return nombre;
    }

    public int getNumProcesadores() {
        return numProcesadores;
    }

    public int getNumMemoria() {
        return numMemoria;
    }

    public int getEspacioDisco() {
        return espacioDisco;
    }
}
