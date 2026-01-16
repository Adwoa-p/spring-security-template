package spring.security.temp.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResponseDto<T> {
    private final T response;
    private final String message;
}
