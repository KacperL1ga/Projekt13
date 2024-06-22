import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.Random;

public class DaneRezerwacjaForm extends JFrame{
    private JTextField imieTextField;
    private JTextField nazwiskoTextField;
    private JButton wyjscieButton;
    private JButton rezerwujButton;
    private JTextField emailTextField;
    private JPanel DaneSystem;
    private JButton losojButton;
    private JLabel Kod;
    private JButton wyczyscButton;
    private JLabel DaneBilet;
    private int width = 700, height = 600;
    String cel,imie, nazwisko, email, wylotwybor;
    int ilosc, total, idlotu, kodRezerwacji, cena, ilekupionych;

    public class DBConnection {
        private static final String URL =
                "jdbc:postgresql://localhost:5432/JumpFlight"; //nazwa_bazy
        private static final String USER = "postgres";
        private static final String PASSWORD = "admin";
        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    public void dodajPasazera(String imie, String nazwisko, String email) {
        String sql = "INSERT INTO public.pasazerowie (imie, nazwisko, email) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, imie);
            pstmt.setString(2, nazwisko);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Użytkownik dodany pomyślnie.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas dodawania użytkownika.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void dodajRezerwacje(int idLotu, int pasazerId, int kodRezerwacji) {
        String sql = "INSERT INTO public.rezerwacja (loty_id, pasazerowie_id, kod_rezerwacji) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idLotu);
            pstmt.setInt(2, pasazerId);
            pstmt.setInt(3, kodRezerwacji);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas dodawania rezerwacji.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void MiejscaUpdate(Integer ilekupionych, Integer idlotu){
        String sql = "UPDATE loty SET wolne_miejsca = wolne_miejsca - ? Where id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, ilekupionych);
            pstmt.setInt(2, idlotu);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas aktualizacji miejsc.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Integer getPasazerId(String imie, String nazwisko) {
        String sql = "SELECT id FROM public.pasazerowie WHERE imie = ? AND nazwisko = ?";
        Integer pasazerId = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, imie);
            pstmt.setString(2, nazwisko);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                pasazerId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pasazerId;
    }

    public DaneRezerwacjaForm(Integer ilosc, Integer total, Integer idlotu, Integer cena){
        super("Dane osoby rezerwujacej bilet");
        this.setContentPane(this.DaneSystem);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width,height);
        this.setLocationRelativeTo(null);

        DaneBilet.setText("Wprowadz dane do rezerwacji lotu (ilosc biletow do wypelniena danych: " + ilosc +" )");
        this.ilosc = ilosc;
        this.ilekupionych = ilosc;
        this.idlotu = idlotu;
                wyjscieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                RezerwacjaForm rezerwacjaForm = new RezerwacjaForm(cel,wylotwybor);
                rezerwacjaForm.setVisible(true);
            }
        });;


        rezerwujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imie = imieTextField.getText();
                nazwisko = nazwiskoTextField.getText();
                email = emailTextField.getText();

                if (DaneRezerwacjaForm.this.ilosc > 0) {
                    if (kodRezerwacji != 0) {
                        dodajPasazera(imie, nazwisko, email);
                        Integer pasazerId = getPasazerId(imie, nazwisko);
                        dodajRezerwacje(idlotu, pasazerId, kodRezerwacji);
                        Kod.setText("Twoj kod rezerwacji");
                        kodRezerwacji = 0;
                        DaneRezerwacjaForm.this.ilosc--;
                        DaneBilet.setText("Wprowadz dane do rezerwacji lotu (ilosc biletow do wypelniena danych: " + ilekupionych +" )");
                    } else {
                        JOptionPane.showMessageDialog(null, "Nie udało się pobrać ID pasażera lub kod rezerwacji jest bledny.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }

                    if(DaneRezerwacjaForm.this.ilosc == 0){
                        MiejscaUpdate(ilekupionych,idlotu);
                        JOptionPane.showMessageDialog(null, "Do zapłaty:\nUbezpieczenie: "+ilekupionych+"*"+"150 zl\nBilety: "+ilekupionych+"*"+cena+" zl\nRazem: "+total+" zl" , "Platnosc", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                }

            }
        });
        losojButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();
                kodRezerwacji= random.nextInt(900) + 100;
                Kod.setText(String.valueOf(kodRezerwacji));

            }
        });
        wyczyscButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imieTextField.setText("");
                nazwiskoTextField.setText("");
                emailTextField.setText("imie.nazwisko@mail.com");
                Kod.setText("Twoj kod rezerwacji");
                kodRezerwacji = 0;
            }
        });
    }
}

