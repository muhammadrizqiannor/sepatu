package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class SepatuViewFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public SepatuViewFrame(){
        ubahButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih < 0) {
                JOptionPane.showMessageDialog(null,
                        "Pilih Data Dulu",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            TableModel tm = viewTable.getModel();
            int id_sepatu = Integer.parseInt(tm.getValueAt(barisTerpilih,0).toString());
            SepatuInputFrame inputFrame = new SepatuInputFrame();
            inputFrame.setId_sepatu(id_sepatu);
            inputFrame.setVisible(true);
        });
        tambahButton.addActionListener(e -> {
            SepatuInputFrame inputFrame = new SepatuInputFrame();
            inputFrame.setVisible(true);
        });
        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih < 0) {
                JOptionPane.showMessageDialog(null,
                        "Pilih Data Dulu",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            int pilihan = JOptionPane.showConfirmDialog(null,
                    "Yakin Mau Hapus?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION);

            if (pilihan == 0 ){
                TableModel tm = viewTable.getModel();
                int id_sepatu = Integer.parseInt(tm.getValueAt(barisTerpilih,0).toString());
                Connection c = Koneksi.getConnection();
                String deleteSQL = "DELETE FROM sepatu WHERE id_sepatu = ?";
                try {
                    PreparedStatement ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1, id_sepatu);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        cariButton.addActionListener(e -> {

            String keyword = cariTextField.getText();
            if (keyword.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi kata kunci pencarian", "" +
                        "validasi kata kunci pencarian kosong",
                        JOptionPane.WARNING_MESSAGE);
                cariTextField.requestFocus();
                return;
            }
            Connection c = Koneksi.getConnection();
            keyword = "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT * FROM sepatu WHERE nama_sepatu like ?";

            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1,keyword);
                ResultSet rs = ps.executeQuery();

                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[7];

                while (rs.next()){
                    row[0] = rs.getInt("id_sepatu");
                    row[1] = rs.getString("nama_sepatu");
                    row[2] = rs.getString("warna");
                    row[3] = rs.getInt("ukuran");
                    row[4] = rs.getString("jenis");
                    row[5] = rs.getString("tipe");
                    row[6] = rs.getString("harga");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTabel();
        });
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTabel();
            }
        });
        isiTabel();
        init();
    }
    public void init(){
        setContentPane(mainPanel);
        setTitle("Data Sepatu");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiTabel(){
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM sepatu";

        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String[] header = {"Id","Nama Sepatu"};
            DefaultTableModel dtm = new DefaultTableModel(header, 0);
            viewTable.setModel(dtm);

            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);

            Object[] row = new Object[7];
            while (rs.next()){
                row[0] = rs.getInt("id_sepatu");
                row[1] = rs.getString("nama_sepatu");
                row[2] = rs.getString("warna");
                row[3] = rs.getInt("ukuran");
                row[4] = rs.getString("jenis");
                row[5] = rs.getString("tipe");
                row[6] = rs.getString("harga");

                dtm.addRow(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
