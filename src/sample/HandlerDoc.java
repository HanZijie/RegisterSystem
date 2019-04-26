package sample;
import com.jfoenix.controls.JFXButton;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class HandlerDoc implements Initializable
{
    static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_data?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    static final String USER = "root";
    static final String PASS = "password";
    static Connection conn = null;
    static Statement stmt = null,stmt2=null;
    LocalDate datetmp;
    LocalDate datetmp2;

    ObservableList<PatientForList> pat_list = FXCollections.observableArrayList();
    ObservableList<Income> income_list = FXCollections.observableArrayList();

    @FXML
    TableView<PatientForList> table_reg;
    @FXML
    TableView<Income> table_income;
    @FXML
    private JFXButton btn_logout,btn_exit;
    @FXML
    private TableColumn<?, ?> col_regnum,col_patname,col_regtime,col_regtype
            ,col_officename,col_docnum,col_docname,col_regtype2,col_regcount,col_income,col_valid;
    @FXML
    Label greatingLabel;
    @FXML
    DatePicker date_end,date_begin;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        date_begin.setValue(LocalDate.now());
        date_end.setValue(LocalDate.now());
        date_begin.valueProperty().addListener((o, oldVal, newVal) -> {
            datetmp = date_begin.getValue();
            on_mouse_entered();
        });
        date_end.valueProperty().addListener((o, oldVal, newVal) -> {
            datetmp2 = date_end.getValue();
            on_mouse_entered();
        });
        col_regnum.setCellValueFactory(new PropertyValueFactory<>("regNum"));
        col_patname.setCellValueFactory(new PropertyValueFactory<>("patName"));
        col_regtime.setCellValueFactory(new PropertyValueFactory<>("regTime"));
        col_regtype.setCellValueFactory(new PropertyValueFactory<>("regType"));
        col_valid.setCellValueFactory(new PropertyValueFactory<>("valid"));

        col_officename.setCellValueFactory(new PropertyValueFactory<>("officeName"));
        col_docnum.setCellValueFactory(new PropertyValueFactory<>("docNum"));
        col_docname.setCellValueFactory(new PropertyValueFactory<>("docName"));
        col_regtype2.setCellValueFactory(new PropertyValueFactory<>("regType"));
        col_regcount.setCellValueFactory(new PropertyValueFactory<>("regCount"));
        col_income.setCellValueFactory(new PropertyValueFactory<>("totalIncome"));

        MainApp.addPropertyChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                on_mouse_entered();
                greatingLabel.setText((String)evt.getNewValue()+"医生，您好！");
            }
        });
    }

    private void on_mouse_entered()
    {
        try
        {
            String time_begin,time_end;
            if(datetmp==null||datetmp2==null)
            {
                System.out.println("Null");
                time_begin = LocalDate.now().toString();
                time_end = LocalDate.now().toString();
            }
            else
            {
                time_begin = datetmp.toString();
                time_end = datetmp2.toString();
            }
            time_begin += " 00:00:00";
            time_end += " 23:59:59";
            pat_list.clear();
            income_list.clear();
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();
            String doc_num=MainApp.pat_doc_num;
            String sql = "select register_num,registration_num,pat_num,"
                    + "reg_datetime,unreg from register_info where "
                    + "doc_num= '"+doc_num+"'";
            ResultSet rs = stmt.executeQuery(sql);
            String register_num=null,registration_num=null,pat_num=null;
            String reg_datetime=null,pat_name=null,expertstr=null;
            String office_name = null,doc_name=null,unreg_str=null;
            boolean expert=true,unreg=false;
            ResultSet rs2 =null;
            int regcount = 0;
            double totalcost = 0;;
            while(rs.next())
            {
                register_num = rs.getString("register_num");
                registration_num = rs.getString("registration_num");
                pat_num = rs.getString("pat_num");
                reg_datetime = rs.getString("reg_datetime");
                unreg = rs.getBoolean("unreg");
                unreg_str = unreg?"否":"是";

                sql = "select expert from registration_info where "
                        + "reg_num= '"+registration_num+"'";
                rs2 = stmt2.executeQuery(sql);
                if(rs2.next())
                {
                    expert = rs2.getBoolean("expert");
                }

                sql = "select pat_name from patient_info where"
                        + " pat_num= '"+pat_num+"'";
                rs2 = stmt2.executeQuery(sql);
                if(rs2.next())
                {
                    pat_name = rs2.getString("pat_name");
                }
                expertstr = expert? "专家号":"普通号";
                pat_list.add(new PatientForList(register_num,pat_name,
                        reg_datetime,expertstr,unreg_str));
            }

            sql = "select office_info.office_name,register_info.doc_num,"
                    + "doctor_info.doc_name,registration_info.expert,"
                    + "count(register_info.doc_num),sum(register_info.reg_cost) "
                    + "from register_info,registration_info,doctor_info,office_info "
                    + "where "
                    + "register_info.registration_num=registration_info.reg_num "
                    + "and doctor_info.doc_num=register_info.doc_num "
                    + "and office_info.office_num=registration_info.office_num "
                    + "and register_info.reg_datetime>='"+time_begin+"' and "
                    + "register_info.reg_datetime<='"+time_end+"'"
                    + "group by register_info.doc_num,registration_info.expert";
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                System.out.println("next->>>>>>>>");
                office_name = rs.getString("office_info.office_name");
                doc_num = rs.getString("register_info.doc_num");
                doc_name = rs.getString("doctor_info.doc_name");

                expert = rs.getBoolean("registration_info.expert");
                regcount = rs.getInt("count(register_info.doc_num)");
                totalcost = rs.getDouble("sum(register_info.reg_cost)");
                expertstr = expert? "专家号":"普通号";
                income_list.add(new Income(office_name, doc_num,doc_name,
                        expertstr,Integer.toString(regcount),Double.toString(totalcost)));
            }
            if(rs!=null)
                rs.close();
            if(rs2!=null)
                rs2.close();
            stmt2.close();
            stmt.close();
            conn.close();
        }
        catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }
        finally
        {
            table_reg.setItems(pat_list);
            System.out.println(income_list);
            table_income.setItems(income_list);
        }
    }

    @FXML
    private void on_btn_exit_clicked(ActionEvent event)
    {
        Event.fireEvent(MainApp.getPrimaryStage(),
                new WindowEvent(MainApp.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST ));
    }

    @FXML
    private void on_btn_logout_clicked(ActionEvent event)
    {
        MainApp.setLoginUi();
    }
}