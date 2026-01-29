import React from 'react';
import { Box } from '@mui/material';
import Header from './Header';
import Sidebar from './Sidebar';

interface MainLayoutProps {
  children: React.ReactNode;
  user?: {
    username: string;
    role: string;
  };
  onLogout?: () => void;
}

const MainLayout: React.FC<MainLayoutProps> = ({ children, user, onLogout }) => {
  return (
    <Box sx={{ display: 'flex' }}>
      <Header user={user} onLogout={onLogout} />
      <Sidebar userRole={user?.role} />
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          p: 3,
          mt: 8, // Height of the AppBar
        }}
      >
        {children}
      </Box>
    </Box>
  );
};

export default MainLayout;
