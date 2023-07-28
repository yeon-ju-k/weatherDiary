package playstudy.weather.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice   // ControllerAdvice : 전체 컨트롤러에서의 예외처리
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)    // 클라이언트에서 서버로 보낼떄 예외가 발생했을 때 500번 오류(internal server error)로 반환
    @ExceptionHandler(Exception.class)      // 모든 예외에 대해 동작하게 설정
    public Exception handleAllException() {
        System.out.println("error from GlobalExceptionHandler");
        return new Exception();
    }

}


