package it.polito.tdp.flightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Airport;
import it.polito.tdp.flightdelays.model.Model;
import it.polito.tdp.flightdelays.model.PairAirportsCount;
import it.polito.tdp.flightdelays.model.Passeggero;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FlightDelaysController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private ComboBox<Airline> cmbBoxLineaAerea;

    @FXML
    private Button caricaVoliBtn;

    @FXML
    private TextField numeroPasseggeriTxtInput;

    @FXML
    private TextField numeroVoliTxtInput;
    
    private Model model ;
    
    @FXML
    void doCaricaVoli(ActionEvent event) {
    	Airline airline = this.cmbBoxLineaAerea.getValue() ;
    	if(airline == null) {
    		this.txtResult.appendText("ERRORE: selezionare una linea aerea.\n");
    		return ;
    	}
    	model.createGraph(airline) ;
    	List<PairAirportsCount> worse = model.getWorst10();
    	if(worse.isEmpty() || worse == null) {
    		this.txtResult.appendText("ERROR\n");
    		return ;
    	}
    	for(PairAirportsCount pac : worse) {
    		this.txtResult.appendText(pac + "\n");
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	try {
    		int K = Integer.parseInt(this.numeroPasseggeriTxtInput.getText());
    		int V = Integer.parseInt(this.numeroVoliTxtInput.getText());
    		Airline airline = this.cmbBoxLineaAerea.getValue() ;
        	if(airline == null) {
        		this.txtResult.appendText("ERRORE: selezionare una linea aerea.\n");
        		return ;
        	}
    		List<Passeggero> delays = model.simula(K,V,airline) ;
    		if(delays!=null) {
    			this.txtResult.appendText("RITARDI ACCUMULATI DAI PASSEGGERI DURANTE LA SIMULAZIONE:\n");
    			for(Passeggero p : delays)
    				this.txtResult.appendText("Passeggero "+ (p.getIdPasseggero()+1) + " delay = "+p.getRitardo()+" minuti\n");
    		}
    		
    	} catch(NumberFormatException e) {
    		this.txtResult.appendText("Inserire un numero di passeggero o di voli validi.\n");
    	}
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert cmbBoxLineaAerea != null : "fx:id=\"cmbBoxLineaAerea\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert caricaVoliBtn != null : "fx:id=\"caricaVoliBtn\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert numeroPasseggeriTxtInput != null : "fx:id=\"numeroPasseggeriTxtInput\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'FlightDelays.fxml'.";

    }
    
	public void setModel(Model model) {
		this.model = model ;
		for(Airline a : model.getAirlines()) {
			this.cmbBoxLineaAerea.getItems().add(a);
		}
	}
}
