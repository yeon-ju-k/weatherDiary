package playstudy.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import playstudy.weather.domain.Diary;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {

    // 날짜값으로 일기를 조회하는 기능
    List<Diary> findAllByDate(LocalDate date);  // 해당 매개변수로 값을 찾는 메소드를 구현해줌 (JPA 가)

    // 날짜 범위값으로 일기를 조회하는 기능
    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    // 날짜 일기 수정하는 기능에서 사용 (같은날짜의 일기가 여러개일 경우 : 첫번째의 일기만 수정)
    Diary getFirstByDate(LocalDate date);   // 첫번째 일기값 찾기

    // 날씨 일기 삭제하는 기능 (같은날짜의 일기를 모두 삭제)
    @Transactional  // 삭제할 때는 @Transactional 애노테이션을 작성해야함! (미작성시 오류발생!)
    void deleteAllByDate(LocalDate date);






}



