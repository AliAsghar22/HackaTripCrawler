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
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Created by Taghizadeh on 13/04/2016.
 */
public class Hotelyar extends WebCrawler {
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
        controller.addSeed("http://en.hotelyar.com/city/11/tehran-hotels");
        controller.start(Hotelyar.class, numberOfCrawlers);
    }


    private final static String VISIT_PATTERN = ".*\\.(html||Aspx)";
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");
    private final static Pattern number=Pattern.compile("[0-9]{1,2}");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return href.startsWith("http://en.hotelyar.com/city/");

    }

    @Override
    public void visit(Page page) {


        if (page.getParseData() instanceof HtmlParseData && page.getWebURL().getURL().contains("city")&& page.getWebURL().getURL().contains("tehran")) {
            System.out.println(page.getWebURL());
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            org.jsoup.nodes.Document doc = Jsoup.parse(html);

            String url = page.getWebURL().getURL();
            String hoteldiscription = null;
            String hotelAddress=null;
            String hotelDistanceToAirport=null;
            String hotelFacilities=null;
            String hotelImage=null;
            String date = Calendar.getInstance().getTime().toString();
            String place =url.split("/")[url.split("/").length-1].replace("-hotels","");
            String hotelName=null;
            int days;
            JSONObject jsonObject=new JSONObject();
            try {

                int e = doc.getElementsByClass("panel").size();
                System.out.println(e);
                for (int i=1;i<e;i++){
                    System.out.println(i);
                    Element hotel=doc.getElementsByClass("panel").get(i);
                    Element elName=doc.getElementsByTag("h3").get(i);
                    Element elAddress=doc.getElementsByClass("dl-horizontal").get(i);

                    try {
                        if (elAddress.getElementsByTag("dt").get(0).text().contains("Address")){
                            hotelAddress=elAddress.getElementsByTag("dt").get(0).nextElementSibling().text();
                        }
                        boolean hasDistance=elAddress.getElementsByTag("dt").get(1).text().contains("Distance");
                        if(hasDistance){
                            hotelDistanceToAirport=elAddress.getElementsByTag("dd").get(1).text();
                            hotelFacilities=elAddress.getElementsByTag("dd").get(2).text();
                            if (elAddress.getElementsByTag("dt").get(3).text().contains("Description"))
                            hoteldiscription=elAddress.getElementsByTag("dd").get(3).text();
                        }
                        else{
                            hotelFacilities=elAddress.getElementsByTag("dd").get(1).text();
                            if (elAddress.getElementsByTag("dd").size()>2)
                            hoteldiscription=elAddress.getElementsByTag("dd").get(2).text();
                        }
                        hotelImage=hotel.getElementsByTag("img").get(0).attr("src").replace("../","http://hotelyar.com/");
                        System.out.println(hotelImage);
                        System.out.println(place);
                        System.out.println(hotelAddress);
                        System.out.println(hotelDistanceToAirport);
                        System.out.println(hotelFacilities);
                        System.out.println(hoteldiscription);
                        indexer.add(place,hotelAddress,hotelDistanceToAirport,hotelFacilities,hoteldiscription);
                        hotelDistanceToAirport=" ";
                        hoteldiscription=" ";

                        jsonObject.put(hotelName,"{'place':'"+place+"','address':'"+hotelAddress+"'}");
                        System.out.println(jsonObject);


                    }
                    catch (Exception ed){
                        ed.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
