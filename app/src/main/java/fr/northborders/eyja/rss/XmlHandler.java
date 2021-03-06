package fr.northborders.eyja.rss;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
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

    private static final String TAG = XmlHandler.class.getSimpleName();
    // Number of articles to download
    private static final int ARTICLES_LIMIT = 25;
    StringBuffer chars = new StringBuffer();
    private RssFeed feedStr = new RssFeed();
    private List<RssFeed> rssList = new ArrayList<RssFeed>();
    private int articlesAdded = 0;

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        chars = new StringBuffer();

        if (qName.equalsIgnoreCase("media:content")) {
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
        } else if (qName.equalsIgnoreCase("media:content")) {

        } else if (localName.equalsIgnoreCase("link")) {

            try {
                feedStr.setUrl(new URL(chars.toString()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        if (localName.equalsIgnoreCase("item")) {
            rssList.add(feedStr);

            if (feedStr.getUrl() != null) {
                /*ParseContentThread parseContentThread = new ParseContentThread(feedStr);
                new Thread(parseContentThread).start();*/
                Document doc = null;
                try {
                    doc = Jsoup.connect(feedStr.getUrl().toString()).get();
                    String text = new String();
                    if (feedStr.getUrl().toString().contains("ouest-france")) { //ouest-france

                        Elements article = doc.select("article");
                        StringBuilder sb = new StringBuilder();

                        for (Element element : article.select("p")) {
                            sb.append(element.text()).append('\n').append('\n');
                        }

                        text = sb.toString().trim();

                        if (text.equals("")) { //vide, essayons de récupérer depuis l'image
                            Element e = doc.select("article").first();
                            Element img = e.select("img").first();
                            if (img != null) {
                                text = img.attr("alt");
                            }
                        }

                    } else { //telegramme
                        for (Element element : doc.select("li")) {
                            element.remove();
                        }
                        for (Element element : doc.select("span")) {
                            element.remove();
                        }
                        for (Element element : doc.select("h1")) {
                            element.remove();
                        }
                        for (Element element : doc.select("time")) {
                            element.remove();
                        }

                        Elements article = doc.select("article");

                        text = Jsoup.parse(article.html().replaceAll("(?i)<br[^>]*>", "br2n")).text();
                        text = text.replaceAll("br2n", "\n").trim();

                    }

                    feedStr.setContent(text);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (feedStr.getTitle().equals("Comment contacter nos correspondants ?")) {
                rssList.remove(feedStr);
            }

            if (feedStr.getContent().equals("")) {
                rssList.remove(feedStr);
            }

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

    public class ParseContentThread implements Runnable {

        RssFeed feed;

        public ParseContentThread(RssFeed feed) {
            this.feed = feed;
        }

        @Override
        public void run() {
            Document doc = null;
            try {
                doc = Jsoup.connect(feed.getUrl().toString()).get();
                String text = new String();
                if (feed.getUrl().toString().contains("ouest-france")) { //ouest-france

                    Elements article = doc.select("article");
                    StringBuilder sb = new StringBuilder();

                    for (Element element : article.select("p")) {
                        sb.append(element.text()).append('\n').append('\n');
                    }

                    text = sb.toString().trim();

                    if (text.equals("")) { //vide, essayons de récupérer depuis l'image
                        Element e = doc.select("article").first();
                        Element img = e.select("img").first();
                        if (img != null) {
                            text = img.attr("alt");
                        }
                    }

                } else { //telegramme
                    for (Element element : doc.select("li")) {
                        element.remove();
                    }
                    for (Element element : doc.select("span")) {
                        element.remove();
                    }
                    for (Element element : doc.select("h1")) {
                        element.remove();
                    }
                    for (Element element : doc.select("time")) {
                        element.remove();
                    }

                    Elements article = doc.select("article");

                    text = article.text();

                }

                feed.setContent(text);

                //Log.d(TAG, String.format("retriving text: %s", text));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}