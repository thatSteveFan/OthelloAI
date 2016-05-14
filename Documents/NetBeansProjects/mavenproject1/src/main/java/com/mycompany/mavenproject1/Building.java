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

    public static final double DEFAULT_SIZE = 100;
    public static final double DEFAULT_ANGLE = 70;
    
    
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();
    private final DoubleProperty size = new SimpleDoubleProperty();
    private final DoubleProperty angle = new SimpleDoubleProperty();

    public Building(Image image)
    {
       this(image, DEFAULT_SIZE); 
       
    }
    
    public Building(Image image, double size)
    {
        this(image, size, DEFAULT_ANGLE);
    }
    
    public Building(Image image, double size, double angle)
    {
        this(image, size, angle, 0, 0);
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Building(Image image, double size, double angle, double x, double y)
    {
        super();
        this.image.setValue(image);
        this.size.setValue(size);
        this.angle.setValue(angle);
        super.setTranslateX(x);
        super.setTranslateX(y);
        
        
        
        Group root = makeBuilding();
        getChildren().add(root);
        
        
    }
    
    
    public double getAngle()
    {
        return angle.get();
    }

    public void setAngle(double value)
    {
        angle.set(value);
    }

    public DoubleProperty angleProperty()
    {
        return angle;
    }

    public double getSize()
    {
        return size.get();
    }

    public void setSize(double value)
    {
        size.set(value);
    }

    public DoubleProperty sizeProperty()
    {
        return size;
    }


    public Image getImage()
    {
        return image.get();
    }

    public void setImage(Image value)
    {
        image.set(value);
    }

    public ObjectProperty<Image> imageProperty()
    {
        return image;
    }

    private Group makeBuilding()
    {
        Group root = new Group(makeFront(), makeLeft(), makeTop(), makeRight());
        return root;
    }

    public Node makeLeft()
    {
        
        ImageView leftSide = new ImageView();
        leftSide.imageProperty().bind(this.image);
        leftSide.fitHeightProperty().bind(this.size);
        leftSide.fitWidthProperty().bind(this.size);
        Group leftGroup = new Group(leftSide);
        Shear leftSideShear = new Shear();
        leftSideShear.yProperty().bind(MathBindings.cos(MathBindings.toRadians(angle)).negate().divide(MathBindings.sin(MathBindings.toRadians(angle))));
        leftSideShear.pivotYProperty().bind(leftGroup.translateYProperty());
        Scale leftSideScale = new Scale(Double.NaN, 1);
        leftSideScale.xProperty().bind(MathBindings.sin(MathBindings.toRadians(angle)));
        leftSide.getTransforms().addAll(leftSideShear, leftSideScale);
        Rotate leftSideRotate = new Rotate(90, Rotate.Y_AXIS);
        leftGroup.getTransforms().add(leftSideRotate);
        leftGroup.translateYProperty().bind(leftSide.fitHeightProperty().negate().add(size));
        return leftGroup;
    }
    
    public Node makeFront()
    {
        
        ImageView front = new ImageView();
        front.imageProperty().bind(image);
        front.fitHeightProperty().bind(this.size);
        front.fitWidthProperty().bind(this.size);
        Rotate frontRotate = new Rotate(0, Rotate.X_AXIS);
        front.getTransforms().add(frontRotate);
        front.translateZProperty().bind(front.fitHeightProperty().negate().multiply(MathBindings.sin(MathBindings.toRadians(angle))));
        front.translateYProperty().bind(front.fitHeightProperty().negate().multiply(MathBindings.cos(MathBindings.toRadians(angle))).add(front.fitHeightProperty()));
        frontRotate.angleProperty().bind(angle);
        return front;
    }
    
    public Node makeRight()
    {
        ImageView rightSide = new ImageView(image.getValue());
        rightSide.imageProperty().bind(this.image);
        rightSide.fitHeightProperty().bind(this.size);
        rightSide.fitWidthProperty().bind(this.size);
        
        Group rightGroup = new Group(rightSide);
        Shear rightSideShear = new Shear();
        
        rightSideShear.yProperty().bind(MathBindings.cos(MathBindings.toRadians(angle)).negate().divide(MathBindings.sin(MathBindings.toRadians(angle))));
        rightSideShear.pivotYProperty().bind(rightGroup.translateYProperty());
        Scale rightSideScale = new Scale(Double.NaN, 1);
        rightSideScale.xProperty().bind(MathBindings.sin(MathBindings.toRadians(angle)));
        rightSide.getTransforms().addAll(rightSideShear, rightSideScale);
        Rotate leftSideRotate = new Rotate(90, Rotate.Y_AXIS);
        rightGroup.getTransforms().add(leftSideRotate);
        rightGroup.translateYProperty().bind(rightSide.fitHeightProperty().negate().add(size));
        rightGroup.translateXProperty().bind((size));
        
        return rightGroup;
    }
    
    public Node makeTop()
    {
        ImageView top = new ImageView();
        top.imageProperty().bind(image);
        top.fitHeightProperty().bind(this.size);
        top.fitWidthProperty().bind(this.size);
        top.translateYProperty().bind(size.multiply(MathBindings.cos(MathBindings.toRadians(angle))).negate());
        top.translateZProperty().bind(size.multiply(MathBindings.sin(MathBindings.toRadians(angle))).negate());
        
        
        
        Group topGroup = new Group(top);
        
        return topGroup;
    }
}
