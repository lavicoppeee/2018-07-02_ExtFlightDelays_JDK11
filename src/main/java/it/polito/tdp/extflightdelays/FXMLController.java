package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Arco;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare ai branch master_turnoB o master_turnoC per turno B o C

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField distanzaMinima;

    @FXML
    private Button btnAnalizza;

    @FXML
    private ComboBox<Airport> cmbBoxAeroportoPartenza;

    @FXML
    private Button btnAeroportiConnessi;

    @FXML
    private TextField numeroVoliTxtInput;

    @FXML
    private Button btnCercaItinerario;

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {

    	this.txtResult.clear();
    	this.cmbBoxAeroportoPartenza.getItems().clear();
    	
    	   String xs=this.distanzaMinima.getText();
       	
           Integer x;
       	
       	try {
       		x=Integer.parseInt(xs);
       	}catch(NumberFormatException e) {
       		
       		txtResult.setText("Devi inserire solo numeri");
       		return ;
       	}
       	
       	this.model.creaGrafo(x);
       	txtResult.appendText("Grafo Creato!\n");
    	txtResult.appendText("# Vertici: " + model.nVertici()+ "\n");
    	txtResult.appendText("# Archi: " + model.nArchi() + "\n");
    	
    	this.cmbBoxAeroportoPartenza.getItems().addAll(model.getVertici());
    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {
    	this.txtResult.clear();
    	Airport partenza=this.cmbBoxAeroportoPartenza.getValue();
        if(partenza == null) {
            	txtResult.clear();
            	txtResult.appendText("Seleziona un airport!\n");
            	return ;
        	}
        
        List<Arco> a=this.model.getConnessi(partenza);
        
        for(Arco v:a) {
        	txtResult.appendText(v.toString()+"\n");
        }
        
    }

    @FXML
    void doCercaItinerario(ActionEvent event) {
    	this.txtResult.clear();
    	Airport partenza=this.cmbBoxAeroportoPartenza.getValue();
    	
        if(partenza == null) {
            	txtResult.clear();
            	txtResult.appendText("Seleziona un airport!\n");
            	return ;
        	}
        
        String xs=this.numeroVoliTxtInput.getText();
       	
        Integer x;
    	
    	try {
    		x=Integer.parseInt(xs);
    	}catch(NumberFormatException e) {
    		
    		txtResult.setText("Devi inserire solo numeri");
    		return ;
    	}
    	
    	this.model.trovaPercorso(partenza, x);
    	
    	txtResult.appendText("Il cammino dall'airport di partenza "+ partenza + " è di peso "+ model.getBestPeso()+ " ed è formato da: \n");
    	
    	 for(Airport a: this.model.getBestCammino()) {
         	txtResult.appendText(a.toString()+"\n");
         }
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCercaItinerario != null : "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
