import { CCol, CRow } from "@coreui/react";
import React, { useEffect, useState } from "react";
import {
  Bar,
  BarChart,
  CartesianGrid,
  Legend,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";
import DashboardService from "../../service/DashboardService";

function SalesCharst() {
  const [statInterval, setStatInterval] = useState("2023");
  const [dataFinal, setDataFinal] = useState([]);
  let dataSales = [
    {
      name: "Tháng 1",
      Doanh_thu: 0,
    },
    {
      name: "Tháng 2",
      Doanh_thu: 0,
    },
    {
      name: "Tháng 3",
      Doanh_thu: 0,
    },
    {
      name: "Tháng 4",
      Doanh_thu: 0,
    },
    {
      name: "Tháng 5",
      Doanh_thu: 0,
    },
    {
      name: "Tháng 6",
      Doanh_thu: 0,
    },
    {
      name: "Tháng 7",
      Doanh_thu: 0,
    },
    {
      name: "Tháng 8",
      Doanh_thu: 0,
    },
    {
      name: "Tháng 9",
      Doanh_thu: 0,
    },
    {
      name: "Tháng 10",
      Doanh_thu: 0,
    },
    {
      name: "Tháng 11",
      Doanh_thu: 0,
    },
    {
      name: "Tháng 12",
      Doanh_thu: 0,
    },
  ];

  useEffect(() => {
    DashboardService.sales(statInterval).then((response) => {
      if (response.data.code === "SUCCESS") {
        var data = response.data.data;
        data.map((item) => {
          let month = new Date(item[1]).getMonth();
          dataSales[month].Doanh_thu =
            dataSales[month].Doanh_thu + item[0];
        });
        setDataFinal(dataSales);
      }
    });
  }, []);
  return (
    <CRow style={{ display: "flex", justifyContent: "center" }}>

      <div style={{ marginTop: "50px", fontWeight: '700', fontSize: 22}}>
        DOANH THU TỔNG HỢP 2023
      </div>
      <div style={{ marginTop: "30px", backgroundColor: "white" }}>
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
          <XAxis dataKey="name" name="Tháng" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="Doanh_thu" fill="#39f" unit="VND" />
        </BarChart>
      </div>
    </CRow>
  );
}

export default SalesCharst;
