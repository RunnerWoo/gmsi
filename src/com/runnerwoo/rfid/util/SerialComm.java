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
package com.runnerwoo.rfid.util;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com>
 */
public class SerialComm implements SerialPortEventListener {

    final static int TIMEOUT = 2000;
    final static int NEW_LINE_ASCII = 10;

    private Enumeration ports = null;
    private HashMap portMap = new HashMap();
    private CommPortIdentifier selectedPortIdentifier;
    private SerialPort serialPort = null;
    private InputStream input = null;
    private OutputStream output = null;
    private boolean isConnected = false;

    public boolean isIsConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
    
    public SerialComm() {
        super();
        searchForPorts();
    }

    //<editor-fold desc="Search available ports">
    private void searchForPorts() {
        ports = CommPortIdentifier.getPortIdentifiers();
        while (ports.hasMoreElements()) {
            CommPortIdentifier curPort = (CommPortIdentifier) ports.nextElement();
            // Select Serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portMap.put(curPort.getName(), curPort);
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold desc="Get available port names">
    public List<String> getPortNames() {
        List<String> list = new ArrayList<>();
        for (Object key : portMap.keySet()) {
            list.add((String)key);
        }
        return list;
    }
    //</editor-fold>

    //<editor-fold desc="Connect the ports">
    public boolean connect(String portName) {
        selectedPortIdentifier = (CommPortIdentifier) portMap.get(portName);

        CommPort commPort = null;
        try {
            commPort = selectedPortIdentifier.open(portName, TIMEOUT);
            serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            initIOStream();
            initListener();
            isConnected = true;
        } catch (PortInUseException ex) {
            Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (UnsupportedCommOperationException ex) {
            Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    //</editor-fold>

    //<editor-fold desc="Initialize IOStream">
    private boolean initIOStream() {
        try {
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    //</editor-fold>

    //<editor-fold desc="Initialize serial event listener">
    private void initListener() {
        try {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (TooManyListenersException ex) {
            Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold desc="@Override serialEvent">
    @Override
    public void serialEvent(SerialPortEvent event) {
        byte[] buffer = new byte[1024];
        int len = 0;
        int data;
        switch (event.getEventType()) {
            case SerialPortEvent.DATA_AVAILABLE:
                try {
                    len = 0;
                    while ((data = input.read()) > -1) {
                        if (data == NEW_LINE_ASCII) {
                            break;
                        }
                        buffer[len++] = (byte) data;
                    }
//                    String str = (new String(buffer, 0, len)).trim();
//                    String[] tag_pos = str.split(" ");
//                    switch (tag_pos.length) {
//                        case 2:
//                            txtPosition.setText(tag_pos[1]);
//                        case 1:
//                            txtTag.setText(tag_pos[0]);
//                        case 0:
//                        default:
//                            break;
//                    }
                    String str = (new String(buffer, 0, len)).trim();
                    System.out.println(str);
                } catch (IOException ex) {
                    Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }
    //</editor-fold>
    
    //<editor-fold desc="Disconnect serial port">
    public boolean disconnect() {
        try {
            input.close();
            output.close();
            serialPort.removeEventListener();
            serialPort.close();
            isConnected = false;
            return true;
        } catch (IOException ex) {
            Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
    }
    //</editor-fold>
    
    //<editor-fold desc="writeData">
    public boolean writeData(String s) {
        try {
            output.write(s.getBytes());
            output.flush();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    //</editor-fold>
}
