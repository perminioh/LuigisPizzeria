
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            PizzeriaGUI gui = new PizzeriaGUI();

            gui.setVisible(true);
        });
    }
}
