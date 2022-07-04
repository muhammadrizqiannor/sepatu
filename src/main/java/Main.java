import frame.SepatuViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
        Koneksi.getConnection();
        SepatuViewFrame viewFrame = new SepatuViewFrame();
        viewFrame.setVisible(true);
    }
}
