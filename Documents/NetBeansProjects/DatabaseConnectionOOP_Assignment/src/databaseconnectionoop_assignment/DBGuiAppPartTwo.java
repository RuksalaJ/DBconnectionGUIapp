/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databaseconnectionoop_assignment;

    
    import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.Vector;

public class DBGuiAppPartTwo extends JFrame {
    // Declarations of UI Components based on labels
    private JTextField textField1_8;   // Table name input
    private JCheckBox checkBox2;        // Enable/Disable search filter controls
    private JComboBox<String> comboBox3;// Columns for filtering
    private JRadioButton radioButton4;  // Search condition option 1 (Exact/Full match)
    private JRadioButton radioButton5;  // Search condition option 2 (Wildcard match)
    private ButtonGroup radioGroup;     // Groups Radio Buttons 4 & 5
    private JTextField textField6;      // Search term input
    private JButton button7;            // Button to generate SELECT SQL
    private JTable table9;              // Non-editable pre-defined Table
    private JTable table10;             // Editable Table for INSERT values
    private JButton button11;           // Button to generate INSERT SQL
    private JTextArea textArea12;       // Display area for generated SQL statements
    private JTable table13;             // Results Table
    private JButton button14;           // Button to execute SELECT SQL

    // Database connection parameters (Modify as per your DB setup)
    private final String DB_URL = "jdbc:mysql://localhost:3306/your_database";
    private final String DB_USER = "root";
    private final String DB_PASS = "password";

    public DBGuiAppPartTwo() {
        initComponents();
        setupEvents();
    }

    private void initComponents() {
        // 1. Text Field for Table Name
        textField1_8 = new JTextField(20);

        // 2. Check Box
        checkBox2 = new JCheckBox("Enable Filters", false);

        // 3. Combo Box for Columns (Sample columns added)
        comboBox3 = new JComboBox<>(new String[]{"id", "name", "specialization", "contact"});
        comboBox3.setEnabled(false);

        // 4 & 5. Radio Buttons grouped together
        radioButton4 = new JRadioButton("Full Match", true);
        radioButton5 = new JRadioButton("Starts With (%)");
        radioButton4.setEnabled(false);
        radioButton5.setEnabled(false);

        radioGroup = new ButtonGroup();
        radioGroup.add(radioButton4);
        radioGroup.add(radioButton5);

        // 6. Text Field for Search Term
        textField6 = new JTextField(20);
        textField6.setEnabled(false);

        // 7. Button to generate SQL
        button7 = new JButton("Generate SELECT SQL");

        // 9. Non-editable Table with a pre-defined model
        DefaultTableModel model9 = new DefaultTableModel(
            new Object[][]{}, 
            new String[]{"Column1", "Column2", "Column3"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes the entire table non-editable
            }
        };
        table9 = new JTable(model9);

        // 10. Editable Table allowing user to input a single row
        DefaultTableModel model10 = new DefaultTableModel(
            new Object[][]{{"", "", ""}}, // Initial blank row for user entry
            new String[]{"Column1", "Column2", "Column3"}
        );
        table10 = new JTable(model10);

        // 11. Button to generate INSERT SQL
        button11 = new JButton("Generate INSERT SQL");

        // 12. Text Area for SQL statement output
        textArea12 = new JTextArea(5, 40);
        textArea12.setEditable(false);

        // 13. Results Table
        table13 = new JTable(new DefaultTableModel());

        // 14. Button to execute SELECT SQL
        button14 = new JButton("Execute SQL & View Results");
    }

    private void setupEvents() {
        // Requirement: If Check Box 2 is not checked, disable controls 3 to 6. If checked, enable them.
        checkBox2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isChecked = (e.getStateChange() == ItemEvent.SELECTED);
                comboBox3.setEnabled(isChecked);
                radioButton4.setEnabled(isChecked);
                radioButton5.setEnabled(isChecked);
                textField6.setEnabled(isChecked);
            }
        });

        // Requirement: When Button 7 is clicked, display the generated SELECT SQL statement in Text Area 12
        button7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = textField1_8.getText().trim();
                if (tableName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid table name.");
                    return;
                }

                String sql = "SELECT * FROM " + tableName;

                // Check if filters are active
                if (checkBox2.isSelected()) {
                    String column = (String) comboBox3.getSelectedItem();
                    String searchTerm = textField6.getText().trim();

                    if (radioButton4.isSelected()) {
                        // Full match exact term (e.g., "Sunil Perera")
                        sql += " WHERE " + column + " = '" + searchTerm + "'";
                    } else if (radioButton5.isSelected()) {
                        // Wildcard suffix term (e.g., "s%")
                        sql += " WHERE " + column + " LIKE '" + searchTerm + "%'";
                    }
                }
                textArea12.setText(sql);
            }
        });

        // Requirement: When Button 11 is clicked, generate an INSERT INTO statement from Table 10 and put in Text Area 12
        button11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = textField1_8.getText().trim();
                if (tableName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid table name.");
                    return;
                }

                DefaultTableModel model = (DefaultTableModel) table10.getModel();
                StringBuilder columns = new StringBuilder();
                StringBuilder values = new StringBuilder();

                for (int i = 0; i < model.getColumnCount(); i++) {
                    columns.append(model.getColumnName(i));
                    Object value = model.getValueAt(0, i);
                    values.append("'").append(value != null ? value.toString() : "").append("'");

                    if (i < model.getColumnCount() - 1) {
                        columns.append(", ");
                        values.append(", ");
                    }
                }

                String insertSql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ");";
                textArea12.setText(insertSql);
            }
        });

        // Requirement: When Button 14 is clicked, execute SELECT SQL and populate JTable 13
        button14.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = textArea12.getText().trim();
                if (query.isEmpty() || !query.toUpperCase().startsWith("SELECT")) {
                    JOptionPane.showMessageDialog(null, "No valid SELECT statement found in Text Area 12.");
                    return;
                }

                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {

                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    // Extract columns dynamically
                    Vector<String> columnNames = new Vector<>();
                    for (int column = 1; column <= columnCount; column++) {
                        columnNames.add(metaData.getColumnName(column));
                    }

                    // Extract rows
                    Vector<Vector<Object>> data = new Vector<>();
                    while (rs.next()) {
                        Vector<Object> vector = new Vector<>();
                        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                            vector.add(rs.getObject(columnIndex));
                        }
                        data.add(vector);
                    }

                    // Update Table 13 Model
                    table13.setModel(new DefaultTableModel(data, columnNames));

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
                }
            }
            
            
        });
    
}}

    
