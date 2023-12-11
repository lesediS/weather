package com.mydemo.demo.controller;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class WeatherService {
	private OkHttpClient client;
	private Response response;
	private String cityName = "Sandton"; //Default city

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	private final String APIKEY = "bb951a21d47bb075cf9c19f573e1a1ad";

	public JSONObject getWeather() {
	    client = new OkHttpClient();
	    Request request = new Request.Builder().url("https://api.openweathermap.org/data/2.5/weather?q=" + getCityName()
	            + "&appid=" + APIKEY + "&units=metric").build(); //Using metric

	    try {
	        response = client.newCall(request).execute();
	        String res = response.body().string();
	        return new JSONObject(res);
	    } catch (IOException | JSONException e) {
	        System.out.println(e.getMessage());
	    }
	    return null;
	}

	public void printWeatherArrayToConsole() {
	    try {
	        JSONObject weatherObject = getWeather();

	        if (weatherObject != null) {
	            System.out.println("Weather Response: " + weatherObject.toString(2));
	        } else {
	            System.out.println("Weather information not found in the response."); //If a city is not found then there is no weather information on it
	        }
	    } catch (JSONException e) {
	        System.out.println(e.getMessage());
	    }
	}
	
	public JSONArray returnWeatherArray() throws JSONException {
		JSONArray weatherJsonArray = getWeather().getJSONArray("weather"); //Return the array "weather"
		return weatherJsonArray;
	}
}
