package appDemineur.model;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;

public class XMLFile
{
	// TODO : Ajouter le fichier xml dans le projet
	public static final String SCORE_FILE_NAME = "C:\\file.xml";
	
	// 
	public static String getScores(String request)
	{
		String result = null;
		
		try
		{
			File fXmlFile = new File(XMLFile.SCORE_FILE_NAME);
			
			if (!fXmlFile.exists())
			{
				fXmlFile.createNewFile();
			}

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = null;
			
			try
			{
				doc = dBuilder.parse(fXmlFile);
			}
			catch (SAXException e)
			{
				// Incapable de parser... Crée un nouveau document
				doc = dBuilder.newDocument();
			}

			// Valide et arrange le document au besoin
			doc = XMLFile.validateDocument(doc);
			
			// 
			XPath xpath = XPathFactory.newInstance().newXPath();
			result = ((Node) xpath.evaluate(request, doc, XPathConstants.NODE)).getTextContent();

			//XMLFile.write(doc, XMLFile.SCORE_FILE_NAME);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	// This method writes a DOM document to a file
	private static void write(Document doc, String fileName)
	{
		try
		{
			// Prepare the output file
			File file = new File(fileName);
			StreamResult result = new StreamResult(file);

			// Write the DOM document to the file
			TransformerFactory tf = TransformerFactory.newInstance();
			tf.setAttribute("indent-number", new Integer(2));
			
			Transformer xformer = tf.newTransformer();

			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			xformer.transform(new DOMSource(doc), result);
		}
		catch (TransformerConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (TransformerException e)
		{
			e.printStackTrace();
		}
	}
	
	// 
	private static Document validateDocument(Document doc)
	{
		try
		{
			Element root = doc.getDocumentElement();

			// S'il y a une racine et que ce n'est pas la bonne
			if (root != null && !root.getNodeName().equals("best_times"))
			{
				// On ne peut pas changer la racine; il faut créer un nouveau document
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

				doc = dBuilder.newDocument();
				root = null;
			}

			// S'il n'y a pas de racine
			if (root == null)
			{
				root = doc.createElement("best_times");
				doc.appendChild(root);
			}
			
			String[] levelNames = new String[] {"beginner", "intermediate", "expert"};
			
			for (String levelName : levelNames)
			{
				XMLFile.validateLevel(doc, root, levelName);
			}
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		
		return doc;
	}

	// 
	private static void validateLevel(Document doc, Element bestTimes, String levelName)
	{
		try
		{
			XPath xpath = XPathFactory.newInstance().newXPath();

			Element level = (Element) xpath.evaluate("level[@name='" + levelName + "']", bestTimes, XPathConstants.NODE);

			if (level == null)
			{
				level = doc.createElement("level");
				level.setAttribute("name", levelName);
				bestTimes.appendChild(level);
			}
			
			Element player = (Element) xpath.evaluate("player", level, XPathConstants.NODE);
			
			if (player == null)
			{
				player = doc.createElement("player");
				level.appendChild(player);
			}
			
			Element time = (Element) xpath.evaluate("time", level, XPathConstants.NODE);
			
			if (time == null)
			{
				time = doc.createElement("time");
				level.appendChild(time);
			}
		}
		catch (XPathExpressionException e)
		{
			e.printStackTrace();
		}
	}
	
}
