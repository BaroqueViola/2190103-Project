package src.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import src.services.Database;

public class DeductionFrame extends JFrame implements ActionListener {

    JTextField lifeInsuranceField;
    JTextField spouseLifeInsuranceField;
    JTextField healthInsuranceField;
    JTextField providentFundField;
    JTextField rmfField;
    JTextField ssfField;
    JTextField ltfField;
    JTextField nsfField;
    JTextField homeLoanInterestField;
    JButton submitButton;

    public DeductionFrame() {
        setTitle("TaxHelper - Deduction Input");
        setSize(600, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon("src/images/logo.png").getImage());
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        headerPanel.setBackground(new Color(245, 245, 245));

        ImageIcon rawIcon = new ImageIcon("src/images/deduction.png");
        Image scaledImg = rawIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImg));

        JLabel titleLabel = new JLabel("Insert your deductions (Baht/in this tax year)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(30, 30, 30));

        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        lifeInsuranceField = createRow(inputPanel, "Life Insurance:");
        spouseLifeInsuranceField = createRow(inputPanel, "Spouse's Life Insurance:");
        healthInsuranceField = createRow(inputPanel, "Health Insurance:");
        providentFundField = createRow(inputPanel, "Provident Fund:");
        rmfField = createRow(inputPanel, "RMF (Retirement Mutual Fund):");
        ssfField = createRow(inputPanel, "SSF (Super Savings Fund):");
        ltfField = createRow(inputPanel, "LTF (Long-Term Equity Fund):");
        nsfField = createRow(inputPanel, "NSF (National Savings Fund):");
        homeLoanInterestField = createRow(inputPanel, "Home Loan Interest:");

        add(new JScrollPane(inputPanel), BorderLayout.CENTER);

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submitButton.setBackground(new Color(72, 201, 111));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setContentAreaFilled(true);
        submitButton.setOpaque(true);
        submitButton.setPreferredSize(new Dimension(140, 40));
        submitButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(submitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JTextField createRow(JPanel parent, String labelText) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setPreferredSize(new Dimension(220, 30));
        label.setToolTipText(getTooltip(labelText)); 

        JTextField field = new JTextField();
        field.setFont(new Font("Consolas", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(250, 30));

        row.add(label);
        row.add(Box.createRigidArea(new Dimension(10, 0)));
        row.add(field);

        parent.add(row);
        parent.add(Box.createRigidArea(new Dimension(0, 10)));

        return field;
    }

    private String getTooltip(String labelText) {
        return switch (labelText) {
            case "Life Insurance:" -> "Premiums paid for your personal life insurance.";
            case "Spouse's Life Insurance:" -> "Premiums paid for your spouse’s life insurance (if married).";
            case "Health Insurance:" -> "Premiums for your health insurance policies.";
            case "Provident Fund:" -> "Your contributions into your company's provident fund.";
            case "RMF (Retirement Mutual Fund):" -> "Investment amount into RMF.";
            case "SSF (Super Savings Fund):" -> "Investment amount into SSF.";
            case "LTF (Long-Term Equity Fund):" -> "Investment amount into LTF (before expiration).";
            case "NSF (National Savings Fund):" -> "Savings into NSF for retirement planning.";
            case "Home Loan Interest:" -> "Interest you paid on your home loan.";
            default -> "Enter deduction amount.";
        };
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            try {
                Database db = Database.getInstance();
                db.set("deduction_life_insurance", parseField(lifeInsuranceField));
                db.set("deduction_spouse_life_insurance", parseField(spouseLifeInsuranceField));
                db.set("deduction_health_insurance", parseField(healthInsuranceField));
                db.set("deduction_provident_fund", parseField(providentFundField));
                db.set("deduction_rmf", parseField(rmfField));
                db.set("deduction_ssf", parseField(ssfField));
                db.set("deduction_ltf", parseField(ltfField));
                db.set("deduction_nsf", parseField(nsfField));
                db.set("deduction_home_loan_interest", parseField(homeLoanInterestField));

                System.out.println("Deductions saved successfully!");

                LoadingScreen loading = new LoadingScreen(this);
                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    private String geminiReply;

                    @Override
                    protected Void doInBackground() throws Exception {
                        String prompt = buildPrompt();
                        geminiReply = src.services.TaxCalculator.calculate(prompt);
                        return null;
                    }

                    @Override
                    protected void done() {
                        loading.dispose();
                        new ResultFrame(geminiReply);
                        dispose();
                    }
                };
                worker.execute();
                loading.setVisible(true);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error processing input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private double parseField(JTextField field) {
        try {
            String text = field.getText().trim();
            if (text.isEmpty()) return 0;
            return Double.parseDouble(text);
        } catch (Exception e) {
            return 0;
        }
    }

    private String buildPrompt() {
        Database db = Database.getInstance();
        return String.format("""
            My job is %s.
            I earn %.2f baht per year.
            Marital status: %s.
            Number of children: %d.
            Disabled dependents: %d.
            Parents under support: %d.
            My deductions are:
            - Life Insurance: %.2f baht
            - Spouse's Life Insurance: %.2f baht
            - Health Insurance: %.2f baht
            - Provident Fund: %.2f baht
            - RMF: %.2f baht
            - SSF: %.2f baht
            - LTF: %.2f baht
            - NSF: %.2f baht
            - Home Loan Interest: %.2f baht.
            
            Please calculate my total tax and provide a markdown-formatted table with calculation steps and summary.
            """,
            db.getString("job"),
            db.getDouble("income"),
            db.getBoolean("married", false) ? "Married" : "Single",
            (int) db.getDouble("children"),
            (int) db.getDouble("disabled_dependents"),
            (int) db.getDouble("parents_supported"),
            db.getDouble("deduction_life_insurance"),
            db.getDouble("deduction_spouse_life_insurance"),
            db.getDouble("deduction_health_insurance"),
            db.getDouble("deduction_provident_fund"),
            db.getDouble("deduction_rmf"),
            db.getDouble("deduction_ssf"),
            db.getDouble("deduction_ltf"),
            db.getDouble("deduction_nsf"),
            db.getDouble("deduction_home_loan_interest")
        );
    }
}
