/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagement;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TableRowAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 *
 * @author Admin
 */
public class ApachePOIWord {
    public XWPFDocument doc;
    public XWPFParagraph paragraph;
    public XWPFTable table;
    public XWPFTableRow row;
    public XWPFTableCell cell;
    public XWPFRun run;
    public String title;
    public String tableTitle[];
    public DefaultTableModel model;
    public File file;
    public String path;
    public JFileChooser chooser = new JFileChooser("E:\\My Work\\2018-2\\Đồ án 1\\Báo cáo\\");
    public FileNameExtensionFilter docxFilter = new FileNameExtensionFilter("Word Document", "docx", "doc");
    public String maPM;
    public ArrayList<String> ttpm = new ArrayList<>();
    public ConnectToMySQL con = new ConnectToMySQL();
    
    public void ApachePOIWord() {
        chooser.setFileFilter(docxFilter);
    }
    
    public static void addTab(XWPFRun run, int n) {
        for (int i=0;i<n;i++) {
            run.addTab();
        }
    }
    
    public static void addBreak(XWPFRun run, int n) {
        for (int i=0;i<n;i++) {
            run.addBreak();
        }
    }
    
    public static void setRunStyle(XWPFRun run,String fontfamily, int fontsize, boolean bold, boolean italic) {
        run.setFontFamily(fontfamily);
        run.setFontSize(fontsize);
        run.setBold(bold);
        run.setItalic(italic);
        
    }
    
    public boolean choosedFile() {
        int retrieval = chooser.showSaveDialog(null);
        if (retrieval == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getAbsolutePath() + ".docx";
            file = new File(path);
            return true;
        }
        return false;
    }
    
    public void setHeader() {
        paragraph = doc.createParagraph();
        run = paragraph.createRun();
        paragraph.setSpacingAfter(10);
        setRunStyle(run, "Times New Roman", 11, true, false);
        run.setText("TRƯỜNG ĐẠI HỌC BÁCH KHOA HÀ NỘI         CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM");
        
        paragraph = doc.createParagraph();
        run = paragraph.createRun();
        paragraph.setIndentFromLeft(1400);
        paragraph.setSpacingAfter(10);
        setRunStyle(run, "Times New Roman", 11, true, false);
        run.setText("Thư viện SOICT");
        addTab(run,4);
        run.setText("Độc lập - Tự do – Hạnh phúc");
        
        paragraph = doc.createParagraph();
        run = paragraph.createRun();
        paragraph.setIndentFromLeft(1450);
        setRunStyle(run, "Times New Roman", 11, true, false);
        run.setText("Nguyễn Trí Sơn");
        addTab(run,4);
        run.setText("      -------------------------------");
    }
    
    public void setDate(Calendar c) {
        paragraph = doc.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        run = paragraph.createRun();
        setRunStyle(run, "Times New Roman", 11, false, true);
        run.setText("Ngày " + c.get(Calendar.DAY_OF_MONTH)+ " tháng " + (c.get(Calendar.MONTH)+1) 
                + " năm " + c.get(Calendar.YEAR));
        addBreak(run,3);
    }
    
    public void setTitle() {
        paragraph = doc.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        setRunStyle(run, "Times New Roman", 12, true, false);
        run.setText(title);
        addBreak(run,1);
    }
    
    public int tinhTienNop() {
        String sTienCoc = ttpm.get(8);
        String[] tachTienCoc = sTienCoc.split(" ");
        int iTienPhat = con.getTienPhat(maPM);
        int iTienCoc = Integer.parseInt(tachTienCoc[0]);
        int iTienNop = iTienPhat - iTienCoc;
        return iTienNop;
    }
    
    public void setTTPM() {
        ttpm = con.getCTPM(ttpm, maPM);
        paragraph = doc.createParagraph();
        paragraph.setIndentFromLeft(1200);
        paragraph.setSpacingAfter(10);
        run = paragraph.createRun();
        setRunStyle(run, "Times New Roman", 12, false, false);
        run.setText("Mã mượn trả: " + ttpm.get(0));
        run.addBreak();
        run.setText("Mã bạn đọc: " + ttpm.get(1));
        run.setText("                                      Tên bạn đọc: " + ttpm.get(2));
        run.addBreak();
        run.setText("Mã nhân viên: " + ttpm.get(3));
        run.setText("                                  Tên nhân viên: " + ttpm.get(4));
        run.addBreak();
        run.setText("Ngày mượn: " + ttpm.get(5));
        run.setText("                               Ngày hẹn trả: " + ttpm.get(6));
        run.addBreak();
        run.setText("Số lượng sách mượn: " + ttpm.get(7));
        run.setText("                                 Tiền cọc: " + ttpm.get(8));
        run.addBreak();
        run.setText("Tổng tiền phạt: " + con.getTienPhat(maPM) + " đồng");
        run.setText("                                 Còn phải nộp: " + tinhTienNop() + " đồng");
        run.addBreak();
    }
    
    public void setTableTitle() {
        table = doc.createTable(1, tableTitle.length);
        table.setStyleID("TableGrid");
        table.setTableAlignment(TableRowAlign.CENTER);
        row = table.getRow(0);
        cell = row.getCell(0);
        paragraph = cell.addParagraph();
        run = paragraph.createRun();
        setRunStyle(run, "Times New Roman", 12, false, false);
        run.setText(tableTitle[0]);
        run.setBold(true);
        for(int i=1;i<tableTitle.length;i++) {
            cell = row.getCell(i);
            paragraph = cell.addParagraph();
            run = paragraph.createRun();
            setRunStyle(run, "Times New Roman", 12, true, false);
            run.setText(tableTitle[i]);
        }
    }
    
    public void setTableContent() {
        for(int i=0;i<model.getRowCount();i++) {
            row = table.createRow();
            for(int j=0;j<model.getColumnCount();j++) {
                cell = row.getCell(j);
                paragraph = cell.addParagraph();
                run = paragraph.createRun();
                setRunStyle(run, "Times New Roman", 12, false, false);
                run.setText((String)model.getValueAt(i, j));
            }
        }
    }
    
    public void setFooter() {
        paragraph = doc.createParagraph();
        paragraph.setIndentFromLeft(1350);
        paragraph.setSpacingAfter(10);
        run = paragraph.createRun();
        setRunStyle(run, "Times New Roman", 12, true, false);
        run.addBreak();
        run.setText("Người lập");
        addTab(run,6);
        run.setText("Xác nhận của thủ thư");
        
        paragraph = doc.createParagraph();
        run = paragraph.createRun();
        paragraph.setIndentFromLeft(900);
        setRunStyle(run, "Times New Roman", 12, false, true);
        run.setText("(Ký và ghi rõ họ tên)");
        addTab(run,5);
        run.setText("  (Ký và ghi rõ họ tên)");
    }
    public void setContent() {
        try {
            if (choosedFile()) {
                doc = new XWPFDocument();
        
                setHeader();
                setDate(Calendar.getInstance());
                setTitle();
                setTableTitle();
                setTableContent();
                setFooter();
        
                FileOutputStream out = new FileOutputStream(file);
        
                doc.write(out);
                out.close();
                System.out.println("Document is created");
            }
        } catch (Exception e) {
            System.out.println(e);
        } 
    }
    public void setPM() {
        try {
            if (choosedFile()) {
                doc = new XWPFDocument();
        
                setHeader();
                setDate(Calendar.getInstance());
                setTitle();
                setTTPM();
                setTableTitle();
                setTableContent();
                setFooter();
        
                FileOutputStream out = new FileOutputStream(file);
        
                doc.write(out);
                out.close();
                System.out.println("Document is created");
            }
        } catch (Exception e) {
            System.out.println(e);
        } 
    }
}
