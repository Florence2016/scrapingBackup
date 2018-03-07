import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

public class ExportCSV {

     static String value;
     static String reviews;
     static String users;
     static String title;
     static Document document;

    public static void main(String[] args) throws Exception {


        //Download the page and parse it
        String url = "https://chrome.google.com/webstore/detail/metamask/nkbihfbeogaeaoehlefnkodbefgpgknn";
        Document doc = Jsoup.connect(url).get();

        // initializing web elements
        Element table = doc.select("div.e-f-w-Va").first();

        //get extension name on the container
        String title = table.select("h1.e-f-w").text();

        //get extension users on the container
        String users = table.select("span.e-f-ih").text().replaceAll(" users", " ");

        //get extension reviews on the container
        String reviews = table.select("span.q-N-nd").text().replaceAll("[()]", "");

        //get value in a tag on the container
        String value = table.select("span.q-N-nd[aria-label]").attr("aria-label").replaceAll("[^0-9.]", "").substring(0,3);

        System.out.println(" Extension Name: "+  title  + " Users: " + users + " Reviews: " + reviews + " Ratings: " + value );

        final PrintWriter out = new PrintWriter("results.csv");
        out.write(" Extension Name: "+  title  + " Users: " + users + " Reviews: " + reviews + " Ratings: " + value );
        out.close();
    }

}
