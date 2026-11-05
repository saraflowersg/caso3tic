package controladores;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorAlmacenamiento {

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
            } else{
                return inicio + "-" + (inicio + cantidad-1); 
            }
        }
    }

    private int tamBloque;
    private int totalBloques;
    private BitSet bloquesOcupados = new BitSet();
    private Map<String, List<Tramo>> asignaciones = new HashMap<>();

    private int bloquesLibres;

    public ControladorAlmacenamiento(double capacidadAlmacenamiento, int tamBloque) {
        this.tamBloque = tamBloque;
        totalBloques = (int) (capacidadAlmacenamiento * KIB_POR_GIB/tamBloque);
        bloquesLibres = totalBloques;
    }

    public int aBloques(int gib) {
        long kib = gib * KIB_POR_GIB;
        return (int) ((kib + tamBloque - 1) / tamBloque);
    }

    public synchronized boolean reservar(String idMV, int gib) {
        int necesarias = aBloques(gib);
        if (necesarias > bloquesLibres) {
            return false;
        }

        List<Tramo> tramosDeLaMV = asignaciones.get(idMV);
        if (tramosDeLaMV == null){
            tramosDeLaMV = new ArrayList<>();
            asignaciones.put(idMV,tramosDeLaMV);
        }

        int faltan = necesarias;
        int desde = 0;
        while (faltan > 0) {
            int inicio = bloquesOcupados.nextClearBit(desde);
            int fin = bloquesOcupados.nextSetBit(inicio);
            if (fin == -1) {
                fin = totalBloques;
            }
            int toma = Math.min(fin - inicio, faltan);
            bloquesOcupados.set(inicio, inicio + toma);
            tramosDeLaMV.add(new Tramo(inicio, toma));
            faltan = faltan - toma;
            desde = inicio + toma;
        }
        bloquesLibres = bloquesLibres - necesarias;
        return true;
    }

    public synchronized int liberar(String idMV) {
        List<Tramo> tramos = asignaciones.remove(idMV);
        if (tramos == null) {
            return 0;
        }
        int devueltos = 0;
        for (int i = 0; i<tramos.size(); i++) {
            Tramo t = tramos.get(i);
            bloquesOcupados.clear(t.inicio, t.inicio + t.cantidad);
            devueltos = devueltos + t.cantidad;
        }
        bloquesLibres = bloquesLibres + devueltos;
        return devueltos;
    }

    public synchronized String duenoDe(int bloque) {
        if (bloquesOcupados.get(bloque) == false) {
            return null;
        }
        ArrayList<String> ids = new ArrayList<>(asignaciones.keySet());
        for (int i = 0; i < ids.size(); i++) {
            String idMV = ids.get(i);
            List<Tramo> tramos = asignaciones.get(idMV);
            for (int j = 0; j < tramos.size(); j++) {
                Tramo t = tramos.get(j);
                if (bloque >= t.inicio && bloque < t.inicio + t.cantidad) {
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
        return bloquesLibres;
    }

    public int getTotal() {
        return totalBloques;
    }

    public int getTamBloque() {
        return tamBloque;
    }

    public synchronized boolean todoLibre() {
        if (bloquesLibres == totalBloques){
            return true;
        }else{
            return false;
        }
    }

    public synchronized String estado() {
        String texto = "Almacenamiento: " + bloquesLibres + "/" + totalBloques + " bloques libres, ";
        return texto;
    }
}