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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 */
public class GoodsListPanel extends JScrollPane {

    private GoodsTableModel tmGoods;
    private JTable table;
    private final GoodsDAO dao;

    public GoodsListPanel(GoodsDAO dao) {
        super();
        this.dao = dao;
        initComponents();
    }
    
    public void updateData() {
        tmGoods.setData(dao.findAll());
    }

    private void initComponents() {
        tmGoods = new GoodsTableModel(dao.findAll());
        table = new JTable(tmGoods);
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setMinWidth(15);
        table.getColumnModel().getColumn(1).setMinWidth(100);
        table.getColumnModel().getColumn(2).setMinWidth(125);
        table.getColumnModel().getColumn(3).setMinWidth(200);
        table.getColumnModel().getColumn(4).setMinWidth(200);
        table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            tableSelectionEvent(e);
        });
        
        setViewportView(table);
        
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    private void tableSelectionEvent(ListSelectionEvent e) {
        int row = table.getSelectedRow();
        if (row > -1 && e.getValueIsAdjusting()) {
            Goods g = tmGoods.getSelectedGoods(row);
            System.out.println(g.toString());
            
            String title = I18N.getString("form_out") + g.getId();
            GoodsForm form = new GoodsForm(g);
            
            int result = JOptionPane.showOptionDialog(
                    this.getParent(),
                    form,
                    title,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    form.OPTIONS,
                    form.OPTIONS[0]);
            
            g = form.getGoods();
            if (result == JOptionPane.YES_OPTION) {
                dao.update(g);
            } else if (result == JOptionPane.NO_OPTION) {
                long curTime = System.currentTimeMillis();
                g.setOutTime(curTime);
                dao.update(g);
            }
            
            updateData();
        }
    }
}
