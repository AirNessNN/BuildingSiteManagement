package dbManager;

import application.AnUtils;
import application.Application;
import application.ProgressbarDialog;
import application.QuickCheckWindow;
import component.AnPopDialog;
import resource.Resource;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CheckMgr implements QuickCheckWindow.QuickOpaControl {
    private String[] sourceNames;

    private String[] selectedNames;

    private String siteName;

    private double value=0d;

    public CheckMgr(String siteName){
        this.siteName=siteName;
        ArrayList<String> tmpList=new ArrayList<>();
        DataTable site=DBManager.getManager().getBuildingSite(siteName);
        if (site==null)return;
        for (int i=0;i<site.getColumn(0).size();i++){
            Object  obj=site.getCellAt(PropertyFactory.LABEL_LEAVE_TIME,i);//离职是空的情况下，工人是在职的
            if (obj==null){
                tmpList.add((String) site.getCellAt(PropertyFactory.LABEL_ID_CARD,i));
            }
        }
        sourceNames=AnUtils.toStringArray(tmpList.toArray());
        if (sourceNames==null)sourceNames=new String[0];

        //将ID转换为名字
        for (int i=0;i<sourceNames.length;i++){
            sourceNames[i]=DBManager.getManager().getWorkerName(sourceNames[i]);
        }
    }



    private String[] selectedNamesToIds(){
        String[] ids=new String[selectedNames.length];
        for (int i=0;i<selectedNames.length;i++){
            ids[i]=DBManager.getManager().getWorkerId(selectedNames[i]);
        }
        return ids;
    }


    @Override
    public Object onSourceDataSet() {
        return sourceNames;
    }

    @Override
    public void setSourceData(Object data) {
        String[] tmp= (String[]) data;
        sourceNames=tmp;
    }

    @Override
    public void onDataFill(Date d1, Date d2) {
        if (selectedNames==null)return;
        selectedNames=selectedNamesToIds();

        Date[] tmpDates=AnUtils.getSectionDates(d1,d2);
        Date[] dates=new Date[AnUtils.isDateYMDEquality(d1,d2)?tmpDates.length+1:tmpDates.length+2];
        dates[0]=d1;
        System.arraycopy(tmpDates, 0, dates, 1, tmpDates.length);
        if (dates.length==tmpDates.length+2)dates[dates.length-1]=d2;

        //开始线程
        int size=selectedNames.length*dates.length;
        Application.startService(()->{
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int index=1;
            for (String selectedName : selectedNames) {
                for (Date date : dates) {
                    DataTable site=DBManager.getManager().getBuildingSite(siteName);
                    site.selectRow(PropertyFactory.LABEL_ID_CARD,selectedName);
                    Date entry= (Date) site.getSelectedRowAt(PropertyFactory.LABEL_ENTRY_TIME);
                    if (entry==null)continue;
                    if (AnUtils.dateAfter(entry,date))continue;

                    DBManager.getManager().getCheckInManager().updateData(
                            selectedName,
                            siteName,
                            date,
                            value,
                            ""
                    );
                    ProgressbarDialog.setState("正在填充："+index+"个，共"+size+"个 "+DBManager.getManager().getWorkerName(selectedName)+" "+AnUtils.formateDate(date)+" 出勤值为："+value,index++);
                }
            }
            ProgressbarDialog.CloseDialog();
        });
        System.out.println(selectedNames.length*dates.length);
        ProgressbarDialog.showDialog("正在填充数据...",0,size);


        AnPopDialog.show(null,"填充完成！",2000);
    }

    @Override
    public boolean onValueGet(Object value) {
        String tmp= (String) value;
        try{
            this.value= (new BigDecimal(tmp).setScale(1, BigDecimal.ROUND_HALF_UP)).doubleValue();
            return true;
        }catch (Exception e){
            Application.errorWindow("请设置一个填充值");
            return false;
        }
    }


    @Override
    public void onSelectedDataGet(String[] arrays) {
        selectedNames=arrays;
    }

    @Override
    public boolean onStartDateSet(String dateFormat) {
        return true;
    }

    @Override
    public boolean onEndDateSet(String dateFormat) {
        return true;
    }
}
