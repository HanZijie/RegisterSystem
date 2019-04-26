package sample;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.Semaphore;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.xml.transform.Source;

public class MainApp extends Application
{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    static Stage primarystage = null;
    private Pane root_reg = null;
    private Pane root_login = null;
    private Pane root_doc = null;
    private static Scene scene_reg = null;
    private static Scene scene_login = null;
    private static Scene scene_doc = null;
    public double x1;
    public double y1;
    public double x_stage;
    public double y_stage;
    private static PropertyChangeSupport propertyChangeSupport =
            new PropertyChangeSupport(MainApp.class);


    static String pat_doc_num;

    public static void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException, SQLException
    {
        try
        {

            primaryStage.setTitle("门诊系统");
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primarystage = primaryStage;
            root_reg = FXMLLoader.load(getClass().getResource("register.fxml"));
            root_login = FXMLLoader.load(getClass().getResource("login.fxml"));
            root_doc = FXMLLoader.load(getClass().getResource("doctor.fxml"));
            scene_reg = new Scene(root_reg);
            scene_login = new Scene(root_login);
            scene_doc = new Scene(root_doc);
            scene_doc.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
            scene_reg.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
            setLoginUi();
            Class.forName(JDBC_DRIVER);
            primarystage.show();

            //
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        scene_reg.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent m) {

                //计算
                primarystage.setX(x_stage + m.getScreenX() - x1);
                primarystage.setY(y_stage + m.getScreenY() - y1);



            }
        });
        scene_reg.setOnDragEntered(null);
        scene_reg.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override public void handle(MouseEvent m) {

                //按下鼠标后，记录当前鼠标的坐标
                x1 = m.getScreenX();
                y1 = m.getScreenY();
                x_stage = primarystage.getX();
                y_stage = primarystage.getY();
            }
        });
        scene_login.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent m) {



                //计算
                primarystage.setX(x_stage + m.getScreenX() - x1);
                primarystage.setY(y_stage + m.getScreenY() - y1);



            }
        });
        scene_login.setOnDragEntered(null);
        scene_login.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override public void handle(MouseEvent m) {

                //按下鼠标后，记录当前鼠标的坐标
                x1 = m.getScreenX();
                y1 = m.getScreenY();
                x_stage = primarystage.getX();
                y_stage = primarystage.getY();
            }
        });
        scene_doc.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent m) {



                //计算
                primarystage.setX(x_stage + m.getScreenX() - x1);
                primarystage.setY(y_stage + m.getScreenY() - y1);



            }
        });
        scene_doc.setOnDragEntered(null);
        scene_doc.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override public void handle(MouseEvent m) {

                //按下鼠标后，记录当前鼠标的坐标
                x1 = m.getScreenX();
                y1 = m.getScreenY();
                x_stage = primarystage.getX();
                y_stage = primarystage.getY();
            }
        });
    }
    public void setPrimaryStage(Stage stage)
    {
        primarystage = stage;
    }

    public static void setLoginUi()
    {
        primarystage.setScene(scene_login);
    }
    public static void setRegUi(String name)
    {
        propertyChangeSupport.firePropertyChange("name"," ",name);
        System.out.println("send");
        primarystage.setScene(scene_reg);
    }
    public static void setDocUi(String name)
    {
        propertyChangeSupport.firePropertyChange("name"," ",name);
        primarystage.setScene(scene_doc);
        primarystage.sizeToScene();
    }
    public static void main(String[] args) {
        launch(args);
    }
    public static Stage getPrimaryStage() {
        return primarystage;
    }
}
