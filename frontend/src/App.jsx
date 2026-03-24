import './App.css'
import { Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import ProtectedRoute from "@/shared/routes/ProtectedRoute";

import Health from '@/shared/pages/Health';
import Navbar from '@/shared/components/Navbar';
import TodayNewsPage from '@/shared/pages/TodayNewsPage';
import LoginPage from '@/shared/pages/LoginPage';
import SignupPage from '@/shared/pages/SignupPage';
import TodayNewsDetailPage from '@/shared/pages/TodayNewsDetailPage';

import AdminPage from '@/features/admin/pages/AdminPage';
import AdminSummaryPage from '@/features/admin/pages/AdminSummaryPage';
import AdminTodayNewsDetailPage from '@/features/admin/pages/AdminTodayNewsDetailPage';
import AdminTermListPage from '@/features/admin/pages/AdminTermListPage';
import AdminTermDetailPage from '@/features/admin/pages/AdminTermDetailPage';

import SummaryPage from '@/features/mypage/pages/SummaryPage';
import MyPage from '@/features/mypage/pages/MyPage';
import MyTerms from '@/features/mypage/pages/MyTerms';
import MyTermDetailPage from '@/features/mypage/pages/MyTermDetailPage';
import MySummaries from '@/features/mypage/pages/MySummaries';
import MySummariesDetailPage from '@/features/mypage/pages/MySummariesDetailPage';
import MyTermEditPage from '@/features/mypage/pages/MyTermEditPage';
import MySummariesEditPage from '@/features/mypage/pages/MySummariesEditPage';
import MyPreferencePage from './features/mypage/pages/MyPreferencePage';

function App() {
  return (
    <>
      <Toaster position="top-center" containerStyle={{ top: 80 }} />
      <Navbar />
      <Routes>
        <Route path="/" element={<TodayNewsPage />} />
        <Route path="/health" element={<Health />} />
        <Route path="/summarize" element={<SummaryPage />} />
        <Route path="/today" element={<TodayNewsPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />

        <Route element={<ProtectedRoute />}>
          <Route path="/mypage" element={<MyPage />} />
          <Route path="/today/:id" element={<TodayNewsDetailPage />} />
          <Route path="/terms" element={<MyTerms />} />
          <Route path="/terms/:id" element={<MyTermDetailPage />} />
          <Route path="/terms/:id/edit" element={<MyTermEditPage />} />
          <Route path="/summaries" element={<MySummaries />} />
          <Route path="/summaries/:id" element={<MySummariesDetailPage />} />
          <Route path="/summaries/:id/edit" element={<MySummariesEditPage />} />
          <Route path="/me/preferences" element={<MyPreferencePage />} />
        </Route>

        <Route element={<ProtectedRoute requireRole="ADMIN" />}>
          <Route path="/admin" element={<AdminPage />} />
          <Route path="/admin/todayNews/:id" element={<AdminTodayNewsDetailPage />} />
          <Route path="/adminSummarize" element={<AdminSummaryPage />} />
          <Route path="/admin/terms" element={<AdminTermListPage />} />
          <Route path="/admin/terms/:id" element={<AdminTermDetailPage />} />
        </Route>
      </Routes>
    </>
  )
}

export default App
