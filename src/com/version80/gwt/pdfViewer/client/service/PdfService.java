package com.version80.gwt.pdfViewer.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.version80.gwt.pdfViewer.shared.PdfPage;

public interface PdfService extends RemoteService{
	PdfPage getPdfPage(String pdfFilepath, boolean isAbsolutePath, int pageNumber);
}