package com.terturl.MMO.Util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class ImageCreator {

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