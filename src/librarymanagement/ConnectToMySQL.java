/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagement;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.BanDoc;
import model.MuonTra;
import model.NhanVien;
import model.PhieuMuon;
import model.Sach;

/**
 *
 * @author Admin
 */
public class ConnectToMySQL {
    public Connection con;
    public final String CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    public final String DB_URL = "jdbc:mysql://localhost:3306/qltv";
    public final String USER_NAME = "root";
    public final String PASSWORD = "sonditnhon";
    public String command;
    public Statement state;
    public PreparedStatement pState;
    public ResultSet result;
    public ArrayList<String> idList;
    
    //Khởi tạo đối tượng
    public ConnectToMySQL() {
        connect();
    }
    
    //Kết nối đến database
    public void connect() {
        try {
            Class.forName(CLASS_NAME);
            con = DriverManager.getConnection(DB_URL,USER_NAME,PASSWORD);    
            state = con.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối tới cơ sở dữ liệu !","Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }
    
    //Ngắt kết nối
    public void disConnect() {
        try {
            con.close();
        } catch (Exception e) {
        }
    }
    
    //Lấy dữ liệu từ bảng
    //Hàm thực thi câu truy vấn và gán dữ liệu vào table model
    public DefaultTableModel query(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            result = state.executeQuery(command);
            ResultSetMetaData rsMD = result.getMetaData();
            int colCount = rsMD.getColumnCount();
            Object arr[] = new Object[colCount+1];
            int count = 1;
            while (result.next()) {
                arr[0] = Integer.toString(count++);
                for(int i=0;i<colCount;i++) {
                    arr[i+1] = result.getString(i+1);
                }
                model.addRow(arr);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi lấy dữ liệu từ cơ sở dữ liệu !", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
        return model;
    }
    
    //Lấy dữ liệu của 1 bảng đơn
    public DefaultTableModel getData(DefaultTableModel model,String table) {
        model.setRowCount(0);
        command = "select * from " + table;
        query(model);
        return model;
    }
    
    //Lấy dữ liệu khi tìm kiếm
    public DefaultTableModel getData(DefaultTableModel model, String table, String column, String value) {
        model.setRowCount(0);
        command = "select * from " + table + " where " + column + " like '%" + value + "%'";
        query(model);
        return model;
    }
    
    //Lấy dữ liệu chi tiết mượn - trả
    public DefaultTableModel getCTMT(DefaultTableModel model) {
        model.setRowCount(0);
        command = "select phieu_muon.id, ban_doc.ho_ten, nhan_vien.ho_ten, phieu_muon.so_luong, phieu_muon.ngay_muon,"
                + "phieu_muon.han_tra, phieu_muon.tien_coc from phieu_muon, ban_doc, nhan_vien where " 
                + "phieu_muon.ma_ban_doc=ban_doc.id and phieu_muon.ma_nhan_vien=nhan_vien.id";
        query(model);
        return model;
    }
    
    public DefaultTableModel getCTPM (DefaultTableModel model,String maPM) {
        model.setRowCount(0);
        command = "select muon_tra.ma_phieu_muon, muon_tra.ma_sach, sach.ten_sach, muon_tra.ngay_tra, muon_tra.tien_phat "
                + "from muon_tra, sach where muon_tra.ma_sach = sach.id and muon_tra.ma_phieu_muon = '" + maPM + "'";
        query(model);
        return model;
    }
    
    public ArrayList<String> getCTPM(ArrayList<String> ctpm, String maPM) {
        command = "select phieu_muon.id, ban_doc.id, ban_doc.ho_ten, nhan_vien.id, "
                + "nhan_vien.ho_ten, phieu_muon.ngay_muon, phieu_muon.han_tra, "
                + "phieu_muon.so_luong, phieu_muon.tien_coc from phieu_muon, ban_doc, nhan_vien where " 
                + "phieu_muon.ma_ban_doc=ban_doc.id and phieu_muon.ma_nhan_vien=nhan_vien.id and phieu_muon.id = '" +maPM + "'";
        try {
            result = state.executeQuery(command);
            if (result.next()) {
                for (int i=0;i<9;i++) {
                    ctpm.add(result.getString(i+1));
                }
            }
        } catch(SQLException e) { 
            System.out.println(e);
        }
        return ctpm;
    }

    //Lấy dữ liệu cần thiết của các bảng
    public ArrayList<String> getData(String table,String columnName) {
        idList = new ArrayList<>();
        command = "select distinct " + columnName + " from " + table + " order by id";
        try {
            result = state.executeQuery(command);
            while (result.next()) {
                idList.add(result.getString(1));
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return idList;
    }
    
    public ArrayList<String> getId(String table, String columnName, String value) {
        ResultSet resultId;
        ArrayList<String> listID = new ArrayList<>();
        command = "select id from " + table + " where " + columnName + " ='" + value + "'";
        try {
            resultId = state.executeQuery(command);
            while (resultId.next()) {
                listID.add(resultId.getString(1));
            }
            resultId.close();
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return listID;
    }
    
    public String getStringValue(String table, String column, String id) {
        String value = "";
        command = "select " + column + " from " + table + " where id = '" + id + "'";
        try {
            result = state.executeQuery(command);
            if (result.next()) {
                value = result.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return value;
    }
    
    public String getPassWord(String id) {
        String password="";
        command = "select mat_khau from nhan_vien where id='" + id + "'";
        try {
            result = state.executeQuery(command);
            if (result.next()) {
                password = result.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return password;
    }
    
    public String getMaPM(String maBD, String maSach) {
        String maPM = "";
        command = "select phieu_muon.id from phieu_muon,muon_tra where "
                + "phieu_muon.id=muon_tra.ma_phieu_muon and phieu_muon.ma_ban_doc='"
                + maBD + "' and muon_tra.ma_sach ='" + maSach + "'";
        try {
            result = state.executeQuery(command);
            if (result.next()) {
                maPM = result.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        System.out.println(maPM);
        return maPM;
    }
    
    /*public String getNgayMuon(String maPM) {
        String ngayMuon = "";
        command = "select ngay_muon from phieu_muon where id = '" + maPM + "'";
        try {
            result = state.executeQuery(command);
            if (result.next()) {
                ngayMuon = result.getString(1);
            }
        } catch (Exception e) { 
        }
        return ngayMuon;
    }*/
    
    public String getHanTra(String maBD,String maSach) {
        String hanTra = "";
        command = "select phieu_muon.han_tra from phieu_muon, muon_tra where phieu_muon.id = muon_tra.ma_phieu_muon and "
                + "phieu_muon.ma_ban_doc = '" + maBD + "' and muon_tra.ma_sach = '" + maSach + "'";
        try {
            result = state.executeQuery(command);
            if (result.next()) {
                hanTra = result.getString(1);
            }
        } catch (SQLException e) { 
            System.out.println(e);
        }
        return hanTra;
    }
    
    public int getTienPhat(String maPM) {
        int iTienPhat = 0;
        ResultSet resultTienPhat;
        command = "select tien_phat from muon_tra where ma_phieu_muon = '" + maPM + "'";
        try {
            resultTienPhat = state.executeQuery(command);
            while (resultTienPhat.next()) {
                String[] tachTienPhat = resultTienPhat.getString(1).split(" ");
                iTienPhat += Integer.parseInt(tachTienPhat[0]);
            }
            resultTienPhat.close();
        } catch (NumberFormatException | SQLException e) { 
            System.out.println(e);
        }
        return iTienPhat;
    }
    
    public int getTienCoc(String maPM) {
        int iTienCoc = 0;
        ResultSet resultTienCoc;
        command = "select tien_coc from phieu_muon where id = '" + maPM + "'";
        try {
            resultTienCoc = state.executeQuery(command);
            while (resultTienCoc.next()) {
                String[] tachTienPhat = resultTienCoc.getString(1).split(" ");
                iTienCoc += Integer.parseInt(tachTienPhat[0]); 
            }
            resultTienCoc.close();
        } catch (NumberFormatException | SQLException e) { 
            System.out.println(e);
        }
        return iTienCoc;
    }
    
    //Lấy dữ liệu thống kê
    public DefaultTableModel thongKeCoBan(DefaultTableModel model, String table, String column) {
        command = "select " + column + ", count(id) from " + table + " group by " + column;
        query(model);
        return model;
    }
    
    public DefaultTableModel thongKePM(DefaultTableModel model, String column) {
        command = "select " + column + ", count(id) from phieu_muon group by " + column;
        try {
            model.setRowCount(0);
            result = state.executeQuery(command);
            ResultSetMetaData rsMD = result.getMetaData();
            int colCount = rsMD.getColumnCount();
            ArrayList<Object[]> listResult = new ArrayList<>();
            int count = 1;
            while (result.next()) {
                Object arr[] = new Object[5];
                arr[0] = Integer.toString(count++);
                for(int i=0;i<colCount;i++) {
                    arr[i+1] = result.getString(i+1);
                }
                System.out.print(arr[1]);
                listResult.add(arr);
            }
            System.out.println(listResult.get(0)[1]);
            for (Object[] array : listResult) {
                int iTongTienCoc = 0;
                int iTongTienPhat = 0;
                ArrayList<String> listMaPM = getId("phieu_muon", column, array[1].toString());
                System.out.println(array[1]);
                for (String maPM : listMaPM) {
                        iTongTienCoc += getTienCoc(maPM);
                        iTongTienPhat += getTienPhat(maPM);
                    }
                array[3] = Integer.toString(iTongTienCoc) + " đồng";
                array[4] = Integer.toString(iTongTienPhat) + " đồng";
                System.out.println(array[0]);
                System.out.println(array[1]);
                System.out.println(array[2]);
                System.out.println(array[3]);
                System.out.println(array[4]);
                model.addRow(array);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi lấy dữ liệu từ cơ sở dữ liệu !", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
        return model;
    }
    
    //Kiếm tra giá trị của cột có tồn tại trong bảng
    public boolean check(String table, String columnName, String value) {
        command = "select * from " + table + " where " + columnName + "='" + value + "'";
        try {
            result = state.executeQuery(command);
            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }
    
    public boolean checkPM(String maBD, String maSach) {
        command = "select * from phieu_muon, muon_tra where phieu_muon.id = muon_tra.ma_phieu_muon and "
                + "phieu_muon.ma_ban_doc = '" + maBD + "' and muon_tra.ma_sach = '" + maSach + "'";
        try {
            result = state.executeQuery(command);
            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }
    
    public boolean checkMaSach(String maSach) {
        command = "select * from sach where id ='" + maSach + "'";
        try {
                result = state.executeQuery(command);
                if (result.next()) {
                    disConnect();
                    return true;
                }
            } catch (SQLException e) {
                System.out.println(e);
            }      
            return false;
    }
    
    public boolean checkMaNhanVien(String maNhanVien) {
            command = "select * from nhan_vien where id ='" + maNhanVien + "'";
            try {
                result = state.executeQuery(command);
                if (result.next()) {
                    disConnect();
                    return true;
                }
            } catch (Exception e) {
                System.out.println(e);
            }      
            return false;
    }
    
    public boolean checkMaBanDoc(String maBanDoc) {
            command = "select * from ban_doc where id ='" + maBanDoc + "'";
            try {
                result = state.executeQuery(command);
                if (result.next()) {
                    disConnect();
                    return true;
                }
            } catch (Exception e) {
                System.out.println(e);
            }      
            return false;
    }
    
    public boolean checkMaPhieuMuon(String maPhieuMuon) {
            command = "select * from phieu_muon where id ='" + maPhieuMuon + "'";
            try {
                result = state.executeQuery(command);
                if (result.next()) {
                    disConnect();
                    return true;
                }
            } catch (Exception e) {
                System.out.println(e);
            }      
            return false;
    }
    
    public boolean checkMuonTra(String maPhieuMuon,String maSach) {
            command = "select * from muon_tra where ma_phieu_muon ='" + maPhieuMuon + "' and "
                    + "ma_sach ='" + maSach + "'";
            try {
                result = state.executeQuery(command);
                if (result.next()) {
                    disConnect();
                    return true;
                }
            } catch (Exception e) {
                System.out.println(e);
            }      
            return false;
    }
    
    //Các hàm insert
    public void insertSach(Sach sach) {
        command = "insert into sach value(?, ?, ?, ?, ?, ?)";
        try {
            pState = con.prepareStatement(command);
            pState.setString(1, sach.getId());
            pState.setString(2, sach.getTenSach());
            pState.setString(3, sach.getTheLoai());
            pState.setString(4, sach.getTenTacGia());
            pState.setString(5, sach.getNhaXuatBan());
            pState.setInt(6, sach.getSoLuong());
            if (pState.executeUpdate()>0) {
                System.out.println("Thêm sách thành công");
            }
            else {
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error!",
                    "Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }
    
    public void insertNhanVien(NhanVien nhanVien) {
        command = "insert into nhan_vien value(?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pState = con.prepareStatement(command);
            pState.setString(1, nhanVien.getId());
            pState.setString(2, nhanVien.getMatKhau());
            pState.setString(3, nhanVien.getHoTen());
            pState.setString(4, nhanVien.getGioiTinh());
            pState.setString(5, nhanVien.getNgaySinh());
            pState.setString(6, nhanVien.getDiaChi());
            pState.setString(7, nhanVien.getSoDT());
            pState.setString(8, nhanVien.getTienLuong());
            if (pState.executeUpdate()>0) {
                System.out.println("Thêm nhân viên thành công");
            }
            else {
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error!",
                    "Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }
    
    public void insertBanDoc(BanDoc banDoc) {
        command = "insert into ban_doc value(?, ?, ?, ?, ?, ?)"; 
        try {
            pState = con.prepareStatement(command);
            pState.setString(1, banDoc.getId());
            pState.setString(2, banDoc.getHoTen());
            pState.setString(3, banDoc.getGioiTinh());
            pState.setString(4, banDoc.getNgaySinh());
            pState.setString(5, banDoc.getDiaChi());
            pState.setString(6, banDoc.getSoDT());
            if (pState.executeUpdate()>0) {
                System.out.println("Thêm bạn đọc thành công");
            }
            else {
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error!",
                    "Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }
    
    public void insertMuonTra(MuonTra muonTra) {
        command = "insert into muon_tra value(?, ?, ?, ?)";
        try {
            pState = con.prepareStatement(command);
            pState.setString(1, muonTra.getMaPhieuMuon());
            pState.setString(2, muonTra.getMaSach());
            pState.setString(3, muonTra.getNgayTra());
            pState.setString(4, muonTra.getTienPhat());
            if (pState.executeUpdate()>0) {
                System.out.println("Thêm thông tin mượn trả thành công");
            }
            else {
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Thêm thành công", 
                    "Message", JOptionPane.INFORMATION_MESSAGE);
            System.out.println(e);
        }
    }
    
    public void insertPhieuMuon(PhieuMuon phieuMuon) {
        command = "insert into phieu_muon value(?, ?, ?, ?, ?, ?, ?)";
        try {
            pState = con.prepareStatement(command);
            pState.setString(1, phieuMuon.getId());
            pState.setString(2, phieuMuon.getMaBanDoc());
            pState.setString(3, phieuMuon.getMaNhanVien());
            pState.setInt(4, phieuMuon.getSoLuong());
            pState.setString(5, phieuMuon.getNgayMuon());
            pState.setString(6, phieuMuon.getHanTra());
            pState.setString(7, phieuMuon.getTienCoc());
            if (pState.executeUpdate()>0) {
                System.out.println("Thêm phiếu mượn thành công");
            }
            else {
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error!",
                    "Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }
    
    //Các hàm cập nhật dữ liệu trong bảng
    public void updateSach(Sach sach) {
        command = "update sach set ten_sach = ?,the_loai = ?,ten_tac_gia = ?,"
                + " nha_xuat_ban = ?, so_luong = ? where id = ?";
        try {
            pState = con.prepareStatement(command);
            pState.setString(1, sach.getTenSach());
            pState.setString(2, sach.getTheLoai());
            pState.setString(3, sach.getTenTacGia());
            pState.setString(4, sach.getNhaXuatBan());
            pState.setInt(5, sach.getSoLuong());
            pState.setString(6, sach.getId());
            if (pState.executeUpdate()>0) {
                System.out.println("Cập nhật sách thành công");
            }
            else {
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch(HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error!",
                    "Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }
    
    public void updateNhanVien(NhanVien nhanVien) {
        command = "update nhan_vien set mat_khau = ?, ho_ten = ?, gioi_tinh = ?, ngay_sinh = ?,"
                + "dia_chi = ?, so_dt = ?, tien_luong = ? where id = ?";
        try {
            pState = con.prepareStatement(command);
            pState.setString(1, nhanVien.getMatKhau());
            pState.setString(2, nhanVien.getHoTen());
            pState.setString(3, nhanVien.getGioiTinh());
            pState.setString(4, nhanVien.getNgaySinh());
            pState.setString(5, nhanVien.getDiaChi());
            pState.setString(6, nhanVien.getSoDT());
            pState.setString(7, nhanVien.getTienLuong());
            pState.setString(8, nhanVien.getId());
            if (pState.executeUpdate()>0) {
                System.out.println("Cập nhật nhân viên thành công");
            }
            else {
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Error!",
                    "Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }
    
    public void updateBanDoc(BanDoc banDoc) {
        command = "update ban_doc set ho_ten = ?, gioi_tinh = ?, ngay_sinh = ?,dia_chi = ?,"
                + " so_dt = ? where id = ?";
        try {
            pState = con.prepareStatement(command);
            pState.setString(1, banDoc.getHoTen());
            pState.setString(2, banDoc.getGioiTinh());
            pState.setString(3, banDoc.getNgaySinh());
            pState.setString(4, banDoc.getDiaChi());
            pState.setString(5, banDoc.getSoDT());
            pState.setString(6, banDoc.getId());
            if (pState.executeUpdate()>0)   {
                System.out.println("Cập nhật bạn đọc thành công");
            }
            else {
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Error!",
                    "Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }
    
    public void updatePhieuMuon(PhieuMuon phieuMuon) {
        command = "update phieu_muon set ma_ban_doc = ?,ma_nhan_vien = ?,so_luong = ?,"
                + " ngay_muon = ?, han_tra = ?, tien_coc = ? where id = ?";
        try {
            pState = con.prepareStatement(command);
            pState.setString(1, phieuMuon.getMaBanDoc());
            pState.setString(2, phieuMuon.getMaNhanVien());
            pState.setInt(3, phieuMuon.getSoLuong());
            pState.setString(4, phieuMuon.getNgayMuon());
            pState.setString(5, phieuMuon.getHanTra());
            pState.setString(6, phieuMuon.getTienCoc());
            pState.setString(7, phieuMuon.getId());
            if (pState.executeUpdate()>0) {
                System.out.println("Cập nhật phiếu mượn thành công");
            } 
            else {
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Error!",
                    "Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }
    
    public void updateMuonTra(MuonTra muonTra) {
        command = "update muon_tra set ngay_tra = ?, tien_phat = ?"
                + " where ma_phieu_muon  = ? and ma_sach = ?";
        try {
            pState = con.prepareStatement(command);
            pState.setString(1, muonTra.getNgayTra());
            pState.setString(2, muonTra.getTienPhat());
            pState.setString(3, muonTra.getMaPhieuMuon());
            pState.setString(4, muonTra.getMaSach());
            if (pState.executeUpdate()>0) {
                System.out.println("Cập nhật mượn trả thành công");
            }
            else {
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Error!",
                    "Error",JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }
    
    //Xóa 1 dòng trong bảng dữ liệu 
    public void delete(String table, String id) {
        command = "delete from " + table + " where id = '" + id + "'";
        try {
            if  (state.executeUpdate(command)>0) {
                JOptionPane.showMessageDialog(null, "Xóa thành công",
                    "Message", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null, "Dữ liệu không tồn tại !",
                    "Error",JOptionPane.WARNING_MESSAGE);
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa dữ liệu !",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }
    
    public void deleteTTMT(String maPM) {
        command = "delete from muon_tra where ma_phieu_muon = ?";
        try {
            pState = con.prepareStatement(command);
            pState.setString(1, maPM);
            if (pState.executeUpdate()>0) {
                JOptionPane.showMessageDialog(null, "Xóa thành công",
                    "Message", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null, "Dữ liệu không tồn tại !",
                    "Error",JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa dữ liệu !",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }
    
    /*public void deleteTTMT(String maPM, String maSach) {
        command = "delete from muon_tra where ma_phieu_muon = ? and ma_sach = ?";
        try {
            pState = con.prepareStatement(command);
            pState.setString(1, maPM);
            pState.setString(2, maSach);
            if (pState.executeUpdate()>0) {
                JOptionPane.showMessageDialog(null, "Xóa thành công",
                    "Message", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null, "Dữ liệu không tồn tại !",
                    "Error",JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa dữ liệu !",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }*/
    
}
