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
import com.runnerwoo.rfid.util.SerialCommUtil;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 */
public class GoodsControllerPanel extends JPanel {

    private final GoodsDAO dao;
    private final GoodsList list;
    private JButton btnAdd;
    private JButton btnConnect;
    private JSignalLight light;
    private SerialPort serialport;
    private boolean on = false;
    private GoodsIntoForm form;
    private JRadioButton rdbInto;
    private JRadioButton rdbOut;
    private boolean io = true;

    public GoodsControllerPanel(GoodsDAO dao, GoodsList list) {
        super();
        this.dao = dao;
        this.list = list;
        initComponents();
    }

    private void initComponents() {
        form = new GoodsIntoForm();
        btnAdd = new JButton(I18N.getString("btn_in"));
        btnConnect = new JButton(I18N.getString("btn_connect"));
        light = new JSignalLight();
        rdbInto = new JRadioButton(I18N.getString("btn_in"), true);
        rdbOut = new JRadioButton(I18N.getString("btn_out"));

        btnAdd.addActionListener((ActionEvent e) -> {
            btnAddActionPerformed(e);
        });
        btnConnect.addActionListener((ActionEvent e) -> {
            btnConnectActionPerformed(e);
        });
        rdbInto.setActionCommand("in");
        rdbInto.addActionListener((ActionEvent e) -> {
            rdbIntoAndOutActionPerformed(e);
        });
        rdbOut.setActionCommand("out");
        rdbOut.addActionListener((ActionEvent e) -> {
            rdbIntoAndOutActionPerformed(e);
        });
        ButtonGroup group = new ButtonGroup();
        group.add(rdbInto);
        group.add(rdbOut);
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panel.add(light);
        panel.add(btnConnect);

        setBorder(new EmptyBorder(10, 10, 10, 12));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(25));
        add(panel);
        add(Box.createVerticalStrut(10));
        add(rdbInto);
        add(rdbOut);
        add(Box.createVerticalStrut(10));
        add(btnAdd);
        add(Box.createVerticalStrut(10));

    }

    private void btnAddActionPerformed(ActionEvent e) {
        showIntoForm();
    }

    private void btnConnectActionPerformed(ActionEvent e) {
        if (!on) {

            String[] portNames = SerialCommUtil.getPortNames();
//            String[] portNames = {"COM3"};
            if (portNames.length == 0) {
                JOptionPane.showMessageDialog(
                        this.getParent(),
                        I18N.getString("txt_no_serial_port"),
                        I18N.getString("title_serial_prot_error"),
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                String portName = (String) JOptionPane.showInputDialog(
                        this.getParent(),
                        I18N.getString("txt_select_your_serial_port"),
                        I18N.getString("title_select_your_serial_port"),
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        portNames,
                        portNames[0]
                );
                if (portName != null && portName.length() > 0) {
                    serialport = SerialCommUtil.getSerialPort(portName);
                    light.openStatus();
                    initIOStream();
                    on = !on;
                    btnConnect.setText(I18N.getString("btn_disconnect"));
                }
            }
        } else {
            SerialCommUtil.closeSerialPort(serialport);
            btnConnect.setText(I18N.getString("btn_connect"));
            light.closeStatus();
            on = !on;
        }
    }

    private void initIOStream() {
        int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
        try {
            serialport.setEventsMask(mask);
            serialport.addEventListener(new SerialPortReader());
        } catch (SerialPortException ex) {
            Logger.getLogger(GoodsListWithSortFilterPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void rdbIntoAndOutActionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "in":
                io = true;
                break;
            case "out":
                io = false;
                break;
        }
    }

    private class SerialPortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {
                if (event.getEventValue() > 0) {
                    try {
                        byte buffer[] = serialport.readBytes();
                        String tag = new String(buffer).trim();
                        if (io) {
                            form.setTagField(tag);
                            showIntoForm();
                        } else {
                            showOutForm(tag);
                        }
                    } catch (SerialPortException ex) {
                        Logger.getLogger(SerialCommUtil.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else if (event.isCTS()) {
                if (event.getEventValue() == 1) {
                    System.out.println("CTS - ON");
                } else {
                    System.out.println("CTS - OFF");
                }
            } else if (event.isDSR()) {
                if (event.getEventValue() == 1) {
                    System.out.println("DSR - ON");
                } else {
                    System.out.println("DSR - OFF");
                }
            }
        }
    }

    private void showIntoForm() {
        int result = JOptionPane.showConfirmDialog(this.getParent(), form,
                I18N.getString("form_in"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            Goods g = form.getGoods();
            if (g.getName() != null && !g.getName().equals("")) {
                dao.create(g);
                form.reset();
                list.updateData();
            }
        }
    }
    
    private void showOutForm(String tag) {
        Goods g = dao.findByTag(tag);
        String title = I18N.getString("form_out") + g.getId();
        GoodsForm form = new GoodsForm(g);

        int result = JOptionPane.showOptionDialog(
                this.getParent(),
                form,
                title,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                GoodsForm.OPTIONS,
                GoodsForm.OPTIONS[0]);

        g = form.getGoods();
        if (result == JOptionPane.YES_OPTION) {
            dao.update(g);
        } else if (result == JOptionPane.NO_OPTION) {
            long curTime = System.currentTimeMillis();
            g.setPos("");
            g.setTag("");
            g.setOutTime(curTime);
            dao.update(g);
        }

        list.updateData();
    }
}
