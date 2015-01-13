package fr.northborders.eyja.rss;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import fr.northborders.eyja.model.RssFeed;

/**
 * Created by thibaultguegan on 13/01/15.
 */
public class SortingOrder implements Comparator<RssFeed> {

    public int compare(RssFeed o1, RssFeed o2) {
        DateFormat format = new SimpleDateFormat("EEE, d LLL yyyy", Locale.ENGLISH);
        Date date1 = new Date();
        Date date2 = new Date();

        try {
            date1 = format.parse(o1.getPubDate());
            date2 = format.parse(o2.getPubDate());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (date2).compareTo(date1);
    }

}
