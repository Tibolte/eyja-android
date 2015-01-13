package fr.northborders.eyja.rss;

import java.util.Comparator;

import fr.northborders.eyja.model.RssFeed;

/**
 * Created by thibaultguegan on 13/01/15.
 */
public class ReverseOrder implements Comparator<RssFeed> {

    public int compare(RssFeed o1, RssFeed o2) {
        return (o2.getTitle()).compareTo(o1.getTitle());
    }

}
