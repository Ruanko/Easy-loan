package com.ruanko.easyloan.data;

/**
 * Created by deserts on 17/7/29.
 */

public class UserContract {
    public static final int INIT_LEVEL = 60;
    public static final int USER_ROLE = 0;
    public static final int ADMIN_ROLE = 2048;
    public static final class UserEntry {
        public static final String TABLE_NAME = "_User";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_SEX = "sex";
        public static final String COLUMN_ROLE = "role";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_REAL_NAME = "real_name";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_PHONE = "mobilePhoneNumber";
        public static final String COLUMN_ID_CARD = "id_card";
        public static final String COLUMN_SCHOOL = "school";
        public static final String COLUMN_HOME = "home";
        public static final String COLUMN_RELATIVE_NAME = "relative_name";
        public static final String COLUMN_RELATIVE_PHONE = "relative_phone";
        public static final String COLUMN_RELATIVE_RELATION = "relative_relation";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_INFO_LEVEL = "info_level";
        public static final String COLUMN_TRADE_LEVEL = "trade_level";

        public static final String COLUMN_AVATAR = "avatar";

    }
}
