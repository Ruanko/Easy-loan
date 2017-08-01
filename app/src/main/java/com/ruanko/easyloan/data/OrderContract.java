/**
 * Created by deserts on 17/7/29.
 */
package com.ruanko.easyloan.data;


public class OrderContract {
    public static final class Status {
        public static final int PENDING = 0;
        public static final int PASS = 1;
        public static final int GRANT = 2;
        public static final int PARTIAL_REPAY = 3;
        public static final int REPAY_PENDING = 6;
        public static final int OVERDUE = 7;
        public static final int DONE = 8;
    }
    public static final class OrderEntry {
        public static final String TABLE_NAME = "Order";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_REPAY_METHOD = "repay_method";
        public static final String COLUMN_GRANT_METHOD = "grant_method";
        public static final String COLUMN_BANK_ACCOUNT = "bank_account";
        public static final String COLUMN_REPAID = "repaid";
        public static final String COLUMN_DEADLINE = "deadline";
        public static final String COLUMN_OWNER = "owner";
        public static final String COLUMN_STATUS = "status";
    }
}
