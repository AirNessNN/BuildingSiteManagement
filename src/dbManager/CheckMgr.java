package dbManager;

import application.AnUtils;
import application.QuickCheckWindow;
import component.AnPopDialog;
import resource.Resource;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckMgr implements QuickCheckWindow.QuickOpaControl {
    private String[] sourceNames;

    private String[] selectedNames;

    private String siteName;

    private double value=0d;

    public CheckMgr(String siteName){
        this.siteName=siteName;
        sourceNames=DBManager.getManager().getBuildingSiteWorkers(siteName);//获取ID
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
    public Object getSourceDatas() {
        return sourceNames;
    }

    @Override
    public void setSourceDatas(Object datas) {
        String[] tmp= (String[]) datas;
        sourceNames=tmp;
    }

    @Override
    public void fillData(Date d1, Date d2) {
        if (selectedNames==null)return;
        selectedNames=selectedNamesToIds();

        Date[] tmpDates=AnUtils.getSectionDates(d1,d2);
        Date[] dates=new Date[AnUtils.isDateYMDEquality(d1,d2)?tmpDates.length+1:tmpDates.length+2];
        dates[0]=d1;
        System.arraycopy(tmpDates, 0, dates, 1, tmpDates.length);
        if (dates.length==tmpDates.length+2)dates[dates.length-1]=d2;

        for (String selectedName : selectedNames) {
            for (Date date : dates) {
                DBManager.getManager().getCheckInManager().updateData(
                        selectedName,
                        siteName,
                        date,
                        value,
                        ""
                );
                System.out.println(selectedName+" "+siteName+" "+new SimpleDateFormat(Resource.DATE_FORMATE).format(date)+" "+DBManager.getManager().getCheckInManager().getValueAt(selectedName,siteName,date));
            }
        }
        AnPopDialog.show(null,"填充完成！",2000);
    }

    @Override
    public void setValue(Object value) {
        String tmp= (String) value;
        try{
            this.value= (new BigDecimal(tmp).setScale(1, BigDecimal.ROUND_HALF_UP)).doubleValue();
        }catch (Exception e){
            this.value=0;
        }
    }

    @Override
    public void selectedCallback(String[] arrays) {
        selectedNames=arrays;
    }
}
