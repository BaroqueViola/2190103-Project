package src.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import src.services.Database;
import src.services.FrameFactory;

public class IncomeFrame extends JFrame implements ActionListener, ItemListener {

    JTextField incomeField;
    JCheckBox marriedCheckbox;
    JCheckBox ownIncomeCheckbox;
    JCheckBox pregnantCheckbox;
    JTextField childrenField;
    JTextField disabledField;
    JTextField parentsField;
    JButton submitButton;

    public IncomeFrame() {
        setTitle("TaxHelper - Income & Personal Info");
        setSize(500, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon("src/images/logo.png").getImage());
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        headerPanel.setBackground(Color.WHITE);

        ImageIcon rawIcon = new ImageIcon("src/images/info.png");
        Image scaledImage = rawIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));

        JLabel titleLabel = new JLabel("Insert Your Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));

        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 70, 20, 70));

        incomeField = createInputField(centerPanel, "Income per year (Baht):");

        marriedCheckbox = createCheckbox(centerPanel, "Married");
        marriedCheckbox.addItemListener(this);

        ownIncomeCheckbox = createCheckbox(centerPanel, "Partner has income");
        ownIncomeCheckbox.setVisible(false);

        pregnantCheckbox = createCheckbox(centerPanel, "Pregnant");
        pregnantCheckbox.setVisible(false);
        pregnantCheckbox.addItemListener(this);

        childrenField = createInputField(centerPanel, "Number of Children:");
        childrenField.setVisible(false);

        disabledField = createInputField(centerPanel, "Number of Disabled Dependents:");
        parentsField = createInputField(centerPanel, "Number of Parents Supported:");

        add(centerPanel, BorderLayout.CENTER);

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        submitButton.setBackground(new Color(72, 201, 111));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setContentAreaFilled(true);
        submitButton.setOpaque(true);
        submitButton.setPreferredSize(new Dimension(160, 45));
        submitButton.addActionListener(this);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(submitButton);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JTextField createInputField(JPanel parent, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(250, 35));
        field.setFont(new Font("Consolas", Font.PLAIN, 18));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        parent.add(label);
        parent.add(Box.createRigidArea(new Dimension(0, 5)));
        parent.add(field);
        parent.add(Box.createRigidArea(new Dimension(0, 15)));

        return field;
    }

    private JCheckBox createCheckbox(JPanel parent, String text) {
        JCheckBox box = new JCheckBox(text);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        box.setBackground(Color.WHITE);
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(box);
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
        return box;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            try {
                double income = Double.parseDouble(incomeField.getText().trim());
                boolean married = marriedCheckbox.isSelected();
                boolean ownIncome = ownIncomeCheckbox.isSelected();
                boolean pregnant = pregnantCheckbox.isSelected();
                int children = 0;
                if (married && !pregnant && childrenField.isVisible()) {
                    String childText = childrenField.getText().trim();
                    if (!childText.isEmpty()) {
                        children = Integer.parseInt(childText);
                    }
                }
                int disabled = Integer.parseInt(disabledField.getText().trim());
                int parents = Integer.parseInt(parentsField.getText().trim());

                Database.getInstance().set("income", income);
                Database.getInstance().set("married", married);
                Database.getInstance().set("have_own_income", ownIncome);
                Database.getInstance().set("pregnant", pregnant);
                Database.getInstance().set("children", children);
                Database.getInstance().set("disabled_dependents", disabled);
                Database.getInstance().set("parents_supported", parents);

                FrameFactory.create("Deduction");
                this.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == marriedCheckbox) {
            boolean married = marriedCheckbox.isSelected();
            ownIncomeCheckbox.setVisible(married);
            pregnantCheckbox.setVisible(married);
            updateChildrenField();
        } else if (e.getSource() == pregnantCheckbox) {
            updateChildrenField();
        }
    }

    private void updateChildrenField() {
        boolean showChildren = marriedCheckbox.isSelected() && !pregnantCheckbox.isSelected();
        childrenField.setVisible(showChildren);
        childrenField.setEnabled(showChildren);
        childrenField.revalidate();
        childrenField.repaint();
    }
}
