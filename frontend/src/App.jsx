import './App.css'
import { Routes, Route } from 'react-router-dom'
import Health from '@/shared/pages/Health';
import SummaryPage from './shared/pages/SummaryPage';

function App() {


  return (
    <>
      <Routes>
      <Route path="/health" element={<Health />} />
      <Route path="/summary" element={<SummaryPage />} />
      </Routes>
    </>
  )
}

export default App
