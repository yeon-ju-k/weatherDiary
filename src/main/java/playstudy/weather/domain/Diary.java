package playstudy.weather.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "diary")
@Getter
@Setter
@NoArgsConstructor  // 매개변수 없는 생성자
public class Diary {

    @Id     //primary key (필수)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동증가컬럼의 생성권한을 DB 로 넘기기
    private int id;

    private String weather;
    private String icon;
    private double temperature;
    private String text;
    private LocalDate date;

    // DateWeather 객체의 데이터로 Diary 객체의 데이터로 변경
    public void setDateWeather(DateWeather dateWeather) {
        this.date = dateWeather.getDate();
        this.weather = dateWeather.getWeather();
        this.icon = dateWeather.getIcon();
        this.temperature = dateWeather.getTemperature();
    }

}


