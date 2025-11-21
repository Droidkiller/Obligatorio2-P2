/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package controller;

import model.*;
import view.AltaEmpleado;
import javax.swing.*;

public class ControladorEmpleado {
    private final Sistema sistema;
    private final AltaEmpleado vista;
    
    public ControladorEmpleado(AltaEmpleado vista) {
        this.vista = vista;
        this.sistema = Sistema.getInstancia();
        sistema.agregarObserver(vista);
        vista.setManagers(sistema.getManagers());
        vista.setAreas(sistema.getAreas());
        agregarListeners();
    }
    
    private void agregarListeners() {
        vista.getBtnCrear().addActionListener(e -> crearEmpleado());
        vista.getBtnCerrar().addActionListener(e -> {
            sistema.quitarObserver(vista);
            vista.dispose();
        });
    }
    
    private void crearEmpleado() {
        String errores = "";

        String nombre = vista.getNombre();
        if (nombre.isBlank()) 
            errores += "El nombre no puede estar vacío.\n";

        Manager manager = vista.getManagerSeleccionado();
        if (manager == null) 
            errores += "Seleccione un manager.\n";

        Area area = vista.getAreaSeleccionada();
        if (area == null) 
            errores += "Seleccione un área.\n";

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
        } else {
            String cvTexto = vista.getCvTexto();
            try {
                sistema.crearEmpleado(nombre, cedula, celular, salario, cvTexto, manager, area);
                JOptionPane.showMessageDialog(vista, "Empleado creado exitosamente.");
                vista.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, ex.getMessage());
            }
        }
    }
}
