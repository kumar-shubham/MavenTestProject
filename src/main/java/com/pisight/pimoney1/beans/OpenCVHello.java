package com.pisight.pimoney1.beans;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class OpenCVHello
{

	static int num =  1;
	public static void main( String[] args ) throws IOException
	{
		List<String> imageFiles = convertPDFtoImage(getFile("insurance", "Insurance (blankout)_2", "pdf"));
		System.out.println(System.getProperty("java.library.path"));
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

		for(String fileName:imageFiles){
			File file = new File(fileName);

			System.out.println("      !!!!!!!!!!!  page " + num  +"!!!!!!!!!!      ");
			Mat m = Highgui.imread(file.getAbsolutePath(), 0);

			int rows = m.rows();
			int cols = m.cols();

			double angle = getAngle(m, file);

			Point center = new Point(cols/2, rows/2);
			Mat m2 = Imgproc.getRotationMatrix2D(center, -angle, 1);
			Mat result = new Mat();
			Size size = new Size(cols, rows);
			Imgproc.warpAffine(m, result, m2, size);
			//		new LoadImage(file.getParent()+"/test1.png",result);
			String imgStr = file.getParent()+"/result/test" + num +".png";
			Highgui.imwrite(imgStr,result);

			Mat m3 = Highgui.imread(imgStr, 0);
			double angle1 = getAngle(m3, file);

			/*if(Math.abs(angle1) > Math.abs(angle)){
				System.out.println("incorrect rotation");
				m2 = Imgproc.getRotationMatrix2D(center, angle, 1);
				Imgproc.warpAffine(m, result, m2, size);
				//			new LoadImage(file.getParent()+"/test1.png",result);
				Highgui.imwrite(imgStr,result);
			}*/
			num++;
			System.out.println("");
		}



	}

	private static List<String> convertPDFtoImage(File file) throws IOException{

		PDDocument document = PDDocument.loadNonSeq(file, null);
		List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
		String path = file.getParent();
		System.out.println("path -> " + path);
		List<String> imageFiles = new ArrayList<String>();
		int page = 0;
		for (PDPage pdPage : pdPages)
		{ 
			++page;
			String filename = path + "/test/temp/temp" + "-" + page + ".png";
			BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
			ImageIOUtil.writeImage(bim, filename, 300);
			imageFiles.add(filename);
		}
		document.close();

		return imageFiles;

	}

	private static double getAngle(Mat m, File file){

		Mat edges = new Mat();
		Mat m1 = new Mat();
		m.copyTo(m1);
		System.out.println(m);

		Imgproc.Canny(m, edges, 50, 150, 3, true);

		//	      new LoadImage(file.getParent()+"/testttt.png",edges);

		int cols = m.cols();

		int minLength = cols/3;
		System.out.println("minLength :: " + minLength);
		Mat lines = new Mat();

		Imgproc.HoughLinesP(edges, lines, 1, Math.PI/180, 100, minLength, 40);

		double angle = 0d;

		int lineCount = lines.cols();
		System.out.println("line count " + lines);
		int ignoreCount = 0;
		for(int x = 0; x<lineCount; x++){

			double[] vec = lines.get(0, x);
			double x1 = vec[0], 
					y1 = vec[1],
					x2 = vec[2],
					y2 = vec[3];

			Point start = new Point(x1, y1);
			Point end = new Point(x2, y2);

			double temp = Math.atan2((y1-y2),(x2-x1))*180/Math.PI;
			if(Math.abs(temp) < 10){
				angle += temp;
				Core.line(m1, start, end, new Scalar(0,0,255), 3);
			}
			else{
				ignoreCount++;
			}

		}


		Highgui.imwrite(file.getParent()+"/test/test" + num + "_1.png",m1);
		//		new LoadImage(file.getParent()+"/test/test" + num + "_1.png",m1);

		System.out.println("angle == " + angle);
		angle = angle/(lineCount-ignoreCount);
		System.out.println("linecount == " + lineCount);
		System.out.println("angle == " + angle);
		return angle;

	}

	private static File getFile(String dir, String name, String type) {
		// TODO Auto-generated method stub

		String fileName = dir + "/" + name + "." + type.toLowerCase();
		System.out.println("FFFFFFFFFFFFFFFFFFFFFF  ::: " + fileName);

		Path p = Paths.get(System.getProperty("user.home"), "Downloads/statements/statements", fileName);

		System.out.println("AAAAAAAAAAAA :: " + p.toString());

		return p.toFile();
	}
}
