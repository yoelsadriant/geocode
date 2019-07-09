package yoel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.iakovlev.timeshape.TimeZoneEngine;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

class Geolocation {
    private static final String API_KEY = "9b36a3727777424eb1287f79e6388c28";

    Geolocation(String srcUrl, String dstUrl) {
        Path src = Paths.get(srcUrl);
        Path dst = Paths.get(dstUrl);
        final BufferedReader br;
        final BufferedWriter bw;
        String line;
        try {
            br = Files.newBufferedReader(src);
            bw = Files.newBufferedWriter(dst);
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String dateTime = data[0];
                double lat = Double.parseDouble(data[1]);
                double lon = Double.parseDouble(data[2]);
                bw.write(writeOutput(dateTime, lat, lon));
                bw.newLine();
            }
            bw.close();
            JOptionPane.showMessageDialog(new JFrame(), "Saving done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String writeOutput(String dateTime, double lat, double lon) {
        String response = getJsonFromReverseGeolocation(lat, lon);
        String result = dateTime + "," + lat + "," + lon + "," + findTimeZoneId(lat, lon) + "," + convertTimeStampToStringDateTime(response);
        System.out.println(result);
        return result;
    }

    private String getJsonFromReverseGeolocation(double lat, double lon) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject
                ("https://api.opencagedata.com/geocode/v1/json?key=" + API_KEY + "&q=" + lat + "+" + lon + "&pretty=1&no_annotations=1", String.class);
    }

    private String convertTimeStampToStringDateTime(String response) {
        JsonElement jElement = new JsonParser().parse(response);
        JsonObject jObject = jElement.getAsJsonObject();
        JsonObject timestamp = jObject.get("timestamp").getAsJsonObject();
        long unixTime = timestamp.get("created_unix").getAsLong();
        Date date = new Date(unixTime * 1000L);
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String stringDate = sdf.format(date);
        return stringDate.replace(' ', 'T');
    }

    private String findTimeZoneId(double lat, double lon) {
        TimeZoneEngine engine = TimeZoneEngine.initialize();
        Optional<ZoneId> zoneId = engine.query(lat, lon);
        String id = "";
        if (zoneId.isPresent()) {
            id = zoneId.get().toString();
        }
        return id;
    }

}



