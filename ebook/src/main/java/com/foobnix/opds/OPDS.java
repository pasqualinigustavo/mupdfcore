package com.foobnix.opds;

import com.foobnix.android.utils.LOG;
import com.foobnix.android.utils.TxtUtils;
import com.foobnix.ext.XmlParser;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.Random;

public class OPDS {
    public static final String CODE_401 = "401";

    public static int random = new Random().nextInt();
    public static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.2997." + random + " Safari/537.36";


    public static Feed getFeedByXml(String xmlString) throws Exception {
        LOG.d("getFeedByXml", xmlString);

        XmlPullParser xpp = XmlParser.buildPullParser();
        xpp.setInput(new StringReader(xmlString));

        int eventType = xpp.getEventType();

        Feed feed = new Feed();

        Entry entry = null;

        boolean isEntry = false;
        boolean isAuthor = false;
        boolean isContent = false, isTitle = false;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (!isEntry) {
                    if ("title".equals(xpp.getName())) {
                        feed.title = xpp.nextText();
                    }
                    if ("subtitle".equals(xpp.getName())) {
                        feed.subtitle = xpp.nextText();
                    }
                    if ("updated".equals(xpp.getName())) {
                        feed.updated = xpp.nextText();
                    }
                    if ("icon".equals(xpp.getName())) {
                        feed.icon = xpp.nextText();
                    }
                    if ("link".equals(xpp.getName())) {
                        feed.links.add(new Link(xpp));
                    }
                }

                if ("entry".equals(xpp.getName())) {
                    entry = new Entry();
                    isEntry = true;
                }
                if ("author".equals(xpp.getName())) {
                    isAuthor = true;
                }
                if (isEntry && "content".equals(xpp.getName())) {
                    isContent = true;
                }
                if (isEntry && "title".equals(xpp.getName())) {
                    isTitle = true;
                }

                if (isEntry) {
                    if ("summary".equals(xpp.getName())) {
                        entry.summary = xpp.nextText();
                    }
                    if ("dc:issued".equals(xpp.getName())) {
                        entry.year = xpp.nextText();
                    }
                    if ("updated".equals(xpp.getName())) {
                        entry.updated = xpp.nextText();
                    }
                    if ("id".equals(xpp.getName())) {
                        entry.id = xpp.nextText();
                    }
                    if (isAuthor && "name".equals(xpp.getName())) {
                        entry.author = xpp.nextText();
                    }
                    if (isAuthor && "uri".equals(xpp.getName())) {
                        entry.authorUrl = xpp.nextText();
                    }
                    if ("category".equals(xpp.getName())) {
                        String label = xpp.getAttributeValue(null, "label");
                        String term = xpp.getAttributeValue(null, "term");
                        if (TxtUtils.isNotEmpty(label)) {
                            entry.category += ", " + label;
                        } else if (TxtUtils.isNotEmpty(term)) {
                            entry.category += ", " + term;
                        }
                    }

                    if ("link".equals(xpp.getName())) {
                        entry.links.add(new Link(xpp));
                    }
                }

            }
            if (eventType == XmlPullParser.TEXT) {
                if (isContent) {
                    String text = xpp.getText();
                    if (TxtUtils.isNotEmpty(text) && !text.equals("\n") && !text.equals("\r")) {
                        text = text.replace(":\n", ":").trim();
                        entry.content += text + "<br/>";
                    }
                }
                if (isTitle) {
                    entry.title += " " + xpp.getText();
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                if ("entry".equals(xpp.getName())) {
                    isEntry = false;
                    entry.category = TxtUtils.replaceFirst(entry.category, ", ", "");
                    feed.entries.add(entry);
                }
                if ("author".equals(xpp.getName())) {
                    isAuthor = false;
                }
                if ("content".equals(xpp.getName())) {
                    isContent = false;
                }
                if ("title".equals(xpp.getName())) {
                    isTitle = false;
                }
            }

            eventType = xpp.next();
        }
        return feed;

    }

}
