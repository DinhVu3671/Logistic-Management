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
  
  import React, { PureComponent, useEffect, useState } from 'react';
  import { BarChart, Bar, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import DashboardService from '../../service/DashboardService';
  
  
  
  const dataSales = [
    {
      name: 'Tháng 1',
      uv: 0,
    },
    {
      name: 'Tháng 2',
      uv: 0,
    },
    {
      name: 'Tháng 3',
      uv: 0,
    },
    {
      name: 'Tháng 4',
      uv: 0,
    },
    {
      name: 'Tháng 5',
      uv: 0,
    },
    {
      name: 'Tháng 6',
      uv: 0,
    },
    {
      name: 'Tháng 7',
      uv: 0,
    },
    {
      name: 'Tháng 8',
      uv: 0,
    },
    {
      name: 'Tháng 9',
      uv: 0,
    },
    {
      name: 'Tháng 10',
      uv: 0,
    },
    {
      name: 'Tháng 11',
      uv: 0,
    },
    {
      name: 'Tháng 12',
      uv: 0,
    },
  ];
  
  const SalesCharst = () => {
    const [statInterval, setStatInterval] = useState("2022");
    const [dataFinal, setDataFinal] = useState([])

    useEffect(() => {
        DashboardService.sales(statInterval).then(response => {
          if (response.data.code === "SUCCESS") {
              var data = response.data.data;
              data.map((item) => {
                let month = new Date(item[1]).getMonth();
                dataSales[month].uv = dataSales[month].uv + item[0]
              })
              setDataFinal(dataSales)
          }
        });
    }, []);
    return (
      <>
        <CContainer>
          <CRow>
            <div style={{marginTop: "20px", fontWeight: 600}}>
              DOANH THU TỔNG HỢP
            </div>
            <div style={{marginTop: "20px", textAlign: "center"}}>
            <BarChart
            width={1000}
            height={400}
            data={dataFinal}
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
            <Bar dataKey="uv" fill="#8884d8" unit="VND" />
            {/* <Bar dataKey="uv" fill="#82ca9d" /> */}
            </BarChart>
            </div>

          </CRow>
  
        </CContainer>
      </>
    )
  }
  
  export default SalesCharst
  