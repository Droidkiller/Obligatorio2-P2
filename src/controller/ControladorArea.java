/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package controller;

import model.Area;
import model.Sistema;
import view.AltaArea;
import javax.swing.*;
import java.util.Comparator;

public class ControladorArea {
    private final Sistema sistema;
    private final AltaArea vistaAlta;

    public ControladorArea(AltaArea vistaAlta) {
        this.vistaAlta = vistaAlta;
        this.sistema = Sistema.getInstancia();
        inicializar();
    }

    private void inicializar() {
        refrescarLista();
        vistaAlta.getBtnCrear().addActionListener(e -> crearArea());
        vistaAlta.getBtnCerrar().addActionListener(e -> vistaAlta.dispose());
    }

    private void refrescarLista() {
        // ordenar por nombre y mostrar en JList
        sistema.getAreas().sort(Comparator.comparing(Area::getNombre));
        String[] nombres = sistema.getAreas().stream()
                                  .map(Area::getNombre)
                                  .toArray(String[]::new);
        vistaAlta.getListAreasReg().setListData(nombres);
    }

    private void crearArea() {
        String nombre = vistaAlta.getTextNom().getText().trim();
        String descripcion = vistaAlta.getTextDescr().getText().trim();
        double presupuesto;

        if (nombre.isEmpty() || descripcion.isEmpty() || vistaAlta.getTextPres().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vistaAlta, "Complete todos los campos.");
            return;
        }

        try {
            presupuesto = Double.parseDouble(vistaAlta.getTextPres().getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vistaAlta, "Presupuesto inválido.");
            return;
        }

        try {
            sistema.crearArea(nombre, descripcion, presupuesto); // <-- pass the values directly
            refrescarLista();
            JOptionPane.showMessageDialog(vistaAlta, "Área creada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaAlta, ex.getMessage());
        }
    }
}
