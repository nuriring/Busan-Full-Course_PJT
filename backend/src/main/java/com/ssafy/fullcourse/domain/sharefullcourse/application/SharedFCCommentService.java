package com.ssafy.fullcourse.domain.sharefullcourse.application;

import com.ssafy.fullcourse.domain.sharefullcourse.dto.SharedFCCommentReq;
import com.ssafy.fullcourse.domain.sharefullcourse.dto.SharedFCCommentRes;
import com.ssafy.fullcourse.domain.sharefullcourse.entity.SharedFCComment;
import com.ssafy.fullcourse.domain.sharefullcourse.entity.SharedFullCourse;
import com.ssafy.fullcourse.domain.sharefullcourse.exception.SharedFCNotFoundException;
import com.ssafy.fullcourse.domain.sharefullcourse.exception.UserNotMatchException;
import com.ssafy.fullcourse.domain.sharefullcourse.repository.SharedFCCommentRepository;
import com.ssafy.fullcourse.domain.sharefullcourse.repository.SharedFCRepository;
import com.ssafy.fullcourse.domain.user.entity.User;
import com.ssafy.fullcourse.domain.user.exception.UserNotFoundException;
import com.ssafy.fullcourse.domain.user.repository.UserRepository;
import com.ssafy.fullcourse.global.error.ServerError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedFCCommentService {

    private final SharedFCCommentRepository sharedFCCommentRepository;
    private final SharedFCRepository sharedFCRepository;

    private final UserRepository userRepository;

    // 공유 풀코스 댓글 전체 조회
    public List<SharedFCCommentRes> listFCComment(Long sharedFcId){

        List<SharedFCCommentRes> commentResList =
                sharedFCCommentRepository.findAllBySharedFullCourse_SharedFcId(sharedFcId).stream().map(
                        comment->SharedFCCommentRes.of(comment)).collect(Collectors.toList());
        Collections.reverse(commentResList);
        return commentResList;
    }

    // 공유 풀코스 댓글 등록
    @Transactional
    public int createFCComment(SharedFCCommentReq sharedFCCommentReq, String email){
        SharedFullCourse sharedFullCourse = sharedFCRepository.findBySharedFcId(sharedFCCommentReq.getSharedFcId());
        if(sharedFullCourse == null) throw new SharedFCNotFoundException();

        User user = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException());


        SharedFCComment sharedFCComment = SharedFCComment.of(sharedFullCourse, sharedFCCommentReq.getComment(),user);
        sharedFCComment.setRegDate(new Date());
        SharedFCComment saved = sharedFCCommentRepository.save(sharedFCComment);
        if(saved == null) throw new ServerError("댓글 등록 중 에러 발생");
        return sharedFCRepository.updateCommentCnt(saved.getSharedFullCourse().getSharedFcId(),1);

    }

    //공유 풀코스 댓글 수정
    @Transactional
    public void updateFCComment(Long commentId, SharedFCCommentReq sharedFCCommentReq, String email){
        SharedFCComment saved = sharedFCCommentRepository.findByFcCommentId(commentId).orElseThrow(()->new SharedFCNotFoundException());
        if(!saved.getUser().getEmail().equals(email)) throw new UserNotMatchException("댓글단 사용자만 수정 가능");

        SharedFCComment sharedFCComment = SharedFCComment.builder()
                .fcCommentId(saved.getFcCommentId())
                .comment(sharedFCCommentReq.getComment())
                .sharedFullCourse(saved.getSharedFullCourse())
                .user(saved.getUser()).build();

        SharedFCComment updated = sharedFCCommentRepository.save(sharedFCComment);
        if(updated == null)throw new ServerError("댓글 등록 중 에러 발생");


    }

    // 공유 풀코스 댓글 삭제
    @Transactional
    public int deleteFCComment(Long commentId, String email){
        SharedFCComment saved = sharedFCCommentRepository.findByFcCommentId(commentId).orElseThrow(()->new SharedFCNotFoundException());
        if(!saved.getUser().getEmail().equals(email)) throw new UserNotMatchException("댓글단 사용자만 삭제 가능");

        sharedFCCommentRepository.delete(saved);
        return sharedFCRepository.updateCommentCnt(saved.getSharedFullCourse().getSharedFcId(),-1);

    }

}
