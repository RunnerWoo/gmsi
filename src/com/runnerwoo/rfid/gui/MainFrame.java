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

import static com.runnerwoo.rfid.locale.I18N.I18N;
import com.runnerwoo.rfid.dao.GoodsDAO;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.tmatesoft.sqljet.core.SqlJetException;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com>
 */
public class MainFrame extends JFrame {

    //<editor-fold defaultstate="collapsed" desc="Variables Define">
    private static final int WIDTH = 750;
    private static final int HEIGHT = 445;
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(I18N.getString("db_time_format"));
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "admin";
    
    private DeviceControllerPanel dcPanel;
    private StatusBarPanel sbPanel;
    private GoodsListPanel glPanel;
    private GoodsControllerPanel gcPanel;
    
    private GoodsDAO goodsDao;
    //</editor-fold>

    public MainFrame() {
        if (this.loginSucceed()) {
            initComponents();
            repaint();
        } else {
            System.exit(0);
        }
    }
    
    private boolean loginSucceed() {
        String status = "";
        String title = I18N.getString("form_login");
        do {
            LoginForm form = new LoginForm(status);
            int result = JOptionPane.showOptionDialog(this,
                    form,
                    title,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    form.OPTIONS,
                    form.OPTIONS[0]);
            if (result == JOptionPane.OK_OPTION) {
                if (form.getUsername().equals(USERNAME) && form.getPassword().equals(PASSWORD)) {
                    return true;
                } else {
                    status = I18N.getString("login_error");
                }
            } else {
                return false;
            }
        } while (true);
    }
    
    private void initComponents() {
        
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);   // use exitProcedure instead of closing window
        setTitle(I18N.getString("title"));
        setLayout(new BorderLayout(5, 5));
        setSize(WIDTH, HEIGHT);

        // Window Closing Event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProcedure();
            }
        });

        //<editor-fold desc="Initialize variables">
        goodsDao = new GoodsDAO();
        
        try {
            goodsDao.open();
        } catch (SqlJetException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dcPanel = new DeviceControllerPanel();
        sbPanel = new StatusBarPanel();
        glPanel = new GoodsListPanel(goodsDao);
        gcPanel = new GoodsControllerPanel(goodsDao, glPanel);
        
        add(dcPanel, BorderLayout.NORTH);
        add(sbPanel, BorderLayout.SOUTH);
        add(glPanel, BorderLayout.CENTER);
        add(gcPanel, BorderLayout.EAST);

        //</editor-fold>
        //pack();
    }
    
    public void exitProcedure() {
        sbPanel.stopTimer();
        goodsDao.close();
        System.exit(0);
    }
    
}
