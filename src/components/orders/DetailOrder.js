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
    CButton,
    CModal,
    CModalHeader,
    CModalTitle,
    CModalBody,
    CModalFooter,
    CToaster,
    CToast,
    CToastHeader,
    CToastBody,
} from '@coreui/react'
import orderService from '../../service/OrderService';
import queryString from 'query-string';
import { secondsToHHMMSS } from '../../utilities/utility';
import CIcon from '@coreui/icons-react';
import returnOrderService from '../../service/ReturnOrderService';
import AddReturnOrder from './return-order/AddReturnOrder';
import EditReturnOrder from './return-order/EditReturnOrder';



class DetailOrder extends Component {
    constructor(props) {
        super(props);
        const { location } = props;
        let params = queryString.parse(location.search);
        const { id } = params;
        const orderId = id;
        this.state = ({
            orderId: orderId,
            deleteOrder: {}
        });

    }

    setShowAdd = (showAdd) => {
        this.setState({
            showAdd: showAdd
        })
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
        returnOrderService.get(this.state.orderId).then(response => {
            const returnOrder = response.data.data;
            this.setState({
                returnOrder: returnOrder,
            });
            console.log(this.state.detailOrder)
        }).catch(e => {
            console.log(e);
        });
    }

    showDeleteModal = () => {
        this.setState({
            showDelete: true,
            deleteReturnOrder: this.state.returnOrder,
        });
    }

    setShowDelete = (showDelete) => {
        this.setState({
            showDelete: showDelete
        })
    }

    handleDelete = () => {
        const idOrder = this.state.orderId;
        const idReturnOrder = this.state.returnOrder.id;
        returnOrderService.delete(idOrder, idReturnOrder).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.showSuccessMsg("Delete return order successfully!");
                this.setShowDelete(false);
                this.reloadData();
            }

        })
    }

    handleEdit = () => {
        const orderItems = this.state.returnOrder.orderItems.map(orderItem => {
            return {
                product: orderItem.product,
                order: orderItem.order,
                quantity: orderItem.quantity,
                price: orderItem.price,
                weight: orderItem.weight,
                capacity: orderItem.capacity,
                returnOrder: orderItem.returnOrder,
            }
        });
        console.log("orderItems:",);
        const editingReturnOrder = {
            id: this.state.returnOrder.id,
            weight: this.state.returnOrder.weight,
            capacity: this.state.returnOrder.capacity,
            orderItems: orderItems,
        }
        this.setState({
            editReturnOrder: editingReturnOrder,
            showEdit: true,
        });
    }

    componentDidMount() {
        orderService.get(this.state.orderId).then(response => {
            const detailOrder = response.data.data;
            this.setState({
                orderItems: detailOrder.orderItems,
                detailOrder: detailOrder,
            });
            console.log(this.state.detailOrder)
        }).catch(e => {
            console.log(e);
        });
        returnOrderService.get(this.state.orderId).then(response => {
            const returnOrder = response.data.data;
            this.setState({
                returnOrder: returnOrder,
            });
            console.log(this.state.detailOrder)
        }).catch(e => {
            console.log(e);
        });
    }

    render() {
        return (
            <CContainer>
                {(this.state.detailOrder != null) &&
                    <CCard>
                        <CCardHeader>
                            {"Order " + this.state.detailOrder.code + " detail:"}
                            {/* <b> Tên kho hàng: </b>{this.state.detailOrder.depot.name} */}
                            <b> Tên khách hàng: </b>{this.state.detailOrder.customer.name}
                            <b> Khung thời gian: </b>{this.state.detailOrder.deliveryMode === "STANDARD" ?
                                (secondsToHHMMSS(this.state.detailOrder.customer.startTime) + " - " + secondsToHHMMSS(this.state.detailOrder.customer.endTime))
                                :
                                (secondsToHHMMSS(this.state.detailOrder.deliveryAfterTime) + " - " + secondsToHHMMSS(this.state.detailOrder.deliveryBeforeTime))
                            }
                            <b> Thời gian phục vụ(s): </b>{this.state.detailOrder.timeService}
                            <b> Thời gian lên hàng tại kho(s): </b>{this.state.detailOrder.timeLoading}
                        </CCardHeader>
                        <CCardBody>
                            <CDataTable
                                items={this.state.orderItems}
                                fields={[
                                    { key: 'index', label: 'STT' },
                                    { key: 'productCode', label: 'Mã sản phẩm' },
                                    { key: 'productName', label: 'Tên sản phẩm' },
                                    { key: 'productWeight', label: 'Khối lượng(kg)' },
                                    { key: 'productCapacity', label: 'Thể tích(m3)' },
                                    { key: 'unitPrice', label: 'Đơn giá(VND)' },
                                    { key: 'quantity', label: 'Số lượng' },
                                    { key: 'price', label: 'Thành tiền(VND)' },

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

                                        'productWeight':
                                            (item) => {
                                                return (
                                                    <td>
                                                        <span>{item.product.weight}</span>
                                                    </td>
                                                )
                                            },
                                        'productCapacity':
                                            (item) => {
                                                return (
                                                    <td>
                                                        <span>{item.product.capacity}</span>
                                                    </td>
                                                )
                                            },
                                        'unitPrice':
                                            (item) => {
                                                return (
                                                    <td>
                                                        <span>{item.product.price}</span>
                                                    </td>
                                                )
                                            },
                                    }
                                }
                            />
                        </CCardBody>
                    </CCard>
                }
                <CCard>
                    <CModal
                        show={this.state.showDelete}
                        onClose={() => this.setShowDelete(false)}
                        color="danger"
                    >
                        <CModalHeader closeButton>
                            <CModalTitle>Delete Return Order</CModalTitle>
                        </CModalHeader>
                        <CModalBody>
                            <CCard>
                                <CCardHeader>
                                    Confirm Delete Return Order
                                        </CCardHeader>
                                <CCardBody>
                                    {"Delete return order may affect other data. Do you really want to delete?"}
                                </CCardBody>
                            </CCard>
                        </CModalBody>
                        <CModalFooter>
                            <CButton color="success" onClick={() => this.handleDelete()}>Confirm</CButton>{' '}
                            <CButton color="secondary" onClick={() => this.setShowDelete(false)}>Cancel</CButton>
                        </CModalFooter>
                    </CModal>
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
                    <CCardHeader>
                        <CRow>
                            {this.state.showAdd &&
                                <AddReturnOrder order={this.state.detailOrder} setShowAdd={this.setShowAdd} reloadData={this.reloadData} showSuccessMsg={this.showSuccessMsg} />
                            }
                            {this.state.showEdit &&
                                <EditReturnOrder order={this.state.detailOrder} editReturnOrder={this.state.editReturnOrder} setShowEdit={this.setShowEdit} reloadData={this.reloadData} showSuccessMsg={this.showSuccessMsg} />
                            }
                        </CRow>
                        <CRow>
                            <CCol xs="10">
                                {
                                    !this.state.returnOrder ?
                                        <CButton type="submit" size="sm" color="success" onClick={() => this.setShowAdd(true)}><CIcon name="cil-scrubber" /> Create Return Order</CButton>
                                        :
                                        <CRow>
                                            {"Return Order detail:"}
                                            &nbsp;
                                            <b> {"Tổng khối lượng: "}</b>&nbsp;{" " + this.state.returnOrder.weight + " (kg)"}
                                            &nbsp;
                                            <b> {"Tổng dung tích:"} </b>&nbsp;{" " + this.state.returnOrder.capacity + " (m3)"}
                                        </CRow>
                                }
                            </CCol>
                            {
                                this.state.returnOrder &&
                                <CCol xs="2">
                                    <CButton type="submit" size="sm" color="warning" onClick={() => this.handleEdit()}>Edit</CButton>
                                    &nbsp;
                                    <CButton type="submit" size="sm" color="danger" onClick={() => this.showDeleteModal()}>Delete</CButton>
                                </CCol>
                            }
                        </CRow>
                    </CCardHeader>
                    <CCardBody>
                        {this.state.returnOrder &&
                            <CDataTable
                                items={this.state.returnOrder.orderItems}
                                fields={[
                                    { key: 'index', label: 'STT' },
                                    { key: 'productCode', label: 'Mã sản phẩm' },
                                    { key: 'productName', label: 'Tên sản phẩm' },
                                    { key: 'productWeight', label: 'Khối lượng(kg)' },
                                    { key: 'productCapacity', label: 'Thể tích(m3)' },
                                    { key: 'unitPrice', label: 'Đơn giá(VND)' },
                                    { key: 'quantity', label: 'Số lượng' },

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

                                        'productWeight':
                                            (item) => {
                                                return (
                                                    <td>
                                                        <span>{item.product.weight}</span>
                                                    </td>
                                                )
                                            },
                                        'productCapacity':
                                            (item) => {
                                                return (
                                                    <td>
                                                        <span>{item.product.capacity}</span>
                                                    </td>
                                                )
                                            },
                                        'unitPrice':
                                            (item) => {
                                                return (
                                                    <td>
                                                        <span>{item.product.price}</span>
                                                    </td>
                                                )
                                            },
                                    }
                                }
                            />
                        }

                    </CCardBody>
                </CCard>
            </CContainer>
        )

    }
}
export default DetailOrder
