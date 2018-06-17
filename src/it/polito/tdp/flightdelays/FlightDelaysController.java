package it.polito.tdp.flightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.AirportPartenzaDestinazione;
import it.polito.tdp.flightdelays.model.Model;
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
    		txtResult.appendText("ERRORE: selezionare una linea aerea.\n");
    		return ;
    	}
    	model.createGraph(airline) ;
    	List<AirportPartenzaDestinazione> worst = model.getWorst10() ;
    	if(worst!=null) {
    		txtResult.appendText("Le rotte peggiori sono:\n");
    		for(AirportPartenzaDestinazione apd : worst) {
    			this.txtResult.appendText(apd.getPartenza() + " ---> "+apd.getDestinazione()+ "\n");
    		}
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    		System.out.println("Simula!");
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
