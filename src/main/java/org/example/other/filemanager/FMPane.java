package org.example.other.filemanager;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class FMPane extends JPanel {

    JTable table;
    Path path;
    int selectedRow = -1;

    public FMPane(Path path) {

        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(new BorderLayout());
        this.path = path;

        this.add(new JLabel("FileManager!"), BorderLayout.NORTH);
        createTable();
        displayFiles();
    }

    public void createTable() {
        table = new JTable();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println(e.getClickCount());
                int row = table.getSelectedRow();
                if(row == -1){}
                else if (row == selectedRow) {
                    String fileName = (String) table.getValueAt(row, 1);
                    Path newPath = path.resolve(fileName);
                    System.out.println(newPath);
                    if (Files.isDirectory(newPath)) {
                        path = newPath.normalize();
                        displayFiles();
                    }
                } else{
                    selectedRow = row;
                }
                super.mouseClicked(e);
            }
        });

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(table);

        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void disallowEditing(JTable table) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(new JTextField()) {
                @Override
                public boolean isCellEditable(EventObject anEvent) {
                    return false;
                }
            });
        }
    }

    private Object[][] getFolderContent(Path path){
        record FileData(String name, ImageIcon icon, int priority) {}
        try (var stream = Files.list(path)) {
            List<FileData> files= new ArrayList<>();
            files.add(new FileData("..", (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(path.resolve("..").toFile()), 0));

            stream.forEach(p -> {
                int priority = 10;
                if(p.getFileName().toString().startsWith(".")) priority = 1;
                if(!Files.isDirectory(p)) priority +=10;
                FileData fileData = new FileData(p.getFileName().toString(), (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(p.toFile()), priority);
                files.add(fileData);
            });

            files.sort(Comparator.comparingInt(FileData::priority));

            Object[][] data = new Object[files.size()][2];

            for (int i = 0; i < files.size(); i++){
                data[i][0] = files.get(i).icon();
                data[i][1] = files.get(i).name();
            }

            return data;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ImageTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setIcon((ImageIcon) value);
            label.setHorizontalAlignment(JLabel.CENTER);
            return label;
        }
    }

    private void displayFiles() {
        selectedRow = -1;

        TableModel model = new DefaultTableModel(getFolderContent(path), new String[]{"Icon","File name"});
        table.setModel(model);
        disallowEditing(table);
        table.getColumnModel().getColumn(0).setCellRenderer(new ImageTableCellRenderer());
        table.getColumnModel().getColumn(0).setMaxWidth(30);
        table.setIntercellSpacing(new Dimension(0, 0));
    }
}
