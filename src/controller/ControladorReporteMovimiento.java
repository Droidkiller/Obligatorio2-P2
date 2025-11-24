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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ControladorReporteMovimiento {

    private final Principal principal;
    private final Sistema sistema;

    public ControladorReporteMovimiento(Principal principal) {
        this.principal = principal;
        this.sistema = Sistema.getInstancia();
        agregarMenuListeners();
    }

    private void agregarMenuListeners() {
        principal.getReporteMov().addActionListener(e -> abrirReporteMovimiento());
    }

    private void abrirReporteMovimiento() {
        ReporteMovimiento vista = new ReporteMovimiento(principal, false);

        sistema.agregarObserver(vista);
        agregarListenersVentana(vista);

        vista.actualizar();
        vista.setVisible(true);
    }

    private void agregarListenersVentana(ReporteMovimiento vista) {
        vista.getBtnFiltrar().addActionListener(e -> aplicarFiltros(vista));
        vista.getBtnExportar().addActionListener(e -> exportarCSV(vista));
        vista.getBtnCerrar().addActionListener(e -> {
            sistema.quitarObserver(vista);
            vista.dispose();
        });
    }

    private void aplicarFiltros(ReporteMovimiento vista) {
        String mesStr = vista.getSelectedMes();
        String areaStr = vista.getSelectedArea();
        String empStr = vista.getSelectedEmpleado();

        String[] meses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                           "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" };

        boolean usarFiltroMes = !mesStr.equals("Todos");
        int numeroMes = 0; // 1..12
        if (usarFiltroMes) {
            for (int i = 0; i < meses.length; i++) {
                if (meses[i].equalsIgnoreCase(mesStr)) {
                    numeroMes = i + 1;
                }
            }
        }

        ArrayList<Movimiento> movs = sistema.getMovimientos();
        ArrayList<Movimiento> result = new ArrayList<>();

        for (Movimiento m : movs) {
            boolean okMes = true;
            if (usarFiltroMes) {
                okMes = (m.getMes() == numeroMes);
            }

            boolean okArea = areaStr.equals("Todos") ||
                             m.getAreaOrigen().getNombre().equals(areaStr) ||
                             m.getAreaDestino().getNombre().equals(areaStr);

            boolean okEmp = empStr.equals("Todos") ||
                             m.getEmpleado().getNombre().equals(empStr);

            if (okMes && okArea && okEmp) {
                result.add(m);
            }
        }

        vista.cargarTabla(result);
    }

    private void exportarCSV(ReporteMovimiento vista) {
        JFileChooser chooser = new JFileChooser();
        int opcion = chooser.showSaveDialog(vista);

        if (opcion == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String path = file.getAbsolutePath();

            if (!path.toLowerCase().endsWith(".csv")) {
                path += ".csv";
            }

            ArchivoGrabacion ag = new ArchivoGrabacion(path);
            ag.grabarLinea("Mes,AreaOrigen,AreaDestino,Empleado");

            for (Movimiento m : sistema.getMovimientos()) {
                String linea =
                        m.getMes() + "," +
                        m.getAreaOrigen().getNombre() + "," +
                        m.getAreaDestino().getNombre() + "," +
                        m.getEmpleado().getNombre();

                ag.grabarLinea(linea);
            }
            ag.cerrar();

            JOptionPane.showMessageDialog(vista, "CSV exportado correctamente.");
        }
    }
}
