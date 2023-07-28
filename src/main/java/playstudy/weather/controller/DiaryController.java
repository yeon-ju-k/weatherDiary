package playstudy.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import playstudy.weather.domain.Diary;
import playstudy.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController     // 클라이언트와 소통하는 클래스
public class DiaryController {

    // 서비스에 전달하기 위한 DiaryService 객체 생성
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @ApiOperation(value = "일기 텍스트와 날씨를 이용해서 DB에 일기 저장")
    @PostMapping("/create/diary")   // /create/diary로 일기를 작성한 날짜(get), 일기내용 전달(post)
    void createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "조회할 날짜", example = "2022-02-02") LocalDate date,
                     @RequestBody String text) {

        // [날씨일기 쓰기] 서비스의 createDiary로 전달
        diaryService.createDiary(date, text);
    }

    @ApiOperation("선택한 날짜의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/read/diary")     // 날짜값으로 일기를 조회하는 기능
    List<Diary> readDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // [날씨일기 조회] 서비스의 readDiary로 전달
        return diaryService.readDiary(date);
    }

    @ApiOperation("선택한 기간 중의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/read/diaries")    // 날짜범위값으로 일기를 조회하는 기능
    List<Diary> readDiaries(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "조회할 기간의 첫번째날", example = "2022-02-02") LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "조회할 기간의 마지막날", example = "2022-02-02") LocalDate endDate) {
        // [날씨범위값에 따른 일기 조회] 서비스의 readDiaries로 전달
        return diaryService.readDiaries(startDate, endDate);
    }


    @ApiOperation("선택한 날짜의 일기 데이터를 수정합니다")
    @PutMapping("/update/diary")    // 날씨일기 수정하는 기능
    void updateDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                     @RequestBody String text) {
        // [수정할 날짜를 찾아 해당 일기내용 수정] 서비스의 updateDiary 메소드로 전달
        diaryService.updateDiary(date, text);
    }

    @ApiOperation("선택한 날짜의 일기 데이터를 삭제합니다")
    @DeleteMapping("/delete/diary")     // 날씨 일기 삭제 기능
    void deleteDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        diaryService.deleteDiary(date);
    }

}


