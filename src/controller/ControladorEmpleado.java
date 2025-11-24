/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package controller;

import model.*;
import view.AltaEmpleado;
import view.Principal;
import javax.swing.*;

public class ControladorEmpleado {
    private final Sistema sistema;
    private final Principal principal;
    
    public ControladorEmpleado(Principal principal) {
        this.principal = principal;
        this.sistema = Sistema.getInstancia();
        agregarMenuListeners();
    }

    private void agregarMenuListeners() {
        principal.getAltaEmp().addActionListener(e -> abrirAltaEmpleado());
    }

    private void abrirAltaEmpleado() {
        AltaEmpleado vista = new AltaEmpleado(principal, false);

        sistema.agregarObserver(vista);
        vista.setManagers(sistema.getManagers());
        vista.setAreas(sistema.getAreas());

        vista.getBtnCrear().addActionListener(e -> crearEmpleado(vista));
        vista.getBtnCerrar().addActionListener(e -> {
            sistema.quitarObserver(vista);
            vista.dispose();
        });
        
        vista.actualizar();
        vista.setVisible(true);
    }
    
    private void crearEmpleado(AltaEmpleado vista) {
        String errores = "";

        String nombre = vista.getNombre();
        if (nombre.isBlank()) errores += "El nombre no puede estar vacío.\n";

        Manager manager = vista.getManagerSeleccionado();
        if (manager == null) errores += "Seleccione un manager.\n";

        Area area = vista.getAreaSeleccionada();
        if (area == null) errores += "Seleccione un área.\n";

        int cedula = 0, celular = 0;
        double salario = 0;

        try {
            cedula = vista.getCedula();
            celular = vista.getCelular();
            salario = vista.getSalario();
        } catch (NumberFormatException ex) {
            errores += "Cédula, celular o salario inválidos.\n";
        }

        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(vista, errores);
            return;
        }

        try {
            sistema.crearEmpleado(nombre, cedula, celular, salario, vista.getCvTexto(), manager, area);
            JOptionPane.showMessageDialog(vista, "Empleado creado exitosamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage());
        }
    }
}
