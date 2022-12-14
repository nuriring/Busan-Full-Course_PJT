package com.ssafy.fullcourse.domain.sharefullcourse.repository;

import com.ssafy.fullcourse.domain.sharefullcourse.entity.SharedFullCourse;
import com.ssafy.fullcourse.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SharedFCRepository extends JpaRepository<SharedFullCourse, Long>{
    SharedFullCourse findBySharedFcId(Long shareFcId);
    SharedFullCourse findByFullCourseFcId(Long fcId);
    List<SharedFullCourse> findByUser(User user);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE shared_full_course sfc SET sfc.like_cnt = sfc.like_cnt + :count where shared_fc_id = :sharedFcId",nativeQuery = true)
    int updateLikeCnt(@Param(value="sharedFcId") Long sharedFcId, @Param(value="count") int count);


    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE shared_full_course sfc SET sfc.comment_cnt = sfc.comment_cnt + :count where shared_fc_id = :sharedFcId",nativeQuery = true)
    int updateCommentCnt(@Param(value="sharedFcId") Long sharedFcId, @Param(value="count")int count);


    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE shared_full_course sfc SET sfc.view_cnt = sfc.view_cnt + 1 where shared_fc_id = :sharedFcId",nativeQuery = true)
    int updateViewCnt(@Param(value="sharedFcId") Long sharedFcId);


    List<SharedFullCourse> findSharedFCListByUser_Email(String email);

    Page<SharedFullCourse> findFCListByTitleContains(String keyword, Pageable pageable);
    Page<SharedFullCourse> findAll(Pageable pageable);



}
