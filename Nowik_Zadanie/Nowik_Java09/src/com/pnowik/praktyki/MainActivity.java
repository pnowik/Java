package com.pnowik.praktyki;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.JSONObject;

import javax.imageio.ImageIO;

public class MainActivity {

    private static String htmlString, body = "";

    //Wykorzystywane przy zapiswywaniu obrazka bezpośrednio
    private static int count = 0;


    public static void main(String[] args) {


        //Pobranie danych (liczb) z pliku i dodanie do listy

        Path filePath = Paths.get("nr.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(filePath);
            List<Integer> dataFromFile = new ArrayList<>();
            while (scanner.hasNext()) {
                if (scanner.hasNextInt()) {
                    dataFromFile.add(scanner.nextInt());
                } else {
                    scanner.next();
                }
            }

        //Pobranie danych z template.html

            InputStream is = null;
            try {
                is = new FileInputStream("template.html");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = null;
            try {
                line = buf.readLine();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line).append("\n");
                try {
                    line = buf.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            htmlString = sb.toString();


        //Połączenie z serwisem i zapisanie obrazka

        for (Integer integer : dataFromFile) {
            try {
                jsonGetRequest(String.valueOf(integer));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Dodanie contentu do new_template.html

        addContent();

        //Wyświetlenie strony

        String webPage = String.valueOf(Paths.get("new_template.html"));
        java.awt.Desktop.getDesktop().browse(java.net.URI.create(webPage));

    }catch (IOException e) {
            e.printStackTrace();
        }

    }



    //Funkcja połączenia z serwisem

    private static void jsonGetRequest(String var) {
        JSONObject json;
        try {
            URL url = new URL("https://xkcd.com/" + var + "/info.0.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();
            InputStream inStream = connection.getInputStream();
            String text = new Scanner(inStream).useDelimiter("\\Z").next();
            json = new JSONObject(text);
            inStream.close();


            //Zapisanie obrazka (linku)

            URL urlImage = new URL(json.getString("img"));
            body = body + "\t<img src=\"" + urlImage + "\" \n\tclass=\"img-responsive\" alt=\"Responsive image\"/>\n\n" + "\t<br /><br />\n\n";


            /*
            //Zapisanie obrazka bezpośrednio

            BufferedImage bImage = ImageIO.read(new URL(String.valueOf(urlImage)));
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", byteOutStream );
            byte [] data = byteOutStream.toByteArray();
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(data);
            BufferedImage bImage2 = ImageIO.read(byteInStream);
            ImageIO.write(bImage2, "jpg", new File("image" + count + ".jpg") );

            body = "\n" + body + "<img src=\"" + Paths.get("image" + count + ".jpg") + "\" \nclass=\"img-responsive\" alt=\"Responsive image\"/>\n" + "<br /><br />";

            count++;
            */


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //Funkcja dodania contentu do new_template.html

    private static void addContent () {

        try {
            htmlString = htmlString.replace("$body", body);
            Path filePath2 = Paths.get("new_template.html");
            byte[] strToBytes = htmlString.getBytes();
            Files.write(filePath2, strToBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
