/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	String categoria = this.boxCategoria.getValue();
    	if(categoria==null) {
    		this.txtResult.appendText("Scegli una categoria di reato!\n");
    		return;
    	}
    	
    	Integer anno = this.boxAnno.getValue();
    	if(anno==null) {
    		this.txtResult.appendText("Scegliere un anno!\n");
    		return;
    	}
    	
    	this.model.generateGraph(categoria, anno);
    	this.txtResult.appendText("Il grafo è stato creato!\n");
    	this.txtResult.appendText("Numero vertici: "+this.model.getNumVertici()+"\n");
    	this.txtResult.appendText("Numero archi: "+this.model.getNumArchi()+"\n");
    	
    	List<Adiacenza> pesoMax = this.model.getArchiPesoMax();
    	for(Adiacenza a : pesoMax) {
    		txtResult.appendText(a.toString()+", "+a.getPeso()+"\n");
    	}
    	
    	this.btnPercorso.setDisable(false);
    	
    	this.boxArco.getItems().setAll(this.model.getArchi());
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	
    	Adiacenza adiacenza = this.boxArco.getValue();
    	if(adiacenza==null) {
    		this.txtResult.appendText("Scegliere un arco!\n");
    		return;
    	}
    	
    	this.txtResult.appendText("Il cammino minimo è:\n\n");    	
    	List<String> camminoMin = this.model.getCamminoPesoMin(adiacenza);
    	for(String s : camminoMin) {
    		this.txtResult.appendText(s.toString()+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.btnPercorso.setDisable(true);
    	this.boxCategoria.getItems().setAll(this.model.getOffenseCategory());
    	this.boxAnno.getItems().setAll(this.model.getYears());
    }
}
