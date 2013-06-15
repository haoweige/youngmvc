package org.young.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.young.form.Document;
import org.young.form.Form;

/**
 * @author haoweige@126.com
 */
@SuppressWarnings("unchecked")
public class FormParser {

	/**
	 * parse request parameters into form for doPost method
	 * 
	 * @param request
	 * @return
	 * @throws FileUploadException
	 * @throws IOException
	 */
	public static Form parsePost(HttpServletRequest request)
			throws FileUploadException, IOException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator iterator = items.iterator();
		Form form = new Form();
		while (iterator.hasNext()) {
			Object object = iterator.next();
			if (object != null && object instanceof FileItem) {
				FileItem item = (FileItem) object;
				if (item.isFormField()) {// Field
					String name = item.getFieldName();
					String value = item.getString();
					form.setProperty(name, value);
				} else {// File
					Document document = new Document();
					document.setName(item.getFieldName());
					document.setMimeType(item.getContentType());
					document.setSize(item.getSize());
					document.setInputStream(item.getInputStream());
					form.setProperty(item.getFieldName(), document);
				}
			}
		}
		return form;
	}

	/**
	 * parse request parameters into form for doGet method
	 * 
	 * @param request
	 * @return
	 */
	public static Form parseGet(HttpServletRequest request) {
		Map map = request.getParameterMap();
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		Form form = new Form();
		while (iterator.hasNext()) {
			String name = (String) iterator.next();
			Object value = request.getParameter(name);
			form.setProperty(name, value);
		}
		return form;
	}
}
