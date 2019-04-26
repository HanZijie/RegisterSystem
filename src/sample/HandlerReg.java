package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class HandlerReg implements Initializable
{
    // JDBC �����������ݿ� URL
    static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_data?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    static final String USER = "root";
    static final String PASS = "password";
    static Connection conn = null;
    static Statement stmt = null;
    private boolean flag_of_changedbysel = false;
    private boolean flag_of_changedbyreg = false;
    private boolean flag_of_changedbyunreg = false;
    private double restore = 0;
    private int cost;
    boolean flag_off_cut = false;
    String sel_off_num,sel_doc_num,sel_reg_num;
    String name;


    ObservableList<String> ob_office = FXCollections.observableArrayList();
    ObservableList<String> cut_ob_office = FXCollections.observableArrayList();
    ObservableList<String> ob_doc = FXCollections.observableArrayList();
    ObservableList<String> ob_regname = FXCollections.observableArrayList();
    ObservableList<PatientReg> ob_patreg = FXCollections.observableArrayList();
    ObservableList<PatientReg> ob_unreg = FXCollections.observableArrayList();

    ObservableList<String> deptList = FXCollections.observableArrayList();
    Vector<String> deptListSel = new Vector<>();
    ObservableList<String> doctList = FXCollections.observableArrayList();
    Vector<String> doctListSel = new Vector<>();
    ObservableList<String> typeList = FXCollections.observableArrayList();
    Vector<String> typeListSel = new Vector<>();
    ObservableList<String> nameList = FXCollections.observableArrayList();
    Vector<String> nameListSel = new Vector<>();
    private int deptSel;

    @FXML
    private JFXButton btn_ok,btn_clear,btn_exit;
    @FXML
    private JFXComboBox<String> deptBox,doctBox,typeBox,nameBox;
    @FXML
    private TableView<PatientReg> table_patreg;
    @FXML
    private TableColumn<?, ?>col_regnum,col_regname,col_docnum,col_docname,
            col_regcount,col_regcost,col_unreg,col_regtime;
    @FXML
    private Label lblWorning,feeLabel,accountLabel,greatingLabel,resultLabel;



    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        col_regnum.setCellValueFactory(new PropertyValueFactory<>("regNum"));
        col_regname.setCellValueFactory(new PropertyValueFactory<>("regName"));
        col_docnum.setCellValueFactory(new PropertyValueFactory<>("docNum"));
        col_docname.setCellValueFactory(new PropertyValueFactory<>("docName"));

        col_regcount.setCellValueFactory(new PropertyValueFactory<>("regCount"));
        col_regcost.setCellValueFactory(new PropertyValueFactory<>("regCost"));
        col_unreg.setCellValueFactory(new PropertyValueFactory<>("unReg"));
        col_regtime.setCellValueFactory(new PropertyValueFactory<>("regTime"));
        table_patreg.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        feeLabel.setText("0元");
        accountLabel.setText("0元");


        MainApp.addPropertyChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("recieve");
                name = (String)evt.getNewValue();
                tabunreg_sel_changed();
            }
        });

        try{

            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql = "select office_num,office_name from office_info";
            ResultSet rs = stmt.executeQuery(sql);
            String office_name,office_num;
            while(rs.next())
            {
                office_num = rs.getString("office_num");
                office_name = rs.getString("office_name");
                deptList.add(office_name);
                deptListSel.add(office_num);
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch(Exception se){
            se.printStackTrace();
        }
        deptBox.setItems(deptList);
        deptBox.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->
        {
            tabunreg_sel_changed();
            int sel_index = deptBox.getSelectionModel().getSelectedIndex();
            deptSel = sel_index;
            try {
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                stmt = conn.createStatement();
                String sql = "select doc_name,doc_num from doctor_info where office_num = '" + deptListSel.elementAt(sel_index)+"'";
                ResultSet rs = stmt.executeQuery(sql);
                String doc_name,doc_num;
                doctList.clear();
                doctListSel.clear();
                while(rs.next())
                {
                    doc_num = rs.getString("doc_num");
                    doc_name = rs.getString("doc_name");
                    doctList.add(doc_name);
                    doctListSel.add(doc_num);
                }
                rs.close();
                stmt.close();
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            doctBox.setItems(doctList);
        });
        doctBox.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->
        {
            int sel_index = doctBox.getSelectionModel().getSelectedIndex();
            sel_doc_num = doctListSel.elementAt(sel_index);
            try{
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                stmt = conn.createStatement();
                String sql = "select expert from doctor_info where doc_num = '" + doctListSel.elementAt(sel_index)+"'";
                ResultSet rs = stmt.executeQuery(sql);
                String expert;
                typeList.clear();
                typeListSel.clear();
                if(rs.next()){
                    expert = rs.getString("expert");
                    if(expert.equals("1")){
                        typeList.addAll("普通号","专家号");
                        typeListSel.add("0");
                        typeListSel.add("1");
                    }else{
                        typeList.addAll("普通号");
                        typeListSel.add("0");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        typeBox.setItems(typeList);
        typeBox.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->{
            int sel_index = typeBox.getSelectionModel().getSelectedIndex();
            cost = sel_index + 1;
            feeLabel.setText(String.valueOf(cost)+".0元");
            try{
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                stmt = conn.createStatement();
                String sql = "select reg_name,reg_num from registration_info where office_num = '" + deptListSel.elementAt(deptSel)+"' and expert = '" +
                        typeListSel.elementAt(sel_index)+"'";
                ResultSet rs = stmt.executeQuery(sql);
                nameList.clear();
                nameListSel.clear();
                while(rs.next()) {
                    nameList.add(rs.getString("reg_name"));
                    nameListSel.add(rs.getString("reg_num"));
                }
                sql = "select prestore_amount from patient_info where "
                        + "pat_num = '"+MainApp.pat_doc_num+"'";
                rs = stmt.executeQuery(sql);
                if(rs.next()){
                    restore = rs.getDouble("prestore_amount");
                    accountLabel.setText(Double.toString(restore));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        nameBox.setItems(nameList);
    }

    public void tabunreg_sel_changed() {
        greatingLabel.setText("Hello, "+name+"!");
        boolean flag_unreg = false, expert = false;
        accountLabel.setText(String.valueOf(restore));
        String regnum, regname, docnum, docname, regcount, regcost, unreg, regtime;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;
            ResultSet rs = null;
            sql = "select prestore_amount from patient_info where "
                    + "pat_num = '"+MainApp.pat_doc_num+"'";
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                restore = rs.getDouble("prestore_amount");
                accountLabel.setText(Double.toString(restore));
            }
            sql = "select count(*) as regcount from register_info " +
                    "where pat_num='" + MainApp.pat_doc_num + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int count = rs.getInt("regcount");
                int col_count = ob_patreg.size();
                if (count != col_count || flag_of_changedbyunreg) {
                    ob_patreg.clear();
                    sql = "select reg1.register_num,reg2.reg_name,reg2.expert,reg1.doc_num,"
                            + "doc.doc_name,reg1.pat_count,reg1.unreg,reg1.reg_cost,"
                            + "reg1.reg_datetime from register_info reg1,doctor_info doc,"
                            + "registration_info reg2 where reg1.pat_num='" + MainApp.pat_doc_num + "' "
                            + "and doc.doc_num=reg1.doc_num and reg2.reg_num=reg1.registration_num";
                    rs = stmt.executeQuery(sql);
                    while (rs.next()) {
                        regnum = rs.getString("reg1.register_num");
                        regname = rs.getString("reg2.reg_name");
                        expert = rs.getBoolean("reg2.expert");
                        regname = regname + "（" + (expert ? "专家" : "普通") + "号）";
                        docnum = rs.getString("reg1.doc_num");
                        docname = rs.getString("doc.doc_name");
                        regcount = rs.getString("reg1.pat_count");
                        regcost = rs.getString("reg1.reg_cost");
                        flag_unreg = rs.getBoolean("reg1.unreg");
                        unreg = flag_unreg ? "是" : "否";
                        regtime = rs.getString("reg1.reg_datetime");
                        ob_patreg.add(new PatientReg(regnum, regname, docnum, docname, regcount,
                                regcost, unreg, regtime));
                    }
                    table_patreg.setItems(ob_patreg);
                    flag_of_changedbyunreg = false;
                }
            }
            rs.close();
            if(restore < 0){
                lblWorning.setText("您已欠费，请及时缴费！");
            }
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        }
    }

    @FXML
    private void on_clearsel_clicked(ActionEvent event)
    {
        table_patreg.getSelectionModel().clearSelection();
    }

    @FXML
    private void on_btn_unreg_clicked(ActionEvent event)
    {

        ob_unreg = table_patreg.getSelectionModel().getSelectedItems();
        PatientReg patreg;
        if(ob_unreg == null){
            lblWorning.setText("需要选定一行");
            return;
        }

        for(int i=0;i<ob_unreg.size();++i)
        {
            patreg = ob_unreg.get(i);
            String str_unreg = patreg.getUnReg();
            boolean flag_unreg = str_unreg.equals("是")?true:false;
            if(flag_unreg)
            {
                lblWorning.setText("Worning：无法对已经退号的就诊号进行处理！");
                return;
            }
            flag_of_changedbyunreg = true;
            try
            {
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                stmt = conn.createStatement();
                String sql = "update register_info set unreg=1 where register_num"
                        + "= '"+patreg.getRegNum()+"'";
                stmt.executeUpdate(sql);

                sql = "update register_info set pat_count=pat_count-1 where " +
                        "registration_num in ( select reg3.registration_num from " +
                        "(select registration_num from register_info " +
                        "where register_num='"+patreg.getRegNum()+"') reg3)";
                stmt.executeUpdate(sql);
                stmt.close();
                conn.close();
            }catch(SQLException se){
                // 处理 JDBC 错误
                se.printStackTrace();
            }
        }
        Thread t = new Thread(()->tabunreg_sel_changed());

        t.start();
    }



    @FXML
    private void on_btn_ok_clicked(ActionEvent event)

    {
        int max_pat = 0,regcount=0;
        int registernum=0;
        sel_reg_num = nameListSel.elementAt(nameBox.getSelectionModel().getSelectedIndex());
        if(cost==0||restore==0)
        {
            lblWorning.setText("Worning：请填写完整的挂号信息！");
            return;
        }
        try
        {
            String currtime=null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();

            String sql = "select max_patient from registration_info where "
                    + "reg_num='"+sel_reg_num+"'";

            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                max_pat = rs.getInt("max_patient");
            }

            sql = "select current_date as currtime";
            rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                currtime = rs.getString("currtime");
                currtime += " 00:00:00";
            }

            sql = "select count(*) as regcount from register_info "
                    + "where registration_num ='"+sel_reg_num+"' and "
                    + "reg_datetime>='"+currtime+"' and unreg=0";
            System.out.println("1:"+sql);
            rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                regcount = rs.getInt("regcount");
            }
            if(regcount>=max_pat)
            {
                lblWorning.setText("无法挂号，原因：该号种今日已挂完");
            }
            else
            {
                sql = "select count(*) as regtotal from register_info";
                rs = stmt.executeQuery(sql);
                if(rs.next())
                {
                    registernum = rs.getInt("regtotal");
                    registernum++;
                }

                sql = "insert into register_info (register_num,registration_num,"
                        + "doc_num,pat_num,pat_count,unreg,reg_datetime,reg_cost) "
                        + "values ('"+registernum+"','"+sel_reg_num+"','"+sel_doc_num+"',"
                        + "'"+MainApp.pat_doc_num+"','"+(1+regcount)+"','"+0+"','"+df.format(new Date())+"'"
                        + ",'"+String.valueOf(cost)+"')";
                System.out.println("2:"+sql);
                stmt.executeUpdate(sql);

                double charge;
                charge = restore - cost;
                restore -= cost;
                sql = "update patient_info set prestore_amount = "
                        + ""+charge+"where pat_num= '"+MainApp.pat_doc_num+"'";
                stmt.executeUpdate(sql);

                sql = "update register_info set pat_count= '"+(1+regcount)+"' where "
                        + "registration_num='"+sel_reg_num+"' and reg_datetime>="
                        + "'"+currtime+"'";
                stmt.executeUpdate(sql);

                flag_of_changedbyreg = true;
                resultLabel.setText("挂号成功！");
                lblWorning.setText("");
                Thread t = new Thread(()->tabunreg_sel_changed());
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            se.printStackTrace();
        }
    }
    @FXML
    private void on_btn_exit_clicked(ActionEvent event)
    {
        Event.fireEvent(MainApp.getPrimaryStage(),
                new WindowEvent(MainApp.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST ));
    }
    @FXML
    private void on_btn_clear_clicked(ActionEvent event)
    {

    }

}
