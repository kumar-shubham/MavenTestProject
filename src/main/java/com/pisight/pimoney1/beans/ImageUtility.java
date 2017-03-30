package com.pisight.pimoney1.beans;

import java.io.File;
import java.util.logging.Logger;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class ImageUtility {

	private static Logger LOGGER = Logger.getLogger(ImageUtility.class.getName());
	
	static{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	}
	
	
	public static void rotateImage(File file) throws Exception{
		LOGGER.info("initiating the rotaion process");
		if(!file.exists()){
			LOGGER.warning("file is not valid");
			throw new Exception("file does not exists");
		}
		Mat m = Highgui.imread(file.getAbsolutePath(), 0);
		
		int rows = m.rows();
		int cols = m.cols();

		double angle = getSkewedAngle(m);

		Point center = new Point(cols/2, rows/2);
		Mat m2 = Imgproc.getRotationMatrix2D(center, -angle, 1);
		Mat result = new Mat();
		Size size = new Size(cols, rows);
		Imgproc.warpAffine(m, result, m2, size);
		Highgui.imwrite(file.getAbsolutePath(), result);
		LOGGER.info("Image rotaion done");
	}
	
	
	public static double getSkewedAngle(Mat m){
		return getSkewedAngle(m, 0, 0);
	}
	
	
	public static double getSkewedAngle(Mat m, float minLengthFactor){
		return getSkewedAngle(m, minLengthFactor, 0);
	}
	
	
	public static double getSkewedAngle(Mat m, int thresholdAngle){
		return getSkewedAngle(m, 0, thresholdAngle);
	}
	
	
	public static double getSkewedAngle(Mat m, float minLengthFactor, int thresholdAngle){
		
		LOGGER.info("calculating skewed angle of the image");
		Mat edges = new Mat();
		if(minLengthFactor <= 0){
			minLengthFactor = 3;
		}
		if(thresholdAngle <= 0){
			thresholdAngle = 10;
		}
		Imgproc.Canny(m, edges, 50, 150, 3, true);

		int cols = m.cols();

		double minLength = cols/minLengthFactor;
		Mat lines = new Mat();

		Imgproc.HoughLinesP(edges, lines, 1, Math.PI/180, 100, minLength, 40);

		double angle = 0d;

		int lineCount = lines.cols();
		int ignoreCount = 0;
		for(int x = 0; x<lineCount; x++){

			double[] vec = lines.get(0, x);
			double x1 = vec[0], 
					y1 = vec[1],
					x2 = vec[2],
					y2 = vec[3];

			
			double temp = Math.atan2((y1-y2),(x2-x1))*180/Math.PI;
			if(Math.abs(temp) < thresholdAngle){
				angle += temp;
			}
			else{
				ignoreCount++;
			}

		}
		angle = angle/(lineCount-ignoreCount);
		System.out.println("threhold :: " + thresholdAngle);
		System.out.println("minLengthFactor :: " + minLengthFactor);
		LOGGER.info("skewed angle calculated is " + angle);
		return angle;
	}
}
