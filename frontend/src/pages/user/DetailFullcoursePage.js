import React from 'react';
import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import FullcourseMap from '../../components/user/fullcourse/FullcourseMap';
import FullcourseSide from '../../components/user/fullcourse/FullcourseSide';
import FullcourseMemo from '../../components/user/fullcourse/FullcourseMemo';

// 모바일 컴포넌트
import MobileFullcourseMap from '../../components/user/fullcourse/mobile/MobileFullcourseMap';
import MobileFullcourseHeader from '../../components/user/fullcourse/mobile/MobileFullcourseHeader';
import MobileFullcourseMemo from '../../components/user/fullcourse/mobile/MobileFullcourseMemo';
import FullcoursePlan from '../../components/user/fullcourse/mobile/FullcoursePlan';
import { fetchFullcourseDetail } from '../../features/trip/tripActions';
import styled from 'styled-components';

import ClearIcon from '@mui/icons-material/Clear';

// 모바일&웹 뷰
import {
  BrowserView,
  MobileView,
} from "react-device-detect";
import { clickChangeView } from '../../features/share/shareSlice';

const Wrapper = styled.div`
.detailContent{
  position:relative;
}
.closeBtn{
  position: absolute;
    top: 0.5rem;
    right: 0.5rem;

    &:hover {
      color: #000;
    }

}
`

const DetailBlock = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  max-height: 92vh;
`;

const DetailFullcoursePage = () => {
  const params = useParams();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { userInfo } = useSelector((state) => state.user);
  const { fullcourseDetail } = useSelector((state) => state.trip);
  const { changeView } = useSelector((state) => state.share)
  const [days, setDays] = useState(null);

  const fcId = params.fcId;

  useEffect(async () => {
    const { payload } = await dispatch(fetchFullcourseDetail(fcId));
    if (payload.message !== 'Success') {
      navigate('/404');
    }
  }, [dispatch, fcId]);

  useEffect(() => {
    if (fullcourseDetail) {
      const startDate = fullcourseDetail.startDate.slice(0, 10);
      const endDate = fullcourseDetail.endDate.slice(0, 10);

      const arr1 = startDate.split('-');
      const arr2 = endDate.split('-');

      const start = new Date(arr1[0], arr1[1], arr1[2]);
      const end = new Date(arr2[0], arr2[1], arr2[2]);

      const tmp = days_between(start, end);
      setDays(tmp);
    }
  }, [fullcourseDetail]);

  //일 수 세기
  const days_between = (date1, date2) => {
    // The number of milliseconds in one day
    const ONE_DAY = 1000 * 60 * 60 * 24;
    // Calculate the difference in milliseconds
    const differenceMs = Math.abs(date1 - date2);
    // Convert back to days and return
    return Math.round(differenceMs / ONE_DAY) + 1;
  };

  const onClick = () => {
    dispatch(clickChangeView())
  }

  return (
    <Wrapper>
      {/* 브라우저뷰 */}
      <BrowserView>
        <DetailBlock>
          <FullcourseSide
            days={days}
            userInfo={userInfo}
            fullcourseDetail={fullcourseDetail}
          />
          <FullcourseMap />
          <FullcourseMemo fullcourseDetail={fullcourseDetail} />
        </DetailBlock>
      </BrowserView>

      {/* 모바일뷰 */}
      <MobileView>
      {!changeView ? (
        <>
      <MobileFullcourseHeader
        days={days}
        fullcourseDetail={fullcourseDetail}
        />
        <div className='detailContent'>
        <MobileFullcourseMap />
        <FullcoursePlan
          days={days}
          fullcourseDetail={fullcourseDetail}
          />
          </div>
          </>
        ) :
          (<>
          <div className='closeBtn' onClick={onClick}><ClearIcon/></div>
            <MobileFullcourseMemo fullcourseDetail={fullcourseDetail} /></>)}
          
      
      </MobileView>
    </Wrapper>
  );
};

export default DetailFullcoursePage;
