/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.Sach;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


/**
 *
 * @author Admin
 */
public class ApachePOIExcel {
    public Workbook workbook;
    public Sheet sheet;
    public Row row;
    public Cell cell;
    public File file;
    public String path;
    public InputStream input;
    public JFileChooser chooser = new JFileChooser("E:\\My Work\\2018-2\\Đồ án 1\\Dữ liệu excel");
    public FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("Excel Workbook", "xlxs", "xls");
    public Sach sach;
    public ArrayList<Sach> listSach = new ArrayList<>();
    
    public void ApachePOIExcel() {
        chooser.setFileFilter(excelFilter);
    }
    public boolean choosedFile() {
        int retrieval = chooser.showOpenDialog(null);
        if (retrieval == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getAbsolutePath();
            file = new File(path);
            return true;
        }
        return false;
    }
    
    public void openFile() throws IOException {
        try {
            input = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            
        }
        if (path.endsWith("xls")) {
            workbook = new HSSFWorkbook(input);
        }
        else if (path.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(input);
        }
    }
    
    public ArrayList<Sach> getSachData() {
        if (choosedFile()){
            try {
                openFile();
            } catch (IOException ex) {
                Logger.getLogger(ApachePOIExcel.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Lấy sheet
            sheet = workbook.getSheetAt(0);
            //Lấy tất cả các dòng
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                //Bỏ qua tiêu đề
                if (row.getRowNum() == 0) {
                    continue;
                }
                //Lấy tất cả các ô của dòng
                Iterator<Cell> cellIterator = row.cellIterator();
                //Lấy dữ liệu và gán giá trị cho đối tượng Sách
                sach = new Sach();
                while (cellIterator.hasNext()) {
                    cell = cellIterator.next();
                    setSachProperties(cell); 
                }
                System.out.print(sach.getId()+sach.getTenSach()+sach.getTheLoai()+sach.getTenTacGia()+sach.getNhaXuatBan()+sach.getSoLuong());
                listSach.add(sach);
            }
        }
        try {
            workbook.close();
            input.close();
        } catch (IOException e) {}
        return listSach;
    }
    
    //Gán các thuộc tính cho đối tượng sach
    public void setSachProperties(Cell cell) {
        int columnIndex = cell.getColumnIndex();
        Object cellValue = getCellValue(cell);
        switch(columnIndex) {
            case 0:
                sach.setId((String)cellValue);
                break;
            case 1:
                sach.setTenSach((String)cellValue);
                break;
            case 2:
                sach.setTheLoai((String)cellValue);
                break;
            case 3:
                sach.setTenTacGia((String)cellValue);
                break;
            case 4:
                sach.setNhaXuatBan((String)cellValue);
                break;
            case 5:
                sach.setSoLuong(new BigDecimal((double) cellValue).intValue());
                break;
        } 
    }
    
    //Lấy giá trị của ô
    public static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch(cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case _NONE:
            case BLANK:
            case ERROR:
                break;
            default: break;
        }
        return cellValue;
    }
}
