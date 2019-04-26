package sample;

import javafx.beans.property.SimpleStringProperty;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class Income extends RecursiveTreeObject<Income>
{
    public SimpleStringProperty officeName;
    public SimpleStringProperty docNum;
    public SimpleStringProperty docName;
    public SimpleStringProperty regType;
    public SimpleStringProperty regCount;
    public SimpleStringProperty totalIncome;

    public Income(String oName, String dNum, String dName,
                  String rType,String rCount,String tIncome)
    {
        this.officeName = new SimpleStringProperty(oName);
        this.docNum = new SimpleStringProperty(dNum);
        this.docName = new SimpleStringProperty(dName);
        this.regType = new SimpleStringProperty(rType);
        this.regCount = new SimpleStringProperty(rCount);
        this.totalIncome = new SimpleStringProperty(tIncome);
    }

    public String getOfficeName() {
        return officeName.get();
    }
    public void setOfficeName(String fName) {
        officeName.set(fName);
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

    public String getRegType() {
        return regType.get();
    }
    public void setRegType(String fName) {
        regType.set(fName);
    }

    public String getRegCount() {
        return regCount.get();
    }
    public void setRegCount(String fName) {
        regCount.set(fName);
    }

    public String getTotalIncome() {
        return totalIncome.get();
    }
    public void setTotalIncome(String fName) {
        totalIncome.set(fName);
    }

}