/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Admin
 */
public class NhanVien extends Nguoi {
    private String matKhau;
    private String tienLuong;

    public NhanVien(String id, String hoTen, String gioiTinh, String ngaySinh, String diaChi, String soDT, String tienLuong, String matKhau ) {
        super(id, hoTen, gioiTinh, ngaySinh, diaChi, soDT);
        this.tienLuong = tienLuong;
        this.matKhau = matKhau;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getTienLuong() {
        return tienLuong;
    }

    public void setTienLuong(String tienLuong) {
        this.tienLuong = tienLuong;
    }
    
}
