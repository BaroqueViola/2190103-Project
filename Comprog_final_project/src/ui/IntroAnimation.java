package src.ui;

import java.awt.*;
import javax.swing.*;
import src.services.FrameFactory;

public class IntroAnimation extends JFrame {
    public IntroAnimation() {
        setUndecorated(true);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        ImageIcon logoIcon = new ImageIcon("src/images/logo.png");
        Image scaledImage = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setVerticalAlignment(JLabel.CENTER);
        add(logoLabel, BorderLayout.CENTER);

        JLabel textLabel = new JLabel("TaxHelper App");
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        textLabel.setForeground(new Color(50, 50, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        add(textLabel, BorderLayout.SOUTH);

        setVisible(true);

        Timer timer = new Timer(2500, e -> {
            ((Timer) e.getSource()).stop();
            dispose();
            FrameFactory.create("Income");
        });
        timer.setRepeats(false);
        timer.start();
    }
}
