package controladores;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class controladorMemoria {

    private static long KIB_POR_GIB = 1024L * 1024L;
    
    private static class Tramo {
        int inicio;
        int cantidad;

        Tramo(int inicio, int cantidad) {
            this.inicio = inicio;
            this.cantidad = cantidad;
        }

        @Override
        public String toString() {
            if (cantidad == 1){
                return String.valueOf(inicio);
            }else{
                return inicio + "-" + (inicio + cantidad-1);
            }
        }

        
    }

    private int tamPagina;
    private int totalPaginas;
    private BitSet paginasOcupadas = new BitSet();
    private Map<String, List<Tramo>> asignaciones = new HashMap<>();
    private int paginasLibres;

    public controladorMemoria(double capacidadMemoria, int tamPagina) {
        this.tamPagina = tamPagina;
        totalPaginas = (int) (capacidadMemoria * KIB_POR_GIB/tamPagina);
        paginasLibres = totalPaginas;
    }

    public int aPaginas(int gib) {
        long kib = gib * KIB_POR_GIB;
        return (int) ((kib + tamPagina - 1) / tamPagina); //tamPagina-1 se usa para asignar una pagina más a valores sobrantes que no encajen exactamente en una pagina
    }

    public synchronized boolean reservar(String idMV, int gib) {
        int necesarias = aPaginas(gib);
        if (necesarias > paginasLibres) {
            return false;
        }

        List<Tramo> tramosDeLaMV = asignaciones.computeIfAbsent(idMV, k -> new ArrayList<>());
        int faltan = necesarias;
        int desde = 0;
        while (faltan > 0) {
            int inicio = paginasOcupadas.nextClearBit(desde);
            int fin = paginasOcupadas.nextSetBit(inicio);
            if (fin == -1) {
                fin = totalPaginas;
            }
            int toma = Math.min(fin - inicio, faltan);
            paginasOcupadas.set(inicio, inicio + toma);
            tramosDeLaMV.add(new Tramo(inicio, toma));
            faltan = faltan - toma;
            desde = inicio + toma;
        }
        paginasLibres = paginasLibres - necesarias;
        return true;
    }

    public synchronized int liberar(String idMV) {
        List<Tramo> tramos = asignaciones.remove(idMV);
        if (tramos == null) {
            return 0;
        }
        int devueltas = 0;
        for (int i = 0; i < tramos.size(); i++) {
            Tramo t = tramos.get(i);
            paginasOcupadas.clear(t.inicio, t.inicio + t.cantidad);
            devueltas = t.cantidad + devueltas;
        }
        paginasLibres = paginasLibres + devueltas;
        return devueltas;
    }

    public synchronized String duenoDe(int pagina) {
        if (paginasOcupadas.get(pagina) == false) {
            return null;
        }
        ArrayList<String> ids = new ArrayList<>(asignaciones.keySet());
        for (int i = 0; i < ids.size(); i++) {
            String idMV = ids.get(i);
            List<Tramo> tramos = asignaciones.get(idMV);
            for (int j = 0; j < tramos.size(); j++) {
                Tramo t = tramos.get(j);
                if (pagina >= t.inicio && pagina < t.inicio + t.cantidad) {
                    return idMV;
                }
            }
        }
        return null;
    }

    public synchronized String asignacionDe(String idMV) {
        List<Tramo> tramos = asignaciones.get(idMV);
        if (tramos == null){
            return "[]";
        }else{
            return tramos.toString();
        }
    }

    public synchronized int getLibres() {
        return paginasLibres;
    }

    public int getTotal() {
        return totalPaginas;
    }

    public int getTamPagina() {
        return tamPagina;
    }

    public synchronized boolean todoLibre() {
        if (paginasLibres == totalPaginas){
            return true;

        }else{
            return false;
        }
    }

    public synchronized String estado() {
        String texto = "Memoria: " + paginasLibres + "/" + totalPaginas + " paginas libres";
        return texto;
    }
}