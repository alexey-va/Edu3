package org.example.other.network.udpstreamer;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class Drawer extends JFrame {

    public Drawer() {
        super("UDP Streamer");
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        add(new MyPane());
    }

}

class MyPane extends JPanel {



    public MyPane() {
        super();

    }

}
