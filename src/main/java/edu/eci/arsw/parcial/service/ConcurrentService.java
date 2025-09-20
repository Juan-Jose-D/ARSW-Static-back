package edu.eci.arsw.parcial.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.web.client.RestTemplate;

@Service
public class ConcurrentService {

    private final RestTemplate restTemplate;
    
    public ConcurrentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void realizarSolicitudesConcurrentes(){
        Thread solicitud = new Thread(() -> {
            procesarDatosExterna();
        });

        solicitud.start();

    }

    public CompletableFuture<List<String>> procesarDatosExterna() {
        return CompletableFuture.supplyAsync(() -> {
            String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=demo";
            
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            List<Map<String, Object>> timeSeries = (List<Map<String, Object>>) response.get("Time Series (5min)");

            List<String> valores = new ArrayList<>();
            if (timeSeries != null) {
                for (Map<String, Object> element : timeSeries) {
                    valores.add((String) element.get("Meta Data"));
                }
            }
            return valores;
        });
    }

}
