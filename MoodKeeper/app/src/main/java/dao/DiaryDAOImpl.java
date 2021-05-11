package dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import pojo.Diary;

import static com.e.moodkeeper.activity.MainActivity.dbHelper;

public class DiaryDAOImpl implements DiaryDAO {
    @Override
    public void insertDiary(Diary diary) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", diary.getUser_id());
        values.put("mood_id", diary.getMood_id());
        values.put("weather_id", diary.getWeather_id());
        values.put("category_id", diary.getCategory_id());
        values.put("diary_name", diary.getDiary_name());
        values.put("diary_content", diary.getDiary_content());
        values.put("diary_date", diary.getDiary_date().getTime());
        values.put("state", diary.getState());
        values.put("anchor", diary.getAnchor().getTime());

        db.insert("diary", null, values);
    }

    @Override
    public void insertDiaryById(Diary diary) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("diary_id", diary.getDiary_id());
        values.put("user_id", diary.getUser_id());
        values.put("mood_id", diary.getMood_id());
        values.put("weather_id", diary.getWeather_id());
        values.put("category_id", diary.getCategory_id());
        values.put("diary_name", diary.getDiary_name());
        values.put("diary_content", diary.getDiary_content());
        values.put("diary_date", diary.getDiary_date().getTime());
        values.put("state", diary.getState());
        values.put("anchor", diary.getAnchor().getTime());

        db.insert("diary", null, values);
    }

    @Override
    public List<Diary> listDiary() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Diary diary = null;
        List<Diary> list = new ArrayList<>();

        Cursor cursor = db.query("diary", null, "state != ?", new String[]{"-1"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int diary_id = cursor.getInt(cursor.getColumnIndex("diary_id"));
                int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
                int mood_id = cursor.getInt(cursor.getColumnIndex("mood_id"));
                int weather_id = cursor.getInt(cursor.getColumnIndex("weather_id"));
                int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
                String diary_name = cursor.getString(cursor.getColumnIndex("diary_name"));
                String diary_content = cursor.getString(cursor.getColumnIndex("diary_content"));
                Date diary_date = new Date(cursor.getLong(cursor.getColumnIndex("diary_date")));
                int state = cursor.getInt(cursor.getColumnIndex("state"));
                Date anchor = new Date(cursor.getLong(cursor.getColumnIndex("anchor")));

                diary = new Diary(diary_id, user_id, mood_id, weather_id, category_id, diary_name, diary_content, diary_date, state, anchor);
                list.add(diary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public List<Diary> listDiaryByDate() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Diary diary = null;
        List<Diary> list = new ArrayList<>();

        Cursor cursor = db.query("diary", null, "state != ?", new String[]{"-1"}, null, null, "diary_date desc");
        if (cursor.moveToFirst()) {
            do {
                int diary_id = cursor.getInt(cursor.getColumnIndex("diary_id"));
                int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
                int mood_id = cursor.getInt(cursor.getColumnIndex("mood_id"));
                int weather_id = cursor.getInt(cursor.getColumnIndex("weather_id"));
                int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
                String diary_name = cursor.getString(cursor.getColumnIndex("diary_name"));
                String diary_content = cursor.getString(cursor.getColumnIndex("diary_content"));
                Date diary_date = new Date(cursor.getLong(cursor.getColumnIndex("diary_date")));
                int state = cursor.getInt(cursor.getColumnIndex("state"));
                Date anchor = new Date(cursor.getLong(cursor.getColumnIndex("anchor")));

                diary = new Diary(diary_id, user_id, mood_id, weather_id, category_id, diary_name, diary_content, diary_date, state, anchor);
                list.add(diary);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public List<Date> markDiaryDate() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<Date> list = new ArrayList<>();

        Cursor cursor = db.query("diary", null, "state != ?", new String[]{"-1"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Date diary_date = new Date(cursor.getLong(cursor.getColumnIndex("diary_date")));
                list.add(diary_date);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public void updateDiary(Diary diary) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("diary_id", diary.getDiary_id());
        values.put("user_id", diary.getUser_id());
        values.put("mood_id", diary.getMood_id());
        values.put("weather_id", diary.getWeather_id());
        values.put("category_id", diary.getCategory_id());
        values.put("diary_name", diary.getDiary_name());
        values.put("diary_content", diary.getDiary_content());
        values.put("diary_date", diary.getDiary_date().getTime());
        values.put("state", diary.getState());
        values.put("anchor", diary.getAnchor().getTime());

        db.update("diary", values, "diary_id = ?", new String[]{""+diary.getDiary_id()});
    }

    @Override
    public Diary getDiaryById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Diary diary = null;

        Cursor cursor = db.query("diary", null, "diary_id = ?", new String[]{""+id}, null, null, null);
        if (cursor.moveToFirst()) {
            int diary_id = cursor.getInt(cursor.getColumnIndex("diary_id"));
            int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
            int mood_id = cursor.getInt(cursor.getColumnIndex("mood_id"));
            int weather_id = cursor.getInt(cursor.getColumnIndex("weather_id"));
            int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
            String diary_name = cursor.getString(cursor.getColumnIndex("diary_name"));
            String diary_content = cursor.getString(cursor.getColumnIndex("diary_content"));
            Date diary_date = new Date(cursor.getLong(cursor.getColumnIndex("diary_date")));
            int state = cursor.getInt(cursor.getColumnIndex("state"));
            Date anchor = new Date(cursor.getLong(cursor.getColumnIndex("anchor")));

            diary = new Diary(diary_id, user_id, mood_id, weather_id, category_id, diary_name, diary_content, diary_date, state, anchor);
        }
        cursor.close();
        return diary;
    }

    @Override
    public void deleteDiary(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("diary", "diary_id = ?", new String[]{""+id});
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.execSQL("delete from diary");
    }

    @Override
    public List<Diary> listDiaryForDate(Date date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Diary diary = null;
        List<Diary> list = new ArrayList<>();
        Cursor cursor = db.query("diary", null, "diary_date = ? and state != ?", new String[]{""+date.getTime(),"-1"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int diary_id = cursor.getInt(cursor.getColumnIndex("diary_id"));
                int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
                int mood_id = cursor.getInt(cursor.getColumnIndex("mood_id"));
                int weather_id = cursor.getInt(cursor.getColumnIndex("weather_id"));
                int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
                String diary_name = cursor.getString(cursor.getColumnIndex("diary_name"));
                String diary_content = cursor.getString(cursor.getColumnIndex("diary_content"));
                Date diary_date = new Date(cursor.getLong(cursor.getColumnIndex("diary_date")));
                int state = cursor.getInt(cursor.getColumnIndex("state"));
                Date anchor = new Date(cursor.getLong(cursor.getColumnIndex("anchor")));

                diary = new Diary(diary_id, user_id, mood_id, weather_id, category_id, diary_name, diary_content, diary_date, state, anchor);
                list.add(diary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public List<Integer> monthlyMoodCount(Date begin, Date end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Date dayAfterEnd = calendar.getTime();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int m1 = 0, m2 = 0, m3 = 0, m4 = 0, m5 = 0, m6 = 0, m7 = 0, m8 = 0, m9 = 0;
        List<Integer> list = new ArrayList<>();
        Cursor cursor = db.query("diary", null, "diary_date >= ? and diary_date< ? and state != ?", new String[]{""+begin.getTime(),""+dayAfterEnd.getTime(),"-1"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long diary_date = cursor.getLong(cursor.getColumnIndex("diary_date"));
                int mood_id = cursor.getInt(cursor.getColumnIndex("mood_id"));

                switch (mood_id) {
                    case 1:
                        m1 += 1;
                        break;
                    case 2:
                        m2 += 1;
                        break;
                    case 3:
                        m3 += 1;
                        break;
                    case 4:
                        m4 += 1;
                        break;
                    case 5:
                        m5 += 1;
                        break;
                    case 6:
                        m6 += 1;
                        break;
                    case 7:
                        m7 += 1;
                        break;
                    case 8:
                        m8 += 1;
                        break;
                    case 9:
                        m9 += 1;
                        break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.add(m4);
        list.add(m5);
        list.add(m6);
        list.add(m7);
        list.add(m8);
        list.add(m9);

        return list;
    }

    @Override
    public List<Integer> moodChange(Date begin, Date end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Date dayAfterEnd = calendar.getTime();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        List<Integer> list = new ArrayList<>();
        List<Integer> list = new ArrayList<>(Collections.nCopies(32, 0));
        Cursor cursor = db.query("diary", null, "diary_date >= ? and diary_date< ? and state != ?", new String[]{""+begin.getTime(),""+dayAfterEnd.getTime(),"-1"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
//                long diary_date = cursor.getLong(cursor.getColumnIndex("diary_date"));
                Date diary_date = new Date(cursor.getLong(cursor.getColumnIndex("diary_date")));
                int mood_id = cursor.getInt(cursor.getColumnIndex("mood_id"));

//                Date date = new Date(diary_date);

                list.set(diary_date.getDate(), mood_id);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public List<Diary> getSuncDiary() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Diary diary=null;
        List<Diary> list=new ArrayList<>();

        Cursor cursor = db.query("diary",null,"state != ?",new String[]{"9"},null,null,null);
        if (cursor.moveToFirst())
        {
            do {
                int diary_id = cursor.getInt(cursor.getColumnIndex("diary_id"));
                int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
                int mood_id = cursor.getInt(cursor.getColumnIndex("mood_id"));
                int weather_id = cursor.getInt(cursor.getColumnIndex("weather_id"));
                int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
                String diary_name = cursor.getString(cursor.getColumnIndex("diary_name"));
                String diary_content = cursor.getString(cursor.getColumnIndex("diary_content"));
                Date diary_date = new Date(cursor.getLong(cursor.getColumnIndex("diary_date")));
                int state = cursor.getInt(cursor.getColumnIndex("state"));
                Date anchor = new Date(cursor.getLong(cursor.getColumnIndex("anchor")));

                diary = new Diary(diary_id, user_id, mood_id, weather_id, category_id, diary_name, diary_content, diary_date, state, anchor);
                list.add(diary);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public void setStateAndAnchor(int id, int state, Date anchor) {
        Diary diary=getDiaryById(id);
        if (diary!=null){
            diary.setState(state);
            diary.setAnchor(anchor);
            updateDiary(diary);
        }
    }

    @Override
    public Date getMaxAnchor() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        long anchor=0;
        Cursor cursor = db.query("diary",null,null,null,null,null,"anchor desc");

        if (cursor.moveToFirst())
        {
            anchor=cursor.getLong(cursor.getColumnIndex("anchor"));
        }
        cursor.close();

        return new Date(anchor);
    }

    @Override
    public void setState(int id, int state) {
        Diary diary = getDiaryById(id);
        if (diary != null) {
            diary.setState(state);
            updateDiary(diary);
        }
    }

}
