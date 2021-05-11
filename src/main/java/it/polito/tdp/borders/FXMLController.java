
package it.polito.tdp.borders;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	this.txtResult.clear();
    	int x = 0;
    	try {
    		if(Integer.parseInt(this.txtAnno.getText())>0) {
    			x= Integer.parseInt(this.txtAnno.getText());
    		}else {
    			this.txtResult.appendText("Error - Wrong input");
    		}
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    		this.txtResult.appendText("Error - exception in converting Integer");
    	}
    	if(x>2006 || x<1860) {
    		this.txtResult.appendText("Anno non valido");
    		this.txtAnno.clear();
    		return;
    	}
    	this.model.creaGrafo(x);
    	SimpleGraph grafo = this.model.getGrafo();
    	String s = model.infoGrafo(grafo);
    	this.txtResult.appendText("Grafo Creato!"+"\n");
    	this.txtResult.appendText(s+"\n");
    	
    	List<Border> bordi = new ArrayList(this.model.getCountryPairs(x,this.model.getIdCountryMap()));
    	
    	for(Border b : bordi) {
    	if(b!=null) {
    		this.txtResult.appendText(b.toString()+"\n");
    	}
    	}
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
