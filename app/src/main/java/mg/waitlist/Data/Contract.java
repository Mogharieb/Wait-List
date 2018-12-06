package mg.waitlist.Data;

import android.provider.BaseColumns;

public class Contract
{
    public static final class waitlistEntry implements BaseColumns
        //Basecolums for generate id and auto increament id
    {
        public static final String TABLE_NAME = "waitlist";
        public static final String COLUMN_GUEST_NAME = "guestName";
        public static final String COLUMN_PARTY_SIZE = "partySize";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
