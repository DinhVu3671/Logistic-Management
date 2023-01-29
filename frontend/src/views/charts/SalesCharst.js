import { CContainer, CRow } from "@coreui/react";

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

const dataSales = [
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

const SalesCharst = () => {
  const [statInterval, setStatInterval] = useState("2023");
  const [dataFinal, setDataFinal] = useState([]);

  useEffect(() => {
    DashboardService.sales(statInterval).then((response) => {
      if (response.data.code === "SUCCESS") {
        var data = response.data.data;
        data.map((item) => {
          let month = new Date(item[1]).getMonth();
          dataSales[month - 1].Doanh_thu =
            dataSales[month - 1].Doanh_thu + item[0];
        });
        setDataFinal(dataSales);
      }
    });
  }, []);
  return (
    <>
      <CContainer>
        <CRow style={{ display: "flex", justifyContent: "center" }}>
          <div style={{ marginTop: "20px", fontWeight: 700, fontSize: 20 }}>
            DOANH THU TỔNG HỢP 2023
          </div>
          <div style={{ marginTop: "20px", backgroundColor: "white" }}>
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
              {/* <Bar dataKey="uv" fill="#82ca9d" /> */}
            </BarChart>
          </div>
        </CRow>
      </CContainer>
    </>
  );
};

export default SalesCharst;
