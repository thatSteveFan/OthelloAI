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

    private final ObjectProperty<Image> leftImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> rightImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> topImage = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> frontImage = new SimpleObjectProperty<>();
    private final DoubleProperty depth = new SimpleDoubleProperty();
    private final DoubleProperty angle = new SimpleDoubleProperty();

    
    public Building(Image image)
    {
        this(image, image, image, image);
    }
    
    public Building(Image front,Image left,Image top,Image right)
    {
        this(front, left, top, right, DEFAULT_ANGLE);
    }
    
    public Building(Image front,Image left,Image top,Image right, Double angle)
    {
        this(front, left, top, right, angle, DEFAULT_X, DEFAULT_Y);
    }
    
    public Building(Image front,Image left,Image top,Image right, Double angle, double x, double y)
    {
        frontImage.setValue(front);
        leftImage.setValue(left);
        topImage.setValue(top);
        rightImage.setValue(right);
        this.angle.setValue(angle);
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
        leftSide.fitHeightProperty().bind(this.width);
        leftSide.fitWidthProperty().bind(this.width);
        Group leftGroup = new Group(leftSide);
        Shear leftSideShear = new Shear();
        leftSideShear.yProperty().bind(MathBindings.cos(MathBindings.toRadians(angle)).negate().divide(MathBindings.sin(MathBindings.toRadians(angle))));
        leftSideShear.pivotYProperty().bind(leftGroup.translateYProperty());
        Scale leftSideScale = new Scale(Double.NaN, 1);
        leftSideScale.xProperty().bind(MathBindings.sin(MathBindings.toRadians(angle)));
        leftSide.getTransforms().addAll(leftSideShear, leftSideScale);
        Rotate leftSideRotate = new Rotate(90, Rotate.Y_AXIS);
        leftGroup.getTransforms().add(leftSideRotate);
        leftGroup.translateYProperty().bind(leftSide.fitHeightProperty().negate().add(width));
        return leftGroup;
    }

    public Node makeFront()
    {

        ImageView front = new ImageView();
        front.imageProperty().bind(frontImage);
        front.fitHeightProperty().bind(this.width);
        front.fitWidthProperty().bind(this.width);
        Rotate frontRotate = new Rotate(0, Rotate.X_AXIS);
        front.getTransforms().add(frontRotate);
        front.translateZProperty().bind(front.fitHeightProperty().negate().multiply(MathBindings.sin(MathBindings.toRadians(angle))));
        front.translateYProperty().bind(front.fitHeightProperty().negate().multiply(MathBindings.cos(MathBindings.toRadians(angle))).add(front.fitHeightProperty()));
        frontRotate.angleProperty().bind(angle);
        return front;
    }

    public Node makeRight()
    {
        ImageView rightSide = new ImageView();
        rightSide.imageProperty().bind(rightImage);
        rightSide.fitHeightProperty().bind(this.width);
        rightSide.fitWidthProperty().bind(this.width);

        Group rightGroup = new Group(rightSide);
        Shear rightSideShear = new Shear();

        rightSideShear.yProperty().bind(MathBindings.cos(MathBindings.toRadians(angle)).negate().divide(MathBindings.sin(MathBindings.toRadians(angle))));
        rightSideShear.pivotYProperty().bind(rightGroup.translateYProperty());
        Scale rightSideScale = new Scale(Double.NaN, 1);
        rightSideScale.xProperty().bind(MathBindings.sin(MathBindings.toRadians(angle)));
        rightSide.getTransforms().addAll(rightSideShear, rightSideScale);
        Rotate leftSideRotate = new Rotate(90, Rotate.Y_AXIS);
        rightGroup.getTransforms().add(leftSideRotate);
        rightGroup.translateYProperty().bind(rightSide.fitHeightProperty().negate().add(width));
        rightGroup.translateXProperty().bind((width));

        return rightGroup;
    }

    public Node makeTop()
    {
        ImageView top = new ImageView();
        top.imageProperty().bind(topImage);
        top.fitHeightProperty().bind(this.width);
        top.fitWidthProperty().bind(this.width);
        top.translateYProperty().bind(width.multiply(MathBindings.cos(MathBindings.toRadians(angle))).negate());
        top.translateZProperty().bind(width.multiply(MathBindings.sin(MathBindings.toRadians(angle))).negate());

        Group topGroup = new Group(top);

        return topGroup;
    }
}
