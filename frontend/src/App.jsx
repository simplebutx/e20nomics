import './App.css'
import { Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import Health from '@/shared/pages/Health';
import SummaryPage from './features/pages/SummaryPage';
import Navbar from './shared/components/Navbar';
import MyPage from './features/pages/MyPage';
import TodayNewsPage from './features/pages/TodayNewsPage';
import LoginPage from './features/pages/LoginPage';
import SignupPage from './features/pages/SignupPage';
import AdminPage from './features/pages/AdminPage';
import AdminSummaryPage from './features/pages/AdminSummaryPage';
import MyTerms from './features/components/MyTerms';
import MySummaries from './features/components/MySummaries';

function App() {


  return (
    <>
    <Toaster position="top-center" containerStyle={{top:80,}}/>
     <Navbar />
      <Routes>
      <Route path="/" element={<TodayNewsPage />} />
      <Route path="/health" element={<Health />} />
      <Route path="/summarize" element={<SummaryPage />} />
      <Route path="/mypage" element={<MyPage />} />
      <Route path="/today" element={<TodayNewsPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/admin" element={<AdminPage />} />
      <Route path="/adminSummarize" element={<AdminSummaryPage />} />
      <Route path="/terms" element={<MyTerms />} />
      <Route path="/summaries" element={<MySummaries />} />
      </Routes>
    </>
  )
}

export default App
