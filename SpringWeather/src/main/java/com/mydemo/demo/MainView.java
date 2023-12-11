package com.mydemo.demo;

import org.json.JSONException;
import org.json.JSONObject;

import com.mydemo.demo.controller.WeatherService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
@CssImport("./styles/styles.css")
public class MainView extends VerticalLayout {

	
	private TextField cityTxt;
	private Button searchBtn;
	private Span tempSpan;
	private Span feelsLikeSpan, tempMinSpan, tempMaxSpan, humiditySpan;
	private Span headerSpan;
	private WeatherService weatherService;

	MainView() {
		weatherService = new WeatherService();
		setUpLogo();
		setUpHeader();
		setForm();
		updateUI(weatherService.getWeather());
	}

	private void setUpHeader() {
		VerticalLayout headerLayout = new VerticalLayout();
		headerLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		
		headerSpan = new Span("The Weather");
		headerSpan.setClassName("header");
		
		headerLayout.add(headerSpan);
		add(headerLayout);
	}

	private void setUpLogo() {
	    VerticalLayout setUpLogoLayout = new VerticalLayout();
	    setUpLogoLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
	    setUpLogoLayout.setClassName("logo");

	    add(setUpLogoLayout);
	}

		
	public void updateUI(JSONObject weatherData) {
		try {
			if (weatherData.has("main")) {
				JSONObject mainObject = weatherData.getJSONObject("main");
				String cityName = weatherService.getCityName();

				//The temperature is
				if (mainObject.has("temp")) {
					double temperature = mainObject.getDouble("temp");
					tempSpan.setText("   Temperature in " + cityName + ": " + temperature + "°C");
					
				} else {
					System.out.println("Temperature information not found in the response");
				}
				
				//It feels like
				if(mainObject.has("feels_like")) {
					double feels = mainObject.getDouble("feels_like");
					feelsLikeSpan.setText("   Feels like: " + feels + "°C");
				}else {
					System.out.println("Feels like information not found in the response");
				}
				
				//Min temperatures
				if(mainObject.has("temp_min")) {
					double tempMin = mainObject.getDouble("temp_min");
					tempMinSpan.setText("   Minimum temperature: " + tempMin + "°C");
				}else {
					System.out.println("Temp min information not found in the response");
				}
				
				//Max temperatures
				if(mainObject.has("temp_max")) {
					double tempMax = mainObject.getDouble("temp_max");
					tempMaxSpan.setText("   Maximum temperature: " + tempMax + "°C");
				}else {
					System.out.println("Temp max information not found in the response");
				}
				
				//Humidity is
				if(mainObject.has("temp_max")) {
					int humidity = mainObject.getInt("humidity");
					humiditySpan.setText("   Humidity: " + humidity + "%");
				}else {
					System.out.println("Humidity information not found in the response");
				}
								
			} else {
				System.out.println("No main information found in the response");
			}
		} catch (JSONException e) {
			System.out.println("Error parsing JSON: " + e.getMessage());
		}
	}

	private void setForm() {
		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		formLayout.setSpacing(true);

		feelsLikeSpan = new Span();
		feelsLikeSpan.setClassName("body");
		
		tempMinSpan = new Span();
		tempMinSpan.setClassName("body");
		
		tempMaxSpan = new Span(); 
		tempMaxSpan.setClassName("body");
		
		humiditySpan = new Span();
		humiditySpan.setClassName("body");
		
		tempSpan = new Span();
		tempSpan.setClassName("tempBody");


		cityTxt = new TextField();
		cityTxt.getStyle().set("--lumo-secondary-text-color", "#21a998"); //Text field label colour
		cityTxt.setLabel("Weather for... ");
		cityTxt.setWidth("50%");

		searchBtn = new Button("Get weather");
		searchBtn.setIcon(VaadinIcon.SEARCH.create());
		
		searchBtn.setClassName("button");
		searchBtn.addClickListener(clickEvent -> {
			if (cityTxt.getValue().equals("")) {
				Notification.show("Enter city");
			} else {
				try {
					// Set the city name using the setter
					weatherService.setCityName(cityTxt.getValue());
					JSONObject weatherData = weatherService.getWeather();

					if (weatherData != null) {
						updateUI(weatherData);
						cityTxt.clear();
					} else {
						System.out.println("Failed to retrieve weather data");
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});

		formLayout.add(cityTxt, searchBtn, tempSpan, feelsLikeSpan, tempMinSpan, tempMaxSpan, humiditySpan);
		add(formLayout);
	}
}
