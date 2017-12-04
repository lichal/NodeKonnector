package crorg.node_konnector.Shapes;

import android.graphics.Point;

import java.io.Serializable;

/**
 * Created by d on 12/3/17.
 */

public class MyPoint extends Point implements Serializable{
    public MyPoint(){
        super();
    }

    public MyPoint(int x, int y){
        super(x, y);
    }
}
