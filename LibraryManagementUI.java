import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LibraryManagementUI extends JFrame {

    // UI Components
    private JTextField txtTitle = new JTextField(20);
    private JTextField txtAuthor = new JTextField(20);
    private JTextField txtISBN = new JTextField(20);
    private JButton btnAddBook = new JButton("Add Book to Database");
    private JButton btnClear = new JButton("Clear Fields");

    public LibraryManagementUI() {
        // Setup the main window frame
        setTitle("Library Management System - Add Book");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create a panel with a grid layout for labels and inputs
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(new JLabel("Book Title:"));
        mainPanel.add(txtTitle);
        
        mainPanel.add(new JLabel("Author Name:"));
        mainPanel.add(txtAuthor);
        
        mainPanel.add(new JLabel("ISBN Number:"));
        mainPanel.add(txtISBN);

        // Create a separate panel for buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(btnAddBook);
        buttonPanel.add(btnClear);

        // Add layout panels to frame
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Event Listeners ---

        // Action for "Add Book" button
        btnAddBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBookToDatabase();
            }
        });

        // Action for "Clear" button
        btnClear.addActionListener(e -> clearFields());
    }

    private void addBookToDatabase() {
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String isbn = txtISBN.getText().trim();

        // Basic validation
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // SQL Insert Query
        String sqlQuery = "INSERT INTO books (title, author, isbn) VALUES (?, ?, ?)";

        // JDBC Connection and Execution using try-with-resources to automatically close connections
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, isbn);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                 JOptionPane.showMessageDialog(this, "Success! Book added to LibraryDB.");
                 clearFields();
            } else {
                 JOptionPane.showMessageDialog(this, "Failed to add book.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            // Handle errors (e.g., duplicate ISBN or connection failure)
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: \n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtTitle.setText("");
        txtAuthor.setText("");
        txtISBN.setText("");
    }

    public static void main(String[] args) {
        // Run the GUI in the Event Dispatch Thread (best practice for Swing)
        SwingUtilities.invokeLater(() -> {
            new LibraryManagementUI().setVisible(true);
        });
    }
}
