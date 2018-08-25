package sample;

import Classes.Correo;
import Classes.Excel;
import Classes.Traspaso;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import static Classes.Excel.leerExcel;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Traspaso de articulos entre tiendas");
        primaryStage.setScene(new Scene(root, 415, 200));
        primaryStage.show();


    }




    public static void main(String[] args) {
      //  ArrayList<Traspaso> lista = Excel.leerExcel(new File("C:\\Users\\iworth\\iCloudDrive\\Desktop\\p.xlsx"),8);
      //  System.out.println(lista.toString());
      //  Excel.escribirExcel("hola",lista);
      //  Correo.enviarCorreo("","","",new File(""));
        launch(args);

    }
}
