package project.trendpick_pro.domain.member.entity.dto;

import lombok.*;
import project.trendpick_pro.domain.member.entity.Member;

@Getter
@NoArgsConstructor
@Setter
public class MemberInfoDto {

    private Long memberId;
    private String name;
    private String email;

    @Builder
    public MemberInfoDto (Member member) {
        this.memberId = member.getId();
        this.name = member.getUsername();
        this.email = member.getEmail();
        this.phone = member.getPhoneNumber();
        this.address = member.getAddress();
    }

    public static MemberInfoDto of(Member member){
        return new MemberInfoDto(member);
    }

    private String phone;
    private String address;
}
