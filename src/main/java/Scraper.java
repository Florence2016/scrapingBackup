import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class Scraper {

    String title,users, reviews, ratings;


    public void scraperExt() throws IOException {
        //Download the page and parse it
        String url = "https://chrome.google.com/webstore/detail/metamask/nkbihfbeogaeaoehlefnkodbefgpgknn";
        Document doc = Jsoup.connect(url).get();

        // initializing web elements
        Element table = doc.select("div.e-f-w-Va").first();

        //get extension name on the container
        title = table.select("h1.e-f-w").text();

        //get extension users on the container
        users = table.select("span.e-f-ih").text().replaceAll(" users", " ");

        //get extension reviews on the container
        reviews = table.select("span.q-N-nd").text().replaceAll("[()]", "");

        //get value in a tag on the container
        ratings = table.select("span.q-N-nd[aria-label]").attr("aria-label").replaceAll("[^0-9.]", "").substring(0,3);
    }
}
