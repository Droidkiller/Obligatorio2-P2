/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package controller;

import java.util.Collections;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import model.Area;
import model.Empleado;
import model.Sistema;
import view.ReporteEstadoAreas;
import view.Principal;

public class ControladorReporteEstadoAreas {

    private final Principal principal;
    private final Sistema sistema;

    public ControladorReporteEstadoAreas(Principal principal) {
        this.principal = principal;
        this.sistema = Sistema.getInstancia();
        agregarMenuListeners();
    }

    private void agregarMenuListeners() {
        principal.getReporteAreas().addActionListener(e -> abrirReporte());
    }

    private void abrirReporte() {
        ReporteEstadoAreas vista = new ReporteEstadoAreas(principal, false);
        sistema.agregarObserver(vista);
        cargarAreas(vista);
        vista.setVisible(true);
        vista.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sistema.quitarObserver(vista);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                sistema.quitarObserver(vista);
            }
        });

        
    }

    private void cargarAreas(ReporteEstadoAreas vista) {
        List<Area> areasOrdenadas = sistema.getAreas();

        // Orden descendente por porcentaje usado
        Collections.sort(
            areasOrdenadas,
            (a1, a2) -> Double.compare(
                sistema.porcentajeArea(a2),
                sistema.porcentajeArea(a1)
            )
        );

        vista.cargarListadoAreas(areasOrdenadas, e -> {
                Area area = (Area)((javax.swing.JButton)e.getSource()).getClientProperty("obj");
                seleccionarArea(area, vista);
            }
        );

        if (!areasOrdenadas.isEmpty()) {
            seleccionarArea(areasOrdenadas.get(0), vista);
        }
    }

    private void seleccionarArea(Area area, ReporteEstadoAreas vista) {
        double porcentaje = sistema.porcentajeArea(area);
        vista.mostrarAreaSeleccionada(area, porcentaje);

        List<Empleado> empleados = sistema.getEmpleadosOrdenados(area);

        vista.cargarEmpleados(
            empleados,
            e -> {
                Empleado emp = (Empleado)((javax.swing.JButton)e.getSource()).getClientProperty("obj");
                vista.mostrarPopupEmpleado(emp);
            }
        );
    }
}