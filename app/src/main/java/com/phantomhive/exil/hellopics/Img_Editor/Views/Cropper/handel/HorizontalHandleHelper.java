package com.phantomhive.exil.hellopics.Img_Editor.Views.Cropper.handel;

import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.phantomhive.exil.hellopics.Img_Editor.Views.Cropper.Util.AspectRatioUtil;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Cropper.edge.Edge;

public class HorizontalHandleHelper extends HandleHelper {
    // Member Variables ////////////////////////////////////////////////////////////////////////////

    private Edge mEdge;

    // Constructor /////////////////////////////////////////////////////////////////////////////////

    HorizontalHandleHelper(Edge edge) {
        super(edge, null);
        mEdge = edge;
    }

    // HandleHelper Methods ////////////////////////////////////////////////////////////////////////

    @Override
    void updateCropWindow(float x,
                          float y,
                          float targetAspectRatio,
                          @NonNull RectF imageRect,
                          float snapRadius) {

        // Adjust this Edge accordingly.
        mEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio);

        float left = Edge.LEFT.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();

        // After this Edge is moved, our crop window is now out of proportion.
        final float targetWidth = AspectRatioUtil.calculateWidth(Edge.getHeight(), targetAspectRatio);

        // Adjust the crop window so that it maintains the given aspect ratio by
        // moving the adjacent edges symmetrically in or out.
        final float difference = targetWidth - Edge.getWidth();
        final float halfDifference = difference / 2;
        left -= halfDifference;
        right += halfDifference;

        Edge.LEFT.setCoordinate(left);
        Edge.RIGHT.setCoordinate(right);

        // Check if we have gone out of bounds on the sides, and fix.
        if (Edge.LEFT.isOutsideMargin(imageRect, snapRadius)
                && !mEdge.isNewRectangleOutOfBounds(Edge.LEFT, imageRect, targetAspectRatio)) {

            final float offset = Edge.LEFT.snapToRect(imageRect);
            Edge.RIGHT.offset(-offset);
            mEdge.adjustCoordinate(targetAspectRatio);
        }

        if (Edge.RIGHT.isOutsideMargin(imageRect, snapRadius)
                && !mEdge.isNewRectangleOutOfBounds(Edge.RIGHT, imageRect, targetAspectRatio)) {

            final float offset = Edge.RIGHT.snapToRect(imageRect);
            Edge.LEFT.offset(-offset);
            mEdge.adjustCoordinate(targetAspectRatio);
        }
    }
}
