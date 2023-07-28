package playstudy.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import playstudy.weather.domain.DateWeather;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DateWeatherRepository extends JpaRepository<DateWeather, LocalDate> {

    // 과거의 날짜값에 해당하는 날씨데이터 가져오기
    List<DateWeather> findAllByDate(LocalDate date);

}


