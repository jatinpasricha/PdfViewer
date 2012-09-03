package com.version80.gwt.pdfViewer.server;

import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.version80.gwt.pdfViewer.shared.PdfPage;

public class PdfRendererHelper {
	public PdfRendererHelper(){	
	}
	
	/**
	 * gets the PDF file from the filepath provided
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public PDFFile getPdfFile(String filepath) throws Exception{
		RandomAccessFile raf = new RandomAccessFile(new File(filepath), "r");
	    FileChannel fc = raf.getChannel ();
	    ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size ());
	    PDFFile pdfFile = new PDFFile (buf);
		
		return pdfFile;
	}
	
	/**
	 * outputs Base64 string of the pdf page image to its original size
	 * pdf file page number starts at 1
	 * @param pdfFile
	 * @param pageNumber
	 * @return
	 * @throws IOException 
	 */
	public String getPdfPageInBase64String(PDFFile pdfFile, int pageNumber) throws Exception{		
		if(pageNumber > pdfFile.getNumPages()){
			return null;
		}
		
	    PDFPage page = pdfFile.getPage(pageNumber);
	    
	    Rectangle2D r2d = page.getBBox();
	    double width = r2d.getWidth();
	    double height = r2d.getHeight();
	      
	    Image image = page.getImage((int) width, (int) height, r2d, null, true, true);
	   
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write((BufferedImage)image, "png", baos );
	    byte[] imageInByte = baos.toByteArray();

	    return ServerUtil.byteArrayToBase64String(imageInByte);
	}
	
	public void setPdfPageSize(PdfPage pdfPage, PDFFile pdfFile, int pageNumber) throws Exception{
		if(pageNumber > pdfFile.getNumPages()){
			throw new Exception("page number doesn't exist.");
		}
		
	    PDFPage page = pdfFile.getPage(pageNumber);
	    
	    Rectangle2D r2d = page.getBBox();
	    pdfPage.setWidth(r2d.getWidth());
	    pdfPage.setHeight(r2d.getHeight());
	}
	
	public PdfPage getPageInPDF(String pdfFilePath, int pageNumber) throws Exception{
		PdfPage page = new PdfPage();
		
		PDFFile pdfFile = getPdfFile(pdfFilePath);
		String pageImage = getPdfPageInBase64String(pdfFile, pageNumber);
		
		page.setPdfTitle(pdfFilePath);
		page.setNumberOfPages(pdfFile.getNumPages());
		page.setCurrentPageNumber(pageNumber);
		page.setPageImage(pageImage);
		setPdfPageSize(page, pdfFile, pageNumber);
		
		return page;
	}
 
}
