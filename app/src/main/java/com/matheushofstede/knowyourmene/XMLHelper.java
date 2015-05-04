package com.matheushofstede.knowyourmene;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;



import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class XMLHelper extends DefaultHandler {
	private String URL_MAIN = "http://he4dless.webege.com/KYM/knowyourmene.xml";
	String TAG = "XMLHelper";
	
	ProgressDialog pde;
	Boolean currTag = false;
	String currTagVal = "";
	public PostValue menes = null;
	public ArrayList<PostValue> knowyourmene = new ArrayList<PostValue>();
	
	
	public void get() {
		try{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser mSaxParser = factory.newSAXParser();
			XMLReader mXmlReader = mSaxParser.getXMLReader();
			mXmlReader.setContentHandler(this);
			InputStream mInputStream = new URL(URL_MAIN).openStream();
			mXmlReader.parse(new InputSource(mInputStream)); 
			
			
			
		}catch(Exception e){
			Log.e(TAG, "Exeption:"+e.getMessage());
			
			
			
		}
		
		
		
		
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(currTag){
			currTagVal = currTagVal + new String(ch, start, length);
			currTag = false;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		currTag = false;
		
		//if(localName.equalsIgnoreCase("id"))
		//packages.setId(currTagVal);
		
		if(localName.equalsIgnoreCase("link"))
		menes.setLink(currTagVal);
		
		else if(localName.equalsIgnoreCase("tag1"))
			menes.setTag1(currTagVal);
		
		else if(localName.equalsIgnoreCase("tag2"))
			menes.setTag2(currTagVal);
		
		else if(localName.equalsIgnoreCase("tag3"))
			menes.setTag3(currTagVal);
		
		else if(localName.equalsIgnoreCase("menes"))
			knowyourmene.add(menes);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		Log.i(TAG, "TAG:"+localName);
		
		currTag = true;
		currTagVal = "";
		
		if(localName.equals("menes"))
			menes = new PostValue();
		
	}
	
	
	

}