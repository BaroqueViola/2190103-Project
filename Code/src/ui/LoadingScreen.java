package src.ui;

import java.awt.*;
import javax.swing.*;

public class LoadingScreen extends JDialog {

    public LoadingScreen(JFrame parent) {
        super(parent, "Loading...", true);
        setLayout(new BorderLayout());
        setSize(300, 150);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JLabel label = new JLabel("Calculating your tax, please wait...", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(label, BorderLayout.CENTER);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        add(progressBar, BorderLayout.SOUTH);
    }
}
