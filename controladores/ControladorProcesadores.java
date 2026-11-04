package controladores;

import java.util.ArrayList;
import java.util.List;

public class ControladorProcesadores {

    private final String[] asignados;
    private int libres;

    public ControladorProcesadores(int procesadores) {
        asignados = new String[procesadores];
        libres = procesadores;

    }

    public synchronized boolean reservar(String idMV, int cantidad) {
        if (cantidad > libres) {
            return false;
        }
        int faltan = cantidad;
        for (int i = 0; i < asignados.length && faltan > 0; i++) {
            if (asignados[i] == null) {
                asignados[i] = idMV;
                faltan = faltan-1;
            }
        }
        libres = libres - cantidad;
        return true;
    }

    public synchronized int liberar(String idMV) {
        int devueltos = 0;
        for (int i = 0; i < asignados.length; i++) {
            if (idMV.equals(asignados[i])) {
                asignados[i] = null;
                devueltos++;
            }
        }
        libres = libres + devueltos;
        return devueltos;
    }

    public synchronized String duenoDe(int procesador) {
        return asignados[procesador];
    }

    public synchronized List<Integer> procesadoresDe(String idMV) {
        List<Integer> lista = new ArrayList<>();
        for (int i = 0; i < asignados.length; i++) {
            if (idMV.equals(asignados[i])) {
                lista.add(i);
            }
        }
        return lista;
    }

    public synchronized int getLibres() {
        return libres;
    }

    public int getTotal() {
        return asignados.length;
    }

    public synchronized boolean todoLibre() {
        if (libres != asignados.length) {
            return false;
        }
        for (int i = 0; i < asignados.length; i++) {
            if (asignados[i] != null) {
                return false;
            }
        }
        return true;
    }

    public synchronized String estado() {
        return "Procesadores: " + libres + "/" + asignados.length + " libres";
    }
}