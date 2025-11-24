/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package model;

import java.util.ArrayList;

public class Area {  
    private String nombre;
    private String descripcion;
    private double presupuesto; // anual 
    private final ArrayList<Empleado> empleados;
    private final double[] costosMensuales; // índice 0 = Enero, 11 = Diciembre
    
    public Area(String nombre, String descripcion, double presupuesto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.presupuesto = presupuesto;
        this.empleados = new ArrayList<>();
        this.costosMensuales = new double[12];
    }
    
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}    
    public double getPresupuesto() {return presupuesto;}
    public void setPresupuesto(double presupuesto) {this.presupuesto = presupuesto;}    
    public ArrayList<Empleado> getEmpleados() {return empleados;}
    public double[] getCostosMensuales() {return costosMensuales;}
    private void setCostosMensuales(int mes, double monto) {
        getCostosMensuales()[mes] += monto;
    }
    
    public void asignarEmpleado(Empleado e, int mesInicio) {
        agregarEmpleado(e);
        double sueldoMensual = e.getSalario();
        for (int m = mesInicio - 1; m < 12; m++) {
            setCostosMensuales(m, sueldoMensual);
        }
    }
    
    public void agregarEmpleado(Empleado e) {
        if (!empleados.contains(e)) {
            empleados.add(e);
        }
    }
    
    public void quitarEmpleado(Empleado e, int mesInicio) {
        empleados.remove(e);
        double sueldoMensual = e.getSalario();
        for (int m = mesInicio - 1; m < 12; m++) {
            getCostosMensuales()[m] -= sueldoMensual;
        }
    }

    public boolean tienePresupuestoDisponible(double sueldo, int mesInicio) {
        boolean tiene=true;
        for (int m = mesInicio - 1; m < 12 && tiene; m++) {
            if (getCostosMensuales()[m] + sueldo > presupuesto / 12.0) {
                tiene=false;
            }
        }
        return tiene;
    }
    
    public void reintegrarPresupuesto(Empleado e, int mesInicio) {
        double sueldoMensual = e.getSalario();
        for (int m = mesInicio - 1; m < 12; m++) {
            setCostosMensuales(m, -sueldoMensual);
        }
    }
 
    @Override
    public String toString() {
        return nombre;
    }
}
