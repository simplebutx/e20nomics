import './App.css'
import { Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import Health from '@/shared/pages/Health';
import SummaryPage from './features/pages/SummaryPage';
import Navbar from './shared/components/Navbar';
import MyPage from './features/pages/MyPage';
import FeedPage from './features/pages/FeedPage';
import TodayNewsPage from './features/pages/TodayNewsPage';
import LoginPage from './features/pages/LoginPage';
import SignupPage from './features/pages/SignupPage';
import AdminPage from './features/pages/AdminPage';
import AdminSummaryPage from './features/pages/AdminSummaryPage';


function App() {


  return (
    <>
    <Toaster position="top-center" />
     <Navbar />
      <Routes>
      <Route path="/health" element={<Health />} />
      <Route path="/summarize" element={<SummaryPage />} />
      <Route path="/mypage" element={<MyPage />} />
      <Route path="/feed" element={<FeedPage />} />
      <Route path="/today" element={<TodayNewsPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/admin" element={<AdminPage />} />
      <Route path="/adminSummarize" element={<AdminSummaryPage />} />
      </Routes>
    </>
  )
}

export default App
