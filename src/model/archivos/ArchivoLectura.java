/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */

package model.archivos;

import java.io.*; 
import java.nio.file.Paths; 
import java.util.Scanner;

public class ArchivoLectura {
    private Scanner in; 
    private String linea;
    
    public ArchivoLectura(String unNombre) { 
        try { 
            in = new Scanner(Paths.get(unNombre)); 
        } 
        catch (IOException e) { 
            System.err.println("Error "); 
            System.exit(1); 
        } 
    }
    
    public boolean hayMasLineas() { 
        boolean hay = false; 
        linea = null; 
        if (in.hasNext()){ 
            linea = in.nextLine(); 
            hay = true; 
        } 
        return hay; 
    }
    
    public String linea() { 
        // devuelve la última linea leida return linea;
        return linea;
    } 
    
    public void cerrar() { 
        in.close(); 
    } 
}
