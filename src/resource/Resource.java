package resource;

import dbManager.Anbean;
import dbManager.Info;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import javax.swing.filechooser.FileSystemView;

/**
 * �������Դ�࣬���ڹ��������������Դ�����������ļ����û��ļ������ݣ�ͼ����Դ
 * @author AN
 *
 */
public class Resource {
	//����
	public final static String FONT_WEI_RUAN_YA_HEI="΢���ź�";
	public final static String FONT_LAN_TING_HEI="������ͤ��ϸ�ڼ���";
	public final static Font FONT_TITLE=new Font(FONT_WEI_RUAN_YA_HEI,Font.PLAIN,40);
	public final static Font FONT_TABLE_ITEM=new Font(FONT_WEI_RUAN_YA_HEI,Font.PLAIN,15);
	//��ɫ
	public final static Color COLOR_LIGHT_BLUE=SystemColor.textHighlight;
	
	//ϵͳ�ļ�
	/**
	 * ����û��Ķ����ļ�
	 */
	public final static int FILE_USER=1;//�û��ļ�
	/**
	 * ��ų������õ��ļ�
	 */
	public final static int FILE_SETTING=2;//�����ļ�




	
	//·��
	public static String getApplicationDirectoryPath() {
		FileSystemView fileSystemView=FileSystemView.getFileSystemView();
		return fileSystemView.getDefaultDirectory().getAbsolutePath()+"\\AnBuildingSiteMgr";
	}

	public static String getDataDirectoryPath(){
        FileSystemView fileSystemView=FileSystemView.getFileSystemView();
        return fileSystemView.getDefaultDirectory().getAbsolutePath()+"\\AnBuildingSiteMgr\\Data";
    }

    /**
     * ��ȡ�����ļ���ʵ��
     * @return
     */
    public static File getDataDirectoryFile(){
	    return new File(getDataDirectoryPath());
    }
	
	/**
	 * ��ȡ��ϵͳ���ĵ�λ�õ��ļ���ʵ��
	 * @return
	 */
	public static File getApplicationDirectoryFile() {
		return new File(getApplicationDirectoryPath());
	}
	
	
	
	/**
	 * ��ó��������ļ�
	 * @param type
	 * @return
	 */
	public static File getApplicationFile(int type) {
		File file=null;
		switch(type) {
		case 1:
			file=new File(getApplicationDirectoryPath()+"\\user.anl");
			break;
		case 2:
			file=new File(getApplicationDirectoryPath()+"\\setting.anl");
			break;
			default :
				break;
		}
		return file;
	}
	
	
	
	
	public static InputStream getResource(String path) {
		InputStream in;
		try {
			in=Resource.class.getResourceAsStream("/resource/"+path);
			return in;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
