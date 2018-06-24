package dbManager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

public class ExcelFile {

    String filePath=null;
    boolean isLoaded=false;

    Workbook workbook=null;

    public ExcelFile(){

    }


    public boolean createWorkbook (String filePath) {
        if (filePath == "")
            return false;

        //创建Workbook和Sheet
        workbook = new XSSFWorkbook( );
        workbook.createSheet("Sheet1");
        workbook.createSheet("Sheet2");
        workbook.createSheet("Sheet3");

        //写入
        boolean b=writeFile(filePath);
        if (!b)
            return false;
        //完成
        this.filePath = filePath;
        isLoaded = true;
        return true;
    }

    /**
     * 表头就是第一行
     * @param data
     * @return
     */
    public boolean fillData(Vector<Vector> data){

        if (data==null)
            return false;
        if (!isLoaded)
            return false;
        //获取工作表
        Sheet sheet=workbook.getSheet("sheet1");

        int rowIndex=0;
        int columnIndex;
        //填充数据
        for (Vector rowV:data){
            Row row=sheet.createRow(rowIndex++);
            columnIndex=0;
            for (Object o:rowV){
                Cell cell=row.createCell(columnIndex++);
                if (o instanceof Double||o instanceof Integer)
                    cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                if (o==null)cell.setCellValue("");
                else cell.setCellValue(o.toString());
            }
        }
        return writeFile(filePath);
    }

    public void fillDatas(boolean isLoaded,Workbook workbook,Vector<Vector> datas){

    }

    public boolean doFill(Vector<Vector> datas){
        fillDatas(isLoaded,workbook,datas);
        return writeFile(filePath);
    }


    public boolean writeFile(String path){
        try {
            File file=new File(path);
            if (!file.exists())
                file.createNewFile();
            FileOutputStream outputStream=new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
