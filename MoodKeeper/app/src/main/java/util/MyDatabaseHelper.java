package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_DIARY= "create table diary("
            +"diary_id integer primary key autoincrement,"
            +"user_id integer,"
            +"mood_id integer,"
            +"weather_id integer,"
            +"category_id integer,"
            +"diary_name text,"
            +"diary_content text,"
            +"diary_date integer,"
            +"state integer,"
            +"anchor integer)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DIARY);
        Toast.makeText(mContext, "创建成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists diary");
        onCreate(db);
    }
}
