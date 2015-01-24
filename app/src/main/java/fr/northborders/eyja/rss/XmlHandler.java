package fr.northborders.eyja.rss;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import fr.northborders.eyja.model.RssFeed;

/**
 * Created by thibaultguegan on 13/01/15.
 */
public class XmlHandler extends DefaultHandler {

    private RssFeed feedStr = new RssFeed();
    private List<RssFeed> rssList = new ArrayList<RssFeed>();

    private int articlesAdded = 0;

    // Number of articles to download
    private static final int ARTICLES_LIMIT = 25;

    StringBuffer chars = new StringBuffer();

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        chars = new StringBuffer();

        if (qName.equalsIgnoreCase("media:content"))
        {
            if (!attributes.getValue("url").toString().equalsIgnoreCase("null")) {
                feedStr.setImgLink(attributes.getValue("url").toString());
            } else {
                feedStr.setImgLink("");
            }
        }
        if (qName.equalsIgnoreCase("enclosure")) {
            if (!attributes.getValue("url").toString().equalsIgnoreCase("null")) {
                feedStr.setImgLink(attributes.getValue("url").toString());
            } else {
                feedStr.setImgLink("");
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equalsIgnoreCase("title")) {
            feedStr.setTitle(chars.toString());
        } else if (localName.equalsIgnoreCase("description")) {

            feedStr.setDescription(chars.toString());
        } else if (localName.equalsIgnoreCase("pubDate")) {

            feedStr.setPubDate(chars.toString());
        } else if (localName.equalsIgnoreCase("encoded")) {

            feedStr.setEncodedContent(chars.toString());
        } else if (qName.equalsIgnoreCase("media:content"))

        {

        } else if (localName.equalsIgnoreCase("link")) {

            try {
                feedStr.setUrl(new URL(chars.toString()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        if (localName.equalsIgnoreCase("item")) {
            rssList.add(feedStr);

            feedStr = new RssFeed();
            articlesAdded++;
            if (articlesAdded >= ARTICLES_LIMIT) {
                throw new SAXException();
            }
        }
    }

    public void characters(char ch[], int start, int length) {
        chars.append(new String(ch, start, length));
    }


    public List<RssFeed> getLatestArticles(String feedUrl) {
        URL url = null;
        try {

            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            url = new URL(feedUrl);
            xr.setContentHandler(this);
            xr.parse(new InputSource(url.openStream()));
        } catch (IOException e) {
        } catch (SAXException e) {

        } catch (ParserConfigurationException e) {

        }

        return rssList;
    }

}