package com.ruanko.easyloanadmin.utilities;

import java.util.Date;

/**
 * Created by deserts on 17/8/1.
 */

public final class DateUtils {
    public static int differentDays(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }
}
