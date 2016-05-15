/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import eu.lestard.advanced_bindings.api.MathBindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;

/**
 *
 * @author pramukh
 */
public class Building extends Region
{

    public static final double DEFAULT_ANGLE = 70;
    public static final double DEFAULT_X = 0;
    public static final double DEFAULT_Y = 0;
    public static final double DEFALUT_HEIGHT = 100;
    public static final double DEFALUT_WIDTH = 100;
    public static final double DEFALUT_DEPTH = 100;

    private final ObjectProperty<Image> leftImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> rightImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> topImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> frontImage = new SimpleObjectProperty<>();
    private final DoubleProperty depth = new SimpleDoubleProperty();
    private final DoubleProperty angle = new SimpleDoubleProperty();
    private final DoubleProperty cos = new SimpleDoubleProperty();
    {
        //more efficient, as less object creations and recalculations
        cos.bind(MathBindings.cos(MathBindings.toRadians(angle)));
    }
    private final DoubleProperty sin = new SimpleDoubleProperty();
    {
        //more efficient, as less object creations and recalculations
        sin.bind(MathBindings.sin(MathBindings.toRadians(angle)));
    }

    public Building(Image image)
    {
        this(image, image, image, image);
    }

    public Building(Image front, Image left, Image top, Image right)
    {
        this(front, left, top, right, DEFAULT_ANGLE);
    }

    public Building(Image front, Image left, Image top, Image right, Double angle)
    {
        this(front, left, top, right, angle, DEFAULT_X, DEFAULT_Y);
    }

    public Building(Image front, Image left, Image top, Image right, Double angle, double x, double y)
    {

        this(front, left, top, right, angle, x, y, DEFALUT_HEIGHT, DEFALUT_WIDTH, DEFALUT_DEPTH);
        

    }

    public Building(Image front, Image left, Image top, Image right, Double angle, double x, double y, double height, double width, double depth)
    {
        frontImage.setValue(front);
        leftImage.setValue(left);
        topImage.setValue(top);
        rightImage.setValue(right);
        this.angle.setValue(angle);
        this.prefHeightProperty().set(height);
        this.prefWidthProperty().set(width);
        this.depth.setValue(depth);
        translateXProperty().set(x);
        translateYProperty().set(y);

        Group root = new Group(makeBuilding());
        getChildren().add(root);

    }

    public Image getLeftImage()
    {
        return leftImage.getValue();
    }

    public void setLeftImage(Image value)
    {
        leftImage.setValue(value);
    }

    public ObjectProperty<Image> leftImageProperty()
    {
        return leftImage;
    }

    public Image getRightImage()
    {
        return rightImage.getValue();
    }

    public void setRightImage(Image value)
    {
        rightImage.setValue(value);
    }

    public ObjectProperty<Image> rightImageProperty()
    {
        return rightImage;
    }

    public Image getTopImage()
    {
        return topImage.getValue();
    }

    public void setTopImage(Image value)
    {
        topImage.setValue(value);
    }

    public ObjectProperty<Image> topImageProperty()
    {
        return topImage;
    }

    public Image getFrontImage()
    {
        return frontImage.getValue();
    }

    public void setFrontImage(Image value)
    {
        frontImage.setValue(value);
    }

    public ObjectProperty<Image> frontImageProperty()
    {
        return frontImage;
    }

    public final void setDepth(Double value)
    {
        depth.set(value);
    }

    public final Double getDepth()
    {
        return depth.get();
    }

    public final DoubleProperty depthProperty()
    {
        return depth;
    }

    public final void setAngle(Double value)
    {
        angle.set(value);
    }

    public final Double getAngle()
    {
        return angle.get();
    }

    public final DoubleProperty angleProperty()
    {
        return angle;
    }

    private Group makeBuilding()
    {
        Group root = new Group(makeFront(), makeLeft(), makeTop(), makeRight());
        return root;
    }

    public Node makeLeft()
    {

        ImageView leftSide = new ImageView();
        leftSide.imageProperty().bind(this.leftImage);
        leftSide.fitHeightProperty().bind(heightProperty());
        leftSide.fitWidthProperty().bind(depthProperty());
        Group leftGroup = new Group(leftSide);
        Shear leftSideShear = new Shear();
        leftSideShear.xProperty().bind(cos.divide(sin));
        //leftSideShear.pivotXProperty().bind(leftGroup.translateXProperty());
        Scale leftSideScale = new Scale(1, Double.NaN);
        leftSideScale.yProperty().bind(sin);
        leftSide.getTransforms().addAll(leftSideShear, leftSideScale);
        Rotate leftSideRotate = new Rotate(90, Rotate.Y_AXIS);
        leftGroup.getTransforms().addAll(leftSideRotate, new Rotate(-90, Rotate.Z_AXIS));
        //leftGroup.translateZProperty().bind(heightProperty().negate().multiply(sin));
        leftGroup.translateYProperty().bind(depth);
        return leftGroup;
    }

    public Node makeFront()
    {

        ImageView front = new ImageView();
        front.imageProperty().bind(frontImage);
        front.fitHeightProperty().bind(this.heightProperty());
        front.fitWidthProperty().bind(this.widthProperty());
        Rotate frontRotate = new Rotate(0, Rotate.X_AXIS);
        front.getTransforms().add(frontRotate);
        front.translateZProperty().bind(front.fitHeightProperty().negate().multiply(sin));
        front.translateYProperty().bind(front.fitHeightProperty().negate().multiply(cos).add(depth));
        frontRotate.angleProperty().bind(angle);
        return front;
    }

    public Node makeRight()
    {
        ImageView rightSide = new ImageView();
        rightSide.imageProperty().bind(rightImage);
        rightSide.fitHeightProperty().bind(heightProperty());
        rightSide.fitWidthProperty().bind(depthProperty());

        Group rightGroup = new Group(rightSide);
        Shear rightSideShear = new Shear();

        rightSideShear.xProperty().bind(cos.divide(sin));
        rightSideShear.pivotXProperty().bind(rightGroup.translateXProperty());
        Scale rightSideScale = new Scale(1, Double.NaN);
        rightSideScale.yProperty().bind(sin);
        rightSide.getTransforms().addAll(rightSideShear, rightSideScale);
        Rotate rightSideRotate = new Rotate(90, Rotate.Y_AXIS);
        rightGroup.getTransforms().addAll(rightSideRotate, new Rotate(-90, Rotate.Z_AXIS));
        rightGroup.translateYProperty().bind(depth);
        rightGroup.translateXProperty().bind((widthProperty()));

        return rightGroup;
    }

    public Node makeTop()
    {
        ImageView top = new ImageView();
        top.imageProperty().bind(topImage);
        top.fitHeightProperty().bind(depth);
        top.fitWidthProperty().bind(widthProperty());
        top.translateYProperty().bind(heightProperty().multiply(cos).negate());
        top.translateZProperty().bind(heightProperty().multiply(sin).negate());

        Group topGroup = new Group(top);

        return topGroup;
    }
}
