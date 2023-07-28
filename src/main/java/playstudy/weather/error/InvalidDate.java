package playstudy.weather.error;

// 예외 처리 클래스 (사용자가 너무 먼 미래, 과거의 날짜의 일기를 작성하려고 할때의 오류 예외)
public class InvalidDate extends RuntimeException {

    // 해당 클래스 호출되었을 때 반환할 문자열
    private static final String MESSAGE = "너무 과거 혹은 미래의 날짜입니다.";

    // 생성자
    public InvalidDate() {
        super(MESSAGE);     // 해당 예외 클래스가 호출될 때 해당 문구가 같이 반환됨!
    }

}
