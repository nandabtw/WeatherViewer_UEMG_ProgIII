package com.uemg.weatherapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

// Atividade principal da aplicação.
 //Responsável por iniciar a consulta e exibir os resultados na ListView.

public class MainActivity extends Activity {

    private static final String TAG = "WeatherApp";
    private static final int DAYS = 7; // Valor fixo conforme requisito

    //Variáveis de instância
    private ListView weatherListView;
    private WeatherArrayAdapter weatherArrayAdapter;
    private TextInputEditText cityEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define o layout principal (activity_main.xml)
        setContentView(R.layout.activity_main);

        //Entrada da cidade
        cityEditText = findViewById(R.id.cityEditText);

        //Exibição da lista (ListView)
        weatherListView = findViewById(R.id.weatherListView);

        // Inicializa o Adapter com uma lista vazia
        weatherArrayAdapter = new WeatherArrayAdapter(this, new ArrayList<Weather>());
        weatherListView.setAdapter(weatherArrayAdapter);

        //Botão de consulta (FloatingActionButton)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inicia a consulta ao clicar no FAB
                loadForecast();
            }
        });

        // Inicia a consulta automaticamente ao abrir o app (opcional, mas útil para teste)
        loadForecast();
    }

    //Inicia a tarefa de rede em uma Thread separada.
     // Simula o comportamento de um AsyncTask.
    private void loadForecast() {
        final String city = cityEditText.getText().toString();

        if (city.trim().isEmpty()) {
            Toast.makeText(this, "Por favor, digite a cidade.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Consultando previsão para " + city + "...", Toast.LENGTH_SHORT).show();

        // Inicia a Thread para não bloquear a UI
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 4. Consumo da API usando GET e 6. Processamento de JSON
                final List<Weather> forecast = WeatherAPITask.getForecast(city, DAYS);

                // Retorna para a UI Thread para atualizar a interface
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(forecast);
                    }
                });
            }
        }).start();
    }

    // Atualiza a ListView com os dados da previsão.
    private void updateUI(List<Weather> forecast) {
        if (forecast != null && !forecast.isEmpty()) {
            // Limpa o adapter e adiciona os novos dados
            weatherArrayAdapter.clear();
            weatherArrayAdapter.addAll(forecast);
            // Notifica o adapter que os dados mudaram
            weatherArrayAdapter.notifyDataSetChanged();
            Log.i(TAG, "Previsão atualizada com sucesso. Exibindo " + forecast.size() + " dias.");
        } else {
            // 5. Tratamento de erros (exibição)
            Toast.makeText(this, "Falha ao obter a previsão. Verifique o Logcat.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Falha ao processar a previsão.");
        }
    }
}
