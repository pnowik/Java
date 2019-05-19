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
import java.util.stream.Collectors;

import org.json.JSONObject;

import javax.imageio.ImageIO;

public class MainActivity {

    private static String htmlString, body = "";

    //Wykorzystywane przy zapiswywaniu obrazka bezpośrednio
    private static int count = 0;


    public static void main(String[] args) {


        //Pobranie danych (liczb) z pliku i dodanie do listy

        try {
            List<Integer> dataFromFile = Files.lines(Paths.get("nr.txt"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());


        //Pobranie danych z template.html

        htmlString = Files.readString(Paths.get("template.html"));


        //Połączenie z serwisem i zapisanie obrazka

        for (Integer integer : dataFromFile) {
                jsonGetRequest(String.valueOf(integer));
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
            String text = new Scanner(inStream, StandardCharsets.UTF_8).useDelimiter("\\Z").next();
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
