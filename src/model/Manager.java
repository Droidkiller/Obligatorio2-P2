/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package model;

import java.util.ArrayList;

public class Manager {

    private String nombre;
    private int cedula;
    private int antiguedad; //en años
    private int celular;
    private final ArrayList<Empleado> empleados; 
    
    public Manager(String nombre, int cedula, int antiguedad, int celular) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.antiguedad = antiguedad;
        this.celular = celular;
        this.empleados = new ArrayList<>();
    }
 
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public int getCedula() {return cedula;}
    public void setCedula(int cedula) {this.cedula = cedula;}

    public int getAntiguedad() {return antiguedad;}
    public void setAntiguedad(int antiguedad) {this.antiguedad = antiguedad;}

    public int getCelular() {return celular;}
    public void setCelular(int celular) {this.celular = celular;}

    public ArrayList<Empleado> getEmpleados() {return empleados;}
    
    public void agregarEmpleado(Empleado emp){
        empleados.add(emp);
    } 
   
    @Override
    public String toString() {
        return nombre;
    }
 
}
