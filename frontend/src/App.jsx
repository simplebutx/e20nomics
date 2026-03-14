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
import MyTermDetailPage from './features/components/MyTermDetailPage';
import MySummaries from './features/components/MySummaries';
import TodayNewsDetailPage from './features/pages/TodayNewsDetailPage';
import MySummariesDetailPage from './features/components/MySummariesDetailPage';
import MyTermEditPage from './features/components/MyTermEditPage';


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
      <Route path="/today/:id" element={<TodayNewsDetailPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/admin" element={<AdminPage />} />
      <Route path="/adminSummarize" element={<AdminSummaryPage />} />
      <Route path="/terms" element={<MyTerms />} />
      <Route path="/terms/:id" element={<MyTermDetailPage />} />
      <Route path="/terms/:id/edit" element={<MyTermEditPage />} />
      <Route path="/summaries" element={<MySummaries />} />
      <Route path="/summaries/:id" element={<MySummariesDetailPage />} />
      </Routes>
    </>
  )
}

export default App
