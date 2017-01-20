package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import bean.Parola;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SampleController {
	
	private Model m = new Model();
	
	public void setModel(Model m){
		this.m=m;
	}

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtLettere;

    @FXML
    private TextField txtParola;

    @FXML
    private Button btnGenera;

    @FXML
    private Button btnVicini;

    @FXML
    private Button btnConnessi;

    @FXML
    private TextArea txtResult;

    @FXML
    void doConnessi(ActionEvent event) {

    }

    @FXML
    void doGenera(ActionEvent event) {
    	txtResult.clear();
    	try{
    	      int lung =Integer.parseInt(txtLettere.getText());
    	      UndirectedGraph<Parola, DefaultEdge> grafo = m.buildGraph(lung);
    	      txtResult.appendText(grafo.toString());
    	}catch(NumberFormatException e){
    		txtResult.appendText("Formato errato! \n");
    		return;
    	}
    	btnConnessi.setDisable(false);
    	btnVicini.setDisable(false);
    	
    }

    @FXML
    void doVicini(ActionEvent event) {
    	try{
  	      int lung =Integer.parseInt(txtLettere.getText());
  	      String p = txtParola.getText();
  	      if(p.length()!=lung){
  	    	  txtResult.appendText("Lunghezza errata \n ");
  	    	  return;
  	      }
  	   
  	    //  Parola parola = m.trovaParola(p, lung);
  	      
  	      UndirectedGraph<Parola, DefaultEdge> grafo = m.buildGraph(lung);
  	      
  	     List<Parola> vicini = m.getTrovaVicini(p, grafo, lung);
  	     txtResult.appendText(vicini.toString()+" \n");
  	
    	}catch(NumberFormatException e){
  		       txtResult.appendText("Formato errato! \n");
  		       return;
  	}
    	

    }

    @FXML
    void initialize() {
        assert txtLettere != null : "fx:id=\"txtLettere\" was not injected: check your FXML file 'Sample.fxml'.";
        assert txtParola != null : "fx:id=\"txtParola\" was not injected: check your FXML file 'Sample.fxml'.";
        assert btnGenera != null : "fx:id=\"btnGenera\" was not injected: check your FXML file 'Sample.fxml'.";
        assert btnVicini != null : "fx:id=\"btnVicini\" was not injected: check your FXML file 'Sample.fxml'.";
        assert btnConnessi != null : "fx:id=\"btnConnessi\" was not injected: check your FXML file 'Sample.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Sample.fxml'.";
        
        btnConnessi.setDisable(true);
        btnVicini.setDisable(true);
       
        
        

    }
}
