import React from 'react';
import { useDispatch } from 'react-redux';
import styled from 'styled-components';
import CloseIcon from '@mui/icons-material/Close';
import { useState } from 'react';
import { createSharedFc } from '../../../features/share/shareActions';

const AlertDiv = styled.div`
  .modal {
    text-align: center;
    display: none;
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: 99;
    background-color: rgba(0, 0, 0, 0.6);
  }
  .modal button {
    outline: none;
    cursor: pointer;
    border: 0;
  }
  .modal > section {
    width: 90%;
    max-width: 450px;
    margin: 0 auto;
    border: solid 2px;
    border-radius: 0.3rem;
    border-color: #233e8b;
    background-color: black;
    /* 팝업이 열릴때 스르륵 열리는 효과 */
    animation: modal-show 0.3s;
    overflow: hidden;
  }
  .modal > section > header {
    position: relative;
    padding: 16px 50px 16px 50px;
    background-color: #ffffff;
    font-weight: 700;
  }
  .modal > section > header button {
    position: absolute;
    top: 15px;
    right: 15px;
    width: 30px;
    font-size: 21px;
    font-weight: 700;
    text-align: center;
    background-color: transparent;
  }
  .modal > section > main {
    padding: 16px;
    background-color: #ffffff;
    border-top: 1px solid #ffffff;
  }
  .modal > section > main button {
    padding: 6px 12px;
    background-color: #ffffff;
    border-radius: 5px;
    font-size: 13px;
  }
  .modal > section > footer {
    padding: 0px 16px 16px;
    background-color: #ffffff;

    text-align: right;
  }
  .modal > section > footer button {
    padding: 6px 12px;
    background-color: #ffffff;
    border-radius: 5px;
    font-size: 13px;
  }
  .modal.openModal {
    display: flex;
    align-items: center;
    /* 팝업이 열릴때 스르륵 열리는 효과 */
    animation: modal-bg-show 0.3s;
  }
  @keyframes modal-show {
    from {
      opacity: 0;
      margin-top: -50px;
    }
    to {
      opacity: 1;
      margin-top: 0;
    }
  }
  @keyframes modal-bg-show {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
`;

const MyFullcourseShare = (props) => {
  const { open, close, header, fullcourse } = props;
  const dispatch = useDispatch();
  const [inputs, setInPuts] = useState({
    title: '',
    content: '',
  });
  const [fullcourseTags, setFullcourseTags] = useState(['부산']);

  const onChangeInputs = (e) => {
    const { name, value } = e.target;
    setInPuts({ ...inputs, [name]: value });
  };
  const onSubmit = (e) => {
    e.preventDefault();
    dispatch(
      createSharedFc({
        fcId: fullcourse.fcID,
        title: inputs.title,
        detail: inputs.content,
        tags: fullcourseTags,
        thumbnail: fullcourse.thumbnail,
      }),
    );
  };
  return (
    <AlertDiv>
      <div className={open ? 'openModal modal' : 'modal'}>
        {open ? (
          <section>
            <header>
              {header}
              <button className="close" onClick={close}>
                <CloseIcon />
              </button>
            </header>
            <main>
              <form onSubmit={onSubmit}>
                <div>
                  <label>제목</label>
                  <input
                    type="text"
                    id="title"
                    name="title"
                    onChange={onChangeInputs}
                  />
                </div>
                <div>
                  <label>내용</label>
                  <input
                    type="text"
                    id="content"
                    name="content"
                    onChange={onChangeInputs}
                  />
                </div>
                <footer>
                  <button>공유하기</button>
                </footer>
              </form>
            </main>
          </section>
        ) : null}
      </div>
    </AlertDiv>
  );
};
export default MyFullcourseShare;