package fr.northborders.eyja.rss;

import java.util.Comparator;

import fr.northborders.eyja.model.RssFeed;

/**
 * Created by thibaultguegan on 13/01/15.
 */
public class SortingOrder implements Comparator<RssFeed> {

    public int compare(RssFeed o1, RssFeed o2) {
        return (o1.getTitle()).compareTo(o2.getTitle());
    }

}
