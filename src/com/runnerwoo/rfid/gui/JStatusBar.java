/*
 * Copyright (C) 2016 Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com>
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Thanks to StackOverflow wiki (http://stackoverflow.com/a/13380339)
 * 
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com>
 */
public class JStatusBar extends JPanel{
    
    protected JPanel leftPanel;
    protected JPanel rightPanel;
    
    public JStatusBar() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(getWidth(), 23));
        
        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 0));
        leftPanel.setOpaque(false);
        add(leftPanel, BorderLayout.WEST);
        
        rightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 5, 0));
        rightPanel.setOpaque(false);
        add(rightPanel, BorderLayout.EAST);
    }
    
    public void setLeftComponent(JComponent component) {
        leftPanel.add(component);
    }
    
    public void addRightComponent(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 0));
        panel.add(new SeparatorPanel(Color.GRAY, Color.LIGHT_GRAY));
        panel.add(component);
        rightPanel.add(panel);
    }
}
