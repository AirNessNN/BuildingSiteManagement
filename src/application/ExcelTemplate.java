package application;

import dbManager.Bean;
import dbManager.ExcelFile;
import dbManager.Info;
import dbManager.PropertyFactory;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.*;
import resource.Resource;

import java.util.Vector;

public class ExcelTemplate {

    private int propertySize=0;

    private ExcelFile excelFile=new ExcelFile(){
        @Override
        public void fillData(boolean isLoaded, XSSFWorkbook workbook, Vector<Vector> data) {
            if (!isLoaded)return;
            //提取属性名称
            Bean workerProperty=PropertyFactory.createWorker();
            String[] properties=new String[workerProperty.getSize()-3];
            int index=0;
            for (Info info:workerProperty.getArray()){
                if (info.getName().equals(PropertyFactory.LABEL_NUMBER)||info.getName().equals(PropertyFactory.LABEL_AGE)||info.getName().equals(PropertyFactory.LABEL_BIRTH))continue;
                properties[index++]=info.getName();
            }
            propertySize=properties.length;

            //设置样式
            XSSFCellStyle style=workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setBorderBottom(CellStyle.SOLID_FOREGROUND);
            style.setBorderTop(CellStyle.SOLID_FOREGROUND);
            style.setBorderLeft(CellStyle.SOLID_FOREGROUND);
            style.setBorderRight(CellStyle.SOLID_FOREGROUND);
            //填充名称
            XSSFSheet sheet=workbook.getSheet("Sheet1");
            sheet.createRow(0).createCell(0).setCellValue("*为必填，其他选填");
            XSSFRow row=sheet.createRow(1);
            for (int i=0;i<properties.length;i++){
                if (properties[i].equals(PropertyFactory.LABEL_ID_CARD)||properties[i].equals(PropertyFactory.LABEL_NAME))properties[i]="*"+properties[i];//标记必填
                XSSFCell cell=row.createCell(i);
                cell.setCellValue(properties[i]);
                cell.setCellStyle(style);
            }
        }
    };

    public ExcelTemplate(){
        excelFile.createWorkbook(Resource.getApplicationDirectoryPath()+"\\template.xlsx");
    }

    public void createTemplate(){
        excelFile.doFill(null);
    }

    public Vector<Vector<String>>getTemplateData(){
        return excelFile.getExcelData(0,0,propertySize-1);
    }

}
