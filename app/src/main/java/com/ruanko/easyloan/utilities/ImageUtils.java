package com.ruanko.easyloan.utilities;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

/**
 * Created by deserts on 17/7/29.
 */

public final class ImageUtils {

    public static Transformation getRoundedTransformation() {
        return new RoundedTransformationBuilder()
                .oval(true)
                .build();
    }
}
