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
package com.runnerwoo.rfid.dao;

import com.runnerwoo.rfid.entity.Goods;
import com.runnerwoo.rfid.util.RfidSQLiteHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;
import com.runnerwoo.dao.MyBaseDao;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 */
public class GoodsDAO implements MyBaseDao<Goods>{
    private SqlJetDb db;
    private RfidSQLiteHelper dbHelper;
    private String[] allColums = {
        RfidSQLiteHelper.GOODS_COLUMN_ID,
        RfidSQLiteHelper.GOODS_COLUMN_NAME,
        RfidSQLiteHelper.GOODS_COLUMN_TAG,
        RfidSQLiteHelper.GOODS_COLUMN_IN_TIME,
        RfidSQLiteHelper.GOODS_COLUMN_OUT_TIME
    };
    private final static long LIFT_TIME = 1800000;

    public GoodsDAO() {
        dbHelper = new RfidSQLiteHelper();
    }
    
    public void open() throws SqlJetException {
        db = dbHelper.getDatabase();
    }
    
    public void close() {
        dbHelper.close();
    }

    @Override
    public void create(Goods goods) {
        Map<String, Object> values = new HashMap<>();
        values.put(RfidSQLiteHelper.GOODS_COLUMN_NAME, goods.getName());
        values.put(RfidSQLiteHelper.GOODS_COLUMN_TAG, goods.getTag());
        values.put(RfidSQLiteHelper.GOODS_COLUMN_IN_TIME, goods.getInTime());
        values.put(RfidSQLiteHelper.GOODS_COLUMN_OUT_TIME, goods.getOutTime());
        try {
            db.beginTransaction(SqlJetTransactionMode.WRITE);
            ISqlJetTable table = db.getTable(RfidSQLiteHelper.TABLE_GOODS);
            table.insertByFieldNames(values);
            db.commit();
        } catch (SqlJetException ex) {
            Logger.getLogger(GoodsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Goods goods) {
        Map<String, Object> values = new HashMap<>();
        values.put(RfidSQLiteHelper.GOODS_COLUMN_NAME, goods.getName());
        values.put(RfidSQLiteHelper.GOODS_COLUMN_TAG, goods.getTag());
        values.put(RfidSQLiteHelper.GOODS_COLUMN_IN_TIME, goods.getInTime());
        values.put(RfidSQLiteHelper.GOODS_COLUMN_OUT_TIME, (goods.getOutTime()));
        try {
            db.beginTransaction(SqlJetTransactionMode.WRITE);
            ISqlJetTable table = db.getTable(RfidSQLiteHelper.TABLE_GOODS);
            ISqlJetCursor updateCursor = table.lookup(table.getPrimaryKeyIndexName(), goods.getId());
            if (!updateCursor.eof()){
                do {
                    updateCursor.updateByFieldNames(values);
                } while (updateCursor.next());
            }
            updateCursor.close();
            db.commit();
        } catch (SqlJetException ex) {
            Logger.getLogger(GoodsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Goods goods) {
        long id = goods.getId();
        try {
            db.beginTransaction(SqlJetTransactionMode.WRITE);
            ISqlJetTable table = db.getTable(RfidSQLiteHelper.TABLE_GOODS);
            ISqlJetCursor deleteCursor = table.lookup(table.getPrimaryKeyIndexName(), id);
            if (!deleteCursor.eof()) {
                deleteCursor.delete();
            }
            deleteCursor.close();
            db.commit();
        } catch (SqlJetException ex) {
            Logger.getLogger(GoodsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Goods> findAll() {
        List<Goods> list = new ArrayList<>();
        try {
            db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
            ISqlJetTable table = db.getTable(RfidSQLiteHelper.TABLE_GOODS);
            ISqlJetCursor cursor = table.order(table.getPrimaryKeyIndexName());
            if (!cursor.eof()) {
                do {
                    list.add(encapsulate(cursor));
                } while (cursor.next());
            }
            cursor.close();
            db.commit();
        } catch (SqlJetException ex) {
            Logger.getLogger(GoodsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public Goods findById(long id) {
        Goods goods = null;
        try {
            db.beginTransaction(SqlJetTransactionMode.WRITE);
            ISqlJetTable table = db.getTable(RfidSQLiteHelper.TABLE_GOODS);
            ISqlJetCursor cursor = table.lookup(table.getPrimaryKeyIndexName(), id);
            if (!cursor.eof()) {
                goods = encapsulate(cursor);
            }
            cursor.close();
            db.commit();
        } catch (SqlJetException ex) {
            Logger.getLogger(GoodsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return goods;
    }
    
    public Goods findByTag(String tag) {
        Goods goods = null;
        try {
            db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
            ISqlJetTable table = db.getTable(RfidSQLiteHelper.TABLE_GOODS);
            ISqlJetCursor cursor = table.lookup(RfidSQLiteHelper.INDEX_TAG, tag);
            if (!cursor.eof()) {
                goods = encapsulate(cursor);
            }
            cursor.close();
            db.commit();
        } catch (SqlJetException ex) {
            Logger.getLogger(GoodsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return goods;
    }
    
    private Goods encapsulate(ISqlJetCursor cursor) throws SqlJetException {
        Goods goods = new Goods();
        if (!cursor.eof()) {
            goods.setId(cursor.getInteger(0));
            goods.setName(cursor.getString(1));
            goods.setTag(cursor.getString(2));
            goods.setInTime(cursor.getInteger(3));
            goods.setOutTime(cursor.getInteger(4));
        } else {
            return null;
        }
        return goods;
    }
}
