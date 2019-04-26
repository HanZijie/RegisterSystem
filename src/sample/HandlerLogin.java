package sample;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.*;
import java.net.URL;
import java.sql.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


public class HandlerLogin implements Initializable
{
    static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_data?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    static final String USER = "root";
    static final String PASS = "password";
    static Connection conn = null;
    static Statement stmt = null;
    public JFXButton btn_exit,btn_login;
    public JFXPasswordField text_pass;
    public JFXTextField text_id;
    public JFXRadioButton doctorRadioButton;
    public JFXRadioButton patientRadioButton;
    public String name;

    private int patientFlag = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        patientRadioButton.setSelected(true);
        text_pass.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    System.out.println("Enter pressed");
                    on_btn_login_clicked(new ActionEvent());
                }
            }
        });
    }

    @FXML
    private void on_btn_exit_clicked(ActionEvent event) throws SQLException
    {
        Event.fireEvent(MainApp.getPrimaryStage(),
                new WindowEvent(MainApp.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST ));

    }

    @FXML
    private void on_btn_login_clicked(ActionEvent event)
    {
        System.out.println("login");
        String usrId = text_id.getText();
        String usrPass = text_pass.getText();
        try {
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql = null;
            ResultSet rs = null;
            if(usrId.isEmpty() || usrPass.isEmpty()){
                text_pass.setPromptText("Please Input correct password");
                text_id.setPromptText("Please input correct ID");
                return;
            }
            if(patientFlag == 1){
                sql = "select login_cmd,pat_name from patient_info "
                        + "where pat_num = '"+usrId+"'";
            }else{
                sql = "select login_cmd,doc_name from doctor_info "
                        + "where doc_num = '"+usrId+"'";
            }
            rs = stmt.executeQuery(sql);
            System.out.println(sql);
            if(rs.next()) {
                String login_cmd = rs.getString("login_cmd");
                if(patientFlag == 1){
                    name = rs.getString("pat_name");
                }else {
                    name = rs.getString("doc_name");
                }
                System.out.println(usrPass+":"+login_cmd);
                if (login_cmd.equals(usrPass)) {
                    System.out.println("right");
                    MainApp.pat_doc_num = usrId;
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if (patientFlag == 1) {
                        sql = "update patient_info set last_login = "
                                + "'" + df.format(new Date()) + "' where pat_num= '" + text_id + "'";
                    } else if (patientFlag == 0) {
                        sql = "update doctor_info set last_login = "
                                + "'" + df.format(new Date()) + "' where doc_num= '" + text_id + "'";
                    } else return;
                    stmt.executeUpdate(sql);
                    text_pass.clear();
                    if (patientFlag == 1) {
                        System.out.println("Reg");
                        MainApp.setRegUi(name);
                    } else if (patientFlag == 0) {
                        System.out.println("Doc");
                        MainApp.setDocUi(name);
                    }
                }else{
                    System.out.println("wrong");
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch (Exception e){
            System.out.println("connect failed");
            e.printStackTrace();
            return;
        }
    }

    @FXML
    private void on_patient_pressed(ActionEvent event){
        if(patientRadioButton.isSelected()){
            doctorRadioButton.setSelected(false);
            patientFlag = 1;
        }else{
            doctorRadioButton.setSelected(true);
            patientFlag = 0;
        }
    }

    @FXML
    private void on_doctor_pressed(ActionEvent event){
        if(doctorRadioButton.isSelected()){
            patientRadioButton.setSelected(false);
            patientFlag = 0;
        }else{
            patientRadioButton.setSelected(true);
            patientFlag = 1;
        }
    }
}
