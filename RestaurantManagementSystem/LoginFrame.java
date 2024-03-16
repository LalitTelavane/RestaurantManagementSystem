import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        super("Login");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout()); // Use GridBagLayout for more precise control
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add insets for spacing

        // Image panel with "Admin" label
        JPanel imagePanel = new JPanel(new BorderLayout());
        ImageIcon adminIcon = new ImageIcon("images/login.jpg"); // Replace "admin.png" with your image file path
        Image scaledImage = adminIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Set image size to 200 pixels
        JLabel adminImageLabel = new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
        JLabel adminLabel = new JLabel("Admin", SwingConstants.CENTER);
        adminLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Increase font size
        imagePanel.add(adminImageLabel, BorderLayout.CENTER);
        imagePanel.add(adminLabel, BorderLayout.SOUTH);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(imagePanel, gbc); // Add the image panel

        gbc.gridy++;
        gbc.gridwidth = 1;

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15); // Set text field size to 15 columns
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameField, gbc); // Combine label and text field

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15); // Set text field size to 15 columns
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, gbc); // Combine label and text field

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30)); // Set button size
        panel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                // Check if the username and password match predefined values
                if (username.equals("admin") && password.equals("password")) {
                    // If login successful, close the login frame
                    dispose();
                    // Open the RestaurantManagementSystem frame
                    new RestaurantManagementSystem();
                } else {
                    // Show error message for incorrect credentials
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame();
            }
        });
    }
}