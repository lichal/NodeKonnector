package crorg.node_konnector;

/**
 * Created by d on 11/29/17.
 */

public class Scaler {
    public static int width;
    public static int height;
    public Scaler(int width, int height){
        this.width = (int) (width *0.1);
        this.height = (int) (height*0.1);
    }
}
