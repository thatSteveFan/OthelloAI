/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.util.ArrayList;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.transform.Rotate;

/**
 *
 * @author pramukh
 */
public class AxisRotates extends ArrayList<Rotate>
{

    private static final long serialVersionUID = 1L;
    final public Property<Rotate> xRotate;
    final public Property<Rotate> yRotate;
    final public Property<Rotate> zRotate;

    public AxisRotates()
    {
        super();
        xRotate = new SimpleObjectProperty<>(new Rotate(0, Rotate.X_AXIS));
        yRotate = new SimpleObjectProperty<>(new Rotate(0, Rotate.Y_AXIS));
        zRotate = new SimpleObjectProperty<>(new Rotate(0, Rotate.Z_AXIS));

        super.add(xRotate.getValue());
        super.add(yRotate.getValue());
        super.add(zRotate.getValue());

    }

    public double getX()
    {
        return xRotate.getValue().getAngle();
    }

    public double getY()
    {
        return yRotate.getValue().getAngle();
    }

    public double getZ()
    {
        return zRotate.getValue().getAngle();
    }

    public void setX(int x)
    {
        xRotate.getValue().setAngle(x);
    }

    public void setY(int y)
    {
        yRotate.getValue().setAngle(y);
    }

    public void setZ(int z)
    {
        zRotate.getValue().setAngle(z);
    }
}
