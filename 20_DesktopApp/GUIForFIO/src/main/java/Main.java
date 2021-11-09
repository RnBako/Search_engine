import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frameFIO = new JFrame();
        frameFIO.setSize(600, 200);
        frameFIO.setResizable(false);
        frameFIO.setLocationRelativeTo(null);
        frameFIO.add(new FIOForm().getFIOPanel());
        frameFIO.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frameFIO.setVisible(true);
    }
}
