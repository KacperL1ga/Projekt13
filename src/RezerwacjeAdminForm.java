import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class RezerwacjeAdminForm extends JFrame {
    private JButton wyjscieButton;
    private JButton wyszukajButton;
    private JPanel RezerwacjaAdminSystem;
    private JTextField IndexTextField;
    private JButton usunButton;
    private JComboBox<String> WylotcomboBox;
    private JComboBox<String> PrzylotcomboBox;
    private JTextArea ListaTextArea;
    private String wylot, przylot;
    int id;
    private int width = 700, height = 600;

    public class DBConnection {
        private static final String URL =
                "jdbc:postgresql://localhost:5432/JumpFlight"; //nazwa_bazy
        private static final String USER = "postgres";
        private static final String PASSWORD = "admin";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    public void comboboxwylot() {
        String sql = "SELECT DISTINCT wylot FROM public.loty";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            WylotcomboBox.removeAllItems();
            WylotcomboBox.addItem("");
            while (rs.next()) {
                WylotcomboBox.addItem(rs.getString("wylot"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void comboboxprzylot(String wylot) {
        String sql = "SELECT DISTINCT przylot FROM public.loty WHERE wylot = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, wylot);
            try (ResultSet rs = pstmt.executeQuery()) {
                PrzylotcomboBox.removeAllItems();
                PrzylotcomboBox.addItem("");
                while (rs.next()) {
                    PrzylotcomboBox.addItem(rs.getString("przylot"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void wyswietlOsoby(String wylot, String przylot) {
        String sql = "SELECT p.id, p.imie, p.nazwisko, r.kod_rezerwacji " +
                "FROM public.rezerwacja r " +
                "JOIN public.pasazerowie p ON r.pasazerowie_id = p.id " +
                "JOIN public.loty l ON r.loty_id = l.id " +
                "WHERE l.wylot = ? AND l.przylot = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, wylot);
            pstmt.setString(2, przylot);
            try (ResultSet rs = pstmt.executeQuery()) {
                StringBuilder results = new StringBuilder();
                while (rs.next()) {
                    results.append("ID: ").append(rs.getInt("id")).append(" ");
                    results.append("    Imie: ").append(rs.getString("imie")).append(" ");
                    results.append("    Nazwisko: ").append(rs.getString("nazwisko")).append(" ");
                    results.append("    Kod rezerwacji: ").append(rs.getString("kod_rezerwacji")).append("\n");
                }
                ListaTextArea.setText(results.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UsuwaniePasazera(int id) {
        String deleteRezerwacjaSql = "DELETE FROM public.rezerwacja WHERE pasazerowie_id = ?";
        String deletePasazerSql = "DELETE FROM public.pasazerowie WHERE id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteRezerwacjaStmt = connection.prepareStatement(deleteRezerwacjaSql);
                 PreparedStatement deletePasazerStmt = connection.prepareStatement(deletePasazerSql)) {

                deleteRezerwacjaStmt.setInt(1, id);
                deleteRezerwacjaStmt.executeUpdate();

                deletePasazerStmt.setInt(1, id);
                int affectedRows = deletePasazerStmt.executeUpdate();

                if (affectedRows > 0) {
                    connection.commit();
                    JOptionPane.showMessageDialog(null, "Pasażer został pomyślnie usunięty.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    connection.rollback();
                    JOptionPane.showMessageDialog(null, "Nie znaleziono pasażera o podanym ID.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas usuwania pasażera.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public RezerwacjeAdminForm() {
        super("Modyfikcja rezerwacji lotow");
        this.setContentPane(this.RezerwacjaAdminSystem);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.setLocationRelativeTo(null);

        comboboxwylot();
        WylotcomboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedWylot = (String) WylotcomboBox.getSelectedItem();
                if (selectedWylot != null && !selectedWylot.isEmpty()) {
                    comboboxprzylot(selectedWylot);
                } else {
                    PrzylotcomboBox.removeAllItems();
                    PrzylotcomboBox.addItem("");
                }
            }
        });

        wyjscieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AdminSystem adminSystem = new AdminSystem();
                adminSystem.setVisible(true);
            }
        });

        wyszukajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wylot = (String) WylotcomboBox.getSelectedItem();
                przylot = (String) PrzylotcomboBox.getSelectedItem();

                if (wylot != null && przylot != null && !wylot.isEmpty() && !przylot.isEmpty()) {
                    wyswietlOsoby(wylot, przylot);
                } else {
                    JOptionPane.showMessageDialog(null, "Proszę wybrać miasta wylotu i przylotu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        usunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(IndexTextField.getText());
                    UsuwaniePasazera(id);
                    JOptionPane.showMessageDialog(null,"Pomyślnie usunięto rezerwacje pasazera.","Sukces",JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Proszę wprowadzić poprawne ID.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

}
