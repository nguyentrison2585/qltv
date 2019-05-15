/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

public class MuonTra {
    private String maPhieuMuon;
    private String maSach;
    private String ngayTra;
    private String tienPhat;
    
    public MuonTra() {
        
    }
    public MuonTra(String maPhieuMuon, String maSach, String ngayTra, String tienPhat) {
        this.maPhieuMuon = maPhieuMuon;
        this.maSach = maSach;
        this.ngayTra = ngayTra;
        this.tienPhat = tienPhat;
    }
    
    public String getMaPhieuMuon() {
        return maPhieuMuon;
    }

    public void setMaPhieuMuon(String maPhieuMuon) {
        this.maPhieuMuon = maPhieuMuon;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(String ngayTra) {
        this.ngayTra = ngayTra;
    }

    public String getTienPhat() {
        return tienPhat;
    }

    public void setTienPhat(String tienPhat) {
        this.tienPhat = tienPhat;
    }
     
}
