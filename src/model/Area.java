/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package model;

import java.util.ArrayList;

public class Area {
    private String nombre;
    private String descripcion;
    private double presupuesto; //presupuesto anual 
    private ArrayList<Empleado> empleados;
    
    public Area(String nombre, String descripcion, double presupuesto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.presupuesto = presupuesto;
        this.empleados = new ArrayList<>();
    }
    
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    
    public double getPresupuesto() {return presupuesto;}
    public void setPresupuesto(double presupuesto) {this.presupuesto = presupuesto;}
    
    public void agregarEmpleado(Empleado e) {
        this.empleados.add(e);
    }

    public boolean tienePresupuesto(double monto) {
        return this.presupuesto >= monto;
    }

    public void reducirPresupuesto(double monto) {
        this.presupuesto -= monto;
    }
 
    @Override
    public String toString() {
        return nombre;
    }
}
