/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package controller;

import model.Area;
import model.Sistema;
import model.Empleado;
import view.Principal;
import view.AltaArea;
import view.BajaArea;
import view.ModificacionArea;
import view.MovimientoArea;
import javax.swing.*;
import java.util.Comparator;
import java.util.ArrayList;
import javax.swing.event.ListSelectionListener;

public class ControladorArea {
    private final Sistema sistema;
    private final Principal principal;
    
    public ControladorArea(Principal principal) {
        this.principal = principal;
        this.sistema = Sistema.getInstancia();
        agregarMenuListeners();
    }    

    private void agregarMenuListeners() {
        principal.getAltaArea().addActionListener(e -> abrirAltaArea());
        principal.getBajaArea().addActionListener(e -> abrirBajaArea());
        principal.getModifiacionArea().addActionListener(e -> abrirModificacionArea());
        principal.getAreaMov().addActionListener(e -> abrirMovimientosArea());
    }

    
    // Alta de área -------------------------------------------------------------------------
    private void abrirAltaArea() {
        AltaArea vista = new AltaArea(principal, true);
        sistema.agregarObserver(vista);
        refrescarListaAlta(vista);

        vista.getBtnCrear().addActionListener(e -> crearArea(vista));
        vista.getBtnCerrar().addActionListener(e -> {
            sistema.quitarObserver(vista);
            vista.dispose();
        });

        vista.setVisible(true);
    }
    
    private void refrescarListaAlta(AltaArea vista) {
        ArrayList<Area> areas = sistema.getAreas();
        String[] nombres = new String[areas.size()];
        for (int i = 0; i < areas.size(); i++) {
            nombres[i] = areas.get(i).getNombre();
        }
        vista.getListAreasReg().setListData(nombres);
    }


    private void crearArea(AltaArea vistaAlta) {
        boolean ok = true;
        String nombre = vistaAlta.getTextNom().getText().trim();
        String descripcion = vistaAlta.getTextDescr().getText().trim();
        double presupuesto = 0.0;

        if (nombre.isEmpty() || descripcion.isEmpty() || vistaAlta.getTextPres().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vistaAlta, "Complete todos los campos.");
            ok = false;
        } else {
            try {
                presupuesto = Double.parseDouble(vistaAlta.getTextPres().getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vistaAlta, "Presupuesto inválido.");
                ok = false;
            }
        }

        if (ok) {
            boolean existe = false;
            for (Area a : sistema.getAreas()) {
                if (a.getNombre().equalsIgnoreCase(nombre)) {
                    existe = true;
                    break;
                }
            }
            if (existe) {
                JOptionPane.showMessageDialog(vistaAlta, "Ya existe un área con ese nombre.");
            } else {
                try {
                    sistema.crearArea(nombre, descripcion, presupuesto);
                    refrescarListaAlta(vistaAlta);
                    JOptionPane.showMessageDialog(vistaAlta, "Área creada correctamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vistaAlta, ex.getMessage());
                }
            }
        }
    }
    
    // Baja de área -------------------------------------------------------------------
    private void abrirBajaArea() {
        BajaArea vista = new BajaArea(principal, true);
        sistema.agregarObserver(vista);
        refrescarListaBaja(vista);

        vista.getBtnEliminar().addActionListener(e -> eliminarArea(vista));
        vista.getBtnCerrar().addActionListener(e -> {
            sistema.quitarObserver(vista);
            vista.dispose();
        });

        vista.setVisible(true);
    }
    
    private void refrescarListaBaja(BajaArea vista) {
        java.util.List<Area> lista = sistema.getAreas();
        java.util.List<String> nombres = new ArrayList<>();
        for (Area a : lista) {
            if (a.getEmpleados().isEmpty()) {
                nombres.add(a.getNombre());
            }
        }
        vista.getListSinEmpl().setListData(nombres.toArray(new String[0]));
    }

    private void eliminarArea(BajaArea vista) {
        String seleccionado = vista.getListSinEmpl().getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione un área para eliminar.");
        } else {
            Area a = sistema.buscarAreaPorNombre(seleccionado);
            if (a == null) {
                JOptionPane.showMessageDialog(vista, "Área no encontrada.");
            } else {
                int confirm = JOptionPane.showConfirmDialog(vista,
                        "¿Está seguro que desea eliminar el área " + a.getNombre() + "?",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    sistema.eliminarArea(a); 
                    refrescarListaBaja(vista);
                    sistema.notificarObservers();
                    JOptionPane.showMessageDialog(vista, "Área eliminada.");
                }
            }
        }
    }
    
    // Modificación de área ----------------------------------------------------------------------------
    private void abrirModificacionArea() {
        ModificacionArea vista = new ModificacionArea(principal, true);
        sistema.agregarObserver(vista);
        refrescarListaModificacion(vista);

        
        ListSelectionListener selListener = e -> cargarDatosAreaSeleccionada(vista);
        vista.getListAreas().addListSelectionListener(selListener);

        vista.getBtnGuardar().addActionListener(e -> guardarCambiosArea(vista));
        vista.getBtnCerrar().addActionListener(e -> {
            sistema.quitarObserver(vista);
            vista.dispose();
        });

        vista.setVisible(true);
    }

    private void refrescarListaModificacion(ModificacionArea vista) {
        java.util.List<Area> lista = sistema.getAreas();
        String[] nombres = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            nombres[i] = lista.get(i).getNombre();
        }
        vista.getListAreas().setListData(nombres);
    }

    private void cargarDatosAreaSeleccionada(ModificacionArea vista) {
        String seleccionado = vista.getListAreas().getSelectedValue();
        if (seleccionado != null) {
            Area a = sistema.buscarAreaPorNombre(seleccionado);
            if (a != null) {
                vista.getTextNom().setText(a.getNombre());
                vista.getTextDescr().setText(a.getDescripcion());
                vista.getTextPres().setText(String.valueOf(a.getPresupuesto()));
            }
        }
    }

    private void guardarCambiosArea(ModificacionArea vista) {
        String seleccionado = vista.getListAreas().getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione un área.");
        } else {
            Area a = sistema.buscarAreaPorNombre(seleccionado);
            if (a == null) {
                JOptionPane.showMessageDialog(vista, "Área no encontrada.");
            } else {
                String nuevaDescr = vista.getTextDescr().getText().trim();
                if (nuevaDescr.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "La descripción no puede estar vacía.");
                } else {
                    a.setDescripcion(nuevaDescr);
                    sistema.notificarObservers();
                    JOptionPane.showMessageDialog(vista, "Cambios guardados.");
                }
            }
        }
    }
    
    // Movimiento de área -----------------------------------------------------------------------------------------------
    private void abrirMovimientosArea() {
       
        MovimientoArea vista = new MovimientoArea(principal, true);
        sistema.agregarObserver(vista);

        
        java.util.List<Area> lista = sistema.getAreas();
        String[] nombres = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            nombres[i] = lista.get(i).getNombre();
        }
        vista.getCmbOrigen().setModel(new javax.swing.DefaultComboBoxModel<>(nombres));

        
        vista.getCmbOrigen().addActionListener(e -> refrescarEmpleadosParaOrigen(vista));

        vista.getBtnMover().addActionListener(e -> realizarMovimiento(vista));
        vista.getBtnCerrar().addActionListener(e -> {
            sistema.quitarObserver(vista);
            vista.dispose();
        });

        vista.setVisible(true);
    }

    private void refrescarEmpleadosParaOrigen(MovimientoArea vista) {
        String origen = (String) vista.getCmbOrigen().getSelectedItem();
        if (origen != null) {
            Area a = sistema.buscarAreaPorNombre(origen);
            java.util.List<String> nombres = new ArrayList<>();
            if (a != null) {
                for (Empleado e : a.getEmpleados()) {
                    nombres.add(e.getNombre());
                }
            }
            vista.getListEmpleados().setListData(nombres.toArray(new String[0]));

            // populate destination areas (all except origin)
            java.util.List<Area> all = sistema.getAreas();
            java.util.List<String> dest = new ArrayList<>();
            for (Area aa : all) {
                if (!aa.getNombre().equalsIgnoreCase(origen)) dest.add(aa.getNombre());
            }
            vista.getCmbDestino().setModel(new javax.swing.DefaultComboBoxModel<>(dest.toArray(new String[0])));
        }
    }

    private void realizarMovimiento(MovimientoArea vista) {
        boolean ok = true;
        String origen = (String) vista.getCmbOrigen().getSelectedItem();
        String destino = (String) vista.getCmbDestino().getSelectedItem();
        String empleadoNombre = vista.getListEmpleados().getSelectedValue();
        int mesInicio = 1;

        if (origen == null || destino == null || empleadoNombre == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione origen, destino y empleado.");
            ok = false;
        } else {
            try {
                mesInicio = Integer.parseInt((String) vista.getCmbMes().getSelectedItem());
                if (mesInicio < 1 || mesInicio > 12) {
                    JOptionPane.showMessageDialog(vista, "Mes inválido.");
                    ok = false;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Mes inválido.");
                ok = false;
            }
        }

        if (ok) {
            Area aOrigen = sistema.buscarAreaPorNombre(origen);
            Area aDestino = sistema.buscarAreaPorNombre(destino);
            Empleado empleado = null;
            if (aOrigen != null) {
                for (Empleado e : aOrigen.getEmpleados()) {
                    if (e.getNombre().equalsIgnoreCase(empleadoNombre)) {
                        empleado = e;
                        break;
                    }
                }
            }

            if (aOrigen == null || aDestino == null || empleado == null) {
                JOptionPane.showMessageDialog(vista, "Datos inválidos, reintente.");
            } else {
                
                boolean puede = sistema.puedeMoverEmpleado(empleado, aDestino, mesInicio);
                if (!puede) {
                    JOptionPane.showMessageDialog(vista, "No hay presupuesto suficiente en el área destino.");
                } else {
                    try {
                        sistema.moverEmpleado(empleado, aOrigen, aDestino, mesInicio);
                        sistema.notificarObservers();
                        JOptionPane.showMessageDialog(vista, "Movimiento realizado.");
                        refrescarEmpleadosParaOrigen(vista); // update lists
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(vista, ex.getMessage());
                    }
                }
            }
        }
    }

}
