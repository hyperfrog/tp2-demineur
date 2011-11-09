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
import java.io.IOException;
import appDemineur.model.Game;

public class BestTimes
{
	// Chemin du fichier des meilleurs temps
	private String fileName;
	
	// Objet DOM 
	private Document doc = null;
	
	/**
	 * @param fileName
	 */
	public BestTimes(String fileName)
	{
		this.fileName = fileName;
		
		try
		{
			File xmlFile = new File(this.fileName);
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			if (!xmlFile.exists())
			{
				doc = dBuilder.newDocument();
			}
			else
			{
				try
				{
					doc = dBuilder.parse(xmlFile);
				}
				catch (SAXException e)
				{
					// Incapable de parser... Crée un nouveau document
					doc = dBuilder.newDocument();
				}
			}

			// Valide et arrange le document au besoin
			this.validateDocument();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @param xpathRequest
	 * @return
	 */
	public String getData(String xpathRequest)
	{
		String data = null;

		if (this.doc != null)
		{
			try
			{
				// 
				XPath xpath = XPathFactory.newInstance().newXPath();

				Node node = (Node) xpath.evaluate(xpathRequest, this.doc, XPathConstants.NODE);

				if (node != null)
				{
					data = node.getTextContent();
				}
			}
			catch (XPathExpressionException e)
			{
				e.printStackTrace();
			}
		}
		
		return data;
	}
	
	/**
	 * @param xpathRequest
	 * @param newData
	 */
	public void setData(String xpathRequest, String newData)
	{
		if (this.doc != null)
		{
			try
			{
				// 
				XPath xpath = XPathFactory.newInstance().newXPath();

				Node node = (Node) xpath.evaluate(xpathRequest, this.doc, XPathConstants.NODE);

				if (node != null)
				{
					node.setTextContent(newData);
				}
			}
			catch (XPathExpressionException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Écrit les données des meilleurs temps dans le fichier
	 */
	public void write()
	{
		if (this.doc != null)
		{
			try
			{
				// Prepare the output file
				File file = new File(this.fileName);
				StreamResult result = new StreamResult(file);

				// Write the DOM document to the file
				TransformerFactory tf = TransformerFactory.newInstance();
				tf.setAttribute("indent-number", new Integer(2));

				Transformer xformer = tf.newTransformer();

				xformer.setOutputProperty(OutputKeys.INDENT, "yes");

				xformer.transform(new DOMSource(this.doc), result);
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
	}
	
	// Valide l'objet DOM
	private void validateDocument()
	{
		if (this.doc != null)
		{
			try
			{
				Element root = this.doc.getDocumentElement();

				// S'il y a une racine et que ce n'est pas la bonne
				if (root != null && !root.getNodeName().equals("best_times"))
				{
					// On ne peut pas changer la racine; il faut créer un nouveau document
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

					this.doc = dBuilder.newDocument();
					root = null;
				}

				// S'il n'y a pas de racine
				if (root == null)
				{
					root = this.doc.createElement("best_times");
					this.doc.appendChild(root);
				}

				// Valide chacun des niveaux
				for (Game.Level level : Game.LEVELS)
				{
					this.validateLevel(root, level.name.toLowerCase());
				}
			}
			catch (ParserConfigurationException e)
			{
				e.printStackTrace();
			}
		}
	}

	// Valide un niveau (élément level)
	private void validateLevel(Element bestTimes, String levelName)
	{
		if (this.doc != null)
		{
			try
			{
				XPath xpath = XPathFactory.newInstance().newXPath();

				// Présence sous l'élément best_times d'un élément level avec l'attribut name à la bonne valeur ?
				Element level = (Element) xpath.evaluate("level[@name='" + levelName + "']", bestTimes, XPathConstants.NODE);

				if (level == null)
				{
					level = this.doc.createElement("level");
					level.setAttribute("name", levelName);
					bestTimes.appendChild(level);
				}

				// Présence sous l'élément level d'un élément player ?
				Element player = (Element) xpath.evaluate("player", level, XPathConstants.NODE);

				if (player == null)
				{
					player = this.doc.createElement("player");
					level.appendChild(player);
				}

				// Présence sous l'élément level d'un élément time ?
				Element time = (Element) xpath.evaluate("time", level, XPathConstants.NODE);

				if (time == null)
				{
					time = this.doc.createElement("time");
					level.appendChild(time);
				}
			}
			catch (XPathExpressionException e)
			{
				e.printStackTrace();
			}
		}
	}
}
