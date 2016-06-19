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

import static com.runnerwoo.rfid.locale.I18N.I18N;
import com.runnerwoo.rfid.util.SerialCommUtil;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 */
public class DeviceControllerPanel extends JPanel {

    JButton btnConnect;
    JButton btnDisConnect;
    JButton btnOpenDevice;
    JToggleButton tbtnConnect;
    JComboBox<String> cmbSerial;
    JSignalLight light;

    public DeviceControllerPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEADING, 10, 5));

        btnConnect = new JButton(I18N.getString("btn_connect"));
        btnDisConnect = new JButton(I18N.getString("btn_disconnect"));
        btnOpenDevice = new JButton(I18N.getString("btn_open_device"));
        tbtnConnect = new JToggleButton(I18N.getString("btn_connect"));
        cmbSerial = new JComboBox<>();
        light = new JSignalLight();

        btnConnect.setEnabled(false);
        btnDisConnect.setEnabled(false);
        tbtnConnect.setEnabled(true);
        light.closeStatus();

        add(btnOpenDevice);
        add(cmbSerial);
//        add(btnConnect);
//        add(btnDisConnect);
        add(tbtnConnect);
        add(light);
        
        btnConnect.addActionListener((ActionEvent e) -> {
            btnConnectActionPerformed(e);
        });
        btnDisConnect.addActionListener((ActionEvent e) -> {
            btnDisConnectActionPerformed(e);
        });
        btnOpenDevice.addActionListener((ActionEvent e) -> {
            btnOpenDeviceActionPerformed(e);
        });
        tbtnConnect.addItemListener((ItemEvent e) -> {
            tbtnConnectItemStateChanged(e);
        });
    }
    
    private void btnConnectActionPerformed(ActionEvent e) {
        String portName = (String) cmbSerial.getSelectedItem();
        SerialCommUtil.getSerialPort(portName);
        btnOpenDevice.setEnabled(false);
        btnConnect.setEnabled(false);
        btnDisConnect.setEnabled(true);
    }

    private void btnDisConnectActionPerformed(ActionEvent e) {
        btnOpenDevice.setEnabled(true);
        btnConnect.setEnabled(true);
        btnDisConnect.setEnabled(false);
    }

    private void btnOpenDeviceActionPerformed(ActionEvent e) {
        String[] availablePorts = SerialCommUtil.getPortNames();
        cmbSerial.removeAllItems();
        if (availablePorts.length > 0) {
            btnConnect.setEnabled(true);
            tbtnConnect.setEnabled(true);
        }
        for (String port : availablePorts) {
            cmbSerial.addItem(port);
        }
    }
    
    private void tbtnConnectItemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            tbtnConnect.setText(I18N.getString("btn_disconnect"));
            light.openStatus();
        } else {
            tbtnConnect.setText(I18N.getString("btn_connect"));
            light.closeStatus();
        }
    }
}
