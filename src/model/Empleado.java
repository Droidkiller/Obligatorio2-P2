/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package model;

public class Empleado {
    private String nombre;
    private int cedula;
    private int celular;
    private double salario; //mensual 
    private String cv;
    private Manager manager;
    private Area area;
    
    public Empleado(String nombre, int cedula, int celular, double salario, String cv, Manager manager, Area area) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.celular = celular;
        this.salario = salario;
        this.cv = cv;
        this.manager = manager;
        this.area = area;
    }
    
    public String getNombre() { return nombre;}
    private void setNombre(String nombre) {this.nombre = nombre;}

    public int getCedula() {return cedula;}
    private void setCedula(int cedula) {this.cedula = cedula;}

    public int getCelular() {return celular;}
    private void setCelular(int celular) {this.celular = celular;}

    public double getSalario() {return salario;}
    private void setSalario(double salario) {this.salario = salario;}

    public String getCv() {return cv;}
    private void setCv(String cv) {this.cv = cv;}

    public Manager getManager() {return manager;}
    private void setManager(Manager manager) {this.manager = manager;}

    public Area getArea() {return area;}
    private void setArea(Area area) {this.area = area;}


    
}
