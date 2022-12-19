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
import depotService from '../../service/DepotService';
import queryString from 'query-string';

class DetailDepot extends Component {
    constructor(props) {
        super(props);
        const { location } = props;
        let params = queryString.parse(location.search);
        const { id } = params;
        const depotId = id;
        this.state = ({
            depotId: depotId,
        });

    }

    componentDidMount() {
        depotService.get(this.state.depotId).then(response => {
            const detailDepot = response.data.data;
            this.setState({
                correlations: detailDepot.correlations,
                detailDepot: detailDepot,
            });
            console.log(this.state.detailDepot)
        }).catch(e => {
            console.log(e);
        });
    }

    render() {
        return (
            <CContainer>
                <CRow>
                    <CCol >
                        {(this.state.detailDepot != null) &&
                            <CCard>
                                <CCardHeader>
                                    <CRow>
                                        <p>
                                            <b>{"Depot name: "}</b>{this.state.detailDepot.name}
                                            &nbsp;
                                            <b>{"Unloading fee: "}</b>{this.state.detailDepot.unloadingCost + " VND"}
                                        </p>
                                    </CRow>
                                    <CRow>
                                        <p><b>{"Products: "}</b>{this.state.detailDepot.products.length === 0 ? "" : (this.state.detailDepot.products.map(product => product.name).join(", "))}</p>
                                    </CRow>
                                    <CRow>
                                        <b>{"Correlation matrix table"}</b>
                                    </CRow>
                                </CCardHeader>
                                <CCardBody>
                                    <CDataTable
                                        items={this.state.correlations}
                                        fields={[
                                            { key: 'index', label: 'STT' },
                                            { key: 'toNodeCode', label: 'Code' },
                                            { key: 'toNodeName', label: 'Name' },
                                            { key: 'toNodeType', label: 'Type' },
                                            { key: 'distance', label: 'Distance (km)' },
                                            { key: 'time', label: 'The average time (phút)' },
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
export default DetailDepot
