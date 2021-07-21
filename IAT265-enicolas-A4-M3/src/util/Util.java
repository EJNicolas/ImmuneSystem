package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//Class that contains helper methods

public class Util {
	public static float random(double low, double high) {
		return (float) (low + Math.random() * (high - low));
	}
	
	
	// method to load images
		public static BufferedImage loadImage(String imgFile) {
			BufferedImage img = null;
			try {
				img = ImageIO.read(new File(imgFile));
			} catch (IOException e) {
			}
			return img;
		}
		
		public static boolean saveImage(BufferedImage img, String fileName, String fileFormat) {
			try {

				File saveFile = new File(fileName + "." + fileFormat);
				ImageIO.write(img, fileFormat, saveFile);

			} catch (IOException e) {
				return false;
			}
			return true;
		}
		
	public static float radians(float angle){
		return angle/180*(float)Math.PI;		
	}	
	
}
