import React, {useState, useEffect} from 'react'
import Plot from 'react-plotly.js';
import {
    CCol,
    CRow
} from '@coreui/react'
import dashboardService from "../../service/DashboardService";

function MainChartExample() {

    const [statInterval, setStatInterval] = useState({start: '2022-12-02 00:00:00', end: '2022-12-14 00:00:00'});
    const [statData, setStatData] = useState({quantity: [], revenue: []})

    const loadData = useEffect(() => {
        dashboardService.orderItemStat(statInterval).then(response => {
          if (response.data.code === "SUCCESS") {
              var data = response.data.data;
            setStatData({
                quantity: data.quantity,
                revenue: data.revenue
            })
          }
        });
    }, [statInterval]);


    return (
        <CRow>
            <CCol xs="12" md="5" xl="6">
                <Plot
                    data={[
                        {
                            type: 'bar',
                            x: statData["quantity"].map(item => item[0]),
                            y: statData["quantity"].map(item => item[1])
                        }
                    ]}
                    layout={{title: 'Top sản phẩm bán ra', width: 530 }}
                />
            </CCol >
            <CCol xs="12" md="5" xl="4">
                <Plot
                    data={[
                        {
                            type: 'bar',
                            x: statData["revenue"].map(item => item[0]),
                            y: statData["revenue"].map(item => item[1])
                        }
                    ]}
                    layout={{title: 'Top doanh thu theo sản phẩm', width: 530 }}
                />
            </CCol>
            </CRow>
    );
}

export default MainChartExample;