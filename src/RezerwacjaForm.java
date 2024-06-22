import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class RezerwacjaForm extends JFrame{
    private JPanel RezerwacjaSystem;
    private JButton wyjscieButton;
    private JButton dalejButton;
    private JTextArea lot;
    private JCheckBox UbezpieczeniecheckBox2;
    private JTextField Bilety;
    private int width = 800, height = 600;
    private int ilosc, cena,total, idlotu, iloscwolnych;

    public class DBConnection {
        private static final String URL =
                "jdbc:postgresql://localhost:5432/JumpFlight"; //nazwa_bazy
        private static final String USER = "postgres";
        private static final String PASSWORD = "admin";
        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    public void wybranyCel(String cel,JTextArea lot,String wylotwybor) {
        String sql = "SELECT * FROM public.loty WHERE przylot = '" + cel + "' AND wylot = '" +wylotwybor+ "'";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            StringBuilder results = new StringBuilder();
            while (rs.next()) {
                results.append("ID: ").append(rs.getInt("id")).append(", ");
                results.append("    Wylot: ").append(rs.getString("wylot")).append(", ");
                results.append("    Przylot: ").append(rs.getString("Przylot")).append(", ");
                results.append("    Data: ").append(rs.getDate("data")).append(", ");
                results.append("    Czas: ").append(rs.getString("czas")).append(", ");
                results.append("    Wolne miejsca: ").append(rs.getString("wolne_miejsca")).append(", ");
                results.append("    Cena: ").append(rs.getDouble("cena")).append("\n");
                cena = rs.getInt("cena");
                idlotu =rs.getInt("id");
                iloscwolnych =rs.getInt("wolne_miejsca");

            }
            lot.setText(results.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RezerwacjaForm(String cel, String wylotwybor){
        super("Logowanie do systemu");
        this.setContentPane(this.RezerwacjaSystem);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width,height);
        this.setLocationRelativeTo(null);

        wybranyCel(cel,lot,wylotwybor);
        if (lot.getText().trim().isEmpty()) {
            lot.setText("Prawdopodobnie zle wpisales miejsce wylotu/odlotu\nWroc do poprzedniej strony i wprowadz dane poprawnie");
        }
        lot.setMargin(new Insets(0, 10, 0, 10));
        wyjscieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserSystem userSystem = new UserSystem();
                userSystem.setVisible(true);
            }
        });
        dalejButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ilosc = Integer.parseInt(Bilety.getText());
                total = cena * ilosc;
                if (iloscwolnych < ilosc){
                    JOptionPane.showMessageDialog(RezerwacjaForm.this,"W tym samolocie nie ma wystarczającej ilości miejsc\nProsimy wybrac inny lot lub zarezerwować mniejszą liczbe biletow","Uwaga!",JOptionPane.WARNING_MESSAGE);
                    dispose();
                    UserSystem userSystem = new UserSystem();
                    userSystem.setVisible(true);
                }
                else if (UbezpieczeniecheckBox2.isSelected() && ilosc>0) {
                    total = total + 150 * ilosc;
                    dispose();
                    DaneRezerwacjaForm daneRezerwacjaForm = new DaneRezerwacjaForm(ilosc,total,idlotu,cena);
                    daneRezerwacjaForm.setVisible(true);
                } else if (ilosc==0) {
                    JOptionPane.showMessageDialog(RezerwacjaForm.this,"Nie podales ilosci biletow","Uwaga!",JOptionPane.WARNING_MESSAGE);
                } else if (!UbezpieczeniecheckBox2.isSelected() && ilosc>0) {
                    dispose();
                    DaneRezerwacjaForm daneRezerwacjaForm = new DaneRezerwacjaForm(ilosc,total,idlotu, cena);
                    daneRezerwacjaForm.setVisible(true);
                }

            }
        });

    }
}

