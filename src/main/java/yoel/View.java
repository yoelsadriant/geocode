package yoel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

class View extends JFrame {
    private String srcFile;
    private String dstFile;
    View() {
        JPanel panel = new JPanel();
        JTextField textField = new JTextField(40);
        textField.setEditable(false);
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILES", "csv");
        fc.setFileFilter(filter);
        JButton openButton = new JButton("Browse");
        openButton.addActionListener(e -> {
            int returnVal = fc.showOpenDialog(panel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                textField.setText(file.getPath());
                srcFile = file.getPath();
            }
        });
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            int returnVal = fc.showSaveDialog(panel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                dstFile = file.getPath();
                new Geolocation(srcFile, dstFile);
            }
        });
        JPanel btnPanel = new JPanel();
        btnPanel.add(openButton);
        btnPanel.add(saveBtn);
        panel.add(textField, BorderLayout.PAGE_START);
        panel.add(btnPanel, BorderLayout.PAGE_END);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);
        pack();
        setVisible(true);
    }

}
