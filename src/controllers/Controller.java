package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import models.*;
import models.blockPath.*;

import java.net.URL;
import java.security.Key;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.color;

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
    private MyNode[][] nodes;
    private MyNode[] flatNodes;
    private MyNode startNode;
    private MyNode finishNode;
    private int rightClickCount;
    private String selectedSort;
    private String selectedGridSort;
    private boolean isGridActive;
    private ObservableMap<String, String> observableMap;
    private ObservableMap<String, String> observableGridMap;
    private int cols, rows;



    public void onGridReleased(MouseEvent mouseEvent) {

    }

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


    /**
     * Allows the creation of walls in the grid.
     * @param _node
     */
    private void nodeAction(MyNode _node) {
        _node.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.isSecondaryButtonDown() && !mouseEvent.isPrimaryButtonDown()) {
                    startNode.setStart(false);
                    _node.setStart(true);
                    startNode = _node;
                    _node.setNodeBackgroundColor("green", "black");
                }else if (mouseEvent.isPrimaryButtonDown() && mouseEvent.isSecondaryButtonDown()) {
                    finishNode.setFinish(false);
                    _node.setFinish(true);
                    finishNode = _node;
                } else if (_node.isWall()) {
                    _node.setNodeBackgroundColor("white", "black");
                } else {
                    _node.setNodeBackgroundColor("black", "black");
                }
                _node.setWall(!_node.isWall());
            }
        });
        _node.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                if (_node.isWall()) {
                    _node.setNodeBackgroundColor("white", "black");
                } else {
                    _node.setNodeBackgroundColor("black", "black");
                }
                _node.setWall(!_node.isWall());
            }
        });
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

    private void scrollListener() {
        this.scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                if (null != rects) {
                    if (new_val.intValue() < 50) {
                        MyRectangle.setDuration(500 / (new_val.intValue()+1));
                        Astar.duration = 500 / (new_val.intValue()+1);
                    } else {
                        MyRectangle.setDuration(100 / new_val.intValue());
                        Astar.duration =100 / new_val.intValue();
                    }
                    System.out.println(MyRectangle.getDuration());
                }
            }
        });
        if (null != rects) {
            for (MyRectangle rect : this.rects) {
                rect.setDura(MyRectangle.getDuration());
            }
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
        EventHandler<ActionEvent> gridEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(selectedGridSort);
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
        

        this.observableGridMap = FXCollections.observableHashMap();
        this.observableGridMap.put("Dijkstras", "Dijkstras algorithm has a time complexity of O(V**2) where V is the number of vertices in the graph. If using a binary heap, the time can be reduced to O(E*log(V))");
        this.observableGridMap.put("A*", "A* (a-star) algorithm is similar to dijkstras but it uses a heuristic value to direct it's search towards the end node.");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.scrollBar.setBlockIncrement(10);
        this.cols = 0;
        scrollListener();
        fillDescriptionMap();
        this.comboBox.getItems().addAll(this.observableMap.keySet());
        comboAction();
    }
}


