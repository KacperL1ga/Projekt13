import javax.swing.*;

public class WelcomeForm extends JFrame {
    private JProgressBar progressBar1;
    private JPanel WelcomePanel;
    private JLabel Label;
    private int width = 400, height = 400;

    public WelcomeForm() {
        super("Linie Lotnicze JumpFlight");
        this.setContentPane(this.WelcomePanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width,height);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        progression();
    }

    private void progression() {
        int counter = 0;
        while (counter <= 100) {
            Label.setText("Proszę czekać ...");
            progressBar1.setValue(counter);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter += 5;
        }
        dispose(); // Zamknięcie WelcomeForm
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true); // Otwarcie LoginForm
    }
}

