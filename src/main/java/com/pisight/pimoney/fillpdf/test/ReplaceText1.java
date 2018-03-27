package com.pisight.pimoney.fillpdf.test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;

/**
 * This is an example on how to remove text from PDF document.
 *
 * Note: ------------ Because of nature of the PDF structure itself, actually
 * this will not work 100% able to find text that need to be replaced. There are
 * other solutions for that, for example using PDFTextStripper.
 *
 * @author Christian H <chadilukito@gmail.com>
 */
public final class ReplaceText1 {
	/**
	 * Default constructor.
	 */
	private ReplaceText1() {
		// example class should not be instantiated
	}
	
	private static HashMap<String, String> textMap = new HashMap<>();
	
	static {
		textMap.put(".*FAR1_NAME.*", "Kumar Shubham");
		textMap.put(".*FAR1_CODE.*", "123456");
		textMap.put(".*FAR1_FIRM.*", "IPP");
		textMap.put(".*FAR1_CONTACT.*", "7875464959");
		textMap.put(".*FAR1_EMAIL.*", "kumar.shubham@pisight.com");
		textMap.put(".*CLIENT_LNAME.*", "Harsoda");
		textMap.put(".*CLIENT_FNAME_MNAME.*", "Nitin");
		textMap.put(".*CSMr.*", "✅ ");
		textMap.put(".*CSMrs.*", "");
		textMap.put(".*CSMdm.*", "√");
		textMap.put(".*CSMiss.*", "");
		textMap.put(".*CSDr.*", "");
		textMap.put(".*CGM.*", "\u2714");
		textMap.put(".*CGF.*", "");
		textMap.put(".*CMSS.*", "√");
		textMap.put(".*CMSM.*", "");
		textMap.put(".*CMSW.*", "");
		textMap.put(".*CMSD.*", "");
	}

	/**
	 * This will remove all text from a PDF document.
	 *
	 * @param args
	 *            The command line arguments.
	 *
	 * @throws IOException
	 *             If there is an error parsing the document.
	 */
	public static void main(String[] args) throws IOException {
		
		String baseDir = "/home/kumar/kumar/barclays/Forms/";
		String folder = "NTUC/";
		String filename = "NTUC Form -regularpremium-edited";
		String ext = ".pdf";
		String outputMark = "-output";
		
		String oldText = "FAR1_NAME";
		String newText = "Kumar Shubham";
		String oldFile = baseDir+folder+filename+ext;
		String newFile = baseDir+folder+filename+outputMark+ext;
		
		PDDocument document = null;
		try {
			document = PDDocument.load(new File(oldFile));
			if (document.isEncrypted()) {
				System.err.println("Error: Encrypted documents are not supported for this example.");
				System.exit(1);
			}

			System.out.println(oldText + " => " + newText);

			document = _ReplaceText(document, oldText, newText);
			document.save(newFile);
		} finally {
			if (document != null) {
				document.close();
			}
		}
		
		System.out.println("Text replaced");
	}

	private static PDDocument _ReplaceText(PDDocument document, String searchString, String replacement)
			throws IOException {
		if (StringUtils.isEmpty(searchString) || StringUtils.isEmpty(replacement)) {
			return document;
		}

		for (PDPage page : document.getPages()) {
			
			COSDictionary dict = page.getCOSObject();
			Collection<COSName> collection = dict.keySet();
			
			for(COSName base: collection) {
				System.out.println(base + " :: " + dict.getString(base));
			}
			
			PDFStreamParser parser = new PDFStreamParser(page);
			parser.parse();
			List tokens = parser.getTokens();

			for (int j = 0; j < tokens.size(); j++) {
				Object next = tokens.get(j);
				if (next instanceof Operator) {
					Operator op = (Operator) next;

					String pstring = "";
					int prej = 0;

					// Tj and TJ are the two operators that display strings in a PDF
					if (op.getName().equals("Tj")) {
						System.out.println("inside Tj");
						// Tj takes one operator and that is the string to display so lets update that
						// operator
						COSString previous = (COSString) tokens.get(j - 1);
						String string = previous.getString();
						TextMap newStr = searchAndGetReplacement(string);
						if (string.trim().matches(".*"+searchString+".*")) {
							System.out.println("MATCHEDDDDDDDDDDDDDDDDDDDDDDDDDDd => " + string);
						}
						if (newStr != null) {
							System.out.println("matched##########################");
							previous.setValue(string.trim().replace(newStr.key.replace(".*", ""), newStr.value).getBytes());
						}
					} else if (op.getName().equals("TJ")) {
						System.out.println("inside TJ");
						COSArray previous = (COSArray) tokens.get(j - 1);
						for (int k = 0; k < previous.size(); k++) {
							Object arrElement = previous.getObject(k);
							if (arrElement instanceof COSString) {
								COSString cosString = (COSString) arrElement;
								String string = cosString.getString();

								if (j == prej) {
									pstring += string;
								} else {
									prej = j;
									pstring = string;
								}
							}
						}
						
//						System.out.println(pstring);
						TextMap newStr = searchAndGetReplacement(pstring);
						if (newStr != null) {
							System.out.println("matched@@@@@@@@@@@@@@@@@@@@@@@@");
							COSString cosString2 = (COSString) previous.getObject(0);
							cosString2.setValue(pstring.trim().replace(newStr.key.replace(".*", ""), newStr.value).getBytes());

							int total = previous.size() - 1;
							for (int k = total; k > 0; k--) {
								previous.remove(k);
							}
						}
					}
				}
			}

			// now that the tokens are updated we will replace the page content stream.
			PDStream updatedStream = new PDStream(document);
			OutputStream out = updatedStream.createOutputStream(COSName.FLATE_DECODE);
			ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
			tokenWriter.writeTokens(tokens);
			out.close();
			page.setContents(updatedStream);
		}

		return document;
	}
	
	private static TextMap searchAndGetReplacement(String string) {
		Set<String> keys = textMap.keySet();
		System.out.println(keys.size());
		System.out.println(string);
		for(String key: keys) {
//			System.out.println(key);
			if(string.matches(key)) {
				return new TextMap(key, textMap.get(key));
			}
		}
		
		return null;
	}
	
}

class TextMap{
	
	public String key;
	public String value;
	
	TextMap(String key, String value){
		this.key = key;
		this.value = value;
	}
}