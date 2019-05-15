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
public class PhieuMuon {
    private String id;
    private String maBanDoc;
    private String maNhanVien;
    private int soLuong;
    private String ngayMuon;
    private String hanTra;
    private String tienCoc;
    
    public PhieuMuon() {
    }
    
    public PhieuMuon(String id, String maBanDoc, String maNhanVien, int soLuong, String ngayMuon, String hanTra, String tienCoc) {
        this.id = id;
        this.maBanDoc = maBanDoc;
        this.maNhanVien = maNhanVien;
        this.soLuong = soLuong;
        this.ngayMuon = ngayMuon;
        this.hanTra = hanTra;
        this.tienCoc = tienCoc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaBanDoc() {
        return maBanDoc;
    }

    public void setMaBanDoc(String maBanDoc) {
        this.maBanDoc = maBanDoc;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }
    
    public int getSoLuong() {
        return soLuong;
    }
    
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    
    public String getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(String ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public String getHanTra() {
        return hanTra;
    }

    public void setHanTra(String hanTra) {
        this.hanTra = hanTra;
    }

    public String getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(String tienCoc) {
        this.tienCoc = tienCoc;
    }
    
}
