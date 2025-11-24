/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */

package model;

public class Movimiento {
    private int mes;
    private Area areaOrigen;
    private Area areaDestino;
    private Empleado empleado;

    public Movimiento(int mes, Area origen, Area destino, Empleado empleado) {
        this.mes = mes;
        this.areaOrigen = origen;
        this.areaDestino = destino;
        this.empleado = empleado;
    }

    public int getMes() { return mes; }
    public void setMes(int mes) { this.mes = mes; }
    public Area getAreaOrigen() { return areaOrigen; }
    public void setAreaOrigen(Area areaOrigen) { this.areaOrigen = areaOrigen; }
    public Area getAreaDestino() { return areaDestino; }
    public void setAreaDestino(Area areaDestino) { this.areaDestino = areaDestino; }
    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
    
    @Override
    public String toString() {
        return "Movimiento{mes=" + mes +
               ", origen=" + areaOrigen.getNombre() +
               ", destino=" + areaDestino.getNombre() +
               ", empleado=" + empleado.getNombre() +
               '}';
    }
}
