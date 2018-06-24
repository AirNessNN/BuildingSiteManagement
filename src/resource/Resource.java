package resource;

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

	//字符串
	public final static String DATE_FORMATE="yyyy-MM-dd";
	
	//系统文件
	/**
	 * 存放用户的对象文件
	 */
	public final static int FILE_USER=1;//用户文件
	/**
	 * 存放程序设置的文件
	 */
	public final static int FILE_SETTING=2;//设置文件

	/**
	 * 系统文件默认目录
	 */
	private static final FileSystemView fileSystemView=FileSystemView.getFileSystemView();
	private static final String userFilePath=fileSystemView.getDefaultDirectory().getAbsolutePath()+"\\AnBuildingSiteMgr";





	
	//路径
	public static String getApplicationDirectoryPath() {
		return userFilePath;
	}

	/**
	 * 存放每一个用户的数据文件，一个用户一个文件夹
	 * @return 路径
	 */
	public static String getDataDirectoryPath(){
        return userFilePath+"\\Data";
    }

	/**
	 * 存放工人个人数据的文件夹，一个工人一个文件夹
	 * @return 路径
	 */
	public static String getWorkerDirectoryPath(){
		return userFilePath+"\\WorkerFiles";
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
		case FILE_USER:
			file=new File(getApplicationDirectoryPath()+"\\user.anl");
			break;
		case FILE_SETTING:
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
