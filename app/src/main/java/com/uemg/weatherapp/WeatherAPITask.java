package com.uemg.weatherapp;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
  classe responsável por realizar a requisição HTTP e processar o JSON.(API)
  Em uma aplicação Android real, esta lógica estaria dentro de um asynctask
  ou rhread separada para não bloquear a UI.
 */
public class WeatherAPITask {

    private static final String TAG = "WeatherAPITask";
    private static final String BASE_URL = "http://agent-weathermap-env-env.eba-6pzgqekp.us-east-2.elasticbeanstalk.com/api/weather";
    private static final String APP_ID = "AgentWeather2024_a8f3b9c1d7e2f5g6h4i9j0k1l2m3n4o5p6";

    //monta a URL de requisição com os parâmetros obrigatórios.
    public static String createURL(String city, int days ) {
        // A URL real deve ter a cidade codificada (URL-encoded)
        // Para simplicidade e aderência ao básico, usamos String.format
        return String.format("%s?city=%s&days=%d&APPID=%s",
                BASE_URL, city, days, APP_ID);
    }

    // Realiza a requisição HTTP e retorna a lista de previsões. tratamento de erros com try catch
    public static List<Weather> getForecast(String city, int days) {
        String urlString = createURL(city, days);
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String jsonString = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            //tratamento de erros (requisição)
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "Erro HTTP: " + connection.getResponseCode() + " - " + connection.getResponseMessage());
                return null;
            }

            // lê a resposta
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            jsonString = buffer.toString();

            // processamento de JSON no modelo retornado
            return convertJSONtoArrayList(jsonString);

        } catch (JSONException e) {
            //tratamento de erros (parsing JSON)
            Log.e(TAG, "Erro ao processar JSON: " + e.getMessage());
            return null;
        } catch (Exception e) {
            //tratamento de erros (rede/geral)
            Log.e(TAG, "Erro na requisição de rede: " + e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao fechar reader: " + e.getMessage());
                }
            }
        }
    }

    // Converte a String JSON em uma lista de objetos Weather.
    private static List<Weather> convertJSONtoArrayList(String forecastJsonString) throws JSONException {
        List<Weather> forecastList = new ArrayList<>();
        JSONObject forecastJson = new JSONObject(forecastJsonString);

        // O JSON retorna um objeto com a chave "days" que é um array
        JSONArray daysArray = forecastJson.getJSONArray("days");

        for (int i = 0; i < daysArray.length(); i++) {
            JSONObject day = daysArray.getJSONObject(i);

            // Extração dos dados
            String date = day.getString("date");
            double minTempC = day.getDouble("minTempC");
            double maxTempC = day.getDouble("maxTempC");
            String description = day.getString("description");
            
            // A umidade é um valor entre 0 e 1 (ex: 0.75), conforme a estrutura da resposta
            double humidity = day.getDouble("humidity");
            String icon = day.getString("icon"); // O emoji

            Weather weather = new Weather(date, minTempC, maxTempC, description, humidity, icon);
            forecastList.add(weather);
        }
        return forecastList;
    }
}
