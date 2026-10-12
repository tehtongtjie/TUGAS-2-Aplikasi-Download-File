import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    // Titik awal program: mengatur tema lalu menampilkan jendela GUI
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // pakai tampilan bawaan Java
        }

        SwingUtilities.invokeLater(() -> new DownloadFileGUI().setVisible(true));
    }
}