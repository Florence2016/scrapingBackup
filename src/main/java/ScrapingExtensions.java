import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScrapingExtensions extends Scraper {


    private static String[] columns = {"Extension Name", "Users", "Reviews", "Ratings"};

    private static List<Extensions> extensions =  new ArrayList<>();

    static{

        //Download the page and parse it
        String url = "https://chrome.google.com/webstore/detail/metamask/nkbihfbeogaeaoehlefnkodbefgpgknn";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // initializing web elements
        Element table = doc.select("div.e-f-w-Va").first();

        //get extension name on the container
        String title = table.select("h1.e-f-w").text();

        //get extension users on the container
        String users = table.select("span.e-f-ih").text().replaceAll(" users", " ");

        //get extension reviews on the container
        String reviews = table.select("span.q-N-nd").text().replaceAll("[()]", "");

        //get value in a tag on the container
        String ratings = table.select("span.q-N-nd[aria-label]").attr("aria-label").replaceAll("[^0-9.]", "").substring(0,3);

        extensions.add(new Extensions(title, users, reviews,ratings));

    }

    public static void main(String [] args) throws IOException {

        // Create a Workbook
        Workbook workbook = new XSSFWorkbook();     // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances for various things like DataFormat,
           Hyperlink, RichTextString etc in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Extensions");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.GREEN.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Creating cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }


        // Create Other rows and cells with employees data
        int rowNum = 1;
        for(Extensions extension: extensions) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(extension.getTitle());

            row.createCell(1)
                    .setCellValue(extension.getUsers());

            row.createCell(2)
                    .setCellValue(extension.getReviews());
            row.createCell(3)
                    .setCellValue(extension.getRatings());
        }

        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("poi-generated-file.xlsx");
        workbook.write(fileOut);
        fileOut.close();

    }
}
