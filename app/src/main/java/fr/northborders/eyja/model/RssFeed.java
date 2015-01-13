package fr.northborders.eyja.model;

import java.net.URL;

/**
 * Created by thibaultguegan on 12/01/15.
 */
public class RssFeed {

    private long articleId;
    private long feedId;
    private String title;
    private String description;
    private String imgLink;
    private String pubDate;
    private URL url;
    private String encodedContent;

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public long getFeedId() {
        return feedId;
    }
    /**
     * @param feedId the feedId to set
     */
    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return the url
     */
    public URL getUrl() {
        return url;
    }
    /**
     * @param url the url to set
     */
    public void setUrl(URL url) {
        this.url = url;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;


        if (description.contains("<img ")){
            String img = description.substring(description.indexOf("<img "));
            String cleanUp = img.substring(0, img.indexOf(">")+1);
            img = img.substring(img.indexOf("src=") + 5);
            int indexOf = img.indexOf("'");
            if (indexOf==-1){
                indexOf = img.indexOf("\"");
            }
            img = img.substring(0, indexOf);

            //setImgLink(img);

            this.description = this.description.replace(cleanUp, "");
        }
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param pubDate the pubDate to set
     */
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
    /**
     * @return the pubDate
     */
    public String getPubDate() {
        return pubDate;
    }
    /**
     * @param encodedContent the encodedContent to set
     */
    public void setEncodedContent(String encodedContent) {
        this.encodedContent = encodedContent;
    }
    /**
     * @return the encodedContent
     */
    public String getEncodedContent() {
        return encodedContent;
    }
    /**
     * @param imgLink the imgLink to set
     */
    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }
    /**
     * @return the imgLink
     */
    public String getImgLink() {
        return imgLink;
    }

}
