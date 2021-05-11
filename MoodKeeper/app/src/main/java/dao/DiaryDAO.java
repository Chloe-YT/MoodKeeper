package dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pojo.Diary;

public interface DiaryDAO {
    void insertDiary(Diary diary);

    void insertDiaryById(Diary diary);

    List<Diary> listDiary();

    List<Diary> listDiaryByDate();

    List<Date> markDiaryDate();

    void updateDiary(Diary diary);

    void deleteDiary(int id);

    void deleteAll();

    Diary getDiaryById(int id);

    List<Diary> listDiaryForDate(Date date);

    List<Integer> monthlyMoodCount(Date begin, Date end);

    List<Integer> moodChange(Date begin, Date end);

    List<Diary> getSuncDiary();

    void setStateAndAnchor(int id, int state, Date anchor);

    Date getMaxAnchor();

    void setState(int id, int state);
}
