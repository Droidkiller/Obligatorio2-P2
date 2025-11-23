/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package controller;

import model.*;
import view.AltaManagers;
import view.BajaManager;
import view.ModificarManager;
import view.Principal;
import javax.swing.*;

public class ControladorManager {
    private final Sistema sistema;
    private final Principal principal;

    public ControladorManager(Principal principal) {
        this.principal = principal;
        this.sistema = Sistema.getInstancia();
        agregarMenuListeners();
    }
    
    private void agregarMenuListeners() {
        principal.getAltaMan().addActionListener(e -> abrirAltaManager());
        principal.getBajaMan().addActionListener(e -> abrirBajaManager());
        principal.getModificacionMan().addActionListener(e -> abrirModificacionManager());
    }
    
    private void abrirAltaManager() {
        AltaManagers vistaAlta = new AltaManagers(principal, false);        
        vistaAlta.setListaManagers(sistema.getManagers());
        vistaAlta.getBtnCrear().addActionListener(e -> crearManager(vistaAlta));
        vistaAlta.getBtnCerrar().addActionListener(e -> {
            sistema.quitarObserver(vistaAlta);
            vistaAlta.dispose();
        });
        vistaAlta.setVisible(true);
        sistema.agregarObserver(vistaAlta);
    }

    private void abrirBajaManager() {
        BajaManager vistaBaja = new BajaManager(principal, false);
        var managersSinEmp = sistema.getManagersSinEmpleados();
        String[] nombres = new String[managersSinEmp.size()];
        for (int i = 0; i < managersSinEmp.size(); i++) {
            nombres[i] = managersSinEmp.get(i).getNombre();
        }
        vistaBaja.getListManSinEmp().setListData(nombres);
        vistaBaja.getBtnEliminar().addActionListener(e -> eliminarManager(vistaBaja));
        vistaBaja.getBtnCerrar().addActionListener(e -> {
            sistema.quitarObserver(vistaBaja);
            vistaBaja.dispose();
        });
        vistaBaja.setVisible(true);
        sistema.agregarObserver(vistaBaja);
    }

    private void abrirModificacionManager() {
        ModificarManager vistaMod = new ModificarManager(principal, false);
        vistaMod.setVisible(true);
        vistaMod.setListaManagers(sistema.getManagers());
        vistaMod.getListMan().addListSelectionListener(e -> llenarCamposManagerSeleccionado(vistaMod));
        vistaMod.getBtnGuardar().addActionListener(e -> guardarCelularManager(vistaMod));
        vistaMod.getBtnCerrar().addActionListener(e -> {
            sistema.quitarObserver(vistaMod);
            vistaMod.dispose();
        });
        sistema.agregarObserver(vistaMod);
    }
    
    private void llenarCamposManagerSeleccionado(ModificarManager vistaMod) {
        int index = vistaMod.getListMan().getSelectedIndex();
        if (index >= 0) {
            Manager m = sistema.getManagers().get(index);
            vistaMod.setNombre(m.getNombre());
            vistaMod.setCedula(String.valueOf(m.getCedula()));
            vistaMod.setAntiguedad(String.valueOf(m.getAntiguedad()));
            vistaMod.setCelular(String.valueOf(m.getCelular()));
            vistaMod.setCantidadEmpleados(String.valueOf(m.getEmpleados().size()));
        }
    }
    
    private void guardarCelularManager(ModificarManager vistaMod) {
        int index = vistaMod.getListMan().getSelectedIndex();
        if (index != -1) {
            try {
                int nuevoCel = Integer.parseInt(vistaMod.getCelular());
                Manager m = sistema.getManagers().get(index);
                sistema.modificarCelularManager(m, nuevoCel);
                JOptionPane.showMessageDialog(vistaMod, "Celular actualizado.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vistaMod, "Ingrese un número válido para el celular.");
            }
        } else {
            JOptionPane.showMessageDialog(vistaMod, "Seleccione un manager.");
        }
    }
    
    private void crearManager(AltaManagers vistaAlta) {
        String nombre = vistaAlta.getNombre();
        int cedula, antiguedad, celular;

        try {
            cedula = vistaAlta.getCedula();
            antiguedad = vistaAlta.getAntiguedad();
            celular = vistaAlta.getCelular();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vistaAlta, "Ingrese números válidos para cédula, antigüedad y celular.");
            return;
        }

        try {
            sistema.crearManager(nombre, cedula, antiguedad, celular);
            vistaAlta.setListaManagers(sistema.getManagers());
            JOptionPane.showMessageDialog(vistaAlta, "Manager creado exitosamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaAlta, ex.getMessage());
        }
    }
    
    private void eliminarManager(BajaManager vistaBaja) {
        int index = vistaBaja.getListManSinEmp().getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(vistaBaja, "Seleccione un manager para eliminar.");
            return;
        }

        Manager managerAEliminar = sistema.getManagersSinEmpleados().get(index);
        int confirm = JOptionPane.showConfirmDialog(
            vistaBaja,
            "¿Está seguro que desea eliminar al manager " + managerAEliminar.getNombre() + "?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            sistema.eliminarManager(managerAEliminar);
            // refresh list
            vistaBaja.getListManSinEmp().setListData(
                sistema.getManagersSinEmpleados().stream().map(Manager::getNombre).toArray(String[]::new)
            );
            JOptionPane.showMessageDialog(vistaBaja, "Manager eliminado correctamente.");
        }
    }
}
