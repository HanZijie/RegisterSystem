package sample;

import javafx.beans.property.SimpleStringProperty;

public class PatientReg
{
    public SimpleStringProperty regNum;
    public SimpleStringProperty regName;
    public SimpleStringProperty docNum;
    public SimpleStringProperty docName;
    public SimpleStringProperty regCount;
    public SimpleStringProperty regCost;
    public SimpleStringProperty unReg;
    public SimpleStringProperty regTime;

    public PatientReg(String rNum, String rName, String dNum,
                      String dName,String rCount,String rCost,String uReg,String rTime)
    {
        this.regNum = new SimpleStringProperty(rNum);
        this.regName = new SimpleStringProperty(rName);
        this.docNum = new SimpleStringProperty(dNum);
        this.docName = new SimpleStringProperty(dName);
        this.regCount = new SimpleStringProperty(rCount);
        this.regCost = new SimpleStringProperty(rCost);
        this.unReg = new SimpleStringProperty(uReg);
        this.regTime = new SimpleStringProperty(rTime);
    }

    public String getRegNum() {
        return regNum.get();
    }
    public void setRegNum(String fName) {
        regNum.set(fName);
    }

    public String getRegName() {
        return regName.get();
    }
    public void setRegName(String fName) {
        regName.set(fName);
    }

    public String getDocNum() {
        return docNum.get();
    }
    public void setDocNum(String fName) {
        docNum.set(fName);
    }

    public String getDocName() {
        return docName.get();
    }
    public void setDocName(String fName) {
        docName.set(fName);
    }

    public String getRegCount() {
        return regCount.get();
    }
    public void setRegCount(String fName) {
        regCount.set(fName);
    }

    public String getRegCost() {
        return regCost.get();
    }
    public void setRegCost(String fName) {
        regCost.set(fName);
    }

    public String getUnReg() {
        return unReg.get();
    }
    public void setUnReg(String fName) {
        unReg.set(fName);
    }

    public String getRegTime() {
        return regTime.get();
    }
    public void setRegTime(String fName) {
        regTime.set(fName);
    }

}