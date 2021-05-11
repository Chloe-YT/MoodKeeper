package pojo;

import androidx.annotation.NonNull;

import java.util.Date;

public class Diary {
    private int diary_id;
    private int user_id;
    private int mood_id;
    private int weather_id;
    private int category_id;
    private String diary_name;
    private String diary_content;
    private Date diary_date;
    private int state;
    private Date anchor;

    public Diary(int diary_id, int user_id, int mood_id, int weather_id, int category_id, String diary_name, String diary_content, Date diary_date, int state, Date anchor) {
        this.diary_id = diary_id;
        this.user_id = user_id;
        this.mood_id = mood_id;
        this.weather_id = weather_id;
        this.category_id = category_id;
        this.diary_name = diary_name;
        this.diary_content = diary_content;
        this.diary_date = diary_date;
        this.state = state;
        this.anchor = anchor;
    }

    public int getDiary_id() {
        return diary_id;
    }

    public void setDiary_id() {
        this.diary_id = diary_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id() {
        this.user_id = user_id;
    }

    public int getMood_id() {
        return mood_id;
    }

    public void setMood_id() {
        this.mood_id = mood_id;
    }

    public int getWeather_id() {
        return weather_id;
    }

    public void setWeather_id() {
        this.weather_id = weather_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id() {
        this.category_id = category_id;
    }

    public String getDiary_name() {
        return diary_name;
    }

    public void setDiary_name() {
        this.diary_name = diary_name;
    }

    public String getDiary_content() {
        return diary_content;
    }

    public void setDiary_content() {
        this.diary_content = diary_content;
    }

    public Date getDiary_date() {
        return diary_date;
    }

    public void setDiary_date() {
        this.diary_date = diary_date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getAnchor() {
        return anchor;
    }

    public void setAnchor(Date anchor) {
        this.anchor = anchor;
    }

    @NonNull
    @Override
    public String toString() {
        return "Diary{" +
                "diary_id=" + diary_id +
                ", user_id=" + user_id +
                ", mood_id=" + mood_id +
                ", weather_id=" + weather_id +
                ", category_id=" + category_id +
                ", diary_name='" + diary_name + '\''+
                ", diary_content='" + diary_content +'\''+
                ", diary_date=" + diary_date +
                ", state=" + state +
                ", anchor=" + anchor +
                '}';
    }
}
