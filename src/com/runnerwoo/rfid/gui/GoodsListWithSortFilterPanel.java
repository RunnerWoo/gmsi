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

import com.runnerwoo.rfid.dao.GoodsDAO;
import com.runnerwoo.rfid.entity.Goods;
import static com.runnerwoo.rfid.locale.I18N.I18N;
import com.runnerwoo.rfid.util.SerialCommUtil;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 * This panel is to display goods list with sort filter. Thanks to
 * http://stackoverflow.com/a/22067320
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com>
 */
public class GoodsListWithSortFilterPanel extends JPanel implements GoodsList {

    private final GoodsDAO dao;
    private GoodsTableModel model;
    private JTable table;
    private TableRowSorter<TableModel> rowSorter;
    private JLabel jlFilter;
    private JTextField jtfFilter;
    private JButton jbFilter;
    private SerialPort serialport;
    private JButton btnUpdatePos;
    private JButton btnConnect;
    private JSignalLight light;
    private boolean on = false;

    public GoodsListWithSortFilterPanel(GoodsDAO dao) {
        super();
        this.dao = dao;
        initComponents();
    }

    @Override
    public void updateData() {
        model.setData(dao.findAll());
    }

    private void initComponents() {
        jlFilter = new JLabel(I18N.getString("lbl_find_by_key"));
        jtfFilter = new JTextField();
        jbFilter = new JButton(I18N.getString("btn_find"));
        model = new GoodsTableModel(dao.findAll());
        table = new JTable(model);
        rowSorter = new TableRowSorter<>(model);
        JPanel panel = new JPanel(new BorderLayout());
        btnUpdatePos = new JButton(I18N.getString("btn_update_pos"));
        btnConnect = new JButton(I18N.getString("btn_connect"));
        light = new JSignalLight();
        JPanel btnpanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 5));

        btnUpdatePos.setEnabled(false);
        btnUpdatePos.addActionListener((ActionEvent e) -> {
            btnUpdatePosActionPerformed(e);
        });
        btnConnect.addActionListener((ActionEvent e) -> {
            btnConnectActionPerformed(e);
        });

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setMinWidth(15);
        table.getColumnModel().getColumn(1).setMinWidth(65);
        table.getColumnModel().getColumn(2).setMinWidth(65);
        table.getColumnModel().getColumn(3).setMinWidth(65);
        table.getColumnModel().getColumn(4).setMinWidth(175);
        table.getColumnModel().getColumn(5).setMinWidth(175);
        table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            tableSelectionEvent(e);
        });
        table.setRowSorter(rowSorter);
        
        jtfFilter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                findFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                findFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                findFilter();
            }
        });

        jbFilter.addActionListener((ActionEvent ae) -> {
            findFilter();
        });

        panel.add(jlFilter, BorderLayout.WEST);
        panel.add(jtfFilter, BorderLayout.CENTER);
//        panel.add(jbFilter, BorderLayout.EAST);

        btnpanel.add(light);
        btnpanel.add(btnConnect);
        btnpanel.add(btnUpdatePos);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.SOUTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnpanel, BorderLayout.NORTH);
    }

    private void tableSelectionEvent(ListSelectionEvent e) {
        int row = table.getSelectedRow();
        if (row > -1 && e.getValueIsAdjusting()) {
            Goods g = model.getSelectedGoods(row);
            System.out.println(g.toString());

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

            updateData();
        }
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
                    initIOStream();
                    light.openStatus();
                    btnConnect.setText(I18N.getString("btn_disconnect"));
                    on = true;
                }
            }
        } else {
            SerialCommUtil.closeSerialPort(serialport);
            btnConnect.setText(I18N.getString("btn_connect"));
            light.closeStatus();
            on = false;
        }

        btnUpdatePos.setEnabled(on);
    }

    private void btnUpdatePosActionPerformed(ActionEvent e) {

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

    private void findFilter() {
        String text = jtfFilter.getText();
        if (text.trim().length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private class SerialPortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {
                if (event.getEventValue() > 0) {
                    try {
                        byte buffer[] = serialport.readBytes();
                        String getString = new String(buffer);
                        parseDataFromSerialPort(getString);
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

        private void parseDataFromSerialPort(String string) {
            System.out.println(string);
            Map values = splitToMap(string);
            for (Object entry : values.entrySet()) {
                String key = ((Map.Entry<String, String>) entry).getKey();
                String value = ((Map.Entry<String, String>) entry).getValue();
                dao.updatePosByTag(value, key);
            }
//            System.out.println(values.toString());
            updateData();
        }

        private Map<String, String> splitToMap(String in) {
            String[] tokens = in.split("#");
            Map<String, String> map = new HashMap();
            for (String token : tokens) {
                if (token.isEmpty()) {
                    continue;
                }
                String[] keyValue = token.split("@");
                map.put(keyValue[0], keyValue[1]);
            }
            return map;
        }
    }
}
