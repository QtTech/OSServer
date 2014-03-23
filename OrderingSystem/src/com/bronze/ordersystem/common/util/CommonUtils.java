package com.bronze.ordersystem.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.fusesource.hawtbuf.AsciiBuffer;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



public class CommonUtils {
	
	private final static Logger logger = Logger.getLogger(CommonUtils.class);

	private static ObjectMapper objectMapper = new ObjectMapper();
	private static SerialNumber serial = new FileEveryDaySerialNumber(5, OSConfiguration.WebAppRoot + File.separator + OSConfiguration.EveryDaySerialNumberFile);
	private static SerialNumber version = new FileVersionSerialNumber(5, OSConfiguration.WebAppRoot + File.separator + OSConfiguration.VersionSerialNumberFile);
	private static SkinStyleFile skinstyle = new SkinStyleFile(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.SkinStyleFile);
	private static FoodIDFile fooid = new FoodIDFile(OSConfiguration.WebAppRoot + File.separator + OSConfiguration.FoodIDFile);
	
	private static final BASE64Encoder base64Encoder = new BASE64Encoder();
	private static final BASE64Decoder base64Decoder = new BASE64Decoder();
	
	public synchronized static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	public synchronized static String generateUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").trim();
	}
	
	public static Object gerenateObject(String json, Class<?> _class) {
		try {
			return objectMapper.readValue(json, _class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String generateRunningNumber() {
		return serial.getSerialNumber();
	}
	
	public static String generateVersionNumber() {
		return version.getSerialNumber();
	}
	
	public static boolean needUpgrade(String version) {
		return ((FileVersionSerialNumber)CommonUtils.version).needUpgrade(version);
	}
	
	public static String getLatestVersion() {
		return ((FileVersionSerialNumber)CommonUtils.version).getLatestVersion();
	}
	
	public static int getSkinStyle() {
		return skinstyle.getSkinStyle();
	}
	
	public static void setSkinStyle(int style) {
		skinstyle.setSkinStyle(style);
	}
	
	public static boolean SkinIsInitialized() {
		return skinstyle.exists();
	}
	
	public synchronized static int getFoodID() {
		return fooid.getFoodID();
	}
	
	public synchronized static void setFoodID(int id) {
		fooid.setFoodID(id);
	}
	
	public static boolean FoodIDIsInitialized() {
		return fooid.exists();
	}
	
	public static byte[] getBytes(File file) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
			byte[] b = new byte[1024];  
			int n = 0;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

	public final static String TOPIC_KITCHEN = "/kitchen/0";
	public final static String TOPIC_WATERBAR = "/kitchen/1";
	public final static String TOPIC_KITCHEN_TAKE_OUT = "/kitchentakeout/0";
	public final static String TOPIC_WATERBAR_TAKE_OUT = "/kitchentakeout/1";
	public final static String TOPIC_CUSTOMER = "/customer/";
	public final static String TOPIC_ORDERED = "/ordered";
	public final static String TOPIC_CASH = "/cash";
	public final static String TOPIC_CALLING_WAITER = "/callingwaiter";
	
	public static void sendMQTTNotification(String dest_topic, String message) throws Exception {
		String user = env("ACTIVEMQ_USER", MQTTConfig.getConfig().getAdmin());
        String password = env("ACTIVEMQ_PASSWORD", MQTTConfig.getConfig().getPassword());
        String host = env("ACTIVEMQ_HOST", MQTTConfig.getConfig().getHost());
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", MQTTConfig.getConfig().getPort()));
        
        if (message == null) {
        	message = "";
        }
        
        if (message.length() > 256) {
        	logger.error("The message of notification is too long.");
        	return;
        }
        
        Buffer msg = new AsciiBuffer(message);

        MQTT mqtt = new MQTT();
        mqtt.setHost(host, port);
        mqtt.setUserName(user);
        mqtt.setPassword(password);
        mqtt.setConnectAttemptsMax(5);

        FutureConnection connection = mqtt.futureConnection();
        connection.connect().await();

        final LinkedList<Future<Void>> queue = new LinkedList<Future<Void>>();
        UTF8Buffer topic = new UTF8Buffer(dest_topic);

        // Send the publish without waiting for it to complete. This allows us
        // to send multiple message without blocking..
        queue.add(connection.publish(topic, msg, QoS.AT_LEAST_ONCE, false));

        while( !queue.isEmpty() ) {
            queue.removeFirst().await();
        }

        connection.disconnect().await();
    }
	
    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }
    
    public static List<String> refreshFileList(String dirpath) {
        File dir = new File(dirpath);
        File[] files = dir.listFiles();
        List<String> list = new ArrayList<String>();
        
        if (files == null) {
        	return list;
        }
        
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
            	List<String> _list = refreshFileList(files[i].getAbsolutePath());
            	if (_list != null && _list.size() > 0) {
            		list.addAll(_list);
            	}
            } else {
            	if (files[i].getParentFile().getName().equals(OSConfiguration.VersionDirectory)) {
            		list.add(files[i].getName());
            	} else {
            		list.add(files[i].getParentFile().getName() + File.separator + files[i].getName());
            	}
            }
        }

        return list;
    }
    
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        
        return dir.delete();
    }

    public static boolean copyFile(String srcFileName, String destFileName,
            boolean overlay) {
        File srcFile = new File(srcFileName);

        if (!srcFile.exists()) {
            return false;
        } else if (!srcFile.isFile()) {
            return false;
        }

        File destFile = new File(destFileName);
        if (destFile.exists()) {
            if (overlay) {
                new File(destFileName).delete();
            }
        } else {
            if (!destFile.getParentFile().exists()) {
                if (!destFile.getParentFile().mkdirs()) {
                    return false;
                }
            }
        }

        int byteread = 0;
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static boolean copyDirectory(String srcDirName, String destDirName,  
            boolean overlay) {
        File srcDir = new File(srcDirName);
        if (!srcDir.exists()) {
            return false;
        } else if (!srcDir.isDirectory()) {
            return false;
        }

        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }

        File destDir = new File(destDirName);
        if (destDir.exists()) {
            if (overlay) {
                new File(destDirName).delete();
            } else {
                return false;
            }
        } else {
            if (!destDir.mkdirs()) {
                return false;
            }
        }

        boolean flag = true;
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = copyFile(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!flag)
                    break;
            } else if (files[i].isDirectory()) {
                flag = copyDirectory(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!flag)
                    break;
            }
        }

        if (!flag) {
            return false;
        } else {
            return true;
        }
    }
    
    public static void generatePicture(String originalPath, String destinationPath,
    		int width, int height) {
    	try {
			Thumbnails.of(new File(originalPath)).size(width, height).
			outputFormat("png").outputQuality(1.0f).
			toOutputStream(new FileOutputStream(destinationPath));
		} catch (IOException e) {
			logger.info(e);
			e.printStackTrace();
		}
    }
    
    public synchronized static String BASE64URLEncoder(String codes) {
    	String result = "";
    	try {
			result = base64Encoder.encode(URLEncoder.encode(codes, OSConfiguration.CONTENT_TYPE).getBytes(OSConfiguration.CONTENT_TYPE));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
    	return result;
    }
    
    public synchronized static String BASE64URLDecoder(String codes) {
    	String result = "";
		try {
			result = URLDecoder.decode(new String(base64Decoder.decodeBuffer(codes), OSConfiguration.CONTENT_TYPE), OSConfiguration.CONTENT_TYPE);
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		
    	return result;
    }
    
    public synchronized static String generateIndex(List<String> objList) {
    	String obj = null;
    	
    	if (objList == null || objList.size() == 0) {
    		return new String("0");
    	}
    	
    	if (objList.size() == (Integer.valueOf(objList.get(objList.size() - 1)).intValue() + 1)) {
    		return Integer.toString(objList.size());
    	}
    	
    	int i = 0;
    	for (i = 0; i < (Integer.valueOf(objList.get(objList.size() - 1)).intValue() + 1); i++) {
    		if (!objList.contains(String.valueOf(i))) {
    			obj = String.valueOf(i);
    			break;
    		}
    	}
    	
    	if (obj == null) {
    		obj = String.valueOf(Integer.valueOf(objList.get(objList.size() - 1)).intValue() + 1); 
    	}
    	
    	return obj;
    }
    
    public synchronized static int generateCategoryIndex(List<String> objList) {
    	
    	if (objList == null || objList.size() == 0) {
    		return 1;
    	}
    	
    	if (objList.size() == (Integer.valueOf(objList.get(objList.size() - 1)).intValue() + 1)) {
    		return objList.size() + 1;
    	} else if (objList.size() < (Integer.valueOf(objList.get(objList.size() - 1)).intValue() + 1)) {
    		return Integer.valueOf(objList.get(objList.size() - 1)).intValue() + 1;
    	} else {
    		return objList.size();
    	}
    	
    }
    
}
