package com.ssafy.fullcourse.domain.place.application;

import com.ssafy.fullcourse.domain.place.dto.CultureDetailRes;
import com.ssafy.fullcourse.domain.place.dto.PlaceRes;
import com.ssafy.fullcourse.domain.place.entity.Culture;
import com.ssafy.fullcourse.domain.place.entity.CultureLike;
import com.ssafy.fullcourse.domain.place.repository.CultureLikeRepository;
import com.ssafy.fullcourse.domain.place.repository.CultureRepository;
import com.ssafy.fullcourse.domain.review.exception.PlaceNotFoundException;
import com.ssafy.fullcourse.domain.user.entity.User;
import com.ssafy.fullcourse.domain.user.exception.UserNotFoundException;
import com.ssafy.fullcourse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CultureService {

    private final CultureRepository cultureRepository;
    private final CultureLikeRepository cultureLikeRepository;
    private final UserRepository userRepository;


    public Page<PlaceRes> getCultureList(Pageable pageable, String keyword, Integer maxDist, Float lat, Float lng) throws Exception {
        Page<Culture> page = null;
        List<Culture> list = null;
        if (keyword.equals("")) {
            list = cultureRepository.findAll();
        } else {
            list = cultureRepository.findByNameContaining(keyword);
        }
        if(maxDist != 0) list = extractByDist(list, lat, lng, maxDist);
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        page = new PageImpl(list.subList(start, end), pageable, list.size());
        return page.map(PlaceRes::new);
    }


    public CultureDetailRes getCultureDetail(Long placeId) throws Exception {
        return cultureRepository.findByPlaceId(placeId).get().toDetailDto();
    }


    @Transactional
    public boolean cultureLike(Long placeId, Long userId) throws Exception {
        User user = userRepository.findById(userId).get();
        Culture culture = cultureRepository.findByPlaceId(placeId).get();

        if (user == null) {
            throw new UserNotFoundException();
        }
        if (culture == null) {
            throw new PlaceNotFoundException();
        }

        Optional<CultureLike> cultureLike = cultureLikeRepository.findByUserAndPlace(user, culture);

        if (cultureLike.isPresent()) {
            cultureLikeRepository.deleteById(cultureLike.get().getLikeId());
            culture.setLikeCnt(culture.getLikeCnt() - 1);
        } else {
            cultureLikeRepository.save(CultureLike.builder().user(user).place(culture).build());
            culture.setLikeCnt(culture.getLikeCnt() + 1);
        }
        cultureRepository.save(culture);
        return true;
    }

    public static List<Culture> extractByDist(List<Culture> list, Float lat, Float lng, Integer maxDist) {
        for (int i = 0; i < list.size(); i++) {
            Culture c = list.get(i);
            Double dist = Math.sqrt(Math.pow((c.getLat() - lat) * 88.9036, 2) + Math.pow((c.getLng() - lng) * 111.3194, 2));
            if (dist >= maxDist) {
                list.remove(c);
                i--;
            }
        }
        return list;
    }
}
