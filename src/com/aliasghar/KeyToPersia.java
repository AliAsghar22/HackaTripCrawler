package com.aliasghar;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Created by Taghizadeh on 13/04/2016.
 */
public class KeyToPersia extends WebCrawler {
    private static int maxPagesToFetch = 1000;
    private static int maxDepthOfCrawling = 5;
    private static int numberOfCrawlers = 1;
    static Indexer indexer;

    public static void start() throws Exception {
        indexer = Indexer.getIndexer();
        String crawlStorageFolder = "crawler";
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(500);
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        config.setMaxPagesToFetch(maxPagesToFetch);
        config.setResumableCrawling(false);
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed("http://en.key2persia.com/iran-tours/");
        controller.start(KeyToPersia.class, numberOfCrawlers);
    }


    private final static String VISIT_PATTERN = ".*\\.(html||Aspx)";
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");
    private final static Pattern number = Pattern.compile("^(?=.*[0-9])$");


    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if(url.getAnchor() == null)
            return false;
        if(number.matcher(url.getAnchor()).matches() && !FILTERS.matcher(href).matches() && href.startsWith("http://en.key2persia.com/") && href.contains("tour")){
            System.out.println(url.getAnchor());
            return true;
        }
        return false ;
    }

    @Override
    public void visit(Page page) {


        if (page.getParseData() instanceof HtmlParseData) {
//            System.out.println("visiting");
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            org.jsoup.nodes.Document doc = Jsoup.parse(html);

            String url = page.getWebURL().getURL();
            String title = "";
            String discription = "";
            String date = Calendar.getInstance().getTime().toString();
            String place = null;
            int days;
            ArrayList<TourDetail> tourDetail = new ArrayList<>();


            try {
//                System.out.println("getting page info");
                Element e = doc.select("h1.page-title").get(0);
                title = e.text();
                while (e.hasText()) {
                    if (e.tagName().equals("ol")) {
//                        System.out.println("break");
                        break;
                    }
                    String t = "";
                    String d = "";
                    String imageURL = "";
                    e = e.nextElementSibling();
                    if (e.toString().contains("src")) {

                        t = e.text();
                        imageURL = e.getElementsByTag("img").get(0).attr("src");
                        while (e.hasText() &&!e.nextElementSibling().toString().contains("src")) {
                            e = e.nextElementSibling();
//                            System.out.println("adding dic");
                            d += e.text();
                        }
                    }
                }



            } catch (Exception e) {
                System.err.println("KeyToPersia: Unrelated Page");
            }

        }
    }

}
