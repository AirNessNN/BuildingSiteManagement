package Resoure;

import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

public class Resource {
	//×ÖÌå
	public final static String WEI_RUAN_YA_HEI="Î¢ÈíÑÅºÚ";
	public final static String LAN_TING_HEI="·½ÕýÀ¼Í¤³¬Ï¸ºÚ¼òÌå";
	
	//Â·¾¶
	public static String getDocumentDirectory() {
		FileSystemView fileSystemView=FileSystemView.getFileSystemView();
		return fileSystemView.getDefaultDirectory().getAbsolutePath();
	}
	
	
	
	
	public static InputStream getResource(String path) {
		InputStream in=null;
		try {
			in=Resource.class.getResourceAsStream("/Resoure/"+path);
			return in;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
