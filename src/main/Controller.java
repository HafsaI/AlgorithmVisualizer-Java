package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import algorithms.*;

// import static javafx.scene.paint.Color.BLACK;
// import static javafx.scene.paint.Color.color;

public class Controller implements Initializable {


    @FXML
    private AnchorPane sortGraph;
    @FXML
    private AnchorPane grid;
    @FXML
    private Slider scrollBar;
    @FXML
    private Label numLabel;
    @FXML
    private TextField numBars;
    @FXML
    private ComboBox comboBox;
    @FXML
    private TextArea textArea;
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab sortTab;

    @FXML
    private VBox sideVBox;

    @FXML
    private BubbleSort bsort;
    private HeapSort hsort;
    private QuickSort qsort;
    private InsertionSort iSort;
    private MyRectangle[] rects;
    private String selectedSort;
    private String selectedGridSort;
    private ObservableMap<String, String> observableMap;


    /**
     * Clear all anchor panes
     * @param event
     */
    public void handleClear(ActionEvent event) {
        if (!this.sortGraph.getChildren().isEmpty()) {
            this.sortGraph.getChildren().clear();
        }
        if (!this.grid.getChildren().isEmpty()) {
            this.grid.getChildren().clear();
        }
    }

    /**
     * Handle sort button for sort graph. Uses sorting algorithms.
     * @param event
     */
    public void handleSort(ActionEvent event) {
        if (null != this.selectedSort) {
            if (null == this.rects) {
                this.rects = new SortRectangles(this.sortGraph).getRectArr();
            }
            switch (this.selectedSort) {
                case "Bubble Sort":
                    this.bsort = new BubbleSort(this.rects, this.sortGraph);
                    this.bsort.sort();
                    break;
                case "Heap Sort":
                    this.hsort = new HeapSort(this.rects, this.sortGraph);
                    this.hsort.sort();
                    break;
                case "Quick Sort":
                    this.qsort = new QuickSort(this.rects, this.sortGraph);
                    break;
                case "Insertion Sort":
                    this.iSort = new InsertionSort(this.rects, this.sortGraph);
                    break;
                default:
                    System.out.println("No sorting algorithm selected");
                    break;
            }
        }
    }

    /**
     * Create bars used in sorting algorithms.
     * @param event
     */
    public void handleMakeBars(ActionEvent event) {
        makeBars();
    }

    private void makeBars() {
        if (!this.sortGraph.getChildren().isEmpty()) {
            this.sortGraph.getChildren().clear();
        }
        if (!this.numBars.getText().isEmpty()) {
            this.rects = new SortRectangles(Integer.parseInt(this.numBars.getText()), this.sortGraph).getRectArr();
            this.sortGraph.getChildren().addAll(this.rects);
        } else {
            this.rects = new SortRectangles(this.sortGraph).getRectArr();
            this.sortGraph.getChildren().addAll(this.rects);
        }
    }

    public void handleComboBox(ActionEvent event) {
        System.out.println("cbox");
    }

    private void comboAction() {
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedSort = comboBox.getValue().toString();
                System.out.println(selectedSort);
            }
        };
        this.comboBox.setOnAction(event);
    }

    private void fillDescriptionMap() {
        this.observableMap = FXCollections.observableHashMap();
        this.observableMap.put("Bubble Sort", "Bubble Sort has an average of O(n**2) time complexity");
        this.observableMap.put("Heap Sort", "Heap Sort has an overall time complexity of O(n*log(n). Heapify is O(log(n)) and build heap is O(n).");
        this.observableMap.put("Quick Sort", "Quick Sort has average time complexity of O(n*log(n)).");
        this.observableMap.put("Insertion Sort", "");
        
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.scrollBar.setBlockIncrement(10);
        // this.cols = 0;
        fillDescriptionMap();
        this.comboBox.getItems().addAll(this.observableMap.keySet());
        comboAction();
    }
}


