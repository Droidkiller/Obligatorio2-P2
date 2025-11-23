/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package controller;

import com.google.gson.Gson;
import model.Area;
import model.Empleado;
import model.Sistema;
import model.archivos.ArchivoLectura;
import view.Principal;
import view.ReporteInteligente;
import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ControladorReporteInteligente {
    private final Principal principal;
    private final Sistema sistema;
    private final Gson gson;

    public ControladorReporteInteligente(Principal principal) {
        this.principal = principal;
        this.sistema = Sistema.getInstancia();
        this.gson = new Gson();
        agregarMenuListeners();
    }

    private void agregarMenuListeners() {
        principal.getInteligente().addActionListener(e -> abrirReporte());
    }

    private void abrirReporte() {
        ReporteInteligente vista = new ReporteInteligente(principal, false);

        sistema.agregarObserver(vista);

        vista.getBoxOrigen().addActionListener(e -> vista.cargarEmpleadosSegunOrigen());
        vista.getBtnGenerar().addActionListener(e -> generarReporte(vista));
        vista.getBtnCerrar().addActionListener(e -> {
            sistema.quitarObserver(vista);
            vista.dispose();
        });

        vista.actualizar();
        vista.setVisible(true);
    }

    private void generarReporte(ReporteInteligente vista) {
        String nombreOrigen = (String) vista.getBoxOrigen().getSelectedItem();
        String nombreDestino = (String) vista.getBoxDestino().getSelectedItem();
        String nombreEmpleado = (String) vista.getBoxEmpleado().getSelectedItem();

        if (nombreOrigen == null || nombreDestino == null || nombreEmpleado == null) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar área origen, área destino y empleado.");
            return;
        }

        Area origen = sistema.getAreas().stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombreOrigen))
                .findFirst().orElse(null);

        Area destino = sistema.getAreas().stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombreDestino))
                .findFirst().orElse(null);

        if (origen == null || destino == null) {
            JOptionPane.showMessageDialog(vista, "No se encontraron las áreas seleccionadas.");
            return;
        }

        Empleado empleado = sistema.getEmpleados().stream()
                .filter(e -> e.getNombre().equalsIgnoreCase(nombreEmpleado))
                .findFirst().orElse(null);

        if (empleado == null) {
            JOptionPane.showMessageDialog(vista, "No se encontró el empleado seleccionado.");
            return;
        }

        String cv = leerCV(empleado.getCedula());
        String prompt = construirPrompt(origen, destino, empleado, cv);

        vista.setIconoCargando();

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                return llamarGemini(prompt);
            }

            @Override
            protected void done() {
                try {
                    String resultado = get();
                    vista.mostrarResultado(resultado);
                    vista.setIconoExito();
                } catch (Exception ex) {
                    vista.setIconoError();
                    JOptionPane.showMessageDialog(vista, "Error generando reporte: " + ex.getMessage());
                }
            }
        };

        worker.execute();
    }
    
    private String llamarGemini(String prompt) throws Exception {
        String apiKey = System.getenv("ERP_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new Exception("Variable de entorno ERP_API_KEY no definida.");
        }

        URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        String body = """
        {
          "contents": [{
            "parts": [{ "text": "%s" }]
          }]
        }
        """.formatted(escapeJson(prompt));

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes("UTF-8"));
        }

        // Leer respuesta
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        }

        GeminiResponse resp = gson.fromJson(sb.toString(), GeminiResponse.class);

        return resp.candidates[0].content.parts[0].text;
    }
    
    private String construirPrompt(Area origen, Area destino, Empleado emp, String cv) {
        return "Genera un reporte profesional, claro y estructurado analizando si el empleado "
                + emp.getNombre()
                + " debería transferirse desde el área " + origen.getNombre()
                + " hacia el área " + destino.getNombre() + ".\n\n"
                + "Descripción del área de origen:\n" + origen.getDescripcion() + "\n\n"
                + "Descripción del área de destino:\n" + destino.getDescripcion() + "\n\n"
                + "CV del empleado:\n" + cv + "\n\n"
                + "Incluye ventajas, desventajas y una recomendación.";
    }
    
    private String leerCV(int cedula) {
        String ruta = "cvs/CV" + cedula + ".txt";
        ArchivoLectura arch = new ArchivoLectura(ruta);

        StringBuilder sb = new StringBuilder();
        while (arch.hayMasLineas()) {
            sb.append(arch.linea()).append("\n");
        }
        arch.cerrar();
        return sb.toString();
    }
    
    private String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
    
    // ----- Clases para Gemini -----
    private static class Part {
        String text;        
    }

    private static class GeminiResponse {
        Candidate[] candidates;
    }

    private static class Candidate {
        Content content;
    }

    private static class Content {
        Part[] parts;
    }
}
