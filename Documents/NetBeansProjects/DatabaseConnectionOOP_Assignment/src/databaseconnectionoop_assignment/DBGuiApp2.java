/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databaseconnectionoop_assignment;

    
    import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DBGuiApp2 extends JFrame {

    public DBGuiApp2() {
        // Window settings
        setTitle("Database Application GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 650);
        setLayout(new BorderLayout(10, 10));

        // Main container panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ==========================================
        // 1. SELECT SECTION (Top Row)
        // ==========================================
        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        selectPanel.setBorder(BorderFactory.createEtchedBorder());

        selectPanel.add(new JLabel("SELECT * FROM"));
        
        JTextField tfSelectTable = new JTextField("doctors", 8); // Control 1
        selectPanel.add(tfSelectTable);

        JCheckBox cbWhere = new JCheckBox("WHERE"); // Control 2
        selectPanel.add(cbWhere);

        String[] columns = {"dName", "docID", "Specialization"};
        JComboBox<String> comboColumn = new JComboBox<>(columns); // Control 3
        selectPanel.add(comboColumn);

        JRadioButton rbEquals = new JRadioButton("="); // Control 4
        JRadioButton rbLike = new JRadioButton("LIKE"); // Control 5
        ButtonGroup relationGroup = new ButtonGroup();
        relationGroup.add(rbEquals);
        relationGroup.add(rbLike);
        selectPanel.add(rbEquals);
        selectPanel.add(rbLike);

        JTextField tfConditionValue = new JTextField(12); // Control 6
        selectPanel.add(tfConditionValue);

        JButton btnGenerateSelect = new JButton("Generate SQL"); // Control 7
        selectPanel.add(btnGenerateSelect);

        mainPanel.add(selectPanel);
        mainPanel.add(Box.createVerticalStrut(10)); // Spacer

        // ==========================================
        // 2. INSERT SECTION (Middle Row)
        // ==========================================
        JPanel insertPanel = new JPanel(new GridBagLayout());
        insertPanel.setBorder(BorderFactory.createEtchedBorder());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // Label and text field for INSERT INTO
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.weighty = 0;
        insertPanel.add(new JLabel("INSERT INTO"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        JTextField tfInsertTable = new JTextField("doctors", 8); // Control 8
        insertPanel.add(tfInsertTable, gbc);

        // Column List/Table container
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.4; gbc.weighty = 0;
        insertPanel.add(new JLabel("Column", SwingConstants.CENTER), gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.gridheight = 2; gbc.weighty = 1.0;
        JTable tableColumns = new JTable(new DefaultTableModel(new Object[]{"Columns"}, 5)); // Control 9
        insertPanel.add(new JScrollPane(tableColumns), gbc);

        // VALUES Label
        gbc.gridx = 3; gbc.gridy = 1; gbc.gridheight = 1; gbc.weightx = 0; gbc.weighty = 0;
        insertPanel.add(new JLabel("VALUES"), gbc);

        // Values List/Table container
        gbc.gridx = 4; gbc.gridy = 0; gbc.weightx = 0.4;
        insertPanel.add(new JLabel("Values", SwingConstants.CENTER), gbc);
        
        gbc.gridx = 4; gbc.gridy = 1; gbc.gridheight = 2; gbc.weighty = 1.0;
        JTable tableValues = new JTable(new DefaultTableModel(new Object[]{"Values"}, 5)); // Control 10
        insertPanel.add(new JScrollPane(tableValues), gbc);

        // Generate SQL Button for Insert
        gbc.gridx = 5; gbc.gridy = 1; gbc.gridheight = 1; gbc.weightx = 0.2; gbc.weighty = 0;
        JButton btnGenerateInsert = new JButton("Generate SQL"); // Control 11
        insertPanel.add(btnGenerateInsert, gbc);

        mainPanel.add(insertPanel);
        mainPanel.add(Box.createVerticalStrut(10)); // Spacer

        // ==========================================
        // 3. STATEMENT & EXECUTION SECTION (Bottom Area)
        // ==========================================
        JPanel executionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcExec = new GridBagConstraints();
        gbcExec.insets = new Insets(5, 5, 5, 5);
        gbcExec.fill = GridBagConstraints.BOTH;

        // SQL Statement Label
        gbcExec.gridx = 0; gbcExec.gridy = 0; gbcExec.weightx = 1.0;
        executionPanel.add(new JLabel("SQL STATEMENT (Auto-Generated)"), gbcExec);

        // Text Area for SQL script
        gbcExec.gridx = 0; gbcExec.gridy = 1; gbcExec.gridwidth = 2; gbcExec.weighty = 0.3;
        JTextArea taSqlStatement = new JTextArea(4, 50); // Control 12
        taSqlStatement.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        executionPanel.add(new JScrollPane(taSqlStatement), gbcExec);

        // Main output Data Table
        gbcExec.gridx = 0; gbcExec.gridy = 2; gbcExec.gridwidth = 1; gbcExec.weightx = 0.8; gbcExec.weighty = 0.7;
        String[] mainTableHeaders = {"Doc ID", "Doc Name", "Specialization", "Weekdays", "Weekends", "Room"};
        DefaultTableModel model = new DefaultTableModel(mainTableHeaders, 4);
        JTable mainDataTable = new JTable(model); // Control 13
        executionPanel.add(new JScrollPane(mainDataTable), gbcExec);

        // Execute Button
        gbcExec.gridx = 1; gbcExec.gridy = 2; gbcExec.weightx = 0.2; gbcExec.fill = GridBagConstraints.NONE;
        gbcExec.anchor = GridBagConstraints.NORTH;
        JButton btnExecute = new JButton("Execute"); // Control 14
        btnExecute.setPreferredSize(new Dimension(100, 30));
        executionPanel.add(btnExecute, gbcExec);

        mainPanel.add(executionPanel);

        // Add main panel to structural framework
        add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DBGuiApp2().setVisible(true);
        });
    }
}

    

