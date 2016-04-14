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
    private static int maxPagesToFetch = 20000;
    private static int maxDepthOfCrawling = 1;
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
        controller.addSeed("http://en.key2persia.com/iran-glories-of-persia-luxury-16-day-tour");
        controller.start(KeyToPersia.class, numberOfCrawlers);
    }


    private final static String VISIT_PATTERN = ".*\\.(html||Aspx)";
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");


    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return href.startsWith("http://en.key2persia.com/") && href.contains("tour");
    }

    @Override
    public void visit(Page page) {


        if (page.getParseData() instanceof HtmlParseData) {
            System.out.println("visiting");
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            org.jsoup.nodes.Document doc = Jsoup.parse(html);

            String url = page.getWebURL().getURL();
            String title = null;
            String discription = null;
            String date = Calendar.getInstance().getTime().toString();
            String place = null;
            int days;
            ArrayList<TourDetail> tourDetail = new ArrayList<>();


            try {
                System.out.println("getting page info");
                Element e = doc.select("h1.page_title").get(0);
                title = e.toString();
                System.out.println("title: " + title);
                while (e.hasText()){
                    e = e.nextElementSibling();
                    if(e.hasClass("src")){

                    }else{
                        discription += e.toString();
                    }
                }

                System.out.println("discription: " + discription);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
