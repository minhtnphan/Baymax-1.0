/**
 * Develop Baymax Application Form for Baymax 1.0
 * @Author Huyen Nguyen
 * @Author Minh Phan
 */

package net.baymax.swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BaymaxApplication extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField symptomTextField;
    private JTextField priceTextField;
    private JTextField locationTextField;
    private JButton btnSearchForHospital;
    private JLabel label;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserLogin frame = new UserLogin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public BaymaxApplication() {
        setBounds(100, 80, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Baymax 1.0");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 46));
        lblNewLabel.setBounds(423, 13, 273, 93);
        contentPane.add(lblNewLabel);

        //SYMPTOM
        symptomTextField = new JTextField();
        symptomTextField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        symptomTextField.setBounds(481, 100, 281, 68);
        contentPane.add(symptomTextField);
        symptomTextField.setColumns(10);

        JLabel lblSymptom = new JLabel("Symptom");
        lblSymptom.setBackground(Color.BLACK);
        lblSymptom.setForeground(Color.BLACK);
        lblSymptom.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblSymptom.setBounds(200, 100, 193, 52);
        contentPane.add(lblSymptom);

        //PRICE
        priceTextField = new JTextField();
        priceTextField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        priceTextField.setBounds(481, 200, 281, 68);
        contentPane.add(priceTextField);
        priceTextField.setColumns(10);

        JLabel lblPrice = new JLabel("Filter by price");
        lblPrice.setForeground(Color.BLACK);
        lblPrice.setBackground(Color.CYAN);
        lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblPrice.setBounds(200, 200, 193, 52);
        contentPane.add(lblPrice);

        //LOCATION
        locationTextField = new JTextField();
        locationTextField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        locationTextField.setBounds(481, 300, 281, 68);
        contentPane.add(locationTextField);
        locationTextField.setColumns(10);

        JLabel lblLocation = new JLabel("Filter by location");
        lblLocation.setForeground(Color.BLACK);
        lblLocation.setBackground(Color.CYAN);
        lblLocation.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblLocation.setBounds(200, 300, 250, 52);
        contentPane.add(lblLocation);

        btnSearchForHospital = new JButton("Search for hospital");
        btnSearchForHospital.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnSearchForHospital.setBounds(300, 392, 400, 73);
        btnSearchForHospital.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String userSymptom = symptomTextField.getText();
                String userPrice = priceTextField.getText();
                String userLocation = locationTextField.getText();
                try {
                    Connection connection = (Connection) DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/db_baymax", "root", "122130");

                    PreparedStatement st = (PreparedStatement) connection
                            .prepareStatement("SELECT * FROM department where symptom=? and price<? and location=?" +
                                    "and ranking = 5");

                    st.setString(1, userSymptom);
                    st.setString(2, userPrice);
                    st.setString(3, userLocation);
                    ResultSet rs = st.executeQuery();
                    if (rs.next() == false) {
                        JOptionPane.showMessageDialog(btnSearchForHospital, "Baymax can't recommend any hospital." +
                                " Please comeback later!!!");
                    }else {
                        do {
                            String department = rs.getString("department_name");
                            String hospital = rs.getString("hospital");
                            String location = rs.getString("location");
                            Integer price = Integer.valueOf(rs.getString("price"));
                            JOptionPane.showMessageDialog(btnSearchForHospital, "Department: " + department + "\nHospital: " +
                                    hospital + "\nLocation: " + location + "\nPrice: " + price);
                        } while (rs.next());
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });

        contentPane.add(btnSearchForHospital);
    }
}