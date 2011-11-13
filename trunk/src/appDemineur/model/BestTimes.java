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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import appDemineur.model.Game;

/**
 * La classe BestTimes permet la gestion des meilleurs temps par le bias d'un document XML.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */

public class BestTimes
{
	// Document DOM qui reçoit le contenu du fichier XML lors de la lecture
	// et à partir duquel un fichier XML est créé lors de l'écriture
	private Document doc = null;
	
	// Chemin du fichier utilisé
	private static final String FILE_PATH = "best_times.xml";
	
	// Nom des étiquettes et attributs utilisés
	private static final String ROOT_TAG = "best_times";
	private static final String LEVEL_TAG = "level";
	private static final String NAME_ATTR = "name";
	private static final String TIME_TAG = "time";
	private static final String PLAYER_TAG = "player";
	
	// Requêtes XPath utiles
	private static final String LEVEL_XPATH = "/" + ROOT_TAG + "/" + LEVEL_TAG + "[@" + NAME_ATTR + "='%s']";
	private static final String TIME_XPATH = LEVEL_XPATH + "/" + TIME_TAG;
	private static final String PLAYER_XPATH = LEVEL_XPATH + "/" + PLAYER_TAG;
	
	/**
	 * Construit un objet de gestion des meilleurs temps. 
	 * Lit le fichier spécifié s'il existe, puis le valide.
	 */
	public BestTimes()
	{
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			// Malheureusement, getFile() retourne des caractères «échappés», 
			// alors la ligne suivante ne fonctionne pas quand il y a des espaces 
			// ou des caractères accentués dans le chemin du fichier :
			
//			File xmlFile = new File(this.getClass().getResource(BestTimes.FILE_PATH).getFile());

			// Passe-passe pour obtenir un chemin absolu valide
			URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
			File xmlFile = new File(new URI(url.toString()).getPath() + BestTimes.FILE_PATH);
			
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
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Retourne le meilleur temps pour le niveau de difficulté passé en paramètre.
	 * 
	 * @param levelNum le niveau de difficulté de la partie jouée
	 * @return le meilleur temps pour le niveau de difficulté passé en paramètre
	 */
	public String getTime(int levelNum)
	{
		String time = null;

		try
		{
			String levelName = Game.LEVELS[levelNum].name.toLowerCase();
			time = this.getData(String.format(BestTimes.TIME_XPATH, levelName));
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}

		return time;
	}
	
	/**
	 * Modifie le meilleur temps pour le niveau de difficulté passé en paramètre.
	 * 
	 * @param levelNum le niveau de difficulté de la partie jouée
	 * @param time le nouveau meilleur temps
	 */
	public void setTime(int levelNum, String time)
	{
		try
		{
			String levelName = Game.LEVELS[levelNum].name.toLowerCase();
			this.setData(String.format(BestTimes.TIME_XPATH, levelName), time);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Retourne le nom du joueur qui possède le meilleur temps pour le niveau de difficulté passé en paramètre.
	 * 
	 * @param levelNum le niveau de difficulté de la partie jouée
	 * @return le nom du joueur qui possède le meilleur temps
	 */
	public String getPlayer(int levelNum)
	{
		String player = null;
		
		try
		{
			String levelName = Game.LEVELS[levelNum].name.toLowerCase();
			return this.getData(String.format(BestTimes.PLAYER_XPATH, levelName));
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
		
		return player;
	}
	
	/**
	 * Modifie le nom du joueur possédant le meilleur temps pour le niveau de difficulté passé en paramètre.
	 * 
	 * @param levelNum le niveau de difficulté de la partie jouée
	 * @param player le nom du joueur
	 */
	public void setPlayer(int levelNum, String player)
	{
		try
		{
			String levelName = Game.LEVELS[levelNum].name.toLowerCase();
			this.setData(String.format(BestTimes.PLAYER_XPATH, levelName), player);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * Exécute une requête XPath dans le but d'obtenir une valeur présente dans le document courant
	 * 
	 * @param xpathRequest la requête à exécuter
	 * @return null si la requête est invalide, sinon le résultat de la requête
	 */
	private String getData(String xpathRequest)
	{
		String data = null;

		if (this.doc != null)
		{
			try
			{
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
	
	/*
	 * Exécute une requête XPath dans le but de modifier le document courant
	 * 
	 * @param xpathRequest la requête à exécuter
	 * @param newData la nouvelle valeur
	 */
	private void setData(String xpathRequest, String newData)
	{
		if (this.doc != null)
		{
			try
			{
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
	 * Écrit les données des meilleurs temps dans le fichier XML
	 */
	public void write()
	{
		if (this.doc != null)
		{
			try
			{
				// Prépare le fichier de sortie
				URL url= getClass().getProtectionDomain().getCodeSource().getLocation();
				File file = new File(new URI(url.toString()).getPath() + BestTimes.FILE_PATH);

				StreamResult result = new StreamResult(file);

				// Écrit l'objet DOM dans le fichier
				TransformerFactory factory = TransformerFactory.newInstance();
				factory.setAttribute("indent-number", new Integer(2));

				Transformer xformer = factory.newTransformer();

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
			catch (URISyntaxException e)
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
				if (root != null && !root.getNodeName().equals(BestTimes.ROOT_TAG))
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
					root = this.doc.createElement(BestTimes.ROOT_TAG);
					this.doc.appendChild(root);
				}

				// Valide chacun des niveaux
				for (Level level : Game.LEVELS)
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
				Element level = (Element) xpath.evaluate(
						String.format("%s[@%s='%s']", BestTimes.LEVEL_TAG, BestTimes.NAME_ATTR, levelName), 
						bestTimes, 
						XPathConstants.NODE);
				
				if (level == null)
				{
					level = this.doc.createElement(BestTimes.LEVEL_TAG);
					level.setAttribute(BestTimes.NAME_ATTR, levelName);
					bestTimes.appendChild(level);
				}

				// Présence sous l'élément level d'un élément player ?
				Element player = (Element) xpath.evaluate(BestTimes.PLAYER_TAG, level, XPathConstants.NODE);

				if (player == null)
				{
					player = this.doc.createElement(BestTimes.PLAYER_TAG);
					level.appendChild(player);
				}

				// Présence sous l'élément level d'un élément time ?
				Element time = (Element) xpath.evaluate(BestTimes.TIME_TAG, level, XPathConstants.NODE);

				if (time == null)
				{
					time = this.doc.createElement(BestTimes.TIME_TAG);
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
