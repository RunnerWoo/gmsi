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

import com.runnerwoo.rfid.entity.Goods;
import static com.runnerwoo.rfid.locale.I18N.I18N;
import static com.runnerwoo.rfid.gui.MainFrame.DATE_FORMAT;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com>
 */
public class GoodsTableModel extends AbstractTableModel {

    private String[] columnNames = {
        I18N.getString("db_id"),
        I18N.getString("db_name"),
        I18N.getString("db_tag"),
        I18N.getString("db_in_time"),
        I18N.getString("db_out_time")
    };
    private Class[] columnClass = {
        Integer.class,
        String.class,
        String.class,
        Long.class,
        Long.class
    };
    private List<Goods> data;

    public GoodsTableModel() {
        super();
        data = new ArrayList<>();
    }

    public GoodsTableModel(List data) {
        super();
        this.data = data;
    }

    public void setData(List data) {
        this.data = data;
        fireTableDataChanged();
    }

    public Goods getSelectedGoods(int row) {
        return data.get(row);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int i) {
        return columnNames[i];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Goods g = (Goods) (data.get(row));
        switch (col) {
            case 0:
                return g.getId();
            case 1:
                return g.getName();
            case 2:
                return g.getTag();
            case 3:
                return DATE_FORMAT.format(new Date(g.getInTime()));
            case 4:
                return DATE_FORMAT.format(new Date(g.getOutTime()));
        }
        return new String();
    }
}
