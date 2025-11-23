/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JButton;

import model.Area;
import model.Empleado;
import model.Sistema;
import view.ReporteEstadoAreas;

public class ControladorReporteEstadoAreas implements ActionListener {

    private final ReporteEstadoAreas vista;
    private final Sistema sistema = Sistema.getInstancia();

    private List<Area> areasOrdenadas;
    private Area areaSeleccionada;

    public ControladorReporteEstadoAreas(ReporteEstadoAreas vista) {
        this.vista = vista;
        cargarAreas();
    }
    /*Carga de areas*/
    private void cargarAreas() {
        areasOrdenadas = sistema.getAreas();

        // Ordenar por porcentaje de presupuesto usado
        Collections.sort(areasOrdenadas, (a1, a2) -> {
            double p1 = sistema.porcentajeArea(a1);
            double p2 = sistema.porcentajeArea(a2);
            return Double.compare(p2, p1);
        });

        vista.cargarListadoAreas(areasOrdenadas, this);

        if (!areasOrdenadas.isEmpty()) {
            seleccionarArea(areasOrdenadas.get(0));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        Object obj = src.getClientProperty("obj");

        if (obj instanceof Area) {
            seleccionarArea((Area) obj);
        }

        if (obj instanceof Empleado) {
            mostrarEmpleado((Empleado) obj);
        }
    }

  /*Seleccion de area*/
    private void seleccionarArea(Area area) {
        this.areaSeleccionada = area;

        double porcentaje = sistema.porcentajeArea(area);

        vista.mostrarAreaSeleccionada(area, porcentaje);

        List<Empleado> empleados = sistema.getEmpleadosOrdenados(area);

        vista.cargarEmpleados(empleados, this);
    }

    // ---------------------------------------------
    // INFO EMPLEADO
    // ---------------------------------------------
    private void mostrarEmpleado(Empleado e) {
        vista.mostrarPopupEmpleado(e);
    }
}
