package com.uemg.weatherapp;

/**
 * Classe modelo para armazenar os dados de previsão do tempo de um dia.
 * Baseado na estrutura JSON da API.
 */
public class Weather {
    private final String date;
    private final double minTempC;
    private final double maxTempC;
    private final String description;
    private final double humidity;
    private final String icon;

    public Weather(String date, double minTempC, double maxTempC, String description, double humidity, String icon) {
        this.date = date;
        this.minTempC = minTempC;
        this.maxTempC = maxTempC;
        this.description = description;
        this.humidity = humidity;
        this.icon = icon;
    }

    // Getters (apenas o básico para acesso)
    public String getDate() {
        return date;
    }

    public double getMinTempC() {
        return minTempC;
    }

    public double getMaxTempC() {
        return maxTempC;
    }

    public String getDescription() {
        return description;
    }

    public double getHumidity() {
        return humidity;
    }

    public String getIcon() {
        return icon;
    }
}