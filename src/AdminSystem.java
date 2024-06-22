import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AdminSystem extends JFrame{
    private JPanel AdminSystem;
    private JTextField cenaTextField;
    private JTextField wolneTextField;
    private JTextField godzinaTextField;
    private JTextField dataTextField;
    private JTextField wylotTextField;
    private JTextField przylotTextField;
    private JButton wylogujButton;
    private JButton wykonajButton;
    private JButton rezerwacjeButton;
    private JButton wyczyscButton;
    private int width = 700, height = 600;
    String wylot, przylot;
    Time czas;
    Date data;
    int wolne, cena;


    public class DBConnection {
        private static final String URL =
                "jdbc:postgresql://localhost:5432/JumpFlight"; //nazwa_bazy
        private static final String USER = "postgres";
        private static final String PASSWORD = "admin";
        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    public void WpisywanieLotow(String wylot, String przylot, Date data, Time czas, Integer wolne, int cena){
        String sql = "INSERT INTO public.loty (wylot,przylot,data,czas,wolne_miejsca,cena) Values(?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, wylot);
            pstmt.setString(2, przylot);
            pstmt.setDate(3, data);
            pstmt.setTime(4, czas);
            pstmt.setInt(5,wolne);
            pstmt.setInt(6,cena);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Lot dodany pomyślnie.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        } catch(SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas dodawania lotu.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }

    }

    public AdminSystem() {
        super("Logowanie do systemu");
        this.setContentPane(this.AdminSystem);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width,height);
        this.setLocationRelativeTo(null);


        wylogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                LoginForm loginForm =new LoginForm();
                loginForm.setVisible(true);
            }
        });


        wykonajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wylot = wylotTextField.getText();
                przylot = przylotTextField.getText();
                try {
                    data = Date.valueOf(dataTextField.getText());
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    long ms = sdf.parse(godzinaTextField.getText()).getTime();
                    czas = new Time(ms);
                    wolne = Integer.parseInt(wolneTextField.getText());
                    cena = Integer.parseInt(cenaTextField.getText());

                    WpisywanieLotow(wylot, przylot, data, czas, wolne, cena);
                } catch (IllegalArgumentException | ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Proszę wprowadzić poprawne dane.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        rezerwacjeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                RezerwacjeAdminForm rezerwacjeAdminForm = new RezerwacjeAdminForm();
                rezerwacjeAdminForm.setVisible(true);
            }
        });
        wyczyscButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wylotTextField.setText("Miejsce wylotu");
                przylotTextField.setText("Miejsce przylotu");
                dataTextField.setText("Data wylotu (RRRR-MM-DD)");
                godzinaTextField.setText("Godzina wylotu (GG:MM)");
                wolneTextField.setText("Liczba wolnych miejsc");
                cenaTextField.setText("Cena biletu (od osoby)");
            }
        });
    }
}

