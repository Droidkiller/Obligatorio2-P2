/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */

package model;

public class Movimiento {
    private final int mes;
    private final Area areaOrigen;
    private final Area areaDestino;
    private final Empleado empleado;

    public Movimiento(int mes, Area origen, Area destino, Empleado empleado) {
        this.mes = mes;
        this.areaOrigen = origen;
        this.areaDestino = destino;
        this.empleado = empleado;
    }

    public int getMes() { return mes; }
    public Area getAreaOrigen() { return areaOrigen; }
    public Area getAreaDestino() { return areaDestino; }
    public Empleado getEmpleado() { return empleado; }
    
    @Override
    public String toString() {
        return "Movimiento{mes=" + mes +
               ", origen=" + areaOrigen.getNombre() +
               ", destino=" + areaDestino.getNombre() +
               ", empleado=" + empleado.getNombre() +
               '}';
    }
    
}
