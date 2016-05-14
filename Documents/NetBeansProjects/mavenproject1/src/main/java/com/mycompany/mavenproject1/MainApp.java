package com.mycompany.mavenproject1;

import eu.lestard.advanced_bindings.api.MathBindings;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application
{

    public final DoubleProperty buildingAngle = new SimpleDoubleProperty(70);
    private final DoubleProperty sin = new SimpleDoubleProperty();

    
    {
        sin.bind(MathBindings.sin(MathBindings.toRadians(buildingAngle)));
    }

    private final DoubleProperty cos = new SimpleDoubleProperty();

    
    {
        cos.bind(MathBindings.cos(MathBindings.toRadians(buildingAngle)));
    }

    private final DoubleProperty cameraAngle = new SimpleDoubleProperty(20);

    private final DoubleProperty cameraSin = new SimpleDoubleProperty();

    
    {
        cameraSin.bind(MathBindings.sin(MathBindings.toRadians(cameraAngle)));
    }
    private final DoubleProperty cameraCos = new SimpleDoubleProperty();

    
    {
        cameraCos.bind(MathBindings.cos(MathBindings.toRadians(cameraAngle)));
    }

    private final DoubleProperty notreDameXTranslate = new SimpleDoubleProperty(200);
    private final DoubleProperty notreDameYTranslate = new SimpleDoubleProperty(200);

    private final DoubleProperty size = new SimpleDoubleProperty(200);

    private final DoubleProperty zoom = new SimpleDoubleProperty(500);

    @Override
    public void start(Stage stage) throws Exception
    {
        VBox root = new VBox();

        Group subRoot = new Group();

        VBox sliderArea = sliderArea(subRoot);
        TitledPane sliderWrapper = new FoucsRejectingTitledPane("controls", sliderArea, subRoot);

        root.getChildren().add(sliderWrapper);

        ImageView image = new ImageView("https://www.petfinder.com/wp-content/uploads/2012/11/140272627-grooming-needs-senior-cat-632x475.jpg");

        image.setFitWidth(50);
        image.setFitHeight(50 * 475 / 632.0);
        Rotate imageRotate = new Rotate(0, Rotate.X_AXIS);
        imageRotate.angleProperty().bind(cameraAngle);
        image.getTransforms().add(imageRotate);
        image.translateZProperty().bind(cameraSin.multiply(50 * 475).divide(632.0).negate());
        image.setTranslateX(00);
        image.setTranslateY(00);
        subRoot.getChildren().add(image);

//        setUpConstraints(root);
        Scene scene = new Scene(root, 600, 600);

        Building notreDame = new Building(new Image("http://cloud.graphicleftovers.com/15054/81254/color-square-tiles-pattern.jpg"));
        notreDame.angleProperty().bind(buildingAngle);
        notreDame.sizeProperty().bind(size);

        notreDame.translateXProperty().bind(notreDameXTranslate);
        notreDame.translateYProperty().bind(notreDameYTranslate);

        subRoot.getChildren().add(notreDame);

        Building secondBuilding = new Building(new Image("http://cloud.graphicleftovers.com/15054/81254/color-square-tiles-pattern.jpg"));
        secondBuilding.angleProperty().bind(buildingAngle);
        secondBuilding.sizeProperty().bind(size.multiply(3));
        secondBuilding.translateXProperty().bind(notreDameXTranslate.add(size.multiply(1.5)));
        secondBuilding.translateYProperty().bind(notreDameYTranslate.add(size.multiply(1.5)));
        
        subRoot.getChildren().add(secondBuilding);
        
        
        SubScene subScene = new SubScene(subRoot, 350, 250, true, SceneAntialiasing.DISABLED);
        Pane subPane = new Pane(subScene);

        subScene.widthProperty().bind(subPane.widthProperty());
        subScene.heightProperty().bind(subPane.heightProperty());
        subPane.minWidthProperty().set(200);
        subPane.minHeightProperty().set(200);
        subPane.setPrefHeight(Integer.MAX_VALUE - 1);
        //subPane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

        PerspectiveCamera camera = new PerspectiveCamera();

        camera.setFieldOfView(40);
        camera.translateZProperty().bind(zoom.negate());
        Rotate cameraRotate = new Rotate(0, Rotate.X_AXIS);
        cameraRotate.angleProperty().bind(cameraAngle);
        camera.getTransforms().add(cameraRotate);

        IntegerProperty cameraXShift = new SimpleIntegerProperty(0);
        IntegerProperty cameraYShift = new SimpleIntegerProperty(0);

        camera.translateXProperty().bind(stage.widthProperty().negate().divide(2).add(cameraXShift));
        camera.translateYProperty().bind((cameraYShift));

        subScene.setCamera(camera);

        BooleanProperty upKeyDown = new SimpleBooleanProperty(false);
        BooleanProperty downKeyDown = new SimpleBooleanProperty(false);
        BooleanProperty leftKeyDown = new SimpleBooleanProperty(false);
        BooleanProperty rightKeyDown = new SimpleBooleanProperty(false);

        BooleanProperty wKeyDown = new SimpleBooleanProperty(false);
        BooleanProperty aKeyDown = new SimpleBooleanProperty(false);
        BooleanProperty sKeyDown = new SimpleBooleanProperty(false);
        BooleanProperty dKeyDown = new SimpleBooleanProperty(false);

        IntegerProperty startMouseX = new SimpleIntegerProperty();
        IntegerProperty startMouseY = new SimpleIntegerProperty();

        Property<AxisRotates> currentRotation = new SimpleObjectProperty<>();

        subScene.onMousePressedProperty().set(e
                -> 
                {
                    startMouseX.set((int) e.getSceneX());
                    startMouseY.set((int) e.getSceneY());
                    AxisRotates r = new AxisRotates();
                    camera.getTransforms().addAll(r);
                    currentRotation.setValue(r);
                    //System.out.println("pressed");
        });

        subScene.onMouseDraggedProperty().set(e
                -> 
                {
                    //System.out.println("dragged");
                    if (currentRotation.getValue() != null)
                    {
                        currentRotation.getValue().setX((int) e.getSceneY() - startMouseY.get());
                        currentRotation.getValue().setY(-(int) e.getSceneX() + startMouseX.get());
                    }
                    else
                    {
                    }
        });

        subScene.onMouseReleasedProperty().set(e
                -> 
                {
                    currentRotation.setValue(null);
                    //System.out.println("released");
        });

        subScene.onKeyPressedProperty().set((e)
                -> 
                {
                    //System.out.println("Key Pressed");

                    switch(e.getCode())
                    {
                        case UP:upKeyDown.setValue(true);break;
                        case LEFT: leftKeyDown.setValue(true);break;
                        case DOWN:downKeyDown.setValue(true);break;
                        case RIGHT:rightKeyDown.setValue(true);break;
                        case W:wKeyDown.setValue(true);break;
                        case A:aKeyDown.setValue(true);break;
                        case S:sKeyDown.setValue(true);break;
                        case D:dKeyDown.setValue(true);break;
                    }
                    
                    

                    if (e.getCode() == KeyCode.P)
                    {
                        //notreDameRightX.setAngle(notreDameRightX.getAngle() + 1);
                    }
                    e.consume();
        });

        subScene.onKeyReleasedProperty().set(e
                -> 
                {
                    switch(e.getCode())
                    {
                        case UP:upKeyDown.setValue(false);break;
                        case LEFT: leftKeyDown.setValue(false);break;
                        case DOWN:downKeyDown.setValue(false);break;
                        case RIGHT:rightKeyDown.setValue(false);break;
                        case W:wKeyDown.setValue(false);break;
                        case A:aKeyDown.setValue(false);break;
                        case S:sKeyDown.setValue(false);break;
                        case D:dKeyDown.setValue(false);break;
                    }
                    
        });

        subScene.setOnMouseClicked(e -> subScene.requestFocus());

        Timeline t = new Timeline(new KeyFrame(new Duration(5), "animation", e
                                               -> 
                                               {
                                                   if (upKeyDown.getValue() || wKeyDown.getValue())
                                                   {
                                                       cameraYShift.set(cameraYShift.get() - 1);
                                                       image.setTranslateY(image.getTranslateY() - 1);
                                                   }
                                                   if (downKeyDown.getValue() || sKeyDown.getValue())
                                                   {
                                                       cameraYShift.set(cameraYShift.get() + 1);
                                                       image.setTranslateY(image.getTranslateY() + 1);
                                                   }
                                                   if (leftKeyDown.getValue() || aKeyDown.getValue())
                                                   {
                                                       cameraXShift.set(cameraXShift.get() - 1);
                                                       image.setTranslateX(image.getTranslateX() - 1);
                                                   }
                                                   if (rightKeyDown.getValue() || dKeyDown.getValue())
                                                   {
                                                       cameraXShift.set(cameraXShift.get() + 1);
                                                       image.setTranslateX(image.getTranslateX() + 1);
                                                   }

                                       }));
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();

        GridPane grid = grid(10);
        grid.setGridLinesVisible(true);

        grid.setMinSize(1000, 1000);
        //grid.setBackground(new Background(new BackgroundImage(new Image("http://www.myfreetextures.com/wp-content/uploads/2015/01/grass-free-texture.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        subRoot.getChildren().add(grid);

        //grid.add(s, 0, 0);
        GridPane grid2 = grid(10);
        grid2.setGridLinesVisible(true);

        grid2.setMinSize(1000, 1000);
        //grid.setBackground(new Background(new BackgroundImage(new Image("http://www.myfreetextures.com/wp-content/uploads/2015/01/grass-free-texture.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        grid2.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        //root.getChildren().add(grid2);

        root.getChildren().add(subPane);

        subRoot.requestFocus();
        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    private VBox sliderArea(Node focusser)
    {

        Label xLabel = new Label("X Offset");
        Slider xSlider = new FocusRejectingSlider(0, 1000, notreDameXTranslate.getValue(), focusser);
        xSlider.showTickMarksProperty().set(true);
        xSlider.showTickLabelsProperty().set(true);
        xSlider.setMajorTickUnit(100);
        notreDameXTranslate.bind(xSlider.valueProperty());

        Label yLabel = new Label("Y Offset");
        Slider ySlider = new FocusRejectingSlider(0, 1000, notreDameYTranslate.getValue(), focusser);
        ySlider.showTickMarksProperty().set(true);
        ySlider.showTickLabelsProperty().set(true);
        ySlider.setMajorTickUnit(100);
        notreDameYTranslate.bind(ySlider.valueProperty());

        Label buildingAngleLabel = new Label("Building Angle");
        Slider buildingAngleSlider = new FocusRejectingSlider(0, 90, buildingAngle.getValue(), focusser);
        buildingAngle.bind(buildingAngleSlider.valueProperty());

        Label cameraAngleLabel = new Label("Camera Angle");
        Slider cameraAngleSlider = new FocusRejectingSlider(0, 90, cameraAngle.getValue(), focusser);
        cameraAngle.bind(cameraAngleSlider.valueProperty());

        Label sizeLabel = new Label("Size");
        Slider sizeSlider = new FocusRejectingSlider(Double.MIN_NORMAL, 1000, size.getValue(), focusser);
        size.bind(sizeSlider.valueProperty());

        Label zoomLabel = new Label("Zoom");
        Slider zoomSlider = new FocusRejectingSlider(10, 5000, zoom.getValue(), focusser);
        zoom.bind(zoomSlider.valueProperty());

        return new VBox(xLabel, xSlider, yLabel, ySlider, buildingAngleLabel,
                        buildingAngleSlider, cameraAngleLabel, cameraAngleSlider,
                        sizeLabel, sizeSlider, zoomLabel, zoomSlider);
    }

    private GridPane grid(double size)
    {
        GridPane temp = new GridPane();
        List<ColumnConstraints> cList = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100 / size);
            cList.add(cc);
        }
        temp.getColumnConstraints().addAll(cList);

        List<RowConstraints> rList = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100 / size);
            rList.add(rc);
        }
        temp.getRowConstraints().addAll(rList);

        return temp;
    }
}
