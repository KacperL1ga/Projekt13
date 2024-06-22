import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class UserSystem extends JFrame{
    private JPanel UserSystem;
    private JComboBox comboBox1;
    private JButton wylogujButton;
    private JButton dalejButton;
    private JTextField wylotTextField;
    private JButton wyszukajButton;
    private JTextArea Wyniki;
    private JTextField dataOdtextField;
    private JTextField cenaOdtextField;
    private JTextField dataDotextField;
    private JTextField cenaDotextField;
    private JButton wyczyscButton;
    private JCheckBox wolneMiejscaCheckBox;
    private JScrollPane scrollPane;
    private JTextField celTextField;
    private int width = 1000, height = 600;
    private String dataOd, cel, wylot, dataDo,wylotwybor;
    private int cenaOd, cenaDo;

    public class DBConnection {
        private static final String URL =
                "jdbc:postgresql://localhost:5432/JumpFlight"; //nazwa_bazy
        private static final String USER = "postgres";
        private static final String PASSWORD = "admin";
        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }
    public void WyszukajOsoby(String wylot, String dataOd, String dataDo, Integer cenaOd, Integer cenaDo, JTextArea Wyniki) {
        StringBuilder sql = new StringBuilder("SELECT * FROM public.loty WHERE ");

        if (wylot != null && !wylot.isEmpty()) {
            sql.append("wylot = '").append(wylot).append("' AND ");
        }
        if(wolneMiejscaCheckBox.isSelected()){
            sql.append("wolne_miejsca > 0").append(" AND ");
        }

        sql.append("data BETWEEN '").append(dataOd).append("' AND '").append(dataDo)
                .append("' AND cena BETWEEN ").append(cenaOd).append(" AND ").append(cenaDo);

        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {
            StringBuilder results = new StringBuilder();
            while (rs.next()) {
                results.append("    ID: ").append(rs.getInt("id")).append(", ");
                results.append("    Wylot: ").append(rs.getString("wylot")).append(", ");
                results.append("    Przylot: ").append(rs.getString("Przylot")).append(", ");
                results.append("    Data: ").append(rs.getDate("data")).append(", ");
                results.append("    Czas: ").append(rs.getString("czas")).append(", ");
                results.append("    Wolne miejsca: ").append(rs.getString("wolne_miejsca")).append(", ");
                results.append("    Cena: ").append(rs.getDouble("cena")).append("\n");
            }
            Wyniki.setText(results.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void Wypiszloty (JTextArea Wyniki) {
        String sql = "SELECT * FROM public.loty";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            StringBuilder results = new StringBuilder();
            while (rs.next()) {
                results.append("    ID: ").append(rs.getInt("id")).append(", ");
                results.append("    Wylot: ").append(rs.getString("wylot")).append(", ");
                results.append("    Przylot: ").append(rs.getString("Przylot")).append(", ");
                results.append("    Data: ").append(rs.getDate("data")).append(", ");
                results.append("    Czas: ").append(rs.getString("czas")).append(", ");
                results.append("    Wolne miejsca: ").append(rs.getString("wolne_miejsca")).append(", ");
                results.append("    Cena: ").append(rs.getDouble("cena")).append("\n");
            }
            Wyniki.setText(results.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void wylotyComboBox() {
        String sql = "SELECT DISTINCT wylot FROM public.loty";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            comboBox1.removeAllItems();
            comboBox1.addItem("");
            while (rs.next()) {
                comboBox1.addItem(rs.getString("wylot"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public UserSystem(){
        super("Wyszukiwanie lotow");
        this.setContentPane(this.UserSystem);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width,height);
        this.setLocationRelativeTo(null);

        wylotyComboBox();
        Wypiszloty(Wyniki);

        wyszukajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataOd = dataOdtextField.getText();
                dataDo = dataDotextField.getText();
                cenaOd = Integer.parseInt(cenaOdtextField.getText());
                cenaDo = Integer.parseInt(cenaDotextField.getText());
                wylot = comboBox1.getItemAt(comboBox1.getSelectedIndex()).toString();

                WyszukajOsoby(wylot,dataOd,dataDo,cenaOd,cenaDo,Wyniki);

            }
        });

        wylogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            }
        });

        wyczyscButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataOdtextField.setText("2024-01-01");
                dataDotextField.setText("2024-12-31");
                cenaOdtextField.setText("0");
                cenaDotextField.setText("9999");
                comboBox1.setSelectedIndex(0);
                Wypiszloty(Wyniki);
                wolneMiejscaCheckBox.setSelected(false);
            }
        });
        dalejButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cel = celTextField.getText();
                wylotwybor = wylotTextField.getText();
                if(cel == null || cel.trim().isEmpty() && wylotwybor == null || wylotwybor.trim().isEmpty()){
                    JOptionPane.showMessageDialog(UserSystem.this,"Nie podales miejsca dolecowego/startowego","Uwaga!",JOptionPane.WARNING_MESSAGE);
                }
                else {
                    dispose();
                    RezerwacjaForm rezerwacjaForm = new RezerwacjaForm(cel,wylotwybor);
                    rezerwacjaForm.setVisible(true);
                }
            }
        });
    }


}
