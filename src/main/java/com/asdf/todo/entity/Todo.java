package com.asdf.todo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

// @Data는 getter, setter 등을 자동으로 만들어주기 때문에 객체 설계를 망쳐 실무에서는 잘 사용하지 않는다
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo {
    // 이 필드는 테이블의 기본 키이고(@ID), DB가 자동으로 값을 만들어준다(@GeneratedValue
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  타이틀 필드가 null이면 안된다(단순히 의미 표시이지 실질적인 보안/검증 역할을 해주지 않는다)
    // DB 컬럼에 NOT NULL 제약 추가, DB 컬럼명을 title로 지정
    @NonNull
    @Column(nullable = false, name = "title")
    private String title;

    // DB 컬럼명을 description으로 지정
    @Column(name = "description")
    private String description;

    // DB 컬럼에 NOT NULL 제약 추가, DB 컬럼명을 completed 지정
    @Column(nullable = false, name = "completed")
    private boolean completed;

    @Column(
            nullable = false,
            name = "created_at",
            insertable = false, // INSERT할 떄 이 컬럼은 넣지 않는다 -> DB 기본값 사용
            updatable = false, // UPDATE할 떄 이 컬럼은 넣지 않는다 -> 생성 시간은 안바뀜
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP" // DB가 INSERT되는 순간 자동으로 현재 시간 넣음
    )
    private LocalDateTime createdAt;
}
