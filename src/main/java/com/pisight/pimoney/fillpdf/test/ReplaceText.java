package com.pisight.pimoney.fillpdf.test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
public final class ReplaceText {
	/**
	 * Default constructor.
	 */
	private ReplaceText() {
		// example class should not be instantiated
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
		String folder = "Aviva/";
		String filename = "edited_Aviva Form -Insurance_Investment_Application";
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
						if (string.trim().matches(".*"+searchString+".*")) {
							previous.setValue(string.trim().replace(searchString, replacement).getBytes());
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
						
						if (pstring.trim().matches(".*"+searchString+".*")) {
							COSString cosString2 = (COSString) previous.getObject(0);
							cosString2.setValue(pstring.trim().replace(searchString, replacement).getBytes());

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

}