package com.tfgm.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tfgm.models.NewTramStop;
import com.tfgm.models.TramStopContainer;

public class TramController {

    private HashMap<String,NewTramStop> tramStopHashMap = new HashMap<>();

    
    public TramController(String tramDataPath) throws IOException
    {
        JSONArray allTramStopData = new JSONArray(
            Files.readAllLines(Paths.get(tramDataPath))
            .stream()
            .map(n -> String.valueOf(n))
            .collect(Collectors.joining()));

        // System.out.println(allTramStopData);
        

        for (int i = 0; i< allTramStopData.length(); i++)
        {
            JSONObject tempStop = allTramStopData.getJSONObject(i);
            tramStopHashMap.put(
                tempStop.getString("tramStopName"), 
                new NewTramStop(tempStop.getString("location"), tempStop.getString("direction"), tempStop.getString("line"))
                );

        }
        for (int i = 0; i< allTramStopData.length(); i++)
        {
            JSONObject tempStop = allTramStopData.getJSONObject(i);

            NewTramStop currentTramStop = tramStopHashMap.get(tempStop.getString("tramStopName"));

            currentTramStop.setPrevAndNextStops(
                tempStop.getJSONArray("prevStop").toList().stream()
                    .map(n -> tramStopHashMap.get(n))
                    .map(n -> new TramStopContainer(n))
                    .toArray(TramStopContainer[]::new)
            , tempStop.getJSONArray("nextStop").toList().stream()
                .map(n -> tramStopHashMap.get(n))
                .map(n -> new TramStopContainer(n))
                .toArray(TramStopContainer[]::new)
            );

        }

        //MAKE SURE YOU REMOVE APOSTROPHES AND WHITESPACE WHEN FINDING TRAMSTOP
        for(NewTramStop i : tramStopHashMap.values())
        {
            System.out.println(i.getStopName() + i.getDirection());
            System.out.println(i);
        }
    }
}
