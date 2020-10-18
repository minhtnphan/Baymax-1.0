/**
 * Develop User Home Page for Baymax 1.0
 * @Author Huyen Nguyen
 * @Author Minh Phan
 */

package net.baymax.swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserHome extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserHome frame = new UserHome();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public UserHome() {

    }

    /**
     * Create the frame.
     */
    public UserHome(String userSes) {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 80, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //Log-out
        JButton btnLogout = new JButton("Logout");
        btnLogout.setForeground(new Color(0, 0, 0));
        btnLogout.setBackground(UIManager.getColor("Button.disabledForeground"));
        btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 39));
        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserLogin obj = new UserLogin();
                obj.setTitle("User-Login");
                obj.setVisible(true);
            }
        });
        btnLogout.setBounds(247, 70, 491, 114);
        contentPane.add(btnLogout);

        //Change password
        JButton btnChangePass = new JButton("Change password\r\n");
        btnChangePass.setBackground(UIManager.getColor("Button.disabledForeground"));
        btnChangePass.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChangePassword changePassword = new ChangePassword(userSes);
                changePassword.setTitle("Change Password");
                changePassword.setVisible(true);
            }
        });

        btnChangePass.setFont(new Font("Tahoma", Font.PLAIN, 35));
        btnChangePass.setBounds(247, 170, 491, 114);
        contentPane.add(btnChangePass);

        //Bay-max application
        JButton baymaxButton = new JButton("Let's meet Baymax\r\n");
        baymaxButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        baymaxButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BaymaxApplication baymaxApplication = new BaymaxApplication();
                baymaxApplication.setTitle("Let's meet Baymax");
                baymaxApplication.setVisible(true);
            }
        });
        baymaxButton.setFont(new Font("Tahoma", Font.PLAIN, 35));
        baymaxButton.setBounds(247, 270, 491, 114);
        contentPane.add(baymaxButton);

    }
}