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

import com.runnerwoo.rfid.entity.Goods;
import static com.runnerwoo.rfid.locale.I18N.I18N;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 */
public class GoodsIntoForm extends JForm {

    public final static long LIFE_TIME = 8640000000L;   // 100 days
    
    private final String[] labels = {
        I18N.getString("db_name"),
        I18N.getString("db_tag")
    };

    private Goods g;
    private JButton btnReset;
    
    public GoodsIntoForm() {
        super();
        initComponents();
    }

    private void initComponents() {
        btnReset = new JButton(I18N.getString("btn_reset"));
        
        btnReset.addActionListener((ActionEvent e) -> {
            btnResetActionPerformed(e);
        });
        
        addFields(labels);
        addButtons(btnReset);
        pack();
    }

    public Goods getGoods() {
        long curTime = System.currentTimeMillis();
        g = new Goods();
        g.setName(fields.get(0).getText().trim());
        g.setTag(fields.get(1).getText().trim());
        g.setInTime(curTime);
        g.setOutTime(curTime + LIFE_TIME);
        return g;
    }

    private void btnResetActionPerformed(ActionEvent e) {
        reset();
    }
    
    public void reset() {
        for (JTextField txt : fields) {
            txt.setText("");
        }
    }
    
    public JTextField getTagField() {
        return fields.get(1);
    }
 
    public void setTagField(String s) {
        fields.get(1).setText(s);
    }
}
