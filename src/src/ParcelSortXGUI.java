package src;

import javax.swing.*;
import java.awt.*;

public class ParcelSortXGUI extends JFrame {
    private JTextPane textPane;
    private JButton startButton;
    private StringBuilder htmlContent;

    public ParcelSortXGUI() {
        setTitle("ParcelSortX Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        htmlContent = new StringBuilder();
        htmlContent.append("<html><body style='font-family: monospace; font-size: 12px;'>");

        JScrollPane scrollPane = new JScrollPane(textPane);

        startButton = new JButton("Start Simulation");

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(startButton, BorderLayout.SOUTH);

        startButton.addActionListener(e -> {
            startButton.setEnabled(false);

            htmlContent = new StringBuilder();
            htmlContent.append("<html><body style='font-family: monospace; font-size: 12px;'>");
            textPane.setText("");

            new Thread(() -> {
                ParcelSortX.runSimulation(new ParcelSortX.Appender() {
                    @Override
                    public void append(String text) {
                        SwingUtilities.invokeLater(() -> {
                            String coloredText = convertAnsiToHtml(text);
                            htmlContent.append(coloredText);
                            textPane.setText(htmlContent.toString() + "</body></html>");
                            textPane.setCaretPosition(textPane.getDocument().getLength());
                        });
                    }
                });

                SwingUtilities.invokeLater(() -> {
                    htmlContent.append("</body></html>");
                    textPane.setText(htmlContent.toString());
                    startButton.setEnabled(true);
                });
            }).start();
        });
    }

    private String convertAnsiToHtml(String text) {
        text = text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");

        text = text.replace(ParcelSortX.ANSI_RED, "<font color='#E57373'>")
                .replace(ParcelSortX.ANSI_GREEN, "<font color='#4CAF50'>")
                .replace(ParcelSortX.ANSI_BLUE, "<font color='#64B5F6'>")
                .replace(ParcelSortX.ANSI_PURPLE, "<font color='#6A5ACD'>")
                .replace(ParcelSortX.ANSI_RESET, "</font>");

        text = text.replace("\n", "<br>");

        return text;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        SwingUtilities.invokeLater(() -> {
            new ParcelSortXGUI().setVisible(true);
        });
    }
}
