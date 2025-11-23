/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package controller;

import model.Sistema;
import model.Movimiento;
import model.archivos.ArchivoGrabacion;
import view.ReporteMovimiento;
import view.Principal;
import java.util.ArrayList;
import java.io.File;

public class ControladorReporteMovimiento {
    private final ReporteMovimiento vista;
    private final Sistema sistema;
    private ArrayList<Movimiento> listaFiltrada = new ArrayList<>();

    public ControladorReporteMovimiento(Principal principal) {
        this.sistema = Sistema.getInstancia();
        this.vista = new ReporteMovimiento(principal, true);
        agregarListeners();
        vista.actualizar();
        vista.setVisible(true);
    }

    private void agregarListeners() {
        sistema.agregarObserver(vista);
        vista.getBtnFiltrar().addActionListener(e -> aplicarFiltros());
        vista.getBtnExportar().addActionListener(e -> exportarCSV());
        vista.getBtnCerrar().addActionListener(e -> {
            sistema.quitarObserver(vista);
            vista.dispose();
        });
    }
    
    private void aplicarFiltros() {
        String mesStr = vista.getSelectedMes();
        String areaStr = vista.getSelectedArea();
        String empStr = vista.getSelectedEmpleado();

        ArrayList<Movimiento> movs = sistema.getMovimientos();
        ArrayList<Movimiento> result = new ArrayList<>();

        for (Movimiento m : movs) {

            boolean okMes = mesStr.equals("Todos") || 
                            Integer.toString(m.getMes()).equals(mesStr);

            boolean okArea = areaStr.equals("Todos") ||
                    m.getAreaOrigen().getNombre().equals(areaStr) ||
                    m.getAreaDestino().getNombre().equals(areaStr);

            boolean okEmp = empStr.equals("Todos") ||
                    m.getEmployee().getNombre().equals(empStr);

            if (okMes && okArea && okEmp) {
                result.add(m);
            }
        }
        
        listaFiltrada = result;
        vista.cargarTabla(result);
    }

    private void exportarCSV() {
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        int opcion = chooser.showSaveDialog(vista);

        if (opcion == javax.swing.JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String path = file.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".csv")) {
                path = path + ".csv";
            }
            ArchivoGrabacion ag = new ArchivoGrabacion(path);
            ag.grabarLinea("Mes,AreaOrigen,AreaDestino,Empleado");

            for (Movimiento m : listaFiltrada) {
                String linea =
                    m.getMes() + "," +
                    m.getAreaOrigen().getNombre() + "," +
                    m.getAreaDestino().getNombre() + "," +
                    m.getEmployee().getNombre();
                ag.grabarLinea(linea);
            }
            ag.cerrar();
            javax.swing.JOptionPane.showMessageDialog(vista, "CSV exportado correctamente.");
        }
    }
}
