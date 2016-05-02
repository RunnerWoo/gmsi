/*
 * Based on Android SQLiteOpenHelper source
 * https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/java/android/database/sqlite/SQLiteOpenHelper.java
 *
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
package com.runnerwoo.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com>
 */
public abstract class SQLiteOpenHelper {

    private static final String TAG = SQLiteOpenHelper.class.getSimpleName();

    private final String mName;
    private final int mNewVersion;

    private SqlJetDb mDatabase;
    private boolean mIsInitializing;

    /**
     * Create a helper object to create, open, and/or manage a database. This
     * method always returns very quickly. The database is not actually created
     * or opened until {@link #getDatabase} is called.
     *
     * @param name
     * @param version
     */
    public SQLiteOpenHelper(String name, int version) {
        mName = name;
        mNewVersion = version;
    }

    /**
     * 
     * @return name of the SQLite database being opened, as given to 
     * the constructor.
     */
    public String getDatabaseName() {
        return mName;
    }

    /**
     * Create and/or open a database that will be used for reading and writing.
     * The first time this is called, the database will be opened and 
     * {@link #onCreate}, {@link #onUpgrade}.
     * @return a database object valid until {@link #close} is called
     * @throws SqlJetException if the database cannot be opened
     */
    public SqlJetDb getDatabase() throws SqlJetException {
        synchronized (this) {
            if (mDatabase != null) {
                if (!mDatabase.isOpen()) {
                    mDatabase = null;
                } else {
                    return mDatabase;
                }
            }

            if (mIsInitializing) {
                throw new IllegalStateException("getDatabase called recusively");
            }

            SqlJetDb db = mDatabase;
            try {
                mIsInitializing = true;

                if (mName == null || mName.equals("")) {

                } else {
                    File fileDb = new File(mName);
                    db = SqlJetDb.open(fileDb, true);
                }

                final int version = db.getOptions().getUserVersion();
                if (!db.getOptions().isAutovacuum()) {
                    db.getOptions().setAutovacuum(true);
                }
                if (version != mNewVersion) {
                    db.beginTransaction(SqlJetTransactionMode.WRITE);
                    try {
                        if (version == 0) {
                            onCreate(db);
                        } else if (version > mNewVersion) {
                            onDowngrade(db, version, mNewVersion);
                        } else {
                            onUpgrade(db, version, mNewVersion);
                        }
                        db.getOptions().setUserVersion(mNewVersion);
                    } finally {
                        db.commit();
                    }
                }
                
                mDatabase = db;
                return db;
            } finally {
                mIsInitializing = false;
                if (db != null && db != mDatabase) {
                    try {
                        db.close();
                    } catch (SqlJetException ex) {
                        Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    /**
     * Close any open database object.
     */
    public synchronized void close() {
        if (mIsInitializing) {
            throw new IllegalStateException("Closed during initialization");
        }

        if (mDatabase != null && mDatabase.isOpen()) {
            try {
                mDatabase.close();
            } catch (SqlJetException ex) {
                Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
            }
            mDatabase = null;
        }
    }
    
    /**
     * Called when the database is created for the first time. This is where the 
     * creation of tables and the initial population of the tables should happen.
     * 
     * 
     * @param db The database.
     */
    public abstract void onCreate(SqlJetDb db);
    
    
    /**
     * Called when the database needs to be upgraded..
     * 
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    public abstract void onUpgrade(SqlJetDb db, int oldVersion, int newVersion);
    
    /**
     * Called when the database needs to be downgraded.
     * 
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    public void onDowngrade(SqlJetDb db, int oldVersion, int newVersion) {}
    
}
