package com.asdf.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

// @Data는 getter, setter 등을 자동으로 만들어주기 때문에 객체 설계를 망쳐 실무에서는 잘 사용하지 않는다

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo {
    // 이 필드를 클래스 밖에서는 직접 못 건드리고 메서드(getter/setter)로만 접근하게 하기 위해 private 사용

    private Long id;

    //  타이틀 필드가 null이면 안된다.
    //  단순히 의미 표시이지 실질적인 보안/검증 역할을 해주지 않는다
    @NonNull private String title;

    private String description;
    private boolean completed;
}
