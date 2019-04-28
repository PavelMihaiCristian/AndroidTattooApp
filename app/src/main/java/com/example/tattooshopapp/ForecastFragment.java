package com.example.tattooshopapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tattooshopapp.model.ForecastItem;
import com.example.tattooshopapp.persistance.ForecastsDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ForecastFragment extends Fragment {

    static final String REQUEST_URL = "http://api.openweathermap.org/data/2.5/find?q=Horsens&units=metric&APPID=6dd00c246bafc060aba1b00950df8692";

    private TextView mainConditionText;
    private TextView temperatureText;
    private TextView presureText;
    private TextView humidityText;
    private Button getWeatherButton;
    private Button historyButton;
    private EditText searchCriteria;
    private ForecastsDAO forecastsDAO;

    private String location;

    private RecyclerView recyclerView;
    private ForecastItemAdapter forecastItemAdapter;
    private ArrayList<ForecastItem> forecastItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forecast_fragment,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        forecastsDAO = new ForecastsDAO(getActivity());
        mainConditionText = view.findViewById(R.id.mainConditions);
        temperatureText = view.findViewById(R.id.temperature);
        presureText = view.findViewById(R.id.presure);
        humidityText = view.findViewById(R.id.humidity);
        getWeatherButton = view.findViewById(R.id.getWeather);
        historyButton = view.findViewById(R.id.seeHistory);
        searchCriteria = view.findViewById(R.id.searchCity);

        recyclerView = view.findViewById(R.id.forecastList);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        forecastItemAdapter = new ForecastItemAdapter(forecastItems);
        recyclerView.setAdapter(forecastItemAdapter);

        location = "Horsens";

        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = searchCriteria.getText().toString().trim();
                location = cityName;
                GetWeatherAsync getWeatherAsync = new GetWeatherAsync();
                if (cityName.length() == 0){
                    getWeatherAsync.execute(REQUEST_URL);
                } else {
                    StringBuilder stringBuilder = new StringBuilder("http://api.openweathermap.org/data/2.5/find?q=");
                    stringBuilder.append(cityName).append("&units=metric&APPID=6dd00c246bafc060aba1b00950df8692");
                    String url = stringBuilder.toString();
                    getWeatherAsync.execute(url);
                }
                searchCriteria.setText("");
            }
        });
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ForecastItem> items = forecastsDAO.readForecastRecords();
                forecastItems.addAll(items);
            }
        });
        historyButton.setVisibility(View.INVISIBLE);
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream is = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                is = urlConnection.getInputStream();
                jsonResponse = readFromStream(is);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (is != null)
                is.close();
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream is) throws IOException {
        StringBuilder output = new StringBuilder();
        if (is != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private class GetWeatherAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            String jsonResponse = "";
            try {
                url = new URL(strings[0]);
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONObject root = null;
            try {
                root = new JSONObject(s);
                JSONArray weather = root.getJSONArray("list");
                JSONObject firstWeatherElement = weather.getJSONObject(0);
                JSONArray WeatherList = firstWeatherElement.getJSONArray("weather");
                JSONObject WeatherElement = WeatherList.getJSONObject(0);

                String overallState = WeatherElement.getString("main");

                JSONObject mainCondition = firstWeatherElement.getJSONObject("main");
                String temp = mainCondition.getString("temp");
                String pressure = mainCondition.getString("pressure");
                String humidity = mainCondition.getString("humidity");

                mainConditionText.setText("Overall weather condition: " + overallState);
                temperatureText.setText("Temperature: " + temp);
                presureText.setText("Air Preassure: " + pressure);
                humidityText.setText("Air Humidity: " + humidity);

                ForecastItem forecastItem = new ForecastItem(overallState,location,Double.valueOf(temp),Double.valueOf(pressure),Double.valueOf(humidity));
                forecastsDAO.insertForecast(forecastItem);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
