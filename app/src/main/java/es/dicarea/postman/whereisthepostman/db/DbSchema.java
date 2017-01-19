package es.dicarea.postman.whereisthepostman.db;

public class DbSchema {
    public static final class StatusTable {
        public static final String NAME = "status";

        public static final class Cols {
            public static final String ID = "id";
            public static final String DATE = "date";
            public static final String STATUS = "status";
            public static final String TRACKING_ID = "tracking_id";
        }
    }

    public static final class TrackingTable {
        public static final String NAME = "tracking";

        public static final class Cols {
            public static final String ID = "id";
            public static final String CODE = "code";
            public static final String DESC = "desc";
            public static final String ACTIVE = "active";
            public static final String DELETED = "deleted";
        }
    }

}

