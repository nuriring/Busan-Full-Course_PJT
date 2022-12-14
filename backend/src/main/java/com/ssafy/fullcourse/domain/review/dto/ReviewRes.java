package com.ssafy.fullcourse.domain.review.dto;

import com.ssafy.fullcourse.domain.review.entity.baseentity.BaseReview;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel("ReviewPostRequest")
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRes {

//    @NotBlank(message="리뷰 식별자는 필수값입니다.")
//    @Pattern(regexp = "(?=[a-zA-Z]+[0-9a-zA-Z]).{4,16}", message = "아이디는 4~16자 영문 대소문자 또는 숫자를 사용하세요. 첫글자는 알파벳이어야 합니다.")
//    @ApiModelProperty(name="리뷰 ID", example="1")
//    Long reviewId;

    @ApiModelProperty(name="리뷰 내용", example="좋아요")
    Long reviewId;

    @ApiModelProperty(name="리뷰 내용", example="좋아요")
    Long placeId;

//    @ApiModelProperty(name="리뷰 아이디디", example="좋아요")
//    Long userId;

    @ApiModelProperty(name="리뷰 좋아요 수", example="좋아요")
    Long likeCnt;

    @ApiModelProperty(name="리뷰 내용", example="좋아요")
    String content;

    @ApiModelProperty(name="평점", example="3")
    Float score;

    @ApiModelProperty(name = "방문여부")
    Boolean isVisited;

    @ApiModelProperty(name = "이미지")
    String imgUrl;

    @ApiModelProperty(name = "닉네임")
    String nickname;

    @ApiModelProperty(name = "이메일")
    String email;

    @ApiModelProperty(name = "등록날짜")
    Date regDate;
    public ReviewRes(BaseReview baseReview) {
        this.reviewId = baseReview.getReviewId();
        this.content = baseReview.getContent();
        this.score = baseReview.getScore();
        this.placeId = baseReview.getPlace().getPlaceId();
        this.nickname = baseReview.getUser().getNickname();
        this.likeCnt = baseReview.getLikeCnt();
        this.isVisited = baseReview.getIsVisited();
        this.imgUrl = baseReview.getReviewImg();
        this.email = baseReview.getUser().getEmail();
        this.regDate = baseReview.getRegDate();
    }

//    public static ReviewRes of(BaseReview baseReview) {
//        ReviewRes res = new ReviewRes();
//        res.setContent(baseReview.getContent());
//        res.setScore(baseReview.getScore());
//        return res;
//    }
}
