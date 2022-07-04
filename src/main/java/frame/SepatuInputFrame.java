package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SepatuInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JButton batalButton;
    private JButton simpanButton;
    private JTextField warnaTextField;
    private JTextField ukuranTextField;
    private JTextField jenisTextField;
    private JTextField tipeTextField;
    private JTextField hargaTextField;

    int id_sepatu;

    public void setId_sepatu(int id_sepatu) {
        this.id_sepatu = id_sepatu;
    }

    public SepatuInputFrame(){
        simpanButton.addActionListener(e -> {
            String nama_sepatu = namaTextField.getText();

            if (nama_sepatu.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi kata kunci pencarian", "" +
                                "validasi kata kunci pencarian kosong",
                        JOptionPane.WARNING_MESSAGE);
                namaTextField.requestFocus();
                return;
            }

            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
            if(id_sepatu == 0) {
                String cekSQL = "SELECT * FROM sepatu WHERE nama_sepatu = ?";
                ps = c.prepareStatement(cekSQL);
                ps.setString(1,nama_sepatu);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    JOptionPane.showMessageDialog(null,
                            "Data nama sepatu sama sudah ada",
                            "Validasi data sama",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    String insertSQL = "INSERT INTO sepatu SET nama_sepatu = ?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1,nama_sepatu);
                    ps.executeUpdate();
                    dispose();
                }
                } else{
                String cekSQL = "SELECT * FROM sepatu WHERE nama_sepatu = ?";
                ps = c.prepareStatement(cekSQL);
                ps.setString(1,nama_sepatu);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    JOptionPane.showMessageDialog(null,
                            "Data nama sepatu sama sudah ada",
                            "Validasi data sama",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    String updateSQL = "UPDATE sepatu SET nama_sepatu = ? WHERE id = ?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama_sepatu);
                    ps.setInt(2, id_sepatu);
                    ps.executeUpdate();
                    dispose();
                }
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        batalButton.addActionListener(e -> {
            dispose();
        });
        init();
    }
    public void init(){
            setContentPane(mainPanel);
            setTitle("Input Sepatu");
            pack();
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
    }

    public void isiKomponen() throws SQLException {
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM sepatu WHERE id_sepatu = ?";
        PreparedStatement ps;

        try {
        ps = c.prepareStatement(findSQL);
        ps.setInt(1, id_sepatu);
            ResultSet rs = ps.executeQuery();
            if (rs.next());{
                idTextField.setText(String.valueOf(rs.getInt("id_sepatu")));
                namaTextField.setText(rs.getString("nama_sepatu"));
                warnaTextField.setText(rs.getString("warna"));
                ukuranTextField.setText(String.valueOf(rs.getInt("ukuran")));
                jenisTextField.setText(rs.getString("jenis"));
                tipeTextField.setText(rs.getString("tipe"));
                hargaTextField.setText(rs.getString("harga"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
