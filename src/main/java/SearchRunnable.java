import Entity.Offer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class SearchRunnable implements Runnable{
    private String url;
    private List<Offer> syncOffers;
    SearchRunnable(String url, List<Offer> syncOffers){
        this.url = url;
        this.syncOffers = syncOffers;
    }


    @Override
    public void run() {
        Connection.Response response;
        try {
            //Search page loading
            response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .timeout(0)
                    .execute();

            Parser.requestsAmount++;

            //New thread for every offer on this page
            Document document = response.parse();
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
}
