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

import com.runnerwoo.rfid.dao.GoodsDAO;
import com.runnerwoo.rfid.entity.Goods;
import static com.runnerwoo.rfid.locale.I18N.I18N;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 */
public class GoodsControllerPanel extends JPanel {
    private GoodsDAO dao;
    private GoodsListPanel list;
    private JButton btnAdd;
    private JButton btnFind;
    
    public GoodsControllerPanel(GoodsDAO dao, GoodsListPanel list) {
        super();
        this.dao = dao;
        this.list = list;
        initComponents();
    }

    private void initComponents() {
        
        btnAdd = new JButton(I18N.getString("btn_in"));
        btnFind = new JButton(I18N.getString("btn_find"));
        
        btnAdd.addActionListener((ActionEvent e) -> {
            btnAddActionPerformed(e);
        });
        btnFind.addActionListener((ActionEvent e) -> {
            btnFindActionPerformed(e);
        });
        
        setBorder(new EmptyBorder(10, 10, 10, 12));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(btnAdd);
        add(Box.createVerticalStrut(5));
        add(btnFind);
        
    }

    private void btnAddActionPerformed(ActionEvent e) {
        GoodsIntoForm form = new GoodsIntoForm();
        
        int result = JOptionPane.showConfirmDialog(this.getParent(), form,
                I18N.getString("form_in"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            Goods g = form.getGoods();
            if (g.getName() != null && !g.getName().equals("")) {
                dao.create(g);
            }
        }
        
        list.updateData();
    }

    private void btnFindActionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
