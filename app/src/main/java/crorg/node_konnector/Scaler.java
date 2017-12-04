package crorg.node_konnector;

import java.io.Serializable;

/**
 * Created by d on 11/29/17.
 */

public class Scaler implements Serializable {
    public static int width;
    public static int height;
    public Scaler(int width, int height){
        this.width = width;
        this.height = height;
    }
}
