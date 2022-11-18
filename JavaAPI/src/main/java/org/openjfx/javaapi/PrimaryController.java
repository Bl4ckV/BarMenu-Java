package org.openjfx.javaapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.json.JSONObject;

public class PrimaryController {

    @FXML
    private Button bDescargar;
    @FXML
    private CheckBox c1, c2;
    @FXML
    private TableColumn<?, ?> columna1, columna2, columna3;
    @FXML
    private ImageView imagen2;
    @FXML
    private TableView<?> tabla;
    @FXML 
    private ObservableList<Bebida> bebidas = FXCollections.observableArrayList();
    private ArrayList<Bebida> arrayBebidas = new ArrayList<Bebida>();
    @FXML
    private Label lInfo;
    private double totalBytes = 0;
    private String globalData, aux;
    
    String getJson(String url) {
       InputStream inputStream = null;
        try {
            inputStream = new URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String response = null;
        try {
            response = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(response);
        String globalData = jsonObject.toString();
        return globalData;
    }

    @FXML
    void sendRequest(ActionEvent event) {
        columna1.setCellValueFactory(new PropertyValueFactory("nombre"));
        columna2.setCellValueFactory(new PropertyValueFactory("alcoholica"));
        columna3.setCellValueFactory(new PropertyValueFactory("fecha"));
        
        String url = null;
        
        if (c1.isSelected() && !c2.isSelected())
            url = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic";
        else if (!c1.isSelected() && c2.isSelected())
            url = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Non_Alcoholic";
        else
            url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=";
        
        aux = getJson(url);
        bebidas.clear();
        arrayBebidas.clear();
        totalBytes = 0;
        globalData = aux;
        getData("\"strDrink\":", 12, true, 1);
        globalData = aux;
        getData("\"strAlcoholic\":", 16, false, 2);
        globalData = aux;
        getData("\"dateModified\":", 16, false, 3);
        llenarTabla();
        lInfo.setText(getDate());
    }
    
    @FXML
    void onChange(MouseEvent event) {
        bDescargar.setStyle("-fx-background-color: yellow");
    }

    @FXML
    void onChange2(MouseEvent event) {
        bDescargar.setStyle("-fx-background-color: orange");
    }
    
    @FXML
    void showImage(MouseEvent event) {
        if (tabla.getSelectionModel().getSelectedItems().size() > 0) {
            String nombre = arrayBebidas.get(tabla.getSelectionModel().getSelectedIndex()).getNombre();
            String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + nombre;
            globalData = getJson(url);
            getData("\"strDrinkThumb\":", 17, false, 4);
            imagen2.setImage(new Image(globalData.substring(0, globalData.indexOf("\""))));
        }
           
    }

    void getData(String cut, int length, boolean control, int control2) {
        String result;
        int i = 0;
        while (globalData.indexOf(cut) != -1) {
            if (control)
                arrayBebidas.add(new Bebida());
            globalData = globalData.substring(globalData.indexOf(cut) + length);
            result = globalData.substring(0, globalData.indexOf("\"")); 
            totalBytes += (double)result.length();
            switch (control2) {
                case 1:
                    arrayBebidas.get(i).setNombre(result);
                    break;
                case 2:
                    arrayBebidas.get(i).setAlcoholica(result);
                    break;
                case 3:
                    arrayBebidas.get(i).setFecha(result);
                    break;
            }
            i++;
        }
    }
    
    void llenarTabla() {
        for (int i = 0; i < arrayBebidas.size(); i++) 
            bebidas.add(arrayBebidas.get(i));
        tabla.setItems((ObservableList)bebidas);
    }
    
    String getDate() {
        String date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        return "Datos recibidos -> " + date + " (" + totalBytes + " Bytes)";
    }

}
