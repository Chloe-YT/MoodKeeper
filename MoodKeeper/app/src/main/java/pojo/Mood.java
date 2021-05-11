package pojo;

public class Mood {
    private int mood_id;
    private String mood_name;
    private int image_id;

    public Mood(int mood_id, String mood_name, int image_id) {
        this.mood_id = mood_id;
        this.mood_name = mood_name;
        this.image_id = image_id;
    }

    public int getMood_id() {
        return mood_id;
    }

    public void setMood_id() {
        this.mood_id = mood_id;
    }

    public String getMood_name() {
        return mood_name;
    }

    public void setMood_name() {
        this.mood_name = mood_name;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id() {
        this.image_id = image_id;
    }

}
