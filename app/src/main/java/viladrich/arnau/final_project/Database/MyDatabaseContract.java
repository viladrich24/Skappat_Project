package viladrich.arnau.final_project.Database;

import android.provider.BaseColumns;

public final class MyDatabaseContract {

    private MyDatabaseContract() {}

    public static class Table1 implements BaseColumns {

        public static final String TABLE_NAME = "REGISTRE_USUARIS";

        public static final String COLUMN_USER = "user";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_MAIL = "mail";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_MEMORY = "memory";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_NOTI = "noti";
        public static final String COLUMN_IMAGE_ID = "image";
        public static final String COLUMN_NUMERO = "numero";
    }

}