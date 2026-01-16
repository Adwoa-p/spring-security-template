package spring.security.temp.models;

import lombok.Builder;

@Builder
public record EmailRequest (String email){
}
