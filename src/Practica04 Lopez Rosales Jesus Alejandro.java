import javax.swing.*;

class Paint2 extends JFrame {
    private JPanel panel;

    public Paint2() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setLocationRelativeTo(null);
        setSize(800, 600);
        setResizable(false);
        setTitle("Paint 2.0");
        panel = new JPanel();
        setContentPane(panel);
    }

    public static void main(String[] args) {
        Paint2 ventana = new Paint2();
        ventana.setVisible(true);
    }
}
