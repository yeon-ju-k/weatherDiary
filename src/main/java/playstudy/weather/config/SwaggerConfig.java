package playstudy.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// swagger (API 문서화) 설정 클래스
@Configuration      // 스프링 컨테이너에 해당 설정파일 등록하기
@EnableSwagger2     // swagger 라이브러리 활성화
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("playstudy.weather"))    // 웹 페이지 항목 순서
                .paths(PathSelectors.any())     // 출력할 API 함수
                .build().apiInfo(apiInfo());
    }

    private  ApiInfo apiInfo() {    // 웹 API 페이지 구성 정보
        return new ApiInfoBuilder()
                .title("날씨 일기 프로젝트")      // 웹 document 화면의 타이틀 명
                .description("날씨 일기를 CRUD 할 수 있는 백엔드 API 입니다")   // 내용 문자열 출력
                .version("2.0")             // swagger version
                .build();                   // build 닫기
    }

}
