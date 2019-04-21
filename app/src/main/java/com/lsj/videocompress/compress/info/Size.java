package com.lsj.videocompress.compress.info;

/**
 * 功能：
 * 描述：
 * Created by lishoajie on 2017/3/27.
 */

public final class Size {
    private int mWidth;
    private int mHeight;

    /**
     * Create a new immutable Size instance.
     *
     * @param width  The width of the size, in pixels
     * @param height The height of the size, in pixels
     */
    public Size(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public int getLongValue() {
        return Math.max(mWidth, mHeight);
    }

    public int getShortValue() {
        return Math.min(mWidth, mHeight);
    }

    /**
     * Get the width of the size (in pixels).
     *
     * @return width
     */
    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    /**
     * Get the height of the size (in pixels).
     *
     * @return height
     */
    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getPixelsSize() {
        return getWidth() * getHeight();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void swapWidthAndHeight() {
        int temp = mWidth;
        mWidth = mHeight;
        mHeight = temp;
    }

    public void swapByRatio(int targetWidth, int targetHeight) {
        boolean isSourcePortrait = mHeight > mWidth;
        boolean isTargetPortrait = targetHeight > targetWidth;

        if (isSourcePortrait != isTargetPortrait) {
            swapWidthAndHeight();
        }
    }

    /**
     * Check if this size is equal to another size.
     * <p>
     * Two sizes are equal if and only if both their widths and heights are
     * equal.
     * </p>
     * <p>
     * A size object is never equal to any other type of object.
     * </p>
     *
     * @return {@code true} if the objects were equal, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Size) {
            Size other = (Size) obj;
            return mWidth == other.mWidth && mHeight == other.mHeight;
        }
        return false;
    }

    /**
     * Return the size represented as a string with the format {@code "WxH"}
     *
     * @return string representation of the size
     */
    @Override
    public String toString() {
        return mWidth + "x" + mHeight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        // assuming most sizes are <2^16, doing a rotate will give us perfect hashing
        return mHeight ^ ((mWidth << (Integer.SIZE / 2)) | (mWidth >>> (Integer.SIZE / 2)));
    }
}
