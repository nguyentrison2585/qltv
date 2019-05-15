/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
public class Sach {
    private String id;
    private String tenSach;
    private String theLoai;
    private String tenTacGia;
    private String nhaXuatBan;
    private int soLuong;
    
    public Sach() {
    }
    public Sach(String id, String tenSach, String loaiSach, String tenTacGia, String nhaXuatBan, int soLuong) {
        super();    
        this.id = id;
        this.tenSach = tenSach;
        this.theLoai = loaiSach;
        this.tenTacGia = tenTacGia;
        this.nhaXuatBan = nhaXuatBan;
        this.soLuong = soLuong;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }
    
    public String getTheLoai() {
        return theLoai;
    }
    
    public void setTheLoai(String loaiSach) {
        this.theLoai = loaiSach;
    }
    
    public String getTenTacGia() {
        return tenTacGia;
    }

    public void setTenTacGia(String tenTacGia) {
        this.tenTacGia = tenTacGia;
    }

    public String getNhaXuatBan() {
        return nhaXuatBan;
    }

    public void setNhaXuatBan(String nhaXuatBan) {
        this.nhaXuatBan = nhaXuatBan;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
