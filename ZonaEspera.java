import java.util.ArrayList;

public class ZonaEspera {

    private ArrayList<MaquinaVirtual> maquinasVirtuales;// Maquinas Virtualess que no consiguieron sus tres recursos y siguen pendientes
    private int umbral;

    private long liberaciones;// Cuántas veces una MV acabó y devolvió sus recursos

    public ZonaEspera() {
        this.maquinasVirtuales = new ArrayList<MaquinaVirtual>();
        this.umbral = Configuracion.tam2;
        this.liberaciones = 0;
    }

    public synchronized boolean superaUmbral() {
        return maquinasVirtuales.size() >= umbral;
    }

    public synchronized boolean hayMaquinasVirtuales() {
        return maquinasVirtuales.size() > 0;
    }

    public synchronized int cantidadMaquinasVirtuales() {
        return maquinasVirtuales.size();
    }

    public synchronized long getLiberaciones() {
        return liberaciones;
    }

    public synchronized void agregarMaquinaVirtual(MaquinaVirtual maquina) {
        maquinasVirtuales.add(maquina);
    }

    public synchronized void sacarMaquinaVirtual(MaquinaVirtual maquina) {
        maquinasVirtuales.remove(maquina);
    }

    public synchronized void esperarLiberacion(long liberacionesVistas) throws InterruptedException {
        while (liberaciones == liberacionesVistas) {
            wait(); //espera pasiva
        }
    }

    public synchronized void avisarLiberacion() {
        liberaciones++;
        notifyAll();
    }
}