/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import model.archivos.ArchivoGrabacion;
import model.archivos.ArchivoLectura;

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
    
    public double porcentajeArea(Area area) {
        double presupuesto = area.getPresupuesto();
        if (presupuesto <= 0) return 0;

        double total = 0;
        for (Empleado e : area.getEmpleados()) {
            total += e.getSalario() * 12; 
        }
        return (total / presupuesto) * 100.0;
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
    
    public List<Empleado> getEmpleadosOrdenados(Area area) {
        List<Empleado> lista = new ArrayList<>(area.getEmpleados());
        
        lista.sort(Comparator.comparing(Empleado::getNombre, String.CASE_INSENSITIVE_ORDER));
        return lista;
    }


    public void iniciarSistemaVacio() {
        areas.clear();
        managers.clear();
        empleados.clear();
        movimientos.clear();
    }

    public void iniciarSistemaConDatosPrecargados() {
        iniciarSistemaVacio();

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

    public void iniciarSistemaGuardado() {
        File archivo = new File("sistema.txt");
        boolean existe = archivo.exists();
        //se limpian las listas y si no existe un archivo para cargar, el sistema inicia vacío
        iniciarSistemaVacio(); 
        if (existe) {
            ArchivoLectura in = new ArchivoLectura("sistema.txt");
            String section = "";

            HashMap<String, Area> areasMap = new HashMap<>();
            HashMap<Integer, Manager> managersMap = new HashMap<>();
            HashMap<Integer, Empleado> empleadosMap = new HashMap<>();

            boolean hayLineas = in.hayMasLineas();

            while (hayLineas) {
                String linea = in.linea().trim();
                boolean esVacia = linea.length() == 0;

                if (!esVacia) {

                    boolean esSeccion = linea.startsWith("[");
                    if (esSeccion) {
                        section = linea;
                    } else {

                        String[] p = linea.split(";");

                        // AREAS
                        if (section.equals("[AREAS]")) {

                            Area area = new Area(p[0], p[1], Double.parseDouble(p[2]));

                            String[] costos = p[3].split(",");
                            double[] arr = area.getCostosMensuales();

                            int i = 0;
                            while (i < costos.length) {
                                arr[i] = Double.parseDouble(costos[i]);
                                i++;
                            }

                            areas.add(area);
                            areasMap.put(p[0], area);
                        }

                        // MANAGERS
                        if (section.equals("[MANAGERS]")) {

                            int ced = Integer.parseInt(p[0]);
                            Manager m = new Manager(
                                p[1], 
                                ced, 
                                Integer.parseInt(p[3]), 
                                Integer.parseInt(p[2])
                            );

                            managers.add(m);
                            managersMap.put(ced, m);
                        }

                        // EMPLEADOS
                        if (section.equals("[EMPLEADOS]")) {

                            int ced = Integer.parseInt(p[0]);

                            Manager man = null;
                            if (p[5].length() > 0) {
                                man = managersMap.get(Integer.parseInt(p[5]));
                            }

                            Area ar = null;
                            if (p[6].length() > 0) {
                                ar = areasMap.get(p[6]);
                            }

                            Empleado e = new Empleado(
                                p[1],
                                ced,
                                Integer.parseInt(p[2]),
                                Double.parseDouble(p[3]),
                                p[4],
                                man,
                                ar
                            );

                            empleados.add(e);
                            empleadosMap.put(ced, e);

                            if (man != null) man.agregarEmpleado(e);
                            if (ar != null)  ar.agregarEmpleado(e);
                        }

                        // MOVIMIENTOS
                        if (section.equals("[MOVIMIENTOS]")) {

                            int cedEmp = Integer.parseInt(p[3]);

                            Movimiento mov = new Movimiento(
                                Integer.parseInt(p[0]),
                                areasMap.get(p[1]),
                                areasMap.get(p[2]),
                                empleadosMap.get(cedEmp)
                            );

                            movimientos.add(mov);
                        }
                    }
                }

                hayLineas = in.hayMasLineas();
            }

            in.cerrar();
        }
    }
    
    public void guardarSistema() {
        ArchivoGrabacion out = new ArchivoGrabacion("sistema.txt");

        // AREAS
        out.grabarLinea("[AREAS]");
        for (Area a : areas) {
            double[] c = a.getCostosMensuales();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < c.length; i++) {
                if (i > 0) sb.append(",");
                sb.append(c[i]);
            }

            out.grabarLinea(
                a.getNombre() + ";" +
                a.getDescripcion() + ";" +
                a.getPresupuesto() + ";" +
                sb
            );
        }
        out.grabarLinea("");

        // MANAGERS
        out.grabarLinea("[MANAGERS]");
        for (Manager m : managers) {
            // empleados del manager
            StringBuilder empList = new StringBuilder();
            ArrayList<Empleado> lista = m.getEmpleados();

            for (int i = 0; i < lista.size(); i++) {
                if (i > 0) empList.append(",");
                empList.append(lista.get(i).getCedula());
            }

            out.grabarLinea(
                m.getCedula() + ";" +
                m.getNombre() + ";" +
                m.getCelular() + ";" +
                m.getAntiguedad() + ";" +
                empList
            );
        }
        out.grabarLinea("");

        // EMPLEADOS
        out.grabarLinea("[EMPLEADOS]");
        for (Empleado e : empleados) {
            String areaNombre = e.getArea() != null ? e.getArea().getNombre() : "";
            String mgrCedula = e.getManager() != null ? Integer.toString(e.getManager().getCedula()) : "";

            out.grabarLinea(
                e.getCedula() + ";" +
                e.getNombre() + ";" +
                e.getCelular() + ";" +
                e.getSalario() + ";" +
                e.getCv() + ";" +
                mgrCedula + ";" +
                areaNombre
            );
        }
        out.grabarLinea("");

        // MOVIMIENTOS
        out.grabarLinea("[MOVIMIENTOS]");
        for (Movimiento m : movimientos) {
            out.grabarLinea(
                m.getMes() + ";" +
                m.getAreaOrigen().getNombre() + ";" +
                m.getAreaDestino().getNombre() + ";" +
                m.getEmpleado().getCedula()
            );
        }
        out.grabarLinea("");

        out.cerrar();
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
