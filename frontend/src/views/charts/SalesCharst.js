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
  
  import React, { PureComponent } from 'react';
  import { BarChart, Bar, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
  
  
  
  const data = [
    {
      name: 'Tháng 1',
      uv: 4000,
      pv: 2400,
      amt: 2400,
    },
    {
      name: 'Tháng 2',
      uv: 3000,
      pv: 1398,
      amt: 2210,
    },
    {
      name: 'Tháng 3',
      uv: 2000,
      pv: 9800,
      amt: 2290,
    },
    {
      name: 'Tháng 4',
      uv: 2780,
      pv: 3908,
      amt: 2000,
    },
    {
      name: 'Tháng 5',
      uv: 1890,
      pv: 4800,
      amt: 2181,
    },
    {
      name: 'Tháng 6',
      uv: 2390,
      pv: 3800,
      amt: 2500,
    },
    {
      name: 'Tháng 7',
      uv: 3490,
      pv: 4300,
      amt: 2100,
    },
    {
      name: 'Tháng 8',
      uv: 3490,
      pv: 4300,
      amt: 2100,
    },
    {
      name: 'Tháng 9',
      uv: 3490,
      pv: 4300,
      amt: 2100,
    },
    {
      name: 'Tháng 10',
      uv: 3490,
      pv: 4300,
      amt: 2100,
    },
    {
      name: 'Tháng 11',
      uv: 3490,
      pv: 4300,
      amt: 2100,
    },
    {
      name: 'Tháng 12',
      uv: 3490,
      pv: 4300,
      amt: 2100,
    },
  ];
  
  const SalesCharst = () => {
    return (
      <>
        <CContainer>
          <CRow>
            <div>
              DOANH THU TỔNG HỢP
            </div>
            <BarChart
            width={1000}
            height={400}
            data={data}
            margin={{
              top: 5,
              right: 20,
              left: 20,
              bottom: 5,
            }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="name" name="Tháng"/>
            <YAxis />
            <Tooltip />
            <Legend />
            <Bar dataKey="pv" fill="#8884d8" unit="VND" />
            {/* <Bar dataKey="uv" fill="#82ca9d" /> */}
          </BarChart>
          </CRow>
  
        </CContainer>
      </>
    )
  }
  
  export default SalesCharst
  