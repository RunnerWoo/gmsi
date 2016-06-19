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
import static com.runnerwoo.rfid.gui.MainFrame.DATE_FORMAT;
import static com.runnerwoo.rfid.locale.I18N.I18N;
import java.util.Date;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 */
public class GoodsForm extends JForm {

    public static final Object[] OPTIONS = {
        I18N.getString("btn_update"),
        I18N.getString("btn_out"),
        I18N.getString("btn_cancel")
    };

    private final String[] labels = {
        I18N.getString("db_name"),
        I18N.getString("db_tag"),
        I18N.getString("db_pos"),
        I18N.getString("db_in_time"),
        I18N.getString("db_out_time")
    };

    private Goods g;

    public GoodsForm(Goods g) {
        this.g = g;
        initComponents();
    }

    private void initComponents() {

        addFields(labels);
        fields.get(0).setText(g.getName());
        fields.get(1).setText(g.getTag());
        fields.get(2).setText(g.getPos());
        fields.get(3).setText(DATE_FORMAT.format(new Date(g.getInTime())));
        fields.get(4).setText(DATE_FORMAT.format(new Date(g.getOutTime())));
        pack();

    }

    public Goods getGoods() {
        g.setName(fields.get(0).getText().trim());
        g.setTag(fields.get(1).getText().trim());
        return g;
    }
}
