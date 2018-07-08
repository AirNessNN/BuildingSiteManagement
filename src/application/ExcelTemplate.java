package application;

import dbManager.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;
import resource.Resource;

import java.io.IOException;
import java.util.Vector;

public class ExcelTemplate {

    private int propertySize=9;

    public static final String filePath=Resource.getApplicationDirectoryPath()+"\\template.xlsx";

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
            System.out.println("propertySize:"+propertySize);

            //设置样式
            XSSFCellStyle style=workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setBorderBottom(CellStyle.SOLID_FOREGROUND);
            style.setBorderTop(CellStyle.SOLID_FOREGROUND);
            style.setBorderLeft(CellStyle.SOLID_FOREGROUND);
            style.setBorderRight(CellStyle.SOLID_FOREGROUND);
            //填充名称
            XSSFSheet sheet=workbook.getSheet("Sheet1");
            sheet.createRow(0).createCell(0).setCellValue("*为必填，其他选填，请不要修改标题名字，因为名字是数据的标签，没有这些名字我们没办法正确填充信息");
            XSSFRow row=sheet.createRow(1);
            for (int i=0;i<properties.length;i++){
                if (properties[i].equals(PropertyFactory.LABEL_ID_CARD)||properties[i].equals(PropertyFactory.LABEL_NAME))properties[i]="*"+properties[i];//标记必填
                XSSFCell cell=row.createCell(i);
                cell.setCellValue(properties[i]);
                System.out.println(cell.getStringCellValue());
                cell.setCellStyle(style);
            }
        }
    };

    public void createTemplate(){
        excelFile.createWorkbook(filePath);
        excelFile.doFill(null);
    }

    /**
     * 获取模板内的内容
     * @return 返回DateTable
     */
    public DataTable getTemplateData(){
        try {
            excelFile=new ExcelFile(filePath);
        } catch (IOException e) {
            Application.errorWindow("无法读取Excel模板文件："+e.getMessage());
        }

        Vector<Vector<String>>tmp=excelFile.getExcelData(0,0,propertySize-1);

        tmp.remove(0);//移除第一行的说明文字
        int index=tmp.get(0).indexOf("*"+PropertyFactory.LABEL_NAME);
        tmp.get(0).set(index,PropertyFactory.LABEL_NAME);

        index=tmp.get(0).indexOf("*"+PropertyFactory.LABEL_ID_CARD);
        tmp.get(0).set(index,PropertyFactory.LABEL_ID_CARD);


        DataTable dataTable=new DataTable("Excel模板内容");
        for (String text:tmp.get(0)){
            if (text.equals(PropertyFactory.LABEL_ID_CARD)){
                dataTable.addColumn(text,false);
                continue;
            }
            dataTable.addColumn(text,true);
        }
        tmp.remove(0);//再次移除第一行的属性名
        for (Vector<String> rows:tmp){
            dataTable.addRow(rows.toArray());
        }
        return dataTable;
    }

}
