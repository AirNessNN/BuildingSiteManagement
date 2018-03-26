package resource;

import dbManager.Anbean;
import dbManager.Info;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import javax.swing.filechooser.FileSystemView;

/**
 * 程序的资源类，用于管理程序中所有资源，包括配置文件，用户文件，数据，图像资源
 * @author AN
 *
 */
public class Resource {
	//字体
	public final static String FONT_WEI_RUAN_YA_HEI="微软雅黑";
	public final static String FONT_LAN_TING_HEI="方正兰亭超细黑简体";
	public final static Font FONT_TITLE=new Font(FONT_WEI_RUAN_YA_HEI,Font.PLAIN,40);
	public final static Font FONT_TABLE_ITEM=new Font(FONT_WEI_RUAN_YA_HEI,Font.PLAIN,15);
	//颜色
	public final static Color COLOR_LIGHT_BLUE=SystemColor.textHighlight;
	
	//系统文件
	/**
	 * 存放用户的对象文件
	 */
	public final static int FILE_USER=1;//用户文件
	/**
	 * 存放程序设置的文件
	 */
	public final static int FILE_SETTING=2;//设置文件




	
	//路径
	public static String getApplicationDirectoryPath() {
		FileSystemView fileSystemView=FileSystemView.getFileSystemView();
		return fileSystemView.getDefaultDirectory().getAbsolutePath()+"\\AnBuildingSiteMgr";
	}

	public static String getDataDirectoryPath(){
        FileSystemView fileSystemView=FileSystemView.getFileSystemView();
        return fileSystemView.getDefaultDirectory().getAbsolutePath()+"\\AnBuildingSiteMgr\\Data";
    }

    /**
     * 获取数据文件夹实例
     * @return
     */
    public static File getDataDirectoryFile(){
	    return new File(getDataDirectoryPath());
    }
	
	/**
	 * 获取该系统的文档位置的文件夹实例
	 * @return
	 */
	public static File getApplicationDirectoryFile() {
		return new File(getApplicationDirectoryPath());
	}
	
	
	
	/**
	 * 获得程序数据文件
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
