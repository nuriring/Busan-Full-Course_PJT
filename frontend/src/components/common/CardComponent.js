import React from 'react';
import styled from 'styled-components';
import '../../pages/main/main.css';

import AspectRatio from '@mui/joy/AspectRatio';
import Card from '@mui/joy/Card';
import Avatar from '@mui/joy/Avatar';
import CardOverflow from '@mui/joy/CardOverflow';
import { FaCommentDots } from 'react-icons/fa';
import { GoHeart } from 'react-icons/go';

// mui에서 미디어쿼리 사용하는 방법
import { makeStyles, useMediaQuery } from '@material-ui/core';
import { getUserInfo } from '../../api/user';

const useStyles = makeStyles((theme) => ({
  cardMobile: {
    // 테스트용 css
    width: '100%',
  },
}));

const Wapper = styled.div`
  margin: 1vw 1vw 1vw 0vw;
  @media only screen and (min-device-width: 375px) and (max-device-width: 479px) {
    margin: 1vw 7vw;
  }
`;

const Nickname = styled.div`
  font-family: Tmoney;
  font-size: 1vmin;
  color: #333333;
  text-align: center;
`;

const CardTitle = styled.div`
  font-family: Tmoney;
  font-size: 4vmin;
  color: #333333;
  margin-top: 2vh;
`;

const CardContent = styled.div`
  font-family: Tmoney;
  font-size: 2vmin;
  color: #333333;
  margin-top: 1vh;
`;

const CardFooter = styled.div`
  display: flex;
  align-items: center;
  margin-top: 2vh;

  span {
    font-family: Tmoney;
    font-size: 1vmin;
    margin-right: 1vw;
  }
`;

const Like = styled(GoHeart)`
  color: #e36387;
  font-size: 2.5vmin;
`;
const Comment = styled(FaCommentDots)`
  color: #e36387;
  font-size: 2.2vmin;
`;
const CardComponent = (props) => {
  const classes = useStyles();
  const isMobile = useMediaQuery('(max-width: 600px)');
  return (
    <Wapper>
      <Card
        className={isMobile ? classes.cardMobile : null}
        variant="soft"
        sx={{
          width: '16.5vw',
          // boxShadow: '0px 2px 4px 0px rgb(0 0 0 / 10%);' }}, 스투비플래너 카드 예시
          boxShadow: '1px 2px 4px 1px rgb(0 0 0 / 10%);',
          marginTop: '1vh',
          ':hover': { transform: 'scale(1.05)', cursor: 'pointer' },
          ':active': { transform: 'scale(0.95)' },
        }}
      >
        <CardOverflow>
          <AspectRatio ratio="3">
            <img
              src="https://images.unsplash.com/photo-1532614338840-ab30cf10ed36?crop=entropy&auto=format&fit=crop&w=3270"
              alt="card main img"
            />
          </AspectRatio>

          <Avatar
            src="https://images.unsplash.com/photo-1532614338840-ab30cf10ed36?crop=entropy&auto=format&fit=crop&w=3270"
            size="lg"
            sx={{
              position: 'absolute',
              zIndex: 2,
              right: isMobile ? '11vw' : '7.7vw',
              bottom: 0,
              transform: 'translateY(50%)',
              border: '2px solid white',
            }}
          ></Avatar>
        </CardOverflow>
        <CardOverflow
          variant="soft"
          sx={{
            display: 'flex-column',
            textAlign: 'start',
            // gap: 1.5,
            py: 3,
            px: 'var(--Card-padding)',
            // borderTop: '1px solid',
            // borderColor: 'neutral.outlinedBorder',
            // bgcolor: 'background.level1',
            marginTop: '0',
            paddingTop: '4vh',
          }}
        >
          <Nickname>{props.fullcourse.user.userNickName}</Nickname>
          <CardTitle>{props.fullcourse.title}</CardTitle>
          {props.fullcourse.day}
          <CardContent>{props.fullcourse.detail}</CardContent>
          {/* 이 자리에 호준이가 만든 태그 */}

          <CardFooter>
            <Like />
            <span>{props.fullcourse.likeCnt}</span>
            <Comment />
            <span>{props.fullcourse.commentCnt}</span>
          </CardFooter>
        </CardOverflow>
      </Card>
    </Wapper>
  );
};

export default CardComponent;
