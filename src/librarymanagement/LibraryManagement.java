/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagement;

import java.util.ArrayList;
import java.util.Date;


/**
 *
 * @author Admin
 */
public class LibraryManagement {
    public static ArrayList<String> idNVList;
    public static ArrayList<String> pwNVList;
    public static Date date;
    public ConnectToMySQL con = new ConnectToMySQL();
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        //NhanVienMainFrame nvFrame = new NhanVienMainFrame();
        //nvFrame.disPlay();
        //QuanLyMainFrame qlFrame = new QuanLyMainFrame();
        //qlFrame.disPlay();
        LoginFrame login = new LoginFrame();
        login.display();
    }
    
}
