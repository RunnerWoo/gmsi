/*
 * Copyright (C) 2016 Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runnerwoo.rfid.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import oracle.java.layout.SpringUtilities;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 */
public abstract class JForm extends JPanel {

    private final JPanel mainPanel;
    private final JPanel buttonPanel;
    protected List<JTextField> fields;
    private int size;

    public JForm() {
        super();

        fields = new ArrayList<>();
        mainPanel = new JPanel();
        buttonPanel = new JPanel();
        size = 0;

        setLayout(new BorderLayout(10, 10));
        mainPanel.setLayout(new SpringLayout());
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 7, 7));

    }

    public void addFields(String... labels) {
        for (String label : labels) {
            addField(label, false);
        }
    }

    public void addPasswordFields(String... labels) {
        for (String label : labels) {
            addField(label, true);
        }
    }

    public void addField(String label, boolean isPassword) {
        if (label == null || label.trim().equals("")) {
            throw new NullPointerException();
        }
        JLabel l = new JLabel(label, JLabel.TRAILING);
        JTextField t;
        if (isPassword) {
            t = new JPasswordField(18);
        } else {
            t = new JTextField(18);
        }
        l.setLabelFor(t);
        mainPanel.add(l);
        mainPanel.add(t);
        fields.add(t);
        size += 2;
    }

    public void addLabels(String... labels) {
        for (String label : labels) {
            addLabel(label, Color.BLACK);
        }
    }
    
    public void addLabel(String label, Color c) {
        JLabel l = new JLabel(label, JLabel.TRAILING);
        l.setForeground(c);
        size++;
        mainPanel.add(l);
    }

    public void addButtons(JButton... buttons) {
        for (JButton button : buttons) {
            if (button == null) {
                throw new NullPointerException();
            }
            buttonPanel.add(button);
        }
    }

    protected void pack() {
        updateMainPanel();
        mainPanel.setOpaque(true);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateMainPanel() {
        SpringUtilities.makeCompactGrid(mainPanel,
                (int) Math.ceil(size / 2), 2,
                7, 7,
                7, 7);
    }

}
