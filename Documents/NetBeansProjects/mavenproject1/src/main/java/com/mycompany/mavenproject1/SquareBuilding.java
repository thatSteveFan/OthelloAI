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
 * The height is the height it would be if it was 90 degrees. The actual height
 * is 90 * cos(angle)
 *
 * @author pramukh
 */
public class SquareBuilding extends Region
{

    public static final double DEFAULT_SIZE = 100;
    public static final double DEFAULT_ANGLE = 70;

    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();
    private final DoubleProperty size = new SimpleDoubleProperty();
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

    public SquareBuilding(Image image)
    {
        this(image, DEFAULT_SIZE);

    }

    public SquareBuilding(Image image, double size)
    {
        this(image, size, DEFAULT_ANGLE);
    }

    public SquareBuilding(Image image, double size, double angle)
    {
        this(image, size, angle, 0, 0);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public SquareBuilding(Image image, double size, double angle, double x, double y)
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
        leftSideShear.xProperty().bind(cos.divide(sin));
        leftSideShear.pivotXProperty().bind(leftGroup.translateXProperty());
        Scale leftSideScale = new Scale(1, Double.NaN);
        leftSideScale.yProperty().bind(sin);
        leftSide.getTransforms().addAll(leftSideShear, leftSideScale);
        Rotate leftSideRotate = new Rotate(90, Rotate.Y_AXIS);
        leftGroup.getTransforms().addAll(leftSideRotate, new Rotate(90, Rotate.Z_AXIS));
        leftGroup.translateYProperty().bind(size.multiply(cos).negate());
        leftGroup.translateZProperty().bind(size.negate().multiply(sin));
        return leftGroup;
    }

    public Node makeFront()
    {

        ImageView front = new ImageView();
        front.imageProperty().bind(image);
        front.fitHeightProperty().bind(this.size.multiply(MathBindings.pow(cos, -1)));
        front.fitWidthProperty().bind(this.size);
        Rotate frontRotate = new Rotate(0, Rotate.X_AXIS);
        front.getTransforms().add(frontRotate);
        front.translateZProperty().bind(front.fitHeightProperty().negate().multiply(sin));
        front.translateYProperty().bind(front.fitHeightProperty().negate().multiply(cos).add(front.fitHeightProperty()));
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

        rightSideShear.xProperty().bind(cos.divide(sin).negate());
        rightSideShear.pivotXProperty().bind(rightGroup.translateXProperty());
        Scale rightSideScale = new Scale(1, Double.NaN);
        rightSideScale.yProperty().bind(sin);
        rightSide.getTransforms().addAll(rightSideShear, rightSideScale);
        Rotate leftSideRotate = new Rotate(-90, Rotate.Y_AXIS);
        rightGroup.getTransforms().addAll(leftSideRotate, new Rotate(-90, Rotate.Z_AXIS));
        rightGroup.translateYProperty().bind(size.multiply(cos.negate().add(1)));
        rightGroup.translateXProperty().bind((size));
        rightGroup.translateZProperty().bind(size.negate().multiply(sin));

        return rightGroup;
    }

    public Node makeTop()
    {
        ImageView top = new ImageView();
        top.imageProperty().bind(image);
        top.fitHeightProperty().bind(this.size);
        top.fitWidthProperty().bind(this.size);
        top.translateYProperty().bind(size.multiply(cos).negate());
        top.translateZProperty().bind(size.multiply(sin).negate());

        Group topGroup = new Group(top);

        return topGroup;
    }
}
