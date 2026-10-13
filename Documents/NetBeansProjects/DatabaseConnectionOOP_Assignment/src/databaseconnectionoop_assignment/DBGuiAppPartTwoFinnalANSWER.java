/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databaseconnectionoop_assignment;

/**
 *
 * @author DELL
 */

    
    import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DBGuiAppPartTwoFinnalANSWER extends JFrame {

    // Component Declarations with Labels matching Figure 01
    private JTextField tfSelectTable;    // 1
    private JCheckBox chkWhere;           // 2
    private JComboBox<String> cbColumn;   // 3
    private JRadioButton rbExact;         // 4
    private JRadioButton rbWildcard;      // 5
    private JTextField tfSearchTerm;      // 6
    private JButton btnGenerateSelect;    // 7
    
    private JTextField tfInsertTable;    // 8
    private JTable tblColumns;            // 9 (Non-editable)
    private JTable tblValues;             // 10 (Editable)
    private JButton btnGenerateInsert;    // 11
    
    private JTextArea taSqlStatement;     // 12
    private JTable tblResults;            // 13
    private JButton btnExecute;           // 14

    public DBGuiAppPartTwoFinnalANSWER() {
        // Frame setup
        setTitle("Database Application GUI");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- TOP PANEL: SELECT Query Construction ---
        JPanel pnlSelect = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        pnlSelect.add(new JLabel("SELECT * FROM"));
        
        tfSelectTable = new JTextField("doctors", 8); // 1
        pnlSelect.add(tfSelectTable);
        
        chkWhere = new JCheckBox("WHERE"); // 2
        pnlSelect.add(chkWhere);
        
        String[] columns = {"dName", "Specialization", "RoomNo"};
        cbColumn = new JComboBox<>(columns); // 3
        pnlSelect.add(cbColumn);
        
        rbExact = new JRadioButton("Exact", true); // 4
        rbWildcard = new JRadioButton("%s%"); // 5
        ButtonGroup bgRadio = new ButtonGroup(); // Ensures only one selected at a time
        bgRadio.add(rbExact);
        bgRadio.add(rbWildcard);
        pnlSelect.add(rbExact);
        pnlSelect.add(rbWildcard);
        
        pnlSelect.add(new JLabel("LIKE"));
        tfSearchTerm = new JTextField(10); // 6
        pnlSelect.add(tfSearchTerm);
        
        btnGenerateSelect = new JButton("Generate SQL"); // 7
        pnlSelect.add(btnGenerateSelect);

        // --- MIDDLE PANEL: INSERT Query Construction ---
        JPanel pnlInsert = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlInsert.add(new JLabel("INSERT INTO"));
        
        tfInsertTable = new JTextField("doctors", 8); // 8
        pnlInsert.add(tfInsertTable);
        
        // Table 9: Non-editable Column List
        pnlInsert.add(new JLabel("Column"));
        String[] colHeader = {"Columns"};
        String[][] colData = {{"DocID"}, {"DocName"}, {"Specialization"}, {"Weekdays"}, {"Weekends"}, {"RoomNo"}};
        DefaultTableModel modelCols = new DefaultTableModel(colData, colHeader) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Non-editable
        };
        tblColumns = new JTable(modelCols); // 9
        JScrollPane spCols = new JScrollPane(tblColumns);
        spCols.setPreferredSize(new Dimension(120, 100));
        pnlInsert.add(spCols);
        
        // Table 10: Editable Values List
        pnlInsert.add(new JLabel("VALUES"));
        String[] valHeader = {"Values"};
        String[][] valData = {{"", "", "", "", "", ""}}; // 1 row placeholder
        // Transposed or aligned to 1 row with columns matching the database fields
        DefaultTableModel modelVals = new DefaultTableModel(new String[][]{{"", "", "", "", "", ""}}, colData[0]);
        tblValues = new JTable(modelVals); // 10
        JScrollPane spVals = new JScrollPane(tblValues);
        spVals.setPreferredSize(new Dimension(300, 50));
        pnlInsert.add(spVals);
        
        btnGenerateInsert = new JButton("Generate SQL"); // 11
        pnlInsert.add(btnGenerateInsert);

        // --- BOTTOM PANEL: SQL Statement & Results Execution ---
        JPanel pnlBottom = new JPanel(new BorderLayout(5, 5));
        
        JPanel pnlSqlLabel = new JPanel(new BorderLayout());
        pnlSqlLabel.add(new JLabel("SQL STATEMENT (Auto-Generated)"), BorderLayout.WEST);
        
        taSqlStatement = new JTextArea(4, 50); // 12
        taSqlStatement.setLineWrap(true);
        JScrollPane spSql = new JScrollPane(taSqlStatement);
        
        pnlSqlLabel.add(spSql, BorderLayout.SOUTH);
        pnlBottom.add(pnlSqlLabel, BorderLayout.NORTH);
        
        // Results Table 13
        String[] resHeaders = {"Doc ID", "Doc Name", "Specialization", "Weekdays", "Weekends", "Room No"};
        DefaultTableModel modelRes = new DefaultTableModel(null, resHeaders);
        tblResults = new JTable(modelRes); // 13
        JScrollPane spResults = new JScrollPane(tblResults);
        pnlBottom.add(spResults, BorderLayout.CENTER);
        
        btnExecute = new JButton("Execute"); // 14
        JPanel pnlExecButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlExecButton.add(btnExecute);
        pnlBottom.add(pnlExecButton, BorderLayout.SOUTH);

        // Organize main container panels
        JPanel pnlTopMaster = new JPanel(new GridLayout(2, 1, 5, 5));
        pnlTopMaster.add(pnlSelect);
        pnlTopMaster.add(pnlInsert);
        
        add(pnlTopMaster, BorderLayout.NORTH);
        add(pnlBottom, BorderLayout.CENTER);

        // --- FUNCTIONALITY & EVENT HANDLING (Part ii) ---
        
        // Initial state: disable controls 3 to 6 because checkbox is initially unchecked
        toggleWhereControls(false);
        
        // CheckBox listener to enable/disable filtering controls
        chkWhere.addActionListener(e -> toggleWhereControls(chkWhere.isSelected()));
        
        // Button 7: Generate SELECT SQL Statement
        btnGenerateSelect.addActionListener(e -> {
            String tableName = tfSelectTable.getText().trim();
            StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName);
            
            if (chkWhere.isSelected()) {
                String column = cbColumn.getSelectedItem().toString();
                String term = tfSearchTerm.getText().trim();
                
                if (rbWildcard.isSelected()) {
                    sql.append(" WHERE ").append(column).append(" LIKE '%").append(term).append("%'");
                } else {
                    sql.append(" WHERE ").append(column).append(" LIKE '").append(term).append("'");
                }
            }
            taSqlStatement.setText(sql.toString());
        });
        
        // Button 11: Generate INSERT INTO SQL Statement
        btnGenerateInsert.addActionListener(e -> {
            String tableName = tfInsertTable.getText().trim();
            StringBuilder colsPart = new StringBuilder("(");
            StringBuilder valsPart = new StringBuilder("(");
            
            for (int i = 0; i < tblColumns.getRowCount(); i++) {
                colsPart.append(tblColumns.getValueAt(i, 0));
                
                Object valObj = tblValues.getValueAt(0, i);
                String val = (valObj != null) ? valObj.toString().trim() : "";
                valsPart.append("'").append(val).append("'");
                
                if (i < tblColumns.getRowCount() - 1) {
                    colsPart.append(", ");
                    valsPart.append(", ");
                }
            }
            colsPart.append(")");
            valsPart.append(")");
            
            String insertSql = "INSERT INTO " + tableName + " " + colsPart + " VALUES " + valsPart;
            taSqlStatement.setText(insertSql);
        });
        
        // Button 14: Execute SQL Query and display results
        btnExecute.addActionListener(e -> {
            String query = taSqlStatement.getText().trim();
            if (query.isEmpty()) return;
            
            // Standard JDBC Implementation structure
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_db", "user", "pass");
                 Statement stmt = conn.createStatement()) {
                
                if (query.toLowerCase().startsWith("select")) {
                    ResultSet rs = stmt.executeQuery(query);
                    modelRes.setRowCount(0); // Clear previous data
                    
                    while (rs.next()) {
                        modelRes.addRow(new Object[]{
                            rs.getString(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6)
                        });
                    }
                } else {
                    int rowsAffected = stmt.executeUpdate(query);
                    JOptionPane.showMessageDialog(this, rowsAffected + " row(s) inserted successfully!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void toggleWhereControls(boolean enabled) {
        cbColumn.setEnabled(enabled);
        rbExact.setEnabled(enabled);
        rbWildcard.setEnabled(enabled);
        tfSearchTerm.setEnabled(enabled);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DBGuiAppPartTwoFinnalANSWER().setVisible(true));
    }
}


