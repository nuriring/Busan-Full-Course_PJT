package com.ssafy.fullcourse.domain.sharefullcourse.application;

import com.ssafy.fullcourse.domain.fullcourse.entity.FullCourse;
import com.ssafy.fullcourse.domain.fullcourse.repository.FullCourseRepository;
import com.ssafy.fullcourse.domain.sharefullcourse.dto.SharedFCDto;
import com.ssafy.fullcourse.domain.sharefullcourse.dto.SharedFCGetRes;
import com.ssafy.fullcourse.domain.sharefullcourse.dto.SharedFCLikeResDto;
import com.ssafy.fullcourse.domain.sharefullcourse.dto.SharedFCTagDto;
import com.ssafy.fullcourse.domain.sharefullcourse.entity.SharedFCLike;
import com.ssafy.fullcourse.domain.sharefullcourse.entity.SharedFCTag;
import com.ssafy.fullcourse.domain.sharefullcourse.entity.SharedFullCourse;
import com.ssafy.fullcourse.domain.sharefullcourse.exception.AlreadyExistException;
import com.ssafy.fullcourse.domain.sharefullcourse.exception.SharedFCNotFoundException;
import com.ssafy.fullcourse.domain.sharefullcourse.exception.UserNotMatchException;
import com.ssafy.fullcourse.domain.sharefullcourse.repository.SharedFCLikeRepository;
import com.ssafy.fullcourse.domain.sharefullcourse.repository.SharedFCRepository;
import com.ssafy.fullcourse.domain.sharefullcourse.repository.SharedFCTagRepository;
import com.ssafy.fullcourse.domain.user.entity.User;
import com.ssafy.fullcourse.domain.user.exception.UserNotFoundException;
import com.ssafy.fullcourse.domain.user.repository.UserRepository;
import com.ssafy.fullcourse.global.error.ServerError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SharedFCService {

    private final SharedFCRepository sharedFCRepository;
    private final SharedFCTagRepository sharedFCTagRepository;
    private final SharedFCLikeRepository sharedFCLikeRepository;
    private final UserRepository userRepository;
    private final FullCourseRepository fullCourseRepository;

    // ?????? ????????? ??????
    @Transactional
    public SharedFullCourse createSharedFC(SharedFCDto sharedFCDto, List<SharedFCTagDto> tags, String email) {
        Optional<SharedFullCourse> opt = Optional.ofNullable(sharedFCRepository.findByFullCourseFcId(sharedFCDto.getFcId()));
        if(opt.isPresent()) throw new AlreadyExistException("?????? ????????? ????????? ?????????.");

        FullCourse fullCourse = fullCourseRepository.findByFcId(sharedFCDto.getFcId());
        User user = userRepository.findByEmail(email).get();

        SharedFullCourse sharedFullCourse = SharedFullCourse.of(sharedFCDto, fullCourse, user);
        tagDtoE(tags,sharedFullCourse);

        SharedFullCourse saved = sharedFCRepository.save(sharedFullCourse);
        if(saved != null) {
            fullCourse.updateShared(true);
            return saved;
        }
        else throw new ServerError("?????? ????????? ?????? ??? ??? ??? ?????? ????????? ??????????????????.");

    }

    // ?????? ????????? ?????? ??????
    @Transactional
    public SharedFCGetRes detailSharedFC(Long sharedFcId, String email) {
        Optional<SharedFullCourse> opt = Optional.ofNullable(sharedFCRepository.findBySharedFcId(sharedFcId));
        SharedFullCourse sharedFullCourse = opt.orElseThrow(()->new SharedFCNotFoundException());

        Boolean isLike = false;

        System.out.println(opt.get().getSharedFcId()+" "+email);
        if(sharedFCLikeRepository.findByUser_EmailAndSharedFullCourse(email,sharedFullCourse).isPresent()){
            isLike = true;
        }

        System.out.println("???????????? isLike????????? "+isLike);

        SharedFCGetRes res = SharedFCGetRes.of(sharedFullCourse, isLike);
        sharedFCRepository.updateViewCnt(sharedFcId);
        return res;
    }

    // ?????? ????????? ?????? ??????
    @Transactional
    public SharedFCGetRes updateSharedFC(SharedFCDto sharedFCDto, List<SharedFCTagDto> tags, Long sharedFcId, String email) {
        Optional<SharedFullCourse> opt = Optional.ofNullable(sharedFCRepository.findBySharedFcId(sharedFcId));
        SharedFullCourse now = opt.orElseThrow(()->new SharedFCNotFoundException());
        FullCourse fullCourse = fullCourseRepository.findByFcId(sharedFCDto.getFcId());
        for(int i = 0 ; i < now.getSharedFCTags().size();i++) {
            now.getSharedFCTags().remove(i);
        }

        SharedFullCourse sharedFullCourse = SharedFullCourse.sharedFCUpdate(sharedFCDto,now, fullCourse, sharedFcId);

        Boolean isLike = false;
        if(sharedFCLikeRepository.findByUser_EmailAndSharedFullCourse(email,sharedFullCourse).isPresent()){
            isLike = true;
        }

        tagDtoE(tags,sharedFullCourse);

        SharedFullCourse saved = sharedFCRepository.save(sharedFullCourse);

        if(saved != null) return SharedFCGetRes.of(saved, isLike); // ?????? ??????
        else throw new ServerError("?????? ????????? ?????? ??? ????????? ??????????????????."); // ?????? ??? ??????

    }

    // ?????? ????????? ??????
    @Transactional
    public void deleteSharedFC(Long sharedFdId,String email) {
        SharedFullCourse saved =Optional.ofNullable(sharedFCRepository.findBySharedFcId(sharedFdId)).orElseThrow(
                ()->new SharedFCNotFoundException("???????????? ?????? ??????????????? ?????????.")
        );
        if(!email.equals(saved.getUser().getEmail())) throw new UserNotMatchException("????????? ????????????.");


        FullCourse fullCourse = saved.getFullCourse();
        fullCourse.updateShared(false);
        fullCourseRepository.save(fullCourse);
        if(saved == null) throw new SharedFCNotFoundException();
        sharedFCRepository.delete(saved);
    }

    // ?????? ????????? ?????????
    @Transactional
    public SharedFCLikeResDto likeSharedFC(Long sharedId, String email) {
        System.out.println(email);

        SharedFullCourse sharedFullCourse = sharedFCRepository.findBySharedFcId(sharedId);
        if(sharedFullCourse == null) throw new SharedFCNotFoundException();
        // ????????? ??????
        Optional<SharedFCLike> opt = sharedFCLikeRepository.findByUser_EmailAndSharedFullCourse(email, sharedFullCourse);

//        ?????? ??????
        boolean isLike;
        Long likeCnt = sharedFullCourse.getLikeCnt();

        if(opt.isPresent()){ // ????????? ??????
            sharedFCLikeRepository.delete(opt.get());
            sharedFCRepository.updateLikeCnt(sharedId, -1);

            likeCnt--;
            isLike=false;
        }else{ // ?????????
            sharedFCLikeRepository.save(SharedFCLike.builder()
                    .user(userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException()))
                    .sharedFullCourse(sharedFullCourse).build());
            sharedFCRepository.updateLikeCnt(sharedId, 1);

            likeCnt++;
            isLike=true;
        }
        return new SharedFCLikeResDto(isLike,likeCnt);
    }

    // ????????? ??????????????? ??????
    public boolean isShared(Long fcId){
        SharedFullCourse sharedFullCourse = sharedFCRepository.findByFullCourseFcId(fcId);
        if(sharedFullCourse == null ) return false;
        return true;
    }

   public void tagDtoE(List<SharedFCTagDto> tags, SharedFullCourse sharedFullCourse){
        for(SharedFCTagDto tag : tags) {
            SharedFCTag sharedFCTag = SharedFCTag.of(tag,sharedFullCourse);
            sharedFullCourse.getSharedFCTags().add(sharedFCTag);
            sharedFCTag.setSharedFullCourse(sharedFullCourse);
        }
    }

    // ????????? ?????? ??????????????????, ????????? ????????? ??????
    public HashMap<String, Object> getList(String email) {
        HashMap<String,Object> map = new HashMap<>();
        List<SharedFullCourse> sharedFullCourseList = sharedFCRepository.findSharedFCListByUser_Email(email);
        map.put("share", sharedFullCourseList);
        List<FullCourse> fullCourseList = fullCourseRepository.findByUser_Email(email);
        map.put("full", fullCourseList);

        return map;
    }

}
