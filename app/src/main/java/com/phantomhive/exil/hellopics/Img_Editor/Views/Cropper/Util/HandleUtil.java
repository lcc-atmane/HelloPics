package com.phantomhive.exil.hellopics.Img_Editor.Views.Cropper.Util;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import com.phantomhive.exil.hellopics.Img_Editor.Views.Cropper.handel.Handle;

public class HandleUtil {
    public static Handle getPressedHandle(float x,
                                          float y,
                                          float left,
                                          float top,
                                          float right,
                                          float bottom,
                                          float targetRadius) {

        // Find the closest corner handle to the touch point.
        // If the touch point is in the target zone of this closest handle, then this is the pressed handle.
        // Else, check if any of the edges are in the target zone of the touch point.
        // Else, check if the touch point is within the crop window bounds; if so, then choose the center handle.

        Handle closestHandle = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        final float distanceToTopLeft = MathUtil.calculateDistance(x, y, left, top);
        if (distanceToTopLeft < closestDistance) {
            closestDistance = distanceToTopLeft;
            closestHandle = Handle.TOP_LEFT;
        }

        final float distanceToTopRight = MathUtil.calculateDistance(x, y, right, top);
        if (distanceToTopRight < closestDistance) {
            closestDistance = distanceToTopRight;
            closestHandle = Handle.TOP_RIGHT;
        }

        final float distanceToBottomLeft = MathUtil.calculateDistance(x, y, left, bottom);
        if (distanceToBottomLeft < closestDistance) {
            closestDistance = distanceToBottomLeft;
            closestHandle = Handle.BOTTOM_LEFT;
        }

        final float distanceToBottomRight = MathUtil.calculateDistance(x, y, right, bottom);
        if (distanceToBottomRight < closestDistance) {
            closestDistance = distanceToBottomRight;
            closestHandle = Handle.BOTTOM_RIGHT;
        }

        if (closestDistance <= targetRadius) {
            return closestHandle;
        }

        // If we get to this point, none of the corner handles were in the touch target zone, so then we check the edges.
        if (HandleUtil.isInHorizontalTargetZone(x, y, left, right, top, targetRadius)) {
            return Handle.TOP;
        } else if (HandleUtil.isInHorizontalTargetZone(x, y, left, right, bottom, targetRadius)) {
            return Handle.BOTTOM;
        } else if (HandleUtil.isInVerticalTargetZone(x, y, left, top, bottom, targetRadius)) {
            return Handle.LEFT;
        } else if (HandleUtil.isInVerticalTargetZone(x, y, right, top, bottom, targetRadius)) {
            return Handle.RIGHT;
        }

        // If we get to this point, none of the corners or edges are in the touch target zone.
        // Check to see if the touch point is within the bounds of the crop window. If so, choose the center handle.
        if (isWithinBounds(x, y, left, top, right, bottom)) {
            return Handle.CENTER;
        }

        return null;
    }

    /**
     * Calculates the offset of the touch point from the precise location of the specified handle.
     * <p/>
     * The offset will be returned in the 'touchOffsetOutput' parameter; the x-offset will be the
     * first value and the y-offset will be the second value.
     */
    public static void getOffset(@NonNull Handle handle,
                                 float x,
                                 float y,
                                 float left,
                                 float top,
                                 float right,
                                 float bottom,
                                 @NonNull PointF touchOffsetOutput) {

        float touchOffsetX = 0;
        float touchOffsetY = 0;

        // Calculate the offset from the appropriate handle.
        switch (handle) {

            case TOP_LEFT:
                touchOffsetX = left - x;
                touchOffsetY = top - y;
                break;
            case TOP_RIGHT:
                touchOffsetX = right - x;
                touchOffsetY = top - y;
                break;
            case BOTTOM_LEFT:
                touchOffsetX = left - x;
                touchOffsetY = bottom - y;
                break;
            case BOTTOM_RIGHT:
                touchOffsetX = right - x;
                touchOffsetY = bottom - y;
                break;
            case LEFT:
                touchOffsetX = left - x;
                touchOffsetY = 0;
                break;
            case TOP:
                touchOffsetX = 0;
                touchOffsetY = top - y;
                break;
            case RIGHT:
                touchOffsetX = right - x;
                touchOffsetY = 0;
                break;
            case BOTTOM:
                touchOffsetX = 0;
                touchOffsetY = bottom - y;
                break;
            case CENTER:
                final float centerX = (right + left) / 2;
                final float centerY = (top + bottom) / 2;
                touchOffsetX = centerX - x;
                touchOffsetY = centerY - y;
                break;
        }

        touchOffsetOutput.x = touchOffsetX;
        touchOffsetOutput.y = touchOffsetY;
    }

    // Private Methods /////////////////////////////////////////////////////////////////////////////

    /**
     * Determines if the specified coordinate is in the target touch zone for a horizontal bar
     * handle.
     *
     * @param x            the x-coordinate of the touch point
     * @param y            the y-coordinate of the touch point
     * @param handleXStart the left x-coordinate of the horizontal bar handle
     * @param handleXEnd   the right x-coordinate of the horizontal bar handle
     * @param handleY      the y-coordinate of the horizontal bar handle
     * @param targetRadius the target radius in pixels
     *
     * @return true if the touch point is in the target touch zone; false otherwise
     */
    private static boolean isInHorizontalTargetZone(float x,
                                                    float y,
                                                    float handleXStart,
                                                    float handleXEnd,
                                                    float handleY,
                                                    float targetRadius) {

        return (x > handleXStart && x < handleXEnd && Math.abs(y - handleY) <= targetRadius);
    }

    /**
     * Determines if the specified coordinate is in the target touch zone for a vertical bar
     * handle.
     *
     * @param x            the x-coordinate of the touch point
     * @param y            the y-coordinate of the touch point
     * @param handleX      the x-coordinate of the vertical bar handle
     * @param handleYStart the top y-coordinate of the vertical bar handle
     * @param handleYEnd   the bottom y-coordinate of the vertical bar handle
     * @param targetRadius the target radius in pixels
     *
     * @return true if the touch point is in the target touch zone; false otherwise
     */
    private static boolean isInVerticalTargetZone(float x,
                                                  float y,
                                                  float handleX,
                                                  float handleYStart,
                                                  float handleYEnd,
                                                  float targetRadius) {

        return (Math.abs(x - handleX) <= targetRadius && y > handleYStart && y < handleYEnd);
    }

    private static boolean isWithinBounds(float x, float y, float left, float top, float right, float bottom) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }
}
