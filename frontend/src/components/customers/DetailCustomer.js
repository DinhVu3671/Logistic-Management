import React, { useState, useEffect, Component } from 'react'
import { useHistory, useLocation } from 'react-router-dom'

import {
    CCard,
    CCardBody,
    CCardHeader,
    CCol,
    CDataTable,
    CRow,
    CContainer,
} from '@coreui/react'
import customerService from '../../service/CustomerService';
import queryString from 'query-string';

class DetailCustomer extends Component {
    constructor(props) {
        super(props);
        const { location } = props;
        let params = queryString.parse(location.search);
        const { id } = params;
        const customerId = id;
        this.state = ({
            customerId: customerId,
        });

    }

    componentDidMount() {
        customerService.get(this.state.customerId).then(response => {
            const detailCustomer = response.data.data;
            this.setState({
                correlations: detailCustomer.correlations,
                detailCustomer: detailCustomer,
            });
            console.log(this.state.detailCustomer)
        }).catch(e => {
            console.log(e);
        });
    }

    render() {
        return (
            <CContainer>
                <CRow>
                    <CCol >
                        {(this.state.detailCustomer != null) &&
                            <CCard>
                                <CCardHeader>
                                    <CRow>
                                        <p>
                                            <b>{"Tên khách hàng: "}</b>{this.state.detailCustomer.name}
                                            &nbsp;
                                            <b>{"Phí phạt: "}</b>{this.state.detailCustomer.penaltyCost + " VND"}
                                        </p>
                                    </CRow>
                                    <CRow>
                                        <b>{"Bảng ma trận tương quan"}</b>
                                    </CRow>
                                </CCardHeader>
                                <CCardBody>
                                    <CDataTable
                                        items={this.state.correlations}
                                        fields={[
                                            { key: 'index', label: 'STT' },
                                            { key: 'toNodeCode', label: 'Mã điểm' },
                                            { key: 'toNodeName', label: 'Tên điểm' },
                                            { key: 'toNodeType', label: 'Loại điểm' },
                                            { key: 'distance', label: 'Khoảng cách(km)' },
                                            { key: 'time', label: 'Thời gian trung bình(phút)' },
                                        ]}
                                        hover
                                        striped
                                        bordered
                                        size="lg"
                                        scopedSlots={
                                            {
                                                'index':
                                                    (item, index) => {
                                                        return (
                                                            <td className="py-2">
                                                                <span>{(index + 1)}</span>
                                                            </td>
                                                        )
                                                    },
                                                'distance':
                                                    (item) => {
                                                        return (
                                                            <td className="py-2">
                                                                <span>{Math.round((item.distance / 1000) * 100) / 100}</span>
                                                            </td>
                                                        )
                                                    },

                                                'time':
                                                    (item) => {
                                                        return (
                                                            <td>
                                                                <span>{Math.round((item.time / 60) * 10) / 10}</span>
                                                            </td>
                                                        )
                                                    },
                                            }
                                        }
                                    />
                                </CCardBody>
                            </CCard>
                        }
                    </CCol>
                </CRow>
            </CContainer>
        )

    }
}
export default DetailCustomer
