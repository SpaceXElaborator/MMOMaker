package com.terturl.MMO.Util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

/**
 * Currently, only used for creating BufferedImages from Base64 strings
 * @author Sean Rahman
 * @since 0.57.0
 *
 */
public class ImageCreator {

	/**
	 * Will create a BufferedImage by reading a Base64 string and converting the
	 * information into bytes then into a byte array for ImageIO to read
	 * 
	 * @param img The Base64 string
	 * @return BufferedImage created from the string
	 */
	public static BufferedImage fromString(String img) {
		try {
			String b64 = img.split(",")[1];
			byte[] b = Base64.getDecoder().decode(b64);
			return ImageIO.read(new ByteArrayInputStream(b));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}