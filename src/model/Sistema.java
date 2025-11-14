/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package model;

import java.util.ArrayList;

public class Sistema {
    private final ArrayList<Area> areas;
    private final ArrayList<Manager> manager;
    private final ArrayList<Empleado> empleados;
   

    public Sistema() {
        this.areas = new ArrayList<>();
        this.manager = new ArrayList<>();
        this.empleados = new ArrayList<>();
    }
    
    public ArrayList<Area> getAreas() {return areas;}
    public ArrayList<Manager> getManager() {return manager;}
    public ArrayList<Empleado> getEmpleados() {return empleados;}
    
    public void agregarEmpleado(Empleado emp){
        empleados.add(emp);
    } 
    public void quitarEmpleado(Empleado emp){
        empleados.remove(emp);
    }
    
    public void agregarManager(Manager man){
        manager.add(man);
    }
    public void quitarManager(Manager man){
        manager.remove(man);
    }
    
    public void agregarArea(Area a){
        areas.add(a);
    } 
     public void quitarArea(Area a){
        areas.remove(a);
    }
}
