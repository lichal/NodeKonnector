package crorg.node_konnector.Shapes;

import android.graphics.Path;
import android.graphics.drawable.shapes.PathShape;

import java.io.Serializable;

/**
 * Created by d on 12/7/17.
 */

public class MyPathShape extends PathShape implements Serializable{
    /**
     * PathShape constructor.
     *
     * @param path      a Path that defines the geometric paths for this shape
     * @param stdWidth  the standard width for the shape. Any changes to the
     *                  width with resize() will result in a width scaled based
     *                  on the new width divided by this width.
     * @param stdHeight the standard height for the shape. Any changes to the
     *                  height with resize() will result in a height scaled based
     */
    public MyPathShape(Path path, float stdWidth, float stdHeight) {
        super(path, stdWidth, stdHeight);
    }
}
