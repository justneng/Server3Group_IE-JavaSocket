/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ie.server3.work3;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author wanchana
 */
public class Styles {
    
    public static SimpleAttributeSet setStyleMessageWelcome(JTextPane jTextPane, String msg) throws BadLocationException {
        StyledDocument doc = jTextPane.getStyledDocument();
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, Color.BLACK);
        StyleConstants.setBold(style, true);
        doc.insertString(doc.getLength(), msg, style);
        return style;
    }
    
    public static SimpleAttributeSet setStyleMessageSend(JTextPane jTextPane, String msgOut) throws BadLocationException {
        StyledDocument doc = jTextPane.getStyledDocument();
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setBackground(style, Color.LIGHT_GRAY);
        StyleConstants.setBold(style, true);
        if(msgOut.contains("successful")){
            doc.insertString(doc.getLength(), "\n" + msgOut, style);
        }   
        else{
            doc.insertString(doc.getLength(), "\nYou say : " + msgOut, style);
        }
        return style;
    }
    
    public static SimpleAttributeSet setStyleMessageRecieved(JTextPane jTextPane, String msgIn) throws BadLocationException{
        StyledDocument doc = jTextPane.getStyledDocument();
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setBackground(style, Color.GREEN);
        StyleConstants.setBold(style, true);
        doc.insertString(doc.getLength(), "\n" + msgIn, style);
        return style;
    }
    
}
