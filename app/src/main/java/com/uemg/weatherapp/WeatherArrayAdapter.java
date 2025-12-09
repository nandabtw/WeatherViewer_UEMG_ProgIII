package com.uemg.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Adapter personalizado para exibir objetos Weather em uma ListView.
 * Implementa o padrão ViewHolder para otimização, conforme o Capítulo 7.
 */
public class WeatherArrayAdapter extends ArrayAdapter<Weather> {

    // Classe interna para armazenar referências aos Views (ViewHolder Pattern)
    private static class ViewHolder {
        TextView dateTextView;
        TextView iconTextView;
        TextView descriptionTextView;
        TextView tempTextView;
        TextView humidityTextView;
    }

    public WeatherArrayAdapter(Context context, List<Weather> forecast) {
        super(context, -1, forecast); // -1 indica que o layout será inflado manualmente
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtém o objeto Weather para esta posição
        Weather day = getItem(position);

        ViewHolder viewHolder; // Objeto para armazenar referências aos Views

        // Verifica se uma View existente está sendo reutilizada
        if (convertView == null) {
            // Se não houver View para reutilizar, infla o layout list_item.xml
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            // Cria um novo ViewHolder e armazena as referências
            viewHolder = new ViewHolder();
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
            viewHolder.iconTextView = (TextView) convertView.findViewById(R.id.iconTextView);
            viewHolder.descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
            viewHolder.tempTextView = (TextView) convertView.findViewById(R.id.tempTextView);
            viewHolder.humidityTextView = (TextView) convertView.findViewById(R.id.humidityTextView);

            // Armazena o ViewHolder na View para acesso futuro
            convertView.setTag(viewHolder);
        } else {
            // Se a View for reutilizada, recupera o ViewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Preenche os Views com os dados do objeto Weather
        viewHolder.dateTextView.setText(day.getDate());
        viewHolder.iconTextView.setText(day.getIcon());
        viewHolder.descriptionTextView.setText(day.getDescription());
        viewHolder.tempTextView.setText(String.format("Mínima %.1f°C / Máxima %.1f°C",
                day.getMinTempC(), day.getMaxTempC()));
        viewHolder.humidityTextView.setText(String.format("Umidade: %.0f%%",
                day.getHumidity() * 100));

        return convertView;
    }
}
