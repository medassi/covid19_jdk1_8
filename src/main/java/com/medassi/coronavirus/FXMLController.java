package com.medassi.coronavirus;

import com.medassi.coronavirus.models.Data;
import com.medassi.coronavirus.models.Model;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.dialog.ProgressDialog;

public class FXMLController implements Initializable {

    @FXML
    private VBox vBoxWorld;
    @FXML
    private VBox vBoxFR;
    @FXML
    private VBox vBoxDept;
    @FXML
    private ListView<String> lvDept;
    @FXML
    private ListView<String> lvReg;

    private ProgressDialog pd;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
    private Model model;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initModel();
    }

    private void toolTipper(XYChart.Series<String, Integer> serie, String text) {
        serie.getData().forEach((d) -> {
            Tooltip.install(d.getNode(), new Tooltip(d.getXValue() + " : " + d.getYValue() + text));
        });
    }

    private void initModel() {
        Task<Model> task = new Task<Model>() {
            @Override
            protected Model call() throws Exception {
                model = new Model();
                pd.close();
                return model;
            }
        };
        pd = new ProgressDialog(task);
        pd.getDialogPane().getStylesheets().clear();
        pd.getDialogPane().getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());
        pd.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/virus.png"))));
        Stage st = (Stage) pd.getDialogPane().getScene().getWindow();
        st.getIcons().add(new Image(getClass().getResourceAsStream("/images/virus.png")));
        pd.setTitle("Initialisation Covid-19");
        pd.setHeaderText("Evolution des cas Covid-19");
        pd.setContentText("Récupération des données en ligne");
        new Thread(task).start();
        pd.showAndWait();
        loadData();
    }

    private void loadData() {
        lvDept.setItems(FXCollections.observableArrayList(model.getDepts()));
        lvDept.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadPane(model.getlWorld(), vBoxWorld, "Monde");
    }

    @FXML
    private void onMouseClickedLvDept(MouseEvent event) {
        ObservableList<String> selectedItems = lvDept.getSelectionModel().getSelectedItems();
        ArrayList<ArrayList<Data>> llData = new ArrayList<>();
        String univers = "";
        for (String s : selectedItems) {
            llData.add(model.getHmByDep().get(s));
            univers += s + ";";
        }
    }

    @FXML
    private void onMouseClickedLvReg(MouseEvent event) {
      
    }

    private void loadPane(ArrayList<Data> lData, Pane pane, String univers) {
        XYChart.Series<String, Integer> seriesCasConfirmes = new XYChart.Series<>();
        seriesCasConfirmes.setName("Conf. " + univers);
      
        Collections.sort(lData, new Comparator<Data>() {
            @Override
            public int compare(Data o1, Data o2) {
                Date d1 = o1.getDate();
                Date d2 = o2.getDate();
                return d1.compareTo(d2);
            }
        });
        System.out.println("");
        ObservableList<String> categories = FXCollections.observableArrayList();
        for (Data d : lData) {
            categories.add(sdf.format(d.getDate()));
            if (d.getCasConfirmes() != -1) {
                seriesCasConfirmes.getData().add(new XYChart.Data<>(sdf.format(d.getDate()), d.getCasConfirmes()));
            }
          
        }
        CategoryAxis xAxis = new CategoryAxis(categories);
        xAxis.setLabel("Cas - " + univers);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Date");
        LineChart linechart = new LineChart(xAxis, yAxis);
        linechart.getData().add(seriesCasConfirmes);
        linechart.getYAxis().setAutoRanging(true);
        linechart.setTitle(univers);
        toolTipper(seriesCasConfirmes, " cas confimés " + univers);
        pane.getChildren().clear();
        pane.getChildren().add(linechart);
    }
}
