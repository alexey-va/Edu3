package org.example.other.filemanager;

import javax.swing.*;

public class FileManagerFrame extends JFrame {

    public FileManagerFrame(JPanel panel){
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setSize(500,700);
        this.setBounds(400, 400, 500, 700);
        this.setVisible(true);
    }
}
