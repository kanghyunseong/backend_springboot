package com.kh.pcar.back.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberUpdateDTO {

	@Pattern(regexp = "^[가-힣]{2,5}$", message = "이름은 한글만 사용할 수 있습니다. 이름은 2글자 이상 5글자 이하만 사용할 수 있습니다.")
	@Size(min = 2, max = 5, message = "이름은 2글자 이상 5글자 이하만 사용할 수 있습니다. ")
	@NotBlank(message = "사용자 이름은 비어있을 수 없습니다.")
	private String memberName;

	@Pattern(regexp = "^[^@\\s]+@[^@\\s]+$", message = "이메일에는 @가 반드시 포함해야합니다.")
	@NotBlank(message = "사용자 이메일은 비어있을 수 없습니다.")
	private String email;

	@Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "휴대전화 번호는 010으로 시작해 총 11글자가 되어야합니다.")
	@NotBlank(message = "사용자 휴대전화번호는 비어있을 수 없습니다.")
	private String phone;

	private String licenseUrl;
}
