package crorg.node_konnector;

import java.io.Serializable;

/**
 * Created by d on 11/29/17.
 */

public class Scaler implements Serializable {
    public int width;
    public int height;
    public Scaler(int width, int height){
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
