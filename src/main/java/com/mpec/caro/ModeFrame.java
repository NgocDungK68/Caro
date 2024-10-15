package com.mpec.caro;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ModeFrame extends JFrame implements ActionListener {

    public ModeFrame() {
        this.setTitle("Caro");
        this.setSize(220, 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        this.add(panel);
        panel.add(new JLabel("Choose game mode: "));
        
        JButton button1 = new JButton("Two players  ");
        JButton button2 = new JButton("Single player");

        panel.add(button1);
        panel.add(button2);
        button1.addActionListener(this);
        button2.addActionListener(this);
        button2.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {        
        this.dispose();
        MyWindow mw = new MyWindow(e.getActionCommand());
    }
}
