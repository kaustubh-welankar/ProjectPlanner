package MainCode;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import DataProcessing.ProjectGraph;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Controller {

    @FXML TextField activityNameTextField;
    @FXML TextField activityCodeTextField;
    @FXML TextField activityPredecessorsTextField;
    @FXML TextField activityTimeTextField;

    @FXML TableView<DataModel> activityTable;
    @FXML TableColumn<DataModel,String> activityNameColumn;
    @FXML TableColumn<DataModel,String> activityCodeColumn;
    @FXML TableColumn<DataModel,String> activityPredecessorsColumn;
    ObservableList<DataModel> dataList;

    @FXML Button addActivityButton;
    @FXML Button generateChartsButton;

    private File f;
    @FXML
    public void initialize(){
        activityTable.setEditable(true);
        dataList = activityTable.getItems();
        f = null;
    }

    @FXML
    private void addToTable(ActionEvent ae){

        //---Trimming them---
        String aname = activityNameTextField.getText().trim();
        String ano = activityCodeTextField.getText().trim();
        String adeps = activityPredecessorsTextField.getText().trim();
        String time = activityTimeTextField.getText().trim();
        StringBuilder depsSB = new StringBuilder();

        //---Empty String Check---//
        if(aname.isEmpty() || ano.isEmpty() || time.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incomplete information");
            alert.setHeaderText("Dont leave the name, code, time fields empty");
            alert.showAndWait();
            return ;
        }

        //---Checking if action number is an int---
        try{Integer.parseInt(ano);}
        catch (NumberFormatException nfe){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect information");
            alert.setHeaderText("The code must only be a integer(eg 0,2,1000 etc)");
            alert.showAndWait();
            return ;
        }

        //---Checking if deps are ints and sanitizing input----
        try{
            StringTokenizer st = new StringTokenizer(adeps, " ");
            while(st.hasMoreTokens()){
                String s = st.nextToken();
                Integer.parseInt(s);
                depsSB.append(s);
                if(st.hasMoreTokens()){depsSB.append(" ");}
            }
        }catch(NumberFormatException ee){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect information");
            alert.setHeaderText("Give predecessor int codes");
            alert.showAndWait();
            return ;
        }

        //--Check if ano is already present in the string
        for (DataModel d : dataList ) {
            if(d.getActivityCode().equals(ano)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect information");
                alert.setHeaderText("Activity Code has already been used");
                alert.showAndWait();
                return ;
            }
        }


        DataModel t = new DataModel(aname,ano,depsSB.toString(),time);
        dataList.add(t);

        activityNameTextField.clear();
        activityCodeTextField.clear();
        activityPredecessorsTextField.clear();
        activityTimeTextField.clear();
    }

    @FXML
    private void generateGraph(ActionEvent ae){
        try {
            System.out.println(dataList);
            new ProjectGraph(dataList);
        }
        catch (Exception e){System.out.println(e);}

    }

    @FXML
    private void saveAsFile(ActionEvent ae){
        if(dataList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Data");
            alert.setHeaderText("Insert some tasks into the table");
            alert.showAndWait();
            return ;
        }
        FileChooser fc = new FileChooser();
        //f = fc.showOpenDialog(Main.getPrimaryStage());
        fc.setInitialDirectory(new File("/home/kaustubhw/CompSci/ProjectPlannerExamples"));
        f = fc.showSaveDialog(Main.getPrimaryStage());
        System.out.println("File got : " + f.toString());
        try{
            ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(f)
        );
            ArrayList<DataModel> tempDataList = new ArrayList<>();
            for (DataModel d : dataList) {
                tempDataList.add(d);
            }
            oos.writeObject(tempDataList);
        }
        catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }

    }

    @FXML
    private void openFile(ActionEvent ae){
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("/home/kaustubhw/CompSci/ProjectPlannerExamples"));
        f = fc.showOpenDialog(Main.getPrimaryStage());
        System.out.println("File got : " + f.toString());
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(f)
            );
            ArrayList<DataModel> tempDataList = new ArrayList<>();
            tempDataList = (ArrayList<DataModel>)ois.readObject();
            dataList.clear();
            for ( DataModel d: tempDataList ) {
                dataList.add(d);
            }
        }
        catch (IOException ioe){
            System.out.println("IOException");
        }
        catch (ClassNotFoundException cnfe){
            System.out.println("Youre Fucked");
        }
    }

    @FXML
    private void saveFile(ActionEvent ae){
        if(f == null){
            saveAsFile(ae);
        }
        else{
            try{
                ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream(f)
                );
                ArrayList<DataModel> tempDataList = new ArrayList<>();
                for (DataModel d : dataList) {
                    tempDataList.add(d);
                }
                oos.writeObject(tempDataList);
            }
            catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }

    @FXML
    private void newFile(ActionEvent ae){
        f = null;
        dataList.clear();
    }
}
