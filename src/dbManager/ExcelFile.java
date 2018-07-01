package dbManager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Vector;

public class ExcelFile {

    private String filePath=null;
    private boolean isLoaded=false;

    private XSSFWorkbook workbook=null;

    public ExcelFile(){
    }

    public ExcelFile(String src) throws IOException {
        InputStream in=new FileInputStream(src);
        workbook=new XSSFWorkbook(in);
        isLoaded=true;
    }


    public void createWorkbook (String filePath) {
        if (filePath.equals(""))
            return;

        //创建Workbook和Sheet
        workbook = new XSSFWorkbook( );
        workbook.createSheet("Sheet1");
        workbook.createSheet("Sheet2");
        workbook.createSheet("Sheet3");

        //写入
        boolean b=writeFile(filePath);
        if (!b)
            return;
        //完成
        this.filePath = filePath;
        isLoaded = true;
    }

    /**
     * 表头就是第一行
     * @param data 数据
     * @return 成功返回true
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

    /**
     * 用来重写的自定义填充逻辑
     * @param isLoaded 是否已经加载
     * @param workbook 工作簿
     * @param data 数据源
     */
    public void fillData(boolean isLoaded, XSSFWorkbook workbook, Vector<Vector> data){

    }

    public boolean doFill(Vector<Vector> data){
        fillData(isLoaded,workbook,data);
        return writeFile(filePath);
    }

    /**
     * 获取Excel中的数据
     * @param rowStart 开始的行下标
     * @param rowEnd 结束的行下标
     * @param columnStart 开始的列下标
     * @param columnEnd 结束的列下标
     * @return 返回选择的数据区域，如果所选区域是空的，则会返回等量的空集合，所选工作表不存在，则返回null
     */
    public Vector<Vector<String>> getExcelData(int sheetIndex,int rowStart, int rowEnd,int columnStart,int columnEnd){
        Vector<Vector<String>> vectors=new Vector<>();

        if (isLoaded){

            Sheet sheet=workbook.getSheetAt(sheetIndex);
            if (sheet==null)return null;

            for (int i=rowStart;i<=rowEnd;i++){//遍历行

                Row row=sheet.getRow(i);
                Vector<String> rows=new Vector<>();

                for (int j=columnStart;j<=columnEnd;j++){//遍历列
                    if (row==null){
                        rows.add("");
                        continue;
                    }
                    Cell cell=row.getCell(j);
                    if (cell==null)rows.add("");
                }
                vectors.add(rows);
            }
            return vectors;
        }
        return null;
    }

    public Vector<Vector<String>> getExcelData(int sheetIndex,int startCol, int endCol){
        if (!isLoaded)return null;

        XSSFSheet sheet=workbook.getSheetAt(sheetIndex);
        if (sheet==null)return null;

        return getExcelData(sheetIndex,sheet.getFirstRowNum(),sheet.getLastRowNum(),startCol,endCol);
    }



    public boolean writeFile(String path){
        try {
            File file=new File(path);
            if (!file.exists()) {
                boolean b=file.createNewFile();
                if (!b)return false;
            }
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
