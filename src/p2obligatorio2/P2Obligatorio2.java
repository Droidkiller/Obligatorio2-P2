/*
 Aitana Alvarez 340201
 Francisco Bonanni 299134
 */
package p2obligatorio2;

import javax.swing.SwingUtilities;
import view.Bienvenida;

public class P2Obligatorio2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Bienvenida b = new Bienvenida();
            b.setVisible(true);
        });
    }
    
}
