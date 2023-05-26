package com.tfgm;

import java.io.Console;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tfgm.models.*;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        HttpClient httpclient = HttpClients.createDefault();

        ArrayList<TramStop> tramStops = new ArrayList<>();

        // try
        // {
            URIBuilder builder = new URIBuilder("https://api.tfgm.com/odata/Metrolinks");
            // "Id":128071,"Line":"South Manchester","TLAREF":"WTH","PIDREF":"WTH-TPID01",
            // "StationLocation":"Withington","AtcoCode":"9400ZZMAWIT2","Direction":"Outgoing","Dest0":"East Didsbury","Carriages0":"Single","Status0":"Due","Wait0":"3","Dest1":"East Didsbury","Carriages1":"Double","Status1":"Due","Wait1":"14","Dest2":"East Didsbury","Carriages2":"Single","Status2":"Due","Wait2":"16","Dest3":"","Carriages3":"","Status3":"","MessageBoard":"Welcome to Metrolink. For up to date travel information contact Metrolink twitter on @MCRMetrolink or visit www.TfGM.com. Take care of each other Manchester.","Wait3":"","LastUpdated":"2023-05-26T15:29:19Z"


            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", "810dfa20bbd44815b07d8cb6f9d3be96");


            // // Request body
            // StringEntity reqEntity = new StringEntity("{body}");
            // ((HttpRequest) request).setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) 
            {
                String jsonString = EntityUtils.toString(entity);
                JSONObject obj = new JSONObject(jsonString);
                // System.out.println(jsonString.substring(0, 200));
                JSONArray array = new JSONArray(obj.getJSONArray("value"));

                for(int i = 0; i < array.length(); i++)
                {
                    TramStop temp = new TramStop(
                        array.getJSONObject(i).getString("StationLocation"),
                        array.getJSONObject(i).getString("Line"),
                        array.getJSONObject(i).getString("Direction"),
                        array.getJSONObject(i).getInt("Id"));

                    for(int j = 0; j<4; j++)
                    {
                        if(!(array.getJSONObject(i).getString("Dest" + j).equals("")))
                        {
                            temp.addTramTrip(new TramTrip(
                                array.getJSONObject(i).getString("Dest" + j),
                                array.getJSONObject(i).getString("Carriages" + j) == "Single" ? 1:2,
                                array.getJSONObject(i).getString("Status" + j),
                                Integer.parseInt(array.getJSONObject(i).getString("Wait" + j))));
                        }
                    }

                    tramStops.add(new TramStop(temp));



                }

                System.out.println(tramStops);


            }
        // }
        // catch (Exception e)
        // {
        //     System.out.println(e);
        // }
    }

    // // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
}
