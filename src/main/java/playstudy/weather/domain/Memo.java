package playstudy.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter

// 생성자 관련 애노테이션
@NoArgsConstructor  // 매개변수가 없는 생성자를 생성
@AllArgsConstructor // 클래스의 모든 필드에 대한 매개변수가 있는 생성자를 자동으로 생성

// 매핑
@Entity(name="memo")
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String text;

}
