/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package model;

public class Area {
    private String nombre;
    private String descripcion;
    private double presupuesto; //presupuesto anual 
    
    public Area(String nombre, String descripcion, double presupuesto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.presupuesto = presupuesto;
    }
    
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    
    public double getPresupuesto() {return presupuesto;}
    public void setPresupuesto(double presupuesto) {this.presupuesto = presupuesto;}
 
}
