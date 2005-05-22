package org.idanso.weatherstation;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public abstract class XMLHttpWeatherStation extends HttpWeatherStation {

	private DocumentBuilder builder;
	private static Log log=LogFactory.getLog(XMLHttpWeatherStation.class);
	protected XMLHttpWeatherStation() throws ParserConfigurationException
	{
		super();
		log.info("Initializing XMLHTTP based weather station");
		builder=DocumentBuilderFactory.newInstance().newDocumentBuilder();	
	}
	
	protected boolean handleHttpResponse(HttpMethod method) {
		try {
			log.debug("Parsing XML input");
			InputStream is=method.getResponseBodyAsStream();
			Document document=builder.parse(is);
			log.debug("Calling XML data handler");
			return handleXMLHttp(document);
		} catch (IOException e) {
			log.error("IO error at XML parsing",e);
			return false;
		} catch (SAXException e) {
			log.error("SAX error at XML parsing",e);
			return false;
		}
		

	}

	protected abstract boolean handleXMLHttp(Document document);



}
