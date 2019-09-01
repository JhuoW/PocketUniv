package com.pocketuniversity.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtils {

	 public static Bitmap GetLocalOrNetBitmap(String url)  
	    {  
	        Bitmap bitmap = null;  
	        InputStream in = null;  
	        BufferedOutputStream out = null;  
	        try  
	        {  
	            in = new BufferedInputStream(new URL(url).openStream(), 8*1024);  
	            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();  
	            out = new BufferedOutputStream(dataStream,8*1024);  
	            copy(in, out);  
	            out.flush();  
	            byte[] data = dataStream.toByteArray();  
	            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);  
	            data = null;  
	            return bitmap;  
	        }  
	        catch (IOException e)  
	        {  
	            e.printStackTrace();  
	            return null;  
	        }  
	    } 
	 
	 private static void copy(InputStream in, OutputStream out)
	            throws IOException {
	        byte[] b = new byte[2*1024];
	        int read;
	        while ((read = in.read(b)) != -1) {
	            out.write(b, 0, read);
	        }
	    }
}
