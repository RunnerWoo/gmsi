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
package com.runnerwoo.rfid.util;

import com.runnerwoo.util.SQLiteOpenHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 */
public class RfidSQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_GOODS = "goods";
    public static final String GOODS_COLUMN_ID = "id";
    public static final String GOODS_COLUMN_NAME = "name";
    public static final String GOODS_COLUMN_TAG = "tag";
    public static final String GOODS_COLUMN_IN_TIME = "in_time";
    public static final String GOODS_COLUMN_OUT_TIME = "out_time";
    public static final String INDEX_TAG = "index_tag";
    public static final String DATABASE_NAME = "rfid.db";
    public static final int DATABASE_VERSION = 1;
    
    public RfidSQLiteHelper() {
        super(DATABASE_NAME, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SqlJetDb db) {
        String createTableGoodsQuery = "CREATE TABLE " + TABLE_GOODS + "("
                + GOODS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + GOODS_COLUMN_NAME + " TEXT NOT NULL,"
                + GOODS_COLUMN_TAG + " TEXT,"
                + GOODS_COLUMN_IN_TIME + " INTEGER NOT NULL,"
                + GOODS_COLUMN_OUT_TIME + " INTEGER NOT NULL)";
        String createIndexTagQuery = "CREATE INDEX " + INDEX_TAG + " ON "
                + TABLE_GOODS + "(" + GOODS_COLUMN_TAG + ")";
        try {
            db.createTable(createTableGoodsQuery);
            db.createIndex(createIndexTagQuery);
        } catch (SqlJetException ex) {
            Logger.getLogger(RfidSQLiteHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onUpgrade(SqlJetDb db, int oldVersion, int newVersion) {
    }
}
