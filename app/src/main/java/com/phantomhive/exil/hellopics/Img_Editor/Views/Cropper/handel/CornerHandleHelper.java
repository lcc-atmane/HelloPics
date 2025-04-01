package com.phantomhive.exil.hellopics.Img_Editor.Views.Cropper.handel;

import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.phantomhive.exil.hellopics.Img_Editor.Views.Cropper.edge.Edge;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Cropper.edge.EdgePair;

public class CornerHandleHelper extends HandleHelper {
    /**
     * Constructor.
     *
     * @param horizontalEdge the horizontal edge associated with this handle; may be null
     * @param verticalEdge   the vertical edge associated with this handle; may be null
     */
    CornerHandleHelper(Edge horizontalEdge, Edge verticalEdge) {
        super(horizontalEdge, verticalEdge);
    }
    // Constructor /////////////////////////////////////////////////////////////////////////////////



    // HandleHelper Methods ////////////////////////////////////////////////////////////////////////

    @Override
    void updateCropWindow(float x,
                          float y,
                          float targetAspectRatio,
                          @NonNull RectF imageRect,
                          float snapRadius) {

        final EdgePair activeEdges = getActiveEdges(x, y, targetAspectRatio);
        final Edge primaryEdge = activeEdges.primary;
        final Edge secondaryEdge = activeEdges.secondary;

        primaryEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio);
        secondaryEdge.adjustCoordinate(targetAspectRatio);

        if (secondaryEdge.isOutsideMargin(imageRect, snapRadius)) {
            secondaryEdge.snapToRect(imageRect);
            primaryEdge.adjustCoordinate(targetAspectRatio);
        }
    }
}
