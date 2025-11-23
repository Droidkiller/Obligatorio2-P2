/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package model;

import java.util.ArrayList;
import java.io.File;
import model.archivos.ArchivoGrabacion;

public class Sistema {
    private static final Sistema instancia = new Sistema(); 
    private final ArrayList<Area> areas;
    private final ArrayList<Manager> managers;
    private final ArrayList<Empleado> empleados;
    private final ArrayList<Movimiento> movimientos;
    private final ArrayList<Observer> observers;
   

    private Sistema() {
        this.areas = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.movimientos = new ArrayList<>();
        this.observers = new ArrayList<>();
    }
    
    public ArrayList<Area> getAreas() {return areas;}
    public ArrayList<Manager> getManagers() {return managers;}
    public ArrayList<Empleado> getEmpleados() {return empleados;}
    public ArrayList<Movimiento> getMovimientos() {return movimientos;}
    public ArrayList<Observer> getObservers() {return observers;}
    public static Sistema getInstancia() {return instancia;}
    
    public void agregarEmpleado(Empleado emp){
        empleados.add(emp);
    } 
    public void quitarEmpleado(Empleado emp){
        empleados.remove(emp);
    }
    
    public void agregarManager(Manager man){
        managers.add(man);
    }
    public void eliminarManager(Manager man){
        managers.remove(man);
    }
    
    public void crearArea(String nombre, String descripcion, double presupuesto) throws Exception {
        for (Area a : areas) {
            if (a.getNombre().equalsIgnoreCase(nombre)) {
                throw new Exception("Ya existe un área con ese nombre.");
            }
        }
        Area nueva = new Area(nombre, descripcion, presupuesto);
        areas.add(nueva);
        notificarObservers();
    }
    
    public Area buscarAreaPorNombre(String nombre) {
        Area resultado = null;
        boolean encontrado = false;

        for (int i = 0; i < areas.size() && !encontrado; i++) {
            Area a = areas.get(i);
            if (a.getNombre().equalsIgnoreCase(nombre)) {
                resultado = a;
                encontrado = true;
            }
        }

        return resultado;
    }
    
    public void eliminarArea(Area a) {
        areas.remove(a);
        notificarObservers();
    }
    
    public void modificarDescripcionArea(Area a, String nuevaDescripcion) {
        a.setDescripcion(nuevaDescripcion);
        notificarObservers();
    }
    public void moverEmpleado(Empleado e, Area origen, Area destino, int mesInicio) throws Exception {
        if (!puedeMoverEmpleado(e, destino, mesInicio)) {
            throw new Exception("No hay presupuesto suficiente en el área de destino.");
        }
        origen.reintegrarPresupuesto(e, mesInicio);
        destino.asignarEmpleado(e, mesInicio);
        e.setArea(destino);
        notificarObservers();
    }
    public void registrarMovimiento(Movimiento m) {
        movimientos.add(m);
    }
    
    public void crearEmpleado(String nombre, int cedula, int celular,
                          double salario, String cvTexto,
                          Manager manager, Area area) throws Exception {

        if (existeCedula(cedula)) {
            throw new Exception("La cédula ya existe entre managers o empleados.");
        }

        File carpeta = new File("cvs");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }

        String nombreArchivo = "CV" + cedula + ".txt";
        String rutaCompleta = "cvs/" + nombreArchivo;
        ArchivoGrabacion escritura = new ArchivoGrabacion(rutaCompleta);

        for (String linea : cvTexto.split("\n")) {
            escritura.grabarLinea(linea);
        }
        escritura.cerrar();

        Empleado e = new Empleado(nombre,cedula,celular,salario,rutaCompleta,manager,area);
        empleados.add(e);
        area.agregarEmpleado(e);
        manager.agregarEmpleado(e);

        notificarObservers();
    }
    
    public void crearManager(String nombre, int cedula, int antiguedad, int celular) throws Exception {
        if (existeCedula(cedula)) {
            throw new Exception("La cédula ya está registrada.");
        }

        Manager nuevo = new Manager(nombre, cedula, antiguedad, celular);
        managers.add(nuevo);

        notificarObservers();
    }
    
    public void modificarCelularManager(Manager m, int nuevoCelular) {
        m.setCelular(nuevoCelular); 
        notificarObservers();        
    }
    
    public ArrayList<Manager> getManagersSinEmpleados() {
        ArrayList<Manager> resultado = new ArrayList<>();
        for (Manager m : managers) {
            if (m.getEmpleados().isEmpty()) {
                resultado.add(m);
            }
        }
        return resultado;
    }
    
    public ArrayList<Empleado> getEmpleadosEnArea(String nombreArea) {
        ArrayList<Empleado> resultado = new ArrayList<>();

        for (Empleado e : empleados) {
            Area a = e.getArea();
            if (a != null && a.getNombre().equalsIgnoreCase(nombreArea)) {
                resultado.add(e);
            }
        }

        return resultado;
    }

    public void iniciarSistemaVacio() {
        areas.clear();
        managers.clear();
        empleados.clear();
        movimientos.clear();
        notificarObservers();
    }

    public void iniciarSistemaConDatosPrecargados() {

        areas.clear();
        managers.clear();
        empleados.clear();
        movimientos.clear();


        Area personal = new Area(
                "Personal",
                "Reclutamiento de personal, promociones, gestión de cargos",
                100000
        );

        Area rrhh = new Area(
                "RRHH",
                "Relacionamiento en la empresa, organigrama, gestión de equipos",
                80000
        );

        Area seguridad = new Area(
                "Seguridad",
                "Seguridad física, vigilancia, seguridad informática, protocolos y políticas de seguridad",
                120000
        );

        Area comunicaciones = new Area(
                "Comunicaciones",
                "Comunicaciones internas, reglas y protocolos, comunicaciones con proveedores y clientes",
                20000
        );

        Area marketing = new Area(
                "Marketing",
                "Acciones planificadas, publicidad en medios masivos, publicidad en redes, gestión de redes",
                95000
        );

        areas.add(personal);
        areas.add(rrhh);
        areas.add(seguridad);
        areas.add(comunicaciones);
        areas.add(marketing);

        // Ana Martínez, ci 4.568.369-1, celular 099 123 456, antigüedad 10
        Manager ana = new Manager("Ana Martínez", 45683691, 10, 99123456);

        // Ricardo Morales, ci 3.214.589-3, celular 094 121 212, antigüedad 4
        Manager ricardo = new Manager("Ricardo Morales", 32145893, 4, 94121212);

        // Laura Torales, ci 3.589.257-5, celular 099 654 321, antigüedad 1
        Manager laura = new Manager("Laura Torales", 35892575, 1, 99654321);

        // Juan Pablo Zapata, ci 4.555.197-7, celular 099 202 020, antigüedad 5
        Manager juanPablo = new Manager("Juan Pablo Zapata", 45551977, 5, 99202020);

        managers.add(ana);
        managers.add(ricardo);
        managers.add(laura);
        managers.add(juanPablo);


        notificarObservers();
    }

    /**
     * Iniciar el sistema con datos guardados en archivo.
     */
    public void iniciarSistemaGuardado() {

    }


    private boolean existeCedula(int cedula) {
        boolean existe=false;
        for (int i=0;i<empleados.size() && !existe;i++) {
            if (empleados.get(i).getCedula() == cedula) 
                existe=true;
        }
        for (int i=0;i<managers.size() && !existe;i++) {
            if (managers.get(i).getCedula() == cedula) 
                existe=true;
        }
        return existe;
    }
    
    public boolean puedeMoverEmpleado(Empleado e, Area destino, int mesInicio) {
        return destino.tienePresupuestoDisponible(e.getSalario(), mesInicio);
    }
    
    public void agregarObserver(Observer obs) {
        observers.add(obs);
    }

    public void quitarObserver(Observer obs) {
        observers.remove(obs);
    }

    public void notificarObservers() {
        for (Observer obs : observers) {
            obs.actualizar();
        }
    }
}
