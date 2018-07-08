import Entity.Offer;
import Entity.Offers;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parser {
    static int requestsAmount = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        if (args.length == 0) {
            System.out.println("Error. Argument(keyword) is not specified.");
            throw new IllegalArgumentException();
        }
        else if (args.length > 1) {
            System.out.println("Error. Too much arguments(keywords) are specified.");
            throw new IllegalArgumentException();
        }

        String[] genders = {"female", "male", "kids"};

        //Creating of thread-safe collection in which all offers are stored
        List<Offer> syncOffers = Collections.synchronizedList(new ArrayList<>());

        for (String gender : genders) {
            String url = "https://www.aboutyou.de/suche?term="+ args[0] + "&gender=" + gender;

            Connection.Response response;
            try {
                response = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                        .timeout(0)
                        .execute();

                requestsAmount++;

                //New thread for every search page
                Document document = response.parse();
                Element element = document.select("li[class=styles__pageNumbers--1Lsj_]").last();
                if (element != null) {
                    String lastPage = element.text();
                    int numberOfPages = Integer.parseInt(lastPage);
                    for (int i = 2; i <=numberOfPages; i++) {
                        String link = "https://www.aboutyou.de/suche?term=" + args[0] + "&gender=" + gender + "page=" + i;
                        Thread thread = new Thread(new SearchRunnable(link, syncOffers), "Thread: " + link);
                        thread.start();
                        thread.join();
                    }
                }

                //New thread for every offer on this page
                Elements links = document.select("div[class=styles__tile--2s8XN col-sm-6 col-md-4 col-lg-4] > div > a[href]");
                for(Element link : links) {
                    Thread thread = new Thread(new ParserRunnable(link, syncOffers, "INITIAL_LOAD"), "Thread: " + link.attr("abs:href"));
                    thread.start();
                    thread.join();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        //Creating xml file from Offers
        Offers offers = new Offers();
        offers.addAll(syncOffers);
        JAXB.marshal(offers, new File("offers.xml"));

        //Calculating and printing of runtime, memory footprint, request and offers amount
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long actualMemUsed = afterUsedMem - beforeUsedMem;
        System.out.println(actualMemUsed + " bytes      " + actualMemUsed / 1024 + " kilobytes      " + actualMemUsed / 1048576 + " megabytes     ");

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime + " nanoseconds    " + totalTime / 1000000000.0 + " seconds");

        System.out.println(requestsAmount + " requests");

        System.out.println(offers.size() + " offers");
    }
}
