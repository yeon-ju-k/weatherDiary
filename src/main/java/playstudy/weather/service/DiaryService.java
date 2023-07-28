package playstudy.weather.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import playstudy.weather.WeatherApplication;
import playstudy.weather.domain.DateWeather;
import playstudy.weather.domain.Diary;
import playstudy.weather.error.InvalidDate;
import playstudy.weather.repository.DateWeatherRepository;
import playstudy.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiaryService {


    // open API 의 인증키 값 가져오기
    @Value("${openweathermap.key}")
    private String apikey;


    // Repository 와 연결하기 위해 객체 생성
    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;  // date_weather 테이블 연결하는 Repository

    //@AutoWired 가 생략됨! (생성자가 1개이므로)
    public DiaryService(DiaryRepository diaryRepository, DateWeatherRepository dateWeatherRepository) {  // DiaryService가 생성될 때 DiaryRepository도 같이 가져옴
        this.diaryRepository = diaryRepository;
        this.dateWeatherRepository = dateWeatherRepository;
    }

    // 직접 로그 만들기 (Logger - org.slf4j.Logger)
    private static final Logger logger = LoggerFactory.getLogger(WeatherApplication.class);



    
    // 날씨 일기 저장 - 메인 기능
    public void createDiary(LocalDate date, String text) {
        // +) 시작 로그 기록
        logger.info("started to create diary");

        // # 1. 날씨 데이터 DB에서 가져오기 (없으면 현재날짜데이터 가져오기)
        DateWeather dateWeather = getDateWeather(date);

        // # 3. DB에 저장하기 (파싱된 데이터 + 일기 내용) : Repository 클래스 호출
        //      ㄴ DB에 저장(CRUD)하는 것은 Repository 클래스에서 함!
        Diary nowDiary = new Diary();   // 데이터를 저장할 Diary객체
        nowDiary.setDateWeather(dateWeather);   // 가져온 날씨 데이터를 Diary데이터로 변경
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);

        // +) 종료 로그 기록
        logger.info("ed to create diary");

    }
    
    // 해당 날씨데이터가 date_weather 테이블에 존재하는지 확인하는 함수 (없으면 현재 날씨데이터 가져오기)
    private DateWeather getDateWeather(LocalDate date) {
        // 해당 날짜의 데이터 찾기
        List<DateWeather> dateWeatherListFromDB = dateWeatherRepository.findAllByDate(date);

        if (dateWeatherListFromDB.size() == 0) {    // 데이터가 없을 때
            // API에서 해당 날짜의 날씨 데이터 받아오기
            // ㄴ 그러나 과거 5일 이전의 날짜데이터는 유료이기 때문에
            //      -> 정책을 설정해야함! (현재 날씨만을 가져오도록 하거나, 날씨 없이 일기를 쓰도록하거나)
            //          => 현재 날씨만을 가져오도록 설정하기!
            return getWeatherFromApi(0);    // 현재날씨 가져오기
        } else {    // 데이터가 있을 경우
            return dateWeatherListFromDB.get(0);    // 해당 날짜의 데이터 가져오기
        }
    }
    

    // 날씨 일기 조회 기능
    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        // 예외처리 - 3050년의 1월 1일일 때 -> InvalidDate() 예외처리하기
        if (date.isAfter(LocalDate.ofYearDay(3050, 1))) {
            throw new InvalidDate();
        }
        return diaryRepository.findAllByDate(date);
    }

    // 날씨 범위에 따른 일기 조회 기능
    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    // 날씨 일기 수정 기능
    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        nowDiary.setText(text);     // 일기내용 수정
        diaryRepository.save(nowDiary);     // 변경된 내용 DB에 수정하기
    }

    // 날씨 일기 삭제 기능
    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }




    // 매일 1시에 전날의 날씨 데이터를 받아오는 함수
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate() {
        dateWeatherRepository.save(getWeatherFromApi(1));   // 하루 전 날씨 데이터 가져오기

    }

    // 날씨 데이터를 API에서 받아오는 함수 (DateWeather 객체로)
    private DateWeather getWeatherFromApi(int day) {    // day가 1이면 하루전 값, 0이면 오늘 날씨
        // 1. open weather map에서 데이터 받아오기
        String weatherData = getWeatherString();

        // 2. 결과값 파싱해서 Map형식으로 저장
        Map<String, Object> parseWeather = parseWeather(weatherData);

        // 3. DateWeather 형식에 맞게 저장 (어제 날씨)
        DateWeather dateWeatherData = new DateWeather();
        dateWeatherData.setWeather(parseWeather.get("main").toString());  // 날씨
        dateWeatherData.setDate(LocalDate.now().minusDays(day));    // day가 1이면 하루전 값, 0이면 오늘 날씨
        dateWeatherData.setIcon(parseWeather.get("icon").toString());     // 날씨 아이콘
        dateWeatherData.setTemperature((Double) parseWeather.get("temp"));  // 온도

        return dateWeatherData;
    }



    // # 1. open weather map 에서 데이터 받아오기
    private String getWeatherString() {

        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apikey;

        // url을 자바의 URL클래스 타입으로 변경 (오류가 발생할 수 있으므로 try ~ catch로 작성)
        try {
            URL url = new URL(apiUrl);

            // url을 http 형식으로 변경해 -> 연결시키기
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 전송 방식 설정
            connection.setRequestMethod("GET");

            // [응답] 응답 객체 or 오류메세지 저장하기
            int responseCode = connection.getResponseCode();    // ex. 200, 400, 500 등
            BufferedReader br;  // 응답 결과 or 오류 저장할 변수

            if (responseCode == 200) {  // 정상 연결
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {    // 연결 오류
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ( (inputLine = br.readLine()) != null) {  // 응답데이터의 각 줄을 inputLine에 저장
                response.append(inputLine);
            }
            br.close(); // BufferdReader 닫기

            return response.toString();

        } catch (Exception e) {
            return "failed to get response";
        }

    }


    // # 2. 받아온 날씨 데이터 파싱하기
    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();   // 파싱기능
        JSONObject jsonObject;  // 파싱한 데이터를 저장할 객체

        // 1) 데이터 파싱하기
        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 2) 파싱한 데이터 중 필요한 데이터찾기
        Map<String, Object> resultMap = new HashMap<>();    //저장형식 예: <weather, weather의 데이터>

        JSONObject mainData = (JSONObject) jsonObject.get("main");  //키 값이 main인 데이터 저장
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather"); //키 값이 weather인 데이터 저장
        JSONObject weatherData = (JSONObject) weatherArray.get(0);  // 키값이 weather인 데이터의 0번 인덱스의 값 가져오기
        resultMap.put("temp", mainData.get("temp"));
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));

        return resultMap;
    }

}


