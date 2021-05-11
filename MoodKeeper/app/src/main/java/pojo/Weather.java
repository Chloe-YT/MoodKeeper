package pojo;

public class Weather {
    private int weather_id;
    private String weather_name;
    private int image_id;

    public Weather(int weather_id, String weather_name, int image_id) {
        this.weather_id = weather_id;
        this.weather_name = weather_name;
        this.image_id = image_id;
    }

    public int getWeather_id() {
        return weather_id;
    }

    public void setWeather_id() {
        this.weather_id = weather_id;
    }

    public String getWeather_name() {
        return weather_name;
    }

    public void setWeather_name() {
        this.weather_name = weather_name;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id() {
        this.image_id = image_id;
    }

}
