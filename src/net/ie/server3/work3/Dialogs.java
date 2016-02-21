/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ie.server3.work3;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author wanchana
 */
public class Dialogs extends JDialog {

    public int Dialogs(String fileName, String fullPath) {
        Object[] options = {"Save", "Discard"};
        String fileType = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
        int chk = 0;
        ImageIcon image = null;
        if (fileType.equals("jpg") || fileType.equals("png") || fileType.equals("gif") || fileType.equals("bmp")) {
            image = new ImageIcon(fullPath);
//            Image img = (image.getImage()).getScaledInstance(80, 95, java.awt.Image.SCALE_SMOOTH);
//            image = new ImageIcon(img);

            chk = JOptionPane.showOptionDialog(null,
                    "Server3 is trying to transfer file : \"" + fileName + "\" to you.\nDo you want to save or discard it?",
                    "Save or Discard",
//                    new JLabel("Server3 is trying to transfer file : \"" + fileName + 
//                            "\" to you.\nDo you want to save or discard it?", image, JLabel.LEFT),
//                    "Save or Discard",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    image,
                    options,
                    options[0]);
        } 
        else {
            image = new ImageIcon(getClass().getClassLoader().getResource("resources/file-icon.png"));
            Image img = (image.getImage()).getScaledInstance(80, 95, java.awt.Image.SCALE_SMOOTH);
            image = new ImageIcon(img);
            chk = JOptionPane.showOptionDialog(null,
                    "Server3 is trying to transfer file : \"" + fileName + "\" to you.\nDo you want to save or discard it?",
                    "Save or Discard",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    image,
                    options,
                    options[0]);
        }

        return chk;
    }
}
