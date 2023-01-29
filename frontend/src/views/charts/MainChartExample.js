import { CCol, CRow } from "@coreui/react";
import React, { useEffect, useState } from "react";
import Plot from "react-plotly.js";
import dashboardService from "../../service/DashboardService";

function MainChartExample() {
  const [statInterval, setStatInterval] = useState({
    start: "2023-01-01 00:00:00",
    end: "2023-12-31 00:00:00",
  });
  const [statData, setStatData] = useState({ quantity: [], revenue: [] });

  const loadData = useEffect(() => {
    dashboardService.orderItemStat(statInterval).then((response) => {
      if (response.data.code === "SUCCESS") {
        var data = response.data.data;
        setStatData({
          quantity: data.quantity,
          revenue: data.revenue,
        });
      }
    });
  }, [statInterval]);

  return (
    <CRow>
      <CCol xs="12" md="5" xl="6">
        <Plot
          data={[
            {
              type: "bar",
              x: statData["quantity"].map((item) => item[0]),
              y: statData["quantity"].map((item) => item[1]),
            },
          ]}
          layout={{ title: "Top sản phẩm bán ra", width: 530 }}
        />
      </CCol>
      <CCol xs="12" md="5" xl="4">
        <Plot
          data={[
            {
              type: "bar",
              x: statData["revenue"].map((item) => item[0]),
              y: statData["revenue"].map((item) => item[1]),
            },
          ]}
          layout={{ title: "Top doanh thu theo sản phẩm", width: 530 }}
        />
      </CCol>
    </CRow>
  );
}

export default MainChartExample;
