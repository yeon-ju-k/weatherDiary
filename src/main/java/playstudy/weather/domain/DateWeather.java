package playstudy.weather.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "date_weather")
public class DateWeather {

    @Id     // primary key
    private LocalDate date;

    private String weather;
    private String icon;
    private double temperature;

}


