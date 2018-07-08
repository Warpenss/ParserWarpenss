import Entity.Offer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class ParserRunnable implements Runnable {
    private Element link;
    private List<Offer> offers;
    private String offerLoadType;

    ParserRunnable(Element link, List<Offer> offers, String offerLoadType) {
        this.link = link;
        this.offers = offers;
        this.offerLoadType = offerLoadType;
    }

    @Override
    public void run() {
        Connection.Response response;
        try {
            //Offer page loading
            response = Jsoup.connect(link.attr("abs:href"))
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .method(Connection.Method.GET)
                    .execute();

            Document document = Jsoup.connect(link.attr("abs:href"))
                    .data("formhash", "97bfbf").data("hdn_refer", "http://www.a5.cn/")
                    .data("account", "userID").data("autoLogin", "1").data("password", "your password")
                    .cookies(response.cookies())
                    .header("Accept", "application/json, text/javascript, */*; q=0.01")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .get();

            Parser.requestsAmount++;

            //Collecting all needed information
            String name = document.select("span[class=styles__title--3Jos_]").text();
            String brand = StringUtils.substringBefore(document.select("div[class=styles__titleContainer--33zw2] > h1").text(), " |");
            String color = document.select("span[class=styles__title--UFKYd styles__isHoveredState--2BBt9]").text();
            Elements sizes = document.select("div[class*=styles__row--sWR75]");
            String price = document.select("div[class*=productPricesContainer]").get(0).child(0).text();
            String initialPrice = document.select("div[class=priceStyles__strike--PSBGK]").text();
            String description = document.select("div[class=col-sm-6 styles__detailsContainer--1ku-C]").text();
            String articleId = StringUtils.substringAfter(document.select("li[class=styles__articleNumber--1UszN]").text(), "Artikel-Nr: ");
            String shippingCosts = StringUtils.getDigits(document.select("span[class=styles__label--1cfc7]").text());

            //Some offers like glasses doesn't have size
            if (sizes.isEmpty()) {
                String size = "";
                Offer offer = new Offer(name, brand, color, size, price, initialPrice, description, articleId, shippingCosts);
                if (!containsArticleId(offers, articleId)) {
                    offers.add(offer);
                }
            } else
                //Creating new offer for every size
                for (Element element : sizes) {
                    String size = element.child(0).text();
                    Offer offer = new Offer(name, brand, color, size, price, initialPrice, description, articleId, shippingCosts);
                    if (!containsArticleId(offers, articleId)) {
                        offers.add(offer);
                    }
                }


            //If uncomment this - part we can load all other colors from offer page
            //But because all other colors caught from other search pages - there is no need in that now.
//            if (offerLoadType.equals("INITIAL_LOAD")) {
//                try {
//                    Elements links = document.select("div[class=styles__thumbnailWrapper--3uDnG] > a");
//                    for (Element link : links) {
//                        if (!link.attr("abs:href").equals(response.url().toString())) {
//                            Thread thread = new Thread(new ParserRunnable(link, offers, "COLOR_LOAD"), "Thread: " + link.attr("abs:href"));
//                            thread.start();
//                            thread.join();
//                        }
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Check if we have already added this Offer
    boolean containsArticleId(final List<Offer> list, final String articleId) {
        return list.stream().filter(o -> o.getArticleId().equals(articleId)).findFirst().isPresent();
    }
}
