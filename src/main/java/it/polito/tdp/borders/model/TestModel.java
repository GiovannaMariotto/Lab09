package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestModel {

	public static void main(String[] args) {

		Model model = new Model();
		model.creaGrafo(2000);
		Map<Integer,Country> m = model.getIdCountryMap();
		List<Country> c = model.getRaggiungibili(m.get(290));
		//Set<Country> set = model.getDegree(m.get(290));
		System.out.println(c.toString());
		
		
	//	System.out.println("TestModel -- TODO");
		
		
		
//		System.out.println("Creo il grafo relativo al 2000");
	//	model.creaGrafo(2000);
		
//		List<Country> countries = model.getCountries();
//		System.out.format("Trovate %d nazioni\n", countries.size());

//		System.out.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents());
		
//		Map<Country, Integer> stats = model.getCountryCounts();
//		for (Country country : stats.keySet())
//			System.out.format("%s %d\n", country, stats.get(country));		
		
	}

}
