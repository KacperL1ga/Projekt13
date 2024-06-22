import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JPanel PanelLogin;
    private JPasswordField Userpassword;
    private JTextField userName;
    private JButton wyjscieButton;
    private JButton zalogujButton;
    private int width = 400, height = 400;
    String user1 = "admin", password1 = "admin";
    String user2 = "user", password2 = "user";


    public LoginForm(){
        super("Logowanie do systemu");
        this.setContentPane(this.PanelLogin);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width,height);
        this.setLocationRelativeTo(null);


        zalogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String userNameInput = userName.getText();
                String userPasswordInput = new String(Userpassword.getPassword());

                if (userNameInput.equals(user1) && userPasswordInput.equals(password1)){
                    dispose();
                    AdminSystem Adminsystem = new AdminSystem();
                    Adminsystem.setVisible(true);
                } else if (userNameInput.equals(user2) && userPasswordInput.equals(password2)) {
                    dispose();
                    UserSystem usersystem = new UserSystem();
                    usersystem.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null,"Podano błedne dane",
                            "Błąd logowania", JOptionPane.ERROR_MESSAGE);
                    userName.setText("");
                    Userpassword.setText("");
                }
            }
        });
        wyjscieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {dispose();}
        });
    }
}

