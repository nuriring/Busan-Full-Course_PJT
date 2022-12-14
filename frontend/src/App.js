import { Routes, Route } from 'react-router-dom';
import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { fetchUserInfo } from './features/user/userActions';
import './App.css';
// Main
import Layout from './layout/Layout';
import BrowserLayout from './layout/browser/BrowserLayout';
import MainPage from './pages/main/MainPage';
// User
import LoginPage from './pages/user/LoginPage';
import MobileLoginPage from './pages/user/MobileLoginPage';
import ProfilePage from './pages/user/ProfilePage';
import ShareFcPage from './pages/share/SharedFcPage';
import DetailSharedFcPage from './pages/share/DetailSharedFcPage';
// Plan
import PlanPage from './pages/trip/PlanPage';
import DetailFullcoursePage from './pages/user/DetailFullcoursePage';
// survey
import SurveyPage from './pages/survey/SurveyPage';
import RecommendPage from './pages/survey/RecommendPage';
// ar
// import ArPage from './pages/ar/ArPage';
// import WordcloudPage from './pages/wordcloud/WordcloudPage';
// 404
import NotFound from './pages/NotFound';
import MobileNotFound from './pages/MobileNotFound';
import ProtectedLoginRoute from './private/ProtectedLoginRoute';
import ProtectedRoute from './private/ProtectedRoute';

// 모바일&웹 뷰
import { BrowserView, MobileView } from 'react-device-detect';


function App() {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchUserInfo());
  }, [dispatch]);

  


  return (
    <div>
      <MobileView>
        <Routes>
          {/* Main */}
          <Route path="" element={<Layout />}>
          <Route path="" element={<MainPage />} />
          </Route>
          {/* user */}
          <Route path="user" element={<Layout />}>
            <Route element={<ProtectedLoginRoute />}>
              <Route path="login" element={<MobileLoginPage />} />
            </Route>
            <Route element={<ProtectedRoute />}>
              <Route path="profile/:pageNum" element={<ProfilePage />} />
              <Route
                path="fullcourse/:fcId"
                element={<DetailFullcoursePage />}
              />
            </Route>
          </Route>
          <Route path="fullcourse" element={<Layout />}>
            <Route path="" element={<ShareFcPage />} />
            <Route path="detail/:sharedFcId" element={<DetailSharedFcPage />} />
          </Route>
          {/* trip */}
          <Route path="trip" element={<Layout />}>
            <Route element={<ProtectedRoute />}>
              <Route path="plan" element={<PlanPage />} />
              {/* survey 일정짜기 전 설문조사 */}
              <Route path="survey" element={<SurveyPage />} />
              <Route path="recommend" element={<RecommendPage />} />
            </Route>
          </Route>

          {/* <Route path="ar" element={<ArPage />} /> */}

          <Route path="*" element={<MobileNotFound />} />
        </Routes>
        
      </MobileView>
      <BrowserView>
        <div className="App">
          <div>
            <Routes>
              {/* Main */}
              <Route path="" element={<BrowserLayout />}>
                <Route path="" element={<MainPage />} />
              </Route>
              {/* user */}
              <Route path="user" element={<BrowserLayout />}>
                <Route element={<ProtectedLoginRoute />}>
                  <Route path="login" element={<LoginPage />} />
                </Route>
                <Route element={<ProtectedRoute />}>
                  <Route path="profile/:pageNum" element={<ProfilePage />} />
                  <Route
                    path="fullcourse/:fcId"
                    element={<DetailFullcoursePage />}
                  />
                </Route>
              </Route>
              <Route path="fullcourse" element={<BrowserLayout />}>
                <Route path="" element={<ShareFcPage />} />
                <Route
                  path="detail/:sharedFcId"
                  element={<DetailSharedFcPage />}
                />
              </Route>
              {/* trip */}
              <Route path="trip" element={<BrowserLayout />}>
                <Route element={<ProtectedRoute />}>
                  <Route path="plan" element={<PlanPage />} />
                  {/* survey 일정짜기 전 설문조사 */}
                  <Route path="survey" element={<SurveyPage />} />
                  <Route path="recommend" element={<RecommendPage />} />
                </Route>
              </Route>

              {/* <Route path="ar" element={<ArPage />} /> */}

              <Route path="" element={<BrowserLayout />}>
                <Route path="*" element={<NotFound />} />
              </Route>
            </Routes>
          </div>
        </div>
      </BrowserView>
    </div>
  );
}

export default App;
