package src.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

public class ResultFrame extends JFrame {

    private JTextArea resultArea;
    private String markdownContent;

    public ResultFrame(String geminiReply) {
        setTitle("TaxHelper - Result");
        setSize(600, 700);
        setIconImage(new ImageIcon("src/images/logo.png").getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Here are your results", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(header, BorderLayout.NORTH);


        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        markdownContent = geminiReply;
        resultArea.setText(markdownContent);

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton downloadButton = new JButton("Download File");
        downloadButton.setPreferredSize(new Dimension(150, 40));
        downloadButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        downloadButton.setBackground(new Color(52, 152, 219));
        downloadButton.setForeground(Color.WHITE);
        downloadButton.setFocusPainted(false);
        downloadButton.setBorderPainted(false);
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveMarkdownFile();
            }
        });

        JButton doneButton = new JButton("Done");
        doneButton.setPreferredSize(new Dimension(120, 40));
        doneButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        doneButton.setBackground(new Color(72, 201, 111));
        doneButton.setForeground(Color.WHITE);
        doneButton.setFocusPainted(false);
        doneButton.setBorderPainted(false);
        doneButton.addActionListener(e -> animateClose());

        buttonPanel.add(downloadButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(doneButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void saveMarkdownFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File("TaxHelper_Result.md"));
        int option = fileChooser.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                writer.write(markdownContent);
                JOptionPane.showMessageDialog(this, "File saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save file.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void animateClose() {
        Timer timer = new Timer(15, null);
        final int[] height = {getHeight()};
        final int minHeight = 0;

        timer.addActionListener(e -> {
            height[0] -= 15;

            if (height[0] <= minHeight) {
                timer.stop();
                dispose();
                System.exit(0);
            } else {
                setSize(getWidth(), height[0]);
                setLocationRelativeTo(null);
            }
        });

        timer.start();
    }
}
