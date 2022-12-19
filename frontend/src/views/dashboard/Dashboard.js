import {
  CCard,
  CCardBody,
  CCardHeader,
  CCol,
  CDataTable,
  CRow,
  CPagination,
  CInputGroup,
  CInputGroupPrepend,
  CInputGroupText,
  CInput,
  CButton,
  CInputGroupAppend,
  CContainer,
  CButtonGroup,
  CToastBody,
  CToastHeader,
  CToast,
  CToaster,
  CModalHeader,
  CModalTitle,
  CModal,
  CModalBody,
  CFormGroup,
  CLabel,
  CModalFooter,
  CCardText,
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import MainChartExample from '../charts/MainChartExample';

import React, { PureComponent } from 'react';
import { BarChart, Bar, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import SalesCharst from '../charts/SalesCharst';


const Dashboard = () => {
  return (
    <>
      <CContainer style={{backgroundColor: "white"}}>
        <CRow>
          <MainChartExample />
        </CRow>
        <CRow>
          <SalesCharst />
        </CRow>
      </CContainer>
    </>
  )
}

export default Dashboard
