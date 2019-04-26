package sample;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class PatientForList extends RecursiveTreeObject<PatientForList>
{
    public SimpleStringProperty regNum;
    public SimpleStringProperty patName;
    public SimpleStringProperty regTime;
    public SimpleStringProperty regType;
    public SimpleStringProperty valid;

    public PatientForList(String rNum, String pName, String rTime,String rType,String val)
    {
        this.regNum = new SimpleStringProperty(rNum);
        this.patName = new SimpleStringProperty(pName);
        this.regTime = new SimpleStringProperty(rTime);
        this.regType = new SimpleStringProperty(rType);
        this.valid = new SimpleStringProperty(val);
    }

    public String getRegNum() {
        return regNum.get();
    }
    public void setRegNum(String fName) {
        regNum.set(fName);
    }

    public String getPatName() {
        return patName.get();
    }
    public void setPatName(String fName) {
        patName.set(fName);
    }

    public String getRegTime() {
        return regTime.get();
    }
    public void setRegTime(String fName) {
        regTime.set(fName);
    }

    public String getRegType() {
        return regType.get();
    }
    public void setRegType(String fName) {
        regType.set(fName);
    }
    public String getValid()
    {
        return valid.get();
    }
    public void setValid(String val)
    {
        valid.set(val);
    }

}