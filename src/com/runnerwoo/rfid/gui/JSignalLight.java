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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com>
 */
public class JSignalLight extends JPanel {

    private Color currentColor;
    private Color defaultColor;
    private Color openColor;
    private Color closeColor;
    
    public JSignalLight() {
        this.setPreferredSize(new Dimension(25, 25));
        currentColor = Color.LIGHT_GRAY;
        defaultColor = Color.LIGHT_GRAY;
        openColor = Color.GREEN;
        closeColor = Color.RED;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.drawOval(0, 0, 24, 24);
        g.setColor(this.currentColor);
        g.fillOval(2, 2, 20, 20);
    }
    
    public void changeColor(Color c) {
        this.currentColor = c;
        this.repaint();
    }
    
    public void openStatus() {
        this.changeColor(this.openColor);
    }
    
    public void closeStatus() {
        this.changeColor(this.closeColor);
    }
}
