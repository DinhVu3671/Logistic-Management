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
    CButtonGroup,
    CButton,
    CToaster,
    CToast,
    CToastHeader,
    CToastBody,
} from '@coreui/react'
import vehicleService from '../../service/VehicleService';
import queryString from 'query-string';
import { getCategories } from '../../utilities/utility';
import EditVehicleProduct from './EditVehicleProduct';


class DetailVehicle extends Component {
    constructor(props) {
        super(props);
        const { location } = props;
        let params = queryString.parse(location.search);
        const { id } = params;
        const vehicleId = id;
        this.state = ({
            vehicleId: vehicleId,
        });

    }

    componentDidMount() {
        vehicleService.get(this.state.vehicleId).then(response => {
            const detailVehicle = response.data.data;
            this.setState({
                vehicleProducts: detailVehicle.vehicleProducts,
                detailVehicle: detailVehicle,
            });
            console.log(this.state.detailVehicle)
        }).catch(e => {
            console.log(e);
        });
    }

    setShowEdit = (showEdit) => {
        this.setState({
            showEdit: showEdit
        })
    }
    showSuccessMsg = (msg) => {
        this.setState({
            showSuccessMsg: true,
            successMsg: msg,
        })
    }

    setShowSuccessToast = (show) => {
        this.setState({
            showSuccessMsg: show === true ? true : false,
        })
    }

    reloadData = () => {
        vehicleService.get(this.state.vehicleId).then(response => {
            const detailVehicle = response.data.data;
            this.setState({
                vehicleProducts: detailVehicle.vehicleProducts,
                detailVehicle: detailVehicle,
            });
            console.log(this.state.detailVehicle)
        }).catch(e => {
            console.log(e);
        });
    }

    handleEdit = (editVehicleProduct) => {
        const editingVehicleProduct = {
            id: editVehicleProduct.id,
            maxNumber: editVehicleProduct.maxNumber,
            product: editVehicleProduct.product,
            vehicle: editVehicleProduct.vehicle,
        }
        this.setState({
            editVehicleProduct: editingVehicleProduct,
            showEdit: true,
        });
    }

    render() {
        return (
            <CContainer>
                <CCol sm="12" lg="6">
                    <CToaster position='top-right'>
                        <CToast
                            key={'toastSuccess'}
                            show={this.state.showSuccessMsg}
                            autohide={4000}
                            fade={true}
                            onStateChange={(show) => { this.setShowSuccessToast(show) }}
                        >
                            <CToastHeader closeButton>Notification</CToastHeader>
                            <CToastBody>
                                {this.state.successMsg}
                            </CToastBody>
                        </CToast>
                    </CToaster>
                </CCol>
                {this.state.showEdit &&
                    <EditVehicleProduct editVehicleProduct={this.state.editVehicleProduct} setShowEdit={this.setShowEdit} reloadData={this.reloadData} showSuccessMsg={this.showSuccessMsg} />
                }
                <CRow>
                    <CCol >
                        {(this.state.detailVehicle != null) &&
                            <CCard>
                                <CCardHeader>
                                    <h6>{"Vehicle " + this.state.detailVehicle.name + " Detail:"}</h6>
                                    <CRow>
                                        <CCol sm='3'>{"Lượng xăng tiêu thụ: " + this.state.detailVehicle.averageGasConsume + " (l/km)"}</CCol>
                                        <CCol sm='3'>{"Giá xăng: " + this.state.detailVehicle.gasPrice + " (VND/l)"}</CCol>
                                        <CCol sm='3'>{"Vận tốc min: " + this.state.detailVehicle.minVelocity + " (km/h)"}</CCol>
                                        <CCol sm='3'>{"Vận tốc max: " + this.state.detailVehicle.maxVelocity + " (km/h)"}</CCol>
                                    </CRow>
                                    <CRow>
                                        <CCol sm='3'>{"Kích thước thùng: "}</CCol>
                                        <CCol sm='3'>{"Dài: " + this.state.detailVehicle.length + " (m)"}</CCol>
                                        <CCol sm='3'>{"Rộng: " + this.state.detailVehicle.width + " (m)"}</CCol>
                                        <CCol sm='3'>{"Cao: " + this.state.detailVehicle.height + " (m)"}</CCol>
                                    </CRow>
                                    <CRow>
                                        <CCol sm='4'>{"Chi phí cố định: " + this.state.detailVehicle.fixedCost + " (VND)"}</CCol>
                                    </CRow>
                                    <CRow>
                                        <CCol sm='6'>{this.state.detailVehicle.excludedGoodsGroups.length === 0 ? "" : ("Không chở được hàng: " + this.state.detailVehicle.excludedGoodsGroups.map(goodsGroup => goodsGroup.name).join(", "))}</CCol>
                                    </CRow>
                                </CCardHeader>
                                <CCardBody>
                                    <CDataTable
                                        items={this.state.vehicleProducts}
                                        fields={[
                                            { key: 'index', label: 'STT' },
                                            { key: 'productCode', label: 'Mã sản phẩm' },
                                            { key: 'productName', label: 'Tên sản phẩm' },
                                            { key: 'size', label: 'Kích thước thùng(dài*rộng*cao)' },
                                            { key: 'maxNumber', label: 'Số lượng tối đa(thùng)' },
                                            // { key: 'actions', label: 'Action' },

                                        ]}
                                        hover
                                        striped
                                        bvehicleed
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
                                                'productCode':
                                                    (item) => {
                                                        return (
                                                            <td className="py-2">
                                                                <span>{item.product.code}</span>
                                                            </td>
                                                        )
                                                    },

                                                'productName':
                                                    (item) => {
                                                        return (
                                                            <td>
                                                                <span>{item.product.name}</span>
                                                            </td>
                                                        )
                                                    },

                                                'size':
                                                    (item) => {
                                                        return (
                                                            <td>
                                                                <span>{item.product.length + "x" + item.product.width + "x" + item.product.height}</span>
                                                            </td>
                                                        )
                                                    },

                                                // 'actions':
                                                //     (item, index) => {
                                                //         return (
                                                //             <td className="py-2">
                                                //                 <CButtonGroup>
                                                //                     <CButton variant="ghost" color="warning" shape="pill" size="sm"
                                                //                         onClick={() => { this.handleEdit(item) }}
                                                //                     >Edit
                                                //                     </CButton>

                                                //                 </CButtonGroup>
                                                //             </td>
                                                //         )
                                                //     },
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
export default DetailVehicle
