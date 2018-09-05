package sample;


import Classes.Correo;
import Classes.Excel;
import Classes.Traspaso;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.util.ArrayList;

public class Controller {
    @FXML
    TextField txtFile, txtCorreo;
    @FXML
    PasswordField txtPassword;
    @FXML
    Button btoEnviar, btoFileChooser;
    @FXML
    Pane pane;
    public void initialize(){
        btoFileChooser.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);
            if(file!=null)txtFile.setText(file.getAbsolutePath());

        });
        pane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        });
        pane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    for (File file:db.getFiles()) {
                        filePath = file.getAbsolutePath();
                        txtFile.setText(filePath);
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        btoEnviar.setOnAction(event -> {

            Thread t1 = new Thread(() -> {
                btoEnviar.setDisable(true);
                if(!txtFile.getText().equals("") && !txtCorreo.getText().equals("") && !txtPassword.getText().equals("")){
                    String[] columnas = {"PUNTARENAS AL ROBLE","PUNTARENAS A ESPARZA","ROBLE A PUNTARENAS","ROBLE A ESPARZA","ESPARZA A PUNTARENAS","ESPARZA AL ROBLE"};
                    String[] dest = {"puntarenasFraiche@gmail.com","puntarenasFraiche@gmail.com","robleFraiche@gmail.com","robleFraiche@gmail.com","esparzaFraiche@gmail.com","esparzaFraiche@gmail.com"};
                    int[] column = {4,8,12,16,20,24};
                    for(int i =0; i<column.length;i++){
                        ArrayList<Traspaso> traspasos = Excel.leerExcel(new File(txtFile.getText()),column[i]);
                        if(traspasos.size()>0){
                            Workbook workbook = Excel.escribirExcel(columnas[i],traspasos);
                            Correo.enviarCorreo(txtCorreo.getText(),txtPassword.getText(),dest[i],workbook,columnas[i]);
                        }
                    }
                    Platform.runLater(
                            () -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Listo!");
                    alert.setHeaderText("Se enviaron los correos");
                    alert.setContentText("Los correos se han enviado correctamente.");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            alert.close();
                        }
                    });});
                }else{
                    //errors
                    Platform.runLater(
                            () -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Ocurrió un error.");
                                alert.setHeaderText("Fallo de datos.");
                                alert.setContentText("Revise que rellenó todas las casillas.");
                                alert.showAndWait().ifPresent(rs -> {
                                    if (rs == ButtonType.OK) {
                                        alert.close();
                                    }
                                });
                            }
                    );

                }
                btoEnviar.setDisable(false);
            });
            t1.start();

        });


    }
}


